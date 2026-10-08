package com.tradingview.lightweightcharts.runtime.controller

import com.google.gson.JsonElement
import com.tradingview.lightweightcharts.Logger
import com.tradingview.lightweightcharts.api.exception.ChartBridgeException
import com.tradingview.lightweightcharts.api.serializer.Deserializer
import com.tradingview.lightweightcharts.api.serializer.PrimitiveSerializer
import com.tradingview.lightweightcharts.runtime.WebMessageChannel
import com.tradingview.lightweightcharts.runtime.messaging.*
import java.util.concurrent.ConcurrentHashMap
import java.util.concurrent.ConcurrentLinkedDeque

open class WebMessageController : WebMessageChannel.BridgeMessageListener {

    private var webMessageChannel: WebMessageChannel? = null
    private val callbackBuffer = ConcurrentHashMap<String, BufferElement>()
    private val messageBuffer = ConcurrentLinkedDeque<BridgeMessage>()

    // JS replies to a call without a callback only when it fails, so nothing would ever remove its
    // call site. Keep only the most recent ones: an error arrives shortly after the call that caused it.
    private val notificationCallSites = object : LinkedHashMap<String, BridgeCallSite>() {
        override fun removeEldestEntry(eldest: MutableMap.MutableEntry<String, BridgeCallSite>): Boolean {
            return size > MAX_NOTIFICATION_CALL_SITES
        }
    }

    /**
     * Receives the errors reported asynchronously by the bridge. When it is `null`, they are thrown instead.
     */
    @Volatile
    var errorListener: ((ChartBridgeException) -> Unit)? = null

    fun callFunction(
        name: String,
        params: Map<String, Any> = emptyMap()
    ): String {
        return callBridgeFunction(name, params)
    }

    fun callFunction(
        name: String,
        params: Map<String, Any> = emptyMap(),
        callback: (() -> Unit)?
    ): String {
        @Suppress("UNCHECKED_CAST")
        return callBridgeFunction(name, params, callback as? (Any?) -> Unit)
    }

    fun <T : Any?> callFunction(
        name: String,
        params: Map<String, Any> = emptyMap(),
        callback: ((T) -> Unit)?,
        deserializer: Deserializer<out T>
    ): String {
        @Suppress("UNCHECKED_CAST")
        return callBridgeFunction(name, params, callback as? (Any?) -> Unit, deserializer)
    }

    private fun callBridgeFunction(
        name: String,
        params: Map<String, Any> = emptyMap(),
        callback: ((Any?) -> Unit)? = null,
        deserializer: Deserializer<out Any?> = PrimitiveSerializer.NullDeserializer
    ): String {
        val bridge = BridgeFunction(name, params, expectsResult = callback != null)
        val callSite = BridgeCallSite(name)

        if (callback != null) {
            callbackBuffer[bridge.uuid] = BufferElement(callback, deserializer, callSite)
        } else {
            synchronized(notificationCallSites) {
                notificationCallSites[bridge.uuid] = callSite
            }
        }

        messageBuffer.addLast(bridge)
        sendMessages()
        return bridge.uuid
    }

    fun <T : Any> callSubscribe(
        name: String,
        params: Map<String, Any> = emptyMap(),
        callback: (T) -> Unit,
        deserializer: Deserializer<out T>
    ) {
        val bridge = BridgeSubscription(name, params)
        @Suppress("UNCHECKED_CAST")
        callbackBuffer[bridge.uuid] = BufferElement(
            callback as (Any?) -> Unit,
            deserializer,
            BridgeCallSite(name),
            isSubscription = true
        )
        messageBuffer.addLast(bridge)
        sendMessages()
    }

    fun <T : Any> callUnsubscribe(
        name: String,
        subscription: (T) -> Unit
    ) {
        val uuid = callbackBuffer.filterValues { it.callback == subscription }.keys.firstOrNull()
        if (uuid != null) {
            callbackBuffer[uuid] = callbackBuffer[uuid]!!.makeInactive()
            val message = BridgeUnsubscribe(name, uuid)
            messageBuffer.addLast(message)
            sendMessages()
        } else {
            Logger.e("Subscribe cancellation is failed. Key $uuid is not found")
        }
    }

    fun clearSubscriptions() {
        val subscriptionIds = callbackBuffer
            .filterValues { it.isSubscription }
            .keys
            .toList()
        subscriptionIds.forEach(callbackBuffer::remove)
    }

    override fun onMessage(bridgeMessage: BridgeMessage) {
        Logger.d("Received message from web: $bridgeMessage")
        when (bridgeMessage) {
            is BridgeFunctionResult -> {
                val element = callbackBuffer.remove(bridgeMessage.uuid)
                if (element == null) {
                    Logger.w("Ignored function result without callback, bridgeMessage: $bridgeMessage")
                    return
                }

                deliverResult(element, bridgeMessage.result)
            }

            is BridgeSubscribeResult -> {
                val element = callbackBuffer[bridgeMessage.uuid]
                if (element != null && !element.isInactive) {
                    deliverResult(element, bridgeMessage.result)
                } else {
                    Logger.w("Inactive subscription triggered the action")
                }
            }

            is BridgeUnsubscribeResult -> {
                callbackBuffer.remove(bridgeMessage.uuid)
            }

            is BridgeFatalError -> {
                val callSite = bridgeMessage.uuid?.let { uuid ->
                    callbackBuffer.remove(uuid)?.callSite ?: removeNotificationCallSite(uuid)
                }
                val message = bridgeMessage.message

                dispatchError(
                    ChartBridgeException.JsFatalError(
                        functionName = callSite?.functionName ?: bridgeMessage.functionName,
                        jsMessage = message.lineSequence().first(),
                        jsStackTrace = jsStackTraceOf(message),
                        callSite = callSite?.trimToCaller()
                    )
                )
            }
        }
    }

    override fun onError(error: ChartBridgeException) {
        dispatchError(error)
    }

    private fun dispatchError(error: ChartBridgeException) {
        val listener = errorListener ?: throw error
        listener(error)
    }

    // Deserializers may throw anything. Only deserialization is guarded: exceptions thrown by
    // the callback belong to the caller and propagate as usual.
    @Suppress("TooGenericExceptionCaught")
    private fun deliverResult(element: BufferElement, result: JsonElement) {
        val value = try {
            element.deserializer.deserialize(result)
        } catch (e: Exception) {
            dispatchError(
                ChartBridgeException.ResultDeserializationError(
                    functionName = element.callSite.functionName,
                    callStackTrace = element.callSite.trimToCaller().stackTrace,
                    cause = e
                )
            )
            return
        }

        element.callback?.invoke(value)
    }

    private fun removeNotificationCallSite(uuid: String): BridgeCallSite? {
        return synchronized(notificationCallSites) {
            notificationCallSites.remove(uuid)
        }
    }

    private fun jsStackTraceOf(message: String): Array<StackTraceElement> {
        return getStackTraceRegex().findAll(message).map { result ->
            val values = result.groupValues
            StackTraceElement(
                "jsCode",
                values[1],
                values[2],
                values[3].toInt()
            )
        }.toList().toTypedArray()
    }

    private fun getStackTraceRegex(): Regex {
        val methodGroup = "(\\S+)"
        val fileGroup = "(file:[^:]+)"
        val lineGroup = "(\\d+)"
        val columnGroup = "(\\d+)"
        val pattern = "at\\s+$methodGroup\\s+[(]$fileGroup:$lineGroup:$columnGroup[)]"
        return Regex(pattern)
    }

    private fun sendMessages() {
        webMessageChannel?.apply {
            while (messageBuffer.isNotEmpty()) {
                messageBuffer.pollFirst()?.let(::sendMessage)
            }
        }
    }

    fun setWebMessageChannel(webMessageChannel: WebMessageChannel) {
        this.webMessageChannel = webMessageChannel
        webMessageChannel.setOnBridgeMessageListener(this)
        sendMessages()
    }

    internal data class BufferElement(
        val callback: ((Any?) -> Unit)? = null,
        val deserializer: Deserializer<out Any?>,
        val callSite: BridgeCallSite,
        val isInactive: Boolean = false,
        val isSubscription: Boolean = false,
    ) {
        fun makeInactive(): BufferElement = copy(isInactive = true)
    }

    private companion object {
        const val MAX_NOTIFICATION_CALL_SITES = 128
    }
}
