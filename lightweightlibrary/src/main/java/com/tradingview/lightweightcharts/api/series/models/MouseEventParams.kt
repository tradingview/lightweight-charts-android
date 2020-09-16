package com.tradingview.lightweightcharts.api.series.models

data class MouseEventParams(
    val time: Time? = null,
    val point: Point? = null,
    val seriesPrices: Array<BarPrices>? = null
)