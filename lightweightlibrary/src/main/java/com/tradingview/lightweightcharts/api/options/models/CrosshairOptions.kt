package com.tradingview.lightweightcharts.api.options.models

import com.tradingview.lightweightcharts.api.series.enums.CrosshairMode

/** Structure describing crosshair options  */
data class CrosshairOptions(
    /** Crosshair mode */
    val mode: CrosshairMode? = null,
    /** Options of the crosshair vertical line */
    val vertLine: CrosshairLineOptions? = null,
    /** Options of the crosshair horizontal line */
    val horzLine: CrosshairLineOptions? = null
)
