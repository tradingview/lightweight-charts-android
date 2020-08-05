package com.tradingview.lightweightcharts.api.options.models

import com.tradingview.lightweightcharts.api.options.enums.HorizontalAlignment
import com.tradingview.lightweightcharts.api.options.enums.VerticalAlignment

/** Structure describing watermark options */
data class WatermarkOptions (
    /** Color of the watermark */
    val color: String? = null,
    /** Visibility of the watermark. If false, other parameters are ignored */
    val visible: Boolean? = null,
    /** Text of the watermark. Word wrapping is not supported */
    val text: String? = null,
    /** Font size in pixels */
    val fontSize: Int? = null,
    /** Horizontal alignment of the watermark inside the chart area */
    val horzAlign: HorizontalAlignment? = null,
    /** Vertical alignment of the watermark inside the chart area */
    val vertAlign: VerticalAlignment? = null
)
