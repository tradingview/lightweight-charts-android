package com.tradingview.lightweightcharts.view

import android.annotation.SuppressLint
import android.content.Context
import android.util.AttributeSet
import android.webkit.WebView
import androidx.webkit.WebViewClientCompat
import androidx.webkit.WebViewCompat
import androidx.webkit.WebViewFeature.*
import com.tradingview.lightweightcharts.BuildConfig
import com.tradingview.lightweightcharts.api.delegates.ChartApiDelegate
import com.tradingview.lightweightcharts.runtime.controller.WebMessageController
import com.tradingview.lightweightcharts.runtime.WebMessageChannel
import java.lang.Exception
import java.lang.UnsupportedOperationException
import java.util.HashMap

@SuppressLint("RequiresFeature")
open class ChartsView(context: Context, attrs: AttributeSet? = null): WebView(context, attrs) {

    companion object {
        private const val DEFAULT_URL = "file:///android_asset/com/tradingview/lightweightcharts/index.html"
        private val features = listOf(
            CREATE_WEB_MESSAGE_CHANNEL,
            POST_WEB_MESSAGE,
            WEB_MESSAGE_PORT_SET_MESSAGE_CALLBACK,
            WEB_MESSAGE_PORT_POST_MESSAGE
        )
    }

    open val debug = BuildConfig.DEBUG

    val api by lazy { ChartApiDelegate(webMessageController) }

    private val webMessageController = WebMessageController()

    var state: State = State.Preparing()
        private set(value) {
            field = value
            onStateChanged.forEach { it.invoke(value) }
        }

    private var onStateChanged: MutableList<((State) -> Unit)> = mutableListOf()

    private val notSupportedFeatures: List<String> = checkSupportedFeature()

    private val channel: WebMessageChannel by lazy {
        WebViewCompat.createWebMessageChannel(this)
            .asList()
            .let { WebMessageChannel(debug, it) }
            .apply { initConnection(this@ChartsView) }
    }


    init {
        @SuppressLint("SetJavaScriptEnabled")
        settings.javaScriptEnabled = true
        settings.domStorageEnabled = true

        webViewClient = object : WebViewClientCompat() {
            override fun onPageFinished(view: WebView?, url: String?) {
                super.onPageFinished(view, url)
                when {
                    notSupportedFeatures.isEmpty() -> {
                        webMessageController.setWebMessageChannel(channel)
                        state = State.Ready()
                    }
                    else -> {
                        state = State.Error(
                            Exception("WebView does not support features: $notSupportedFeatures")
                        )
                    }
                }
            }
        }
    }

    private fun checkSupportedFeature(): List<String> {
        return HashMap<String, Boolean>(features.size)
            .apply { features.forEach { this[it] = isFeatureSupported(it) } }
            .filter { !it.value }
            .keys.toList()
    }

    override fun onAttachedToWindow() {
        super.onAttachedToWindow()
        state = State.Preparing()
        super.loadUrl(DEFAULT_URL)
    }

    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
        webMessageController.clearWebMessageChannel()
    }

    override fun loadUrl(url: String, additionalHttpHeaders: MutableMap<String, String>) {
        throw UnsupportedOperationException()
    }

    override fun loadUrl(url: String) {
        throw UnsupportedOperationException()
    }

    fun subscribeOnChartStateChange(subscriber: (State) -> Unit) {
        onStateChanged.add(subscriber)
        subscriber.invoke(state)
    }

    fun unsubscribeOnChartStateChange(subscriber: (State) -> Unit) {
        onStateChanged.remove(subscriber)
    }

    interface State {
        class Ready : State
        class Preparing: State
        class Error(val exception: Exception): State
    }
}

