package com.tradingview.lightweightcharts.api.options.models

import androidx.annotation.ColorInt
import com.google.gson.annotations.JsonAdapter
import com.tradingview.lightweightcharts.api.series.enums.LineStyle
import com.tradingview.lightweightcharts.api.series.enums.LineWidth
import com.tradingview.lightweightcharts.api.series.models.ColorAdapter
import com.tradingview.lightweightcharts.api.series.models.ColorWrapper
import com.tradingview.lightweightcharts.api.series.models.ColorWrapper.*

/** 
 * Structure describing a crosshair line (vertical or horizontal) 
 */
data class CrosshairLineOptions(
    /** 
     * Color of a certain crosshair line 
     */

    @JsonAdapter(ColorAdapter::class)
    var color: ColorWrapper? = null,
    /** 
     * Width of a certain crosshair line and corresponding scale label 
     */
    var width: LineWidth? = null,

    /** 
     * Style of a certain crosshair line 
     */
    var style: LineStyle? = null,

    /** 
     * Visibility of a certain crosshair line 
     */
    var visible: Boolean? = null,

    /** 
     * Visibility of corresponding scale label 
     */
    var labelVisible: Boolean? = null,

    /** 
     * Background color of corresponding scale label 
     */
    @JsonAdapter(ColorAdapter::class)
    var labelBackgroundColor: ColorWrapper? = null
)

inline fun crosshairLineOptions(init: CrosshairLineOptions.() -> Unit): CrosshairLineOptions {
    return CrosshairLineOptions().apply(init)
}