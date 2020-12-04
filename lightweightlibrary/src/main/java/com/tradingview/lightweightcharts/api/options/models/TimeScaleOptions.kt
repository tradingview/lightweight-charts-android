package com.tradingview.lightweightcharts.api.options.models

import com.tradingview.lightweightcharts.runtime.plugins.Plugin

data class TimeScaleOptions(
        var rightOffset: Float? = null,
        var barSpacing: Float? = null,
        var fixLeftEdge: Boolean? = null,
        var lockVisibleTimeRangeOnResize: Boolean? = null,
        var rightBarStaysOnScroll: Boolean? = null,
        var borderVisible: Boolean? = null,
        var borderColor: String? = null,
        var visible: Boolean? = null,
        var timeVisible: Boolean? = null,
        var secondsVisible: Boolean? = null,
        var tickMarkFormatter: Plugin? = null
)

inline fun timeScaleOptions(init: TimeScaleOptions.() -> Unit): TimeScaleOptions {
        return TimeScaleOptions().apply(init)
}
