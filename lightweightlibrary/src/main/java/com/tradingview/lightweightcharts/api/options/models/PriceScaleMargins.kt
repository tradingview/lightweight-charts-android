package com.tradingview.lightweightcharts.api.options.models

/** Defines margins of the price scale */
data class PriceScaleMargins(
    /**
     * Top margin in percentages.
     * Must be greater or equal to 0 and less than 100
     *  */
    val top: Float? = null,

    /**
     *  Bottom margin in percentages.
     *  Must be greater or equal to 0 and less than 100
     * */
    val bottom: Float? = null
)