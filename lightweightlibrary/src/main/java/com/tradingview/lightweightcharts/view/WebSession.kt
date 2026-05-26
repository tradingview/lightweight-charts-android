package com.tradingview.lightweightcharts.view

import android.annotation.SuppressLint
import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.webkit.WebResourceRequest
import android.webkit.WebView
import android.widget.FrameLayout
import androidx.webkit.WebViewClientCompat
import com.tradingview.lightweightcharts.view.gesture.TouchDelegate

internal class WebSession @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyle: Int = 0
) : WebView(context, attrs, defStyle) {

    private val touchDelegates: MutableList<TouchDelegate> = mutableListOf()

    init {
        layoutParams = FrameLayout.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.MATCH_PARENT
        )
        setBackgroundColor(0x00000000)
        visibility = View.INVISIBLE

        @SuppressLint("SetJavaScriptEnabled")
        settings.javaScriptEnabled = true
        settings.domStorageEnabled = true
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        touchDelegates.forEach { it.beforeTouchEvent(this) }
        return touchDelegates.any { it.onTouchEvent(this, event) } || super.onTouchEvent(event)
    }

    fun addTouchDelegate(touchDelegate: TouchDelegate) {
        touchDelegates.add(touchDelegate)
    }

    fun removeTouchDelegate(touchDelegate: TouchDelegate) {
        touchDelegates.remove(touchDelegate)
    }

    fun onSessionReady(onReady: () -> Unit) {
        webViewClient = object : WebViewClientCompat() {
            override fun onPageFinished(view: WebView?, url: String?) {
                super.onPageFinished(view, url)
                visibility = View.VISIBLE
                onReady()
            }

            override fun shouldOverrideUrlLoading(view: WebView, request: WebResourceRequest): Boolean {
                if (!request.isForMainFrame) {
                    return false
                }
                return openExternalUrl(request.url)
            }

            @Suppress("OVERRIDE_DEPRECATION")
            override fun shouldOverrideUrlLoading(view: WebView?, url: String?): Boolean {
                return openExternalUrl(url?.let(Uri::parse))
            }
        }
    }

    private fun openExternalUrl(uri: Uri?): Boolean {
        val scheme = uri?.scheme ?: return false
        if (scheme != "http" && scheme != "https") {
            return false
        }

        return try {
            context.startActivity(Intent(Intent.ACTION_VIEW, uri).apply {
                addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            })
            true
        } catch (_: ActivityNotFoundException) {
            true
        }
    }
}
