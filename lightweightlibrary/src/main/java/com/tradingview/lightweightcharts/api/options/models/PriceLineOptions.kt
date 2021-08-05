package com.tradingview.lightweightcharts.api.options.models

import com.tradingview.lightweightcharts.api.series.enums.LineStyle
import com.tradingview.lightweightcharts.api.series.enums.LineWidth
import com.tradingview.lightweightcharts.api.chart.models.color.IntColor

data class PriceLineOptions(
    var price: Float? = null,

    var color: IntColor? = null,

    var lineWidth: LineWidth? = null,
    var lineStyle: LineStyle? = null,
    var axisLabelVisible: Boolean? = null,
    var title: String? = null
)

inline fun priceLineOptions(init: PriceLineOptions.() -> Unit): PriceLineOptions {
    return PriceLineOptions().apply(init)
}