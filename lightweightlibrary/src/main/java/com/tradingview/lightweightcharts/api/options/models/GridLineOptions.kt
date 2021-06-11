package com.tradingview.lightweightcharts.api.options.models

import com.tradingview.lightweightcharts.api.series.enums.LineStyle
import com.tradingview.lightweightcharts.api.series.models.ColorWrapper

/** 
 * Structure describing horizontal or vertical grid line options 
 */
data class GridLineOptions(
    /** 
     * Color of the lines 
     */
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