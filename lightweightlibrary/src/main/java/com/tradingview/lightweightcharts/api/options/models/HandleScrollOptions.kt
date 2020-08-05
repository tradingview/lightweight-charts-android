package com.tradingview.lightweightcharts.api.options.models

data class HandleScrollOptions(
    val mouseWheel: Boolean? = null,
    val pressedMouseMove: Boolean? = null,
    val horzTouchDrag: Boolean? = null,
    val vertTouchDrag: Boolean? = null
)