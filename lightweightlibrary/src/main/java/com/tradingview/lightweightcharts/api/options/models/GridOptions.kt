package com.tradingview.lightweightcharts.api.options.models

/** Structure describing grid options */
data class GridOptions(
    /** Vertical grid line options */
    val vertLines: GridLineOptions? = null,
    /** Horizontal grid line options */
    val horzLines: GridLineOptions? = null
)
