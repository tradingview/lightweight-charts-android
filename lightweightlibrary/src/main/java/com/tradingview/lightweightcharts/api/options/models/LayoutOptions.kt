package com.tradingview.lightweightcharts.api.options.models

import androidx.annotation.ColorInt
import com.google.gson.annotations.JsonAdapter
import com.tradingview.lightweightcharts.api.series.models.ColorAdapter
import com.tradingview.lightweightcharts.api.series.models.IntColor

data class LayoutOptions(
    /**
     * Background color of the chart area and the scales
     */
    @ColorInt
    @JsonAdapter(ColorAdapter::class)
    var backgroundColor: IntColor? = null,

    /**
     * Color of a text on the scales
     */
    @ColorInt
    @JsonAdapter(ColorAdapter::class)
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