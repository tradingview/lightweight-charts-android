package com.tradingview.lightweightcharts.api.options.models

import com.tradingview.lightweightcharts.api.options.common.BarStyleOptions
import com.tradingview.lightweightcharts.api.series.enums.LineStyle
import com.tradingview.lightweightcharts.api.series.enums.LineWidth
import com.tradingview.lightweightcharts.api.series.models.PriceFormat
import com.tradingview.lightweightcharts.api.series.models.PriceScaleId

data class BarSeriesOptions(
    override var title: String? = null,
    override var lastValueVisible: Boolean? = null,
    override var priceLineVisible: Boolean? = null,
    override var priceLineWidth: LineWidth? = null,
    override var priceLineColor: String? = null,
    override var priceLineStyle: LineStyle? = null,
    override var priceFormat: PriceFormat? = null,
    override var baseLineVisible: Boolean? = null,
    override var baseLineColor: String? = null,
    override var baseLineWidth: LineWidth? = null,
    override var baseLineStyle: LineStyle? = null,

    override var upColor: String? = null,
    override var downColor: String? = null,
    override var openVisible: Boolean? = null,
    override var thinBars: Boolean? = null,
    override var overlay: Boolean = false,
    override var scaleMargins: PriceScaleMargins? = null,
    override var priceScaleId: PriceScaleId? = null
) : SeriesOptionsCommon, BarStyleOptions

inline fun barSeriesOptions(init: BarSeriesOptions.() -> Unit): BarSeriesOptions {
    return BarSeriesOptions().apply(init)
}