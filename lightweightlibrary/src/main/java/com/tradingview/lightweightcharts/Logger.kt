package com.tradingview.lightweightcharts

import android.util.Log

object Logger {

    private const val TAG = "LIGHTWEIGHT_CHARTS"

    fun printD(source: Any) {
        Log.d(TAG, source.toString())
    }

    fun printE(source: Any) {
        Log.e(TAG, source.toString())
    }

    fun printW(source: Any) {
        Log.e(TAG, source.toString())
    }

}