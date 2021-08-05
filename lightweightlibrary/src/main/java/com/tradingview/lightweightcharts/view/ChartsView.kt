package com.tradingview.lightweightcharts.view

import android.annotation.SuppressLint
import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.view.ViewGroup.LayoutParams.MATCH_PARENT
import android.webkit.WebView
import android.widget.FrameLayout
import androidx.webkit.WebViewClientCompat
import androidx.webkit.WebViewCompat
import androidx.webkit.WebViewFeature.*
import com.tradingview.lightweightcharts.Logger
import com.tradingview.lightweightcharts.api.delegates.ChartApiDelegate
import com.tradingview.lightweightcharts.runtime.controller.WebMessageController
import com.tradingview.lightweightcharts.runtime.WebMessageChannel
import com.tradingview.lightweightcharts.runtime.messaging.LogLevel
import java.lang.Exception
import java.util.HashMap

@SuppressLint("RequiresFeature")
open class ChartsView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0,
    defStyleRes: Int = 0
) : FrameLayout(context, attrs, defStyleAttr, defStyleRes) {

    companion object {
        private const val DEFAULT_URL = "file:///android_asset/com/tradingview/lightweightcharts/index.html"
        private val features = listOf(
            CREATE_WEB_MESSAGE_CHANNEL,
            POST_WEB_MESSAGE,
            WEB_MESSAGE_PORT_SET_MESSAGE_CALLBACK,
            WEB_MESSAGE_PORT_POST_MESSAGE
        )
    }

    private val webView = WebView(context, attrs, defStyleRes)

    open val logLevel = LogLevel.WARNING

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
        WebViewCompat.createWebMessageChannel(webView)
            .asList()
            .let { WebMessageChannel(logLevel, it) }
            .apply { initConnection(webView) }
    }


    init {
        with(webView) {
            layoutParams = LayoutParams(MATCH_PARENT, MATCH_PARENT)
            setBackgroundColor(0x00000000)
            visibility = View.INVISIBLE

            @SuppressLint("SetJavaScriptEnabled")
            settings.javaScriptEnabled = true
            settings.domStorageEnabled = true
            webViewClient = object : WebViewClientCompat() {
                override fun onPageFinished(view: WebView?, url: String?) {
                    super.onPageFinished(view, url)
                    when {
                        notSupportedFeatures.isEmpty() -> {
                            visibility = View.VISIBLE
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
        addView(webView)
    }

    private fun checkSupportedFeature(): List<String> {
        return HashMap<String, Boolean>(features.size)
            .apply { features.forEach { this[it] = isFeatureSupported(it) } }
            .filter { !it.value }
            .keys.toList()
    }

    override fun onAttachedToWindow() {
        super.onAttachedToWindow()
        if (state is State.Ready) {
            Logger.d("Reattach to window")
            webMessageController.setWebMessageChannel(channel)
        } else {
            state = State.Preparing()
            webView.loadUrl(DEFAULT_URL)
        }
    }

    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
        webMessageController.clearWebMessageChannel()
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

