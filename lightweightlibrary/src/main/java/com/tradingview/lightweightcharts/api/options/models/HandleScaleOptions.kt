package com.tradingview.lightweightcharts.api.options.models

data class HandleScaleOptions(
    var mouseWheel: Boolean? = null,
    var pinch: Boolean? = null,
    var axisPressedMouseMove: Boolean? = null,
    var axisFloatClickReset: Boolean? = null
)

inline fun handleScaleOptions(init: HandleScaleOptions.() -> Unit): HandleScaleOptions {
    return HandleScaleOptions().apply(init)
}