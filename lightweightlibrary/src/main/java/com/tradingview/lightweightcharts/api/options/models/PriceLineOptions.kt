package com.tradingview.lightweightcharts.api.options.models

import com.tradingview.lightweightcharts.api.series.enums.LineStyle
import com.tradingview.lightweightcharts.api.series.enums.LineWidth

data class PriceLineOptions(
    val price: Float? = null,
    val color: String? = null,
    val lineWidth: LineWidth? = null,
    val lineStyle: LineStyle? = null
)
