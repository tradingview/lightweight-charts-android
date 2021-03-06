package com.tradingview.lightweightcharts.api.options.models

import androidx.annotation.ColorInt
import com.google.gson.annotations.JsonAdapter
import com.tradingview.lightweightcharts.api.series.enums.LineStyle
import com.tradingview.lightweightcharts.api.series.enums.LineWidth
import com.tradingview.lightweightcharts.api.series.models.ColorAdapter
import com.tradingview.lightweightcharts.api.series.models.IntColor

data class PriceLineOptions(
    var price: Float? = null,

    @ColorInt
    @JsonAdapter(ColorAdapter::class)
    var color: IntColor? = null,

    var lineWidth: LineWidth? = null,
    var lineStyle: LineStyle? = null,
    var axisLabelVisible: Boolean? = null,
    var title: String? = null
)

inline fun priceLineOptions(init: PriceLineOptions.() -> Unit): PriceLineOptions {
    return PriceLineOptions().apply(init)
}