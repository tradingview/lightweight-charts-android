package com.tradingview.lightweightcharts.api.series.models

/**
 * Represents a range of bars and the number of bars outside the range.
 */
data class BarsInfo(
    /**
     * The time of the first bar in the range, if any bars are in the range.
     */
    val from: Time? = null,

    /**
     * The time of the last bar in the range, if any bars are in the range.
     */
    val to: Time? = null,

    /**
     * The number of bars before the start of the range.
     * Positive if there are some bars before the range,
     * negative if the first bar of the series is inside the range.
     */
    val barsBefore: Float,

    /**
     * The number of bars after the end of the range.
     * Positive if there are some bars after the range,
     * negative if the last bar of the series is inside the range.
     */
    val barsAfter: Float,
)
