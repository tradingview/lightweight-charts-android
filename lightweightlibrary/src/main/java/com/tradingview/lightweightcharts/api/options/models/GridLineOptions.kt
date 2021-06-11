package com.tradingview.lightweightcharts.api.options.models

import androidx.annotation.ColorInt
import com.google.gson.annotations.JsonAdapter
import com.tradingview.lightweightcharts.api.series.enums.LineStyle
import com.tradingview.lightweightcharts.api.series.models.ColorAdapter
import com.tradingview.lightweightcharts.api.series.models.ColorWrapper
import com.tradingview.lightweightcharts.api.series.models.ColorWrapper.IntColor

/** 
 * Structure describing horizontal or vertical grid line options 
 */
data class GridLineOptions(
    /** 
     * Color of the lines 
     */
    @JsonAdapter(ColorAdapter::class)
    var color: ColorWrapper? = null,

    /** 
     * Style of the lines 
     */
    var style: LineStyle? = null,

    /** 
     * Visibility of the lines 
     */
    var visible: Boolean? = null
)

inline fun gridLineOptions(init: GridLineOptions.() -> Unit): GridLineOptions {
    return GridLineOptions().apply(init)
}