package com.tradingview.lightweightcharts.api.options.models

import androidx.annotation.ColorInt
import com.google.gson.annotations.JsonAdapter
import com.tradingview.lightweightcharts.api.options.common.CandlestickStyleOptions
import com.tradingview.lightweightcharts.api.series.enums.LineStyle
import com.tradingview.lightweightcharts.api.series.enums.LineWidth
import com.tradingview.lightweightcharts.api.series.models.ColorAdapter
import com.tradingview.lightweightcharts.api.series.models.IntColor
import com.tradingview.lightweightcharts.api.series.models.PriceFormat
import com.tradingview.lightweightcharts.api.series.models.PriceScaleId
import com.tradingview.lightweightcharts.runtime.plugins.Plugin

data class CandlestickSeriesOptions(
    override var title: String? = null,
    override var lastValueVisible: Boolean? = null,
    override var priceLineVisible: Boolean? = null,
    override var priceLineWidth: LineWidth? = null,

    @ColorInt
    @JsonAdapter(ColorAdapter::class)
    override var priceLineColor: IntColor? = null,
    override var priceLineStyle: LineStyle? = null,
    override var priceFormat: PriceFormat? = null,
    override var baseLineVisible: Boolean? = null,

    @ColorInt
    @JsonAdapter(ColorAdapter::class)
    override var baseLineColor: IntColor? = null,

    override var baseLineWidth: LineWidth? = null,
    override var baseLineStyle: LineStyle? = null,

    @ColorInt
    @JsonAdapter(ColorAdapter::class)
    override var upColor: IntColor? = null,

    @ColorInt
    @JsonAdapter(ColorAdapter::class)
    override var downColor: IntColor? = null,

    override var wickVisible: Boolean? = null,
    override var borderVisible: Boolean? = null,

    @ColorInt
    @JsonAdapter(ColorAdapter::class)
    override var borderColor: IntColor? = null,

    @ColorInt
    @JsonAdapter(ColorAdapter::class)
    override var borderUpColor: IntColor? = null,

    @ColorInt
    @JsonAdapter(ColorAdapter::class)
    override var borderDownColor: IntColor? = null,

    @ColorInt
    @JsonAdapter(ColorAdapter::class)
    override var wickColor: IntColor? = null,

    @ColorInt
    @JsonAdapter(ColorAdapter::class)
    override var wickUpColor: IntColor? = null,

    @ColorInt
    @JsonAdapter(ColorAdapter::class)
    override var wickDownColor: IntColor? = null,

    override var overlay: Boolean = false,
    override var scaleMargins: PriceScaleMargins? = null,
    override var priceScaleId: PriceScaleId? = null,
    override val autoscaleInfoProvider: Plugin? = null
) : SeriesOptionsCommon, CandlestickStyleOptions

inline fun candlestickSeriesOptions(init: CandlestickSeriesOptions.() -> Unit): CandlestickSeriesOptions {
    return CandlestickSeriesOptions().apply(init)
}