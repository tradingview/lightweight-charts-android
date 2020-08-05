package com.tradingview.lightweightcharts.api.options.models

import com.tradingview.lightweightcharts.api.series.enums.LineStyle

/** Structure describing horizontal or vertical grid line options */
data class GridLineOptions(
    /** Color of the lines */
    val color: String? = null,

    /** Style of the lines */
    val style: LineStyle? = null,

    /** Visibility of the lines */
    val visible: Boolean? = null
)