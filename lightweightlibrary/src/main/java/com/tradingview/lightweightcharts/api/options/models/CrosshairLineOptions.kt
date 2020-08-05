package com.tradingview.lightweightcharts.api.options.models

import com.tradingview.lightweightcharts.api.series.enums.LineStyle
import com.tradingview.lightweightcharts.api.series.enums.LineWidth

/** Structure describing a crosshair line (vertical or horizontal) */
data class CrosshairLineOptions(
    /** Color of a certain crosshair line */
    val color: String? = null,
    /** Width of a certain crosshair line and corresponding scale label */
    val width: LineWidth? = null,
    /** Style of a certain crosshair line */
    val style: LineStyle? = null,
    /** Visibility of a certain crosshair line */
    val visible: Boolean? = null,
    /** Visibility of corresponding scale label */
    val labelVisible: Boolean? = null,
    /** Background color of corresponding scale label */
    val labelBackgroundColor: String? = null
)