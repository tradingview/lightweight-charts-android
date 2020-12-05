package com.tradingview.lightweightcharts.api.options.models

import com.tradingview.lightweightcharts.api.options.common.HistogramStyleOptions
import com.tradingview.lightweightcharts.api.series.enums.LineStyle
import com.tradingview.lightweightcharts.api.series.enums.LineWidth
import com.tradingview.lightweightcharts.api.series.models.PriceFormat
import com.tradingview.lightweightcharts.api.series.models.PriceScaleId
import com.tradingview.lightweightcharts.runtime.plugins.Plugin

data class HistogramSeriesOptions(
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
    override var color: String? = null,
    override var base: Float? = null,
    override var overlay: Boolean = false,
    override var scaleMargins: PriceScaleMargins? = null,
    override var priceScaleId: PriceScaleId? = null,
    override val autoscaleInfoProvider: Plugin? = null
): SeriesOptionsCommon, HistogramStyleOptions

inline fun histogramSeriesOptions(init: HistogramSeriesOptions.() -> Unit): HistogramSeriesOptions {
    return HistogramSeriesOptions().apply(init)
}