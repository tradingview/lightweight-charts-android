package com.tradingview.lightweightcharts.example.app.view

import android.content.Context
import android.util.AttributeSet
import com.tradingview.lightweightcharts.runtime.messaging.LogLevel
import com.tradingview.lightweightcharts.view.ChartsView

class DebugChartsView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0,
    defStyleRes: Int = 0
): ChartsView(context, attrs, defStyleAttr, defStyleRes) {
    override val logLevel: LogLevel = LogLevel.DEBUG
}