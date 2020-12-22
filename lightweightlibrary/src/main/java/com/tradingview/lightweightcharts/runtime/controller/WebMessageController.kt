package com.tradingview.lightweightcharts.runtime.controller

import com.tradingview.lightweightcharts.Logger
import com.tradingview.lightweightcharts.api.serializer.Serializer
import com.tradingview.lightweightcharts.runtime.WebMessageChannel
import com.tradingview.lightweightcharts.runtime.messaging.*
import java.util.concurrent.ConcurrentHashMap
import java.util.concurrent.ConcurrentLinkedDeque

open class WebMessageController: WebMessageChannel.BridgeMessageListener {

    private var webMessageChannel: WebMessageChannel? = null
    private val callbackBuffer = ConcurrentHashMap<String, BufferElement>()
    private val messageBuffer = ConcurrentLinkedDeque<BridgeMessage>()

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

    fun <T: Any?> callFunction(
        name: String,
        params: Map<String, Any> = emptyMap(),
        callback: ((T) -> Unit)?,
        serializer: Serializer<out T>? = null
    ): String {
        @Suppress("UNCHECKED_CAST")
        return callBridgeFunction(name, params, callback as? (Any?) -> Unit, serializer)
    }

    private fun callBridgeFunction(
        name: String,
        params: Map<String, Any> = emptyMap(),
        callback: ((Any?) -> Unit)? = null,
        serializer: Serializer<out Any?>? = null
    ): String {
        val bridge = BridgeFunction(name, params)

        @Suppress("UNCHECKED_CAST")
        callbackBuffer[bridge.uuid] = BufferElement(
            callback,
            serializer,
            Thread.currentThread().stackTrace
        )

        messageBuffer.addLast(bridge)
        sendMessages()
        return bridge.uuid
    }

    fun <T: Any> callSubscribe(
        name: String,
        params: Map<String, Any> = emptyMap(),
        callback: (T?) -> Unit,
        serializer: Serializer<out T>? = null
    ) {
        val bridge = BridgeSubscription(name, params)
        @Suppress("UNCHECKED_CAST")
        callbackBuffer[bridge.uuid] = BufferElement(
            callback as (Any?) -> Unit,
            serializer,
            Thread.currentThread().stackTrace
        )
        messageBuffer.addLast(bridge)
        sendMessages()
    }

    fun <T: Any> callUnsubscribe(
        name: String,
        callback: (T?) -> Unit
    ) {
        val uuid = callbackBuffer.filterValues { it.callback == callback }.keys.firstOrNull()
        if (uuid != null) {
            val message = BridgeUnsubscribe(name, uuid)
            messageBuffer.addLast(message)
            sendMessages()
        } else {
            Logger.printE("Subscribe cancellation is failed. Key $uuid is not found")
        }
    }

    override fun onMessage(bridgeMessage: BridgeMessage) {
        Logger.printD("Received message from web: $bridgeMessage")
        when (bridgeMessage) {
            is BridgeFunctionResult -> {
                val element = callbackBuffer.remove(bridgeMessage.uuid)
                    ?: throw IllegalStateException(
                        "Could not apply the function result, bridgeMessage: $bridgeMessage")

                element.invoke(bridgeMessage.result)
            }

            is BridgeSubscribeResult -> {
                val element = callbackBuffer[bridgeMessage.uuid]
                    ?: throw IllegalStateException(
                        "Could not apply the subscription result, bridgeMessage: $bridgeMessage")

                element.invoke(bridgeMessage.result)
            }

            is BridgeUnsubscribeResult -> {
                val element = callbackBuffer.remove(bridgeMessage.uuid)
                    ?: throw IllegalStateException(
                        "Could not apply the subscription cancellation result, " +
                                "bridgeMessage: $bridgeMessage")

                element.invoke(bridgeMessage.result)
            }

            is BridgeFatalError -> {
                val element = callbackBuffer.remove(bridgeMessage.uuid)
                val exception = IllegalStateException()
                exception.stackTrace = element?.stackTrace ?: emptyArray()
                throw IllegalStateException(bridgeMessage.message, exception)
            }
        }
    }

    private fun sendMessages() {
        webMessageChannel?.apply {
            while(messageBuffer.isNotEmpty()) {
                messageBuffer.pollFirst()?.let(::sendMessage)
            }
        }
    }

    fun setWebMessageChannel(webMessageChannel: WebMessageChannel) {
        this.webMessageChannel = webMessageChannel
        webMessageChannel.setOnBridgeMessageListener(this)
        sendMessages()
    }

    fun clearWebMessageChannel() {
        webMessageChannel?.setOnBridgeMessageListener(null)
        webMessageChannel = null
    }

    data class BufferElement(
        val callback: ((Any?) -> Unit)? = null,
        val serializer: Serializer<out Any?>? = null,
        val stackTrace: Array<StackTraceElement>
    ) {
        fun invoke(any: Any?) {
            callback?.invoke(serializer?.serialize(any) ?: any)
        }
    }
}

