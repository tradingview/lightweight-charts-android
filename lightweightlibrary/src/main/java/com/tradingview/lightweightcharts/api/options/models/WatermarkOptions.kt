package com.tradingview.lightweightcharts.api.options.models

import androidx.annotation.ColorInt
import com.google.gson.annotations.JsonAdapter
import com.tradingview.lightweightcharts.api.options.enums.HorizontalAlignment
import com.tradingview.lightweightcharts.api.options.enums.VerticalAlignment
import com.tradingview.lightweightcharts.api.series.models.ColorAdapter
import com.tradingview.lightweightcharts.api.series.models.IntColor

/**
 * Structure describing watermark options
 */
data class WatermarkOptions (
    /**
     * Color of the watermark
     */
    @ColorInt
    @JsonAdapter(ColorAdapter::class)
    var color: IntColor? = null,

    /**
     * Visibility of the watermark. If false, other parameters are ignored
     */
    var visible: Boolean? = null,

    /**
     * Font family
     */
    var fontFamily: String? = null,

    /**
     * Font style
     */
    var fontStyle: String? = null,

    /**
     * Text of the watermark. Word wrapping is not supported
     */
    var text: String? = null,

    /**
     * Font size in pixels
     */
    var fontSize: Int? = null,

    /**
     * Horizontal alignment of the watermark inside the chart area
     */
    var horzAlign: HorizontalAlignment? = null,

    /**
     * Vertical alignment of the watermark inside the chart area
     */
    var vertAlign: VerticalAlignment? = null
)

inline fun watermarkOptions(init: WatermarkOptions.() -> Unit): WatermarkOptions {
    return WatermarkOptions().apply(init)
}
