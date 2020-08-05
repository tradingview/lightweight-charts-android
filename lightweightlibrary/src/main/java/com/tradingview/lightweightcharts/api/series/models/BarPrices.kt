package com.tradingview.lightweightcharts.api.series.models

data class BarPrices (
    val id: String,
    val prices: BarPrice
)

data class BarPrice(
    val open: Float? = null,
    val high: Float? = null,
    val low: Float? = null,
    val close: Float? = null,
    val value: Float? = null
)