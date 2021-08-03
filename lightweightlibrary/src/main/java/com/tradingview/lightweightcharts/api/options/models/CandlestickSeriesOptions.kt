package com.tradingview.lightweightcharts.api.options.models

import com.tradingview.lightweightcharts.api.options.common.CandlestickStyleOptions
import com.tradingview.lightweightcharts.api.series.enums.LineStyle
import com.tradingview.lightweightcharts.api.series.enums.LineWidth
import com.tradingview.lightweightcharts.api.series.enums.PriceLineSource
import com.tradingview.lightweightcharts.api.series.models.ColorWrapper
import com.tradingview.lightweightcharts.api.series.models.PriceFormat
import com.tradingview.lightweightcharts.api.series.models.PriceScaleId
import com.tradingview.lightweightcharts.runtime.plugins.Plugin

data class CandlestickSeriesOptions(
    override var title: String? = null,
    override var lastValueVisible: Boolean? = null,
    override var priceLineVisible: Boolean? = null,
    override var priceLineSource: PriceLineSource? = null,
    override var priceLineWidth: LineWidth? = null,

    override var priceLineColor: ColorWrapper? = null,
    override var priceLineStyle: LineStyle? = null,
    override var priceFormat: PriceFormat? = null,
    override var baseLineVisible: Boolean? = null,

    override var baseLineColor: ColorWrapper? = null,

    override var baseLineWidth: LineWidth? = null,
    override var baseLineStyle: LineStyle? = null,

    override var upColor: ColorWrapper? = null,

    override var downColor: ColorWrapper? = null,

    override var wickVisible: Boolean? = null,
    override var borderVisible: Boolean? = null,

    override var borderColor: ColorWrapper? = null,

    override var borderUpColor: ColorWrapper? = null,

    override var borderDownColor: ColorWrapper? = null,

    override var wickColor: ColorWrapper? = null,

    override var wickUpColor: ColorWrapper? = null,

    override var wickDownColor: ColorWrapper? = null,

    override var overlay: Boolean? = null,
    override var scaleMargins: PriceScaleMargins? = null,
    override var priceScaleId: PriceScaleId? = null,
    override var autoscaleInfoProvider: Plugin? = null,
    override var visible: Boolean? = null
) : SeriesOptionsCommon, CandlestickStyleOptions

inline fun candlestickSeriesOptions(init: CandlestickSeriesOptions.() -> Unit): CandlestickSeriesOptions {
    return CandlestickSeriesOptions().apply(init)
}