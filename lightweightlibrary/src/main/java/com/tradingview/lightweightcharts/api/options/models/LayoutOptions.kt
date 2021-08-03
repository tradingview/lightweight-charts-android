package com.tradingview.lightweightcharts.api.options.models

import com.tradingview.lightweightcharts.api.series.models.color.IntColor

data class LayoutOptions(
    /**
     * Background color of the chart area and the scales
     */
    var backgroundColor: IntColor? = null,

    /**
     * Color of a text on the scales
     */
    var textColor: IntColor? = null,

    /**
     * Font size of a text on the scales in pixels
     */
    var fontSize: Int? = null,

    /**
     * Font family of a text on the scales
     */
    var fontFamily: String? = null
)

inline fun layoutOptions(init: LayoutOptions.() -> Unit): LayoutOptions {
    return LayoutOptions().apply(init)
}