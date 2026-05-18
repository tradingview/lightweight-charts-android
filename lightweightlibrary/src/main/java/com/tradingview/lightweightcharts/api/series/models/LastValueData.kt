package com.tradingview.lightweightcharts.api.series.models

import com.tradingview.lightweightcharts.api.chart.models.color.IntColor

data class LastValueData(
    val noData: Boolean? = null,
    val price: Float? = null,
    val text: String? = null,
    val formattedPriceAbsolute: String? = null,
    val formattedPricePercentage: String? = null,
    val color: IntColor? = null,
)
