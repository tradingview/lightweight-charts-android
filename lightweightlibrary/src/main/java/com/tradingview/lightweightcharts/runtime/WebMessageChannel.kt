package com.tradingview.lightweightcharts.runtime

import android.annotation.SuppressLint
import android.net.Uri
import android.webkit.WebView
import androidx.webkit.WebMessageCompat
import androidx.webkit.WebMessagePortCompat
import androidx.webkit.WebViewCompat
import com.google.gson.JsonParseException
import com.tradingview.lightweightcharts.Logger
import com.tradingview.lightweightcharts.api.exception.ChartBridgeException
import com.tradingview.lightweightcharts.api.serializer.gson.GsonProvider
import com.tradingview.lightweightcharts.runtime.messaging.*

@SuppressLint("RequiresFeature")
class WebMessageChannel(private val logLevel: LogLevel, ports: List<WebMessagePortCompat>) {
    private val serializer = GsonProvider.newInstance()

    private val jsPort = ports[1]
    private val nativePort = ports[0]
    private var onBridgeMessageListener: BridgeMessageListener? = null

    init {
        nativePort.setWebMessageCallback(object : WebMessagePortCompat.WebMessageCallbackCompat() {
            @Suppress("TooGenericExceptionCaught")
            override fun onMessage(port: WebMessagePortCompat, webMessage: WebMessageCompat?) {
                if (webMessage == null) {
                    Logger.w("Web message is null")
                    return
                }

                val bridgeMessage = try {
                    bridgeMessageOf(webMessage)
                } catch (e: Exception) {
                    onBridgeMessageListener?.onError(ChartBridgeException.MalformedMessageError(e))
                    return
                }
                onBridgeMessageListener?.onMessage(bridgeMessage)
            }
        })
    }

    fun initConnection(webView: WebView) {
        WebViewCompat.postWebMessage(
            webView,
            webMessageOf(ConnectionMessage(logLevel), jsPort),
            Uri.EMPTY
        )
    }

    fun sendMessage(bridgeMessage: BridgeMessage) {
        nativePort.postMessage(webMessageOf(bridgeMessage))
    }

    fun setOnBridgeMessageListener(listener: BridgeMessageListener?) {
        onBridgeMessageListener = listener
    }

    private fun webMessageOf(
        bridgeMessage: BridgeMessage,
        port: WebMessagePortCompat? = null
    ): WebMessageCompat {
        val jsonMessage = serializer.toJson(bridgeMessage)
        return WebMessageCompat(jsonMessage, port?.let { arrayOf(it) })
    }

    private fun bridgeMessageOf(webMessage: WebMessageCompat): BridgeMessage {
        val message: BridgeMessage? = serializer.fromJson(
            webMessage.data,
            BridgeMessage::class.java
        )

        // Gson bypasses Kotlin null-safety, so a malformed message can leave these fields null
        @Suppress("SENSELESS_COMPARISON")
        if (message == null || message.messageType == null || message.data == null) {
            throw JsonParseException("Bridge message has no type or data")
        }

        return when (message.messageType) {
            MessageType.FUNCTION_RESULT -> BridgeFunctionResult(message)
            MessageType.SUBSCRIBE_RESULT -> BridgeSubscribeResult(message)
            MessageType.UNSUBSCRIBE_RESULT -> BridgeUnsubscribeResult(message)
            MessageType.FATAL_ERROR -> BridgeFatalError(message)
            else -> message
        }
    }

    interface BridgeMessageListener {
        fun onMessage(bridgeMessage: BridgeMessage)

        fun onError(error: ChartBridgeException) {
            throw error
        }
    }
}
