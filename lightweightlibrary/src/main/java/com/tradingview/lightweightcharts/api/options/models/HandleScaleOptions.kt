package com.tradingview.lightweightcharts.api.options.models

data class HandleScaleOptions(
    var mouseWheel: Boolean? = null,
    var pinch: Boolean? = null,
    var axisPressedMouseMove: AxisPressedMouseMoveOptions? = null,
    var axisFloatClickReset: Boolean? = null
)

data class AxisPressedMouseMoveOptions(
    var time: Boolean? = null,
    var price: Boolean? = null
)

inline fun handleScaleOptions(init: HandleScaleOptions.() -> Unit): HandleScaleOptions {
    return HandleScaleOptions().apply(init)
}

inline fun axisPressedMouseMoveOptions(
    init: AxisPressedMouseMoveOptions.() -> Unit
): AxisPressedMouseMoveOptions {
    return AxisPressedMouseMoveOptions().apply(init)
}