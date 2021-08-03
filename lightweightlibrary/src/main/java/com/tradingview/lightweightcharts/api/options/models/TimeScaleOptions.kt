package com.tradingview.lightweightcharts.api.options.models

import com.tradingview.lightweightcharts.api.series.models.color.IntColor
import com.tradingview.lightweightcharts.runtime.plugins.Plugin

data class TimeScaleOptions(
    var rightOffset: Float? = null,
    var barSpacing: Float? = null,
    var fixLeftEdge: Boolean? = null,
    var lockVisibleTimeRangeOnResize: Boolean? = null,
    var rightBarStaysOnScroll: Boolean? = null,
    var borderVisible: Boolean? = null,

    var borderColor: IntColor? = null,
    var visible: Boolean? = null,
    var timeVisible: Boolean? = null,
    var secondsVisible: Boolean? = null,
    var shiftVisibleRangeOnNewBar: Boolean? = null,
    var tickMarkFormatter: Plugin? = null
)

inline fun timeScaleOptions(init: TimeScaleOptions.() -> Unit): TimeScaleOptions {
        return TimeScaleOptions().apply(init)
}
