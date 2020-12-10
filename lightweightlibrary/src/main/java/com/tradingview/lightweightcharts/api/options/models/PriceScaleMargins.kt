package com.tradingview.lightweightcharts.api.options.models

/**
 * Defines margins of the price scale
 */
data class PriceScaleMargins(
    /**
     * Top margin in percentages.
     * Must be greater or equal to 0 and less than 100
     */
    var top: Float? = null,

    /**
     *  Bottom margin in percentages.
     *  Must be greater or equal to 0 and less than 100
     */
    var bottom: Float? = null
)

inline fun priceScaleMargins(init: PriceScaleMargins.() -> Unit): PriceScaleMargins {
    return PriceScaleMargins().apply(init)
}