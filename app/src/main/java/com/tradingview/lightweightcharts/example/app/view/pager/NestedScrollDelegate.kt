package com.tradingview.lightweightcharts.example.app.view.pager

import android.content.Context
import android.util.TypedValue
import android.view.GestureDetector
import android.view.MotionEvent
import android.view.ViewGroup
import com.tradingview.lightweightcharts.view.gesture.TouchDelegate
import kotlin.math.abs

class NestedScrollDelegate(private val context: Context) : TouchDelegate {
    private var lastXDown = 0
    private var limitToScrollParent = 0.0
    private val scrollingPart = 0.8

    private lateinit var gestureDetector: GestureDetector

    override fun beforeTouchEvent(view: ViewGroup) {
        if (!::gestureDetector.isInitialized) {
            val listener = object : GestureDetector.SimpleOnGestureListener() {
                override fun onLongPress(e: MotionEvent) {
                    view.requestDisallowInterceptTouchEvent(true)
                }
            }
            gestureDetector = GestureDetector(context, listener)
        }
    }

    override fun onTouchEvent(view: ViewGroup, event: MotionEvent): Boolean {
        gestureDetector.onTouchEvent(event)

        val eventX = event.x.toInt()
        val deltaX = lastXDown - eventX
        limitToScrollParent = view.width * scrollingPart

        when (event.action) {
            MotionEvent.ACTION_MOVE -> {
                if (isHorizontalScroll(deltaX)) {
                    view.requestDisallowInterceptTouchEvent(true)
                }
                return false
            }

            MotionEvent.ACTION_DOWN -> {
                if (eventX > limitToScrollParent) {
                    view.requestDisallowInterceptTouchEvent(true)
                }
                lastXDown = eventX
                return false
            }

            MotionEvent.ACTION_UP, MotionEvent.ACTION_CANCEL -> {
                view.requestDisallowInterceptTouchEvent(false)
                lastXDown = 0
                return false
            }
            else -> return false
        }
    }

    private fun isHorizontalScroll(deltaX: Int): Boolean {
        val horizontalScrollingDelta = dpToPx(10)
        return abs(deltaX) > horizontalScrollingDelta
    }

    private fun dpToPx(value: Int): Int {
        return TypedValue.applyDimension(
            TypedValue.COMPLEX_UNIT_DIP,
            value.toFloat(),
            context.resources.displayMetrics).toInt()
    }
}