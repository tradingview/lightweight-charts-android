package com.tradingview.lightweightcharts.api.options.models

data class TimeScaleOptions(
    val rightOffset: Float?= null,
    val barSpacing: Float? = null,
    val fixLeftEdge: Boolean? = null,
    val lockVisibleTimeRangeOnResize: Boolean? = null,
    val rightBarStaysOnScroll: Boolean? = null,
    val borderVisible: Boolean? = null,
    val borderColor: String? = null,
    val visible: Boolean? = null,
    val timeVisible: Boolean? = null,
    val secondsVisible: Boolean? = null
)
