package com.tradingview.lightweightcharts.api.options.models

import androidx.annotation.ColorInt
import com.google.gson.annotations.JsonAdapter
import com.tradingview.lightweightcharts.api.options.common.BarStyleOptions
import com.tradingview.lightweightcharts.api.series.enums.LineStyle
import com.tradingview.lightweightcharts.api.series.enums.LineWidth
import com.tradingview.lightweightcharts.api.series.enums.PriceLineSource
import com.tradingview.lightweightcharts.api.series.models.ColorAdapter
import com.tradingview.lightweightcharts.api.series.models.ColorWrapper
import com.tradingview.lightweightcharts.api.series.models.ColorWrapper.IntColor
import com.tradingview.lightweightcharts.api.series.models.PriceFormat
import com.tradingview.lightweightcharts.api.series.models.PriceScaleId
import com.tradingview.lightweightcharts.runtime.plugins.Plugin

data class BarSeriesOptions(
    override var title: String? = null,
    override var lastValueVisible: Boolean? = null,
    override var priceLineVisible: Boolean? = null,
    override var priceLineSource: PriceLineSource? = null,
    override var priceLineWidth: LineWidth? = null,

    @JsonAdapter(ColorAdapter::class)
    override var priceLineColor: ColorWrapper? = null,

    override var priceLineStyle: LineStyle? = null,
    override var priceFormat: PriceFormat? = null,
    override var baseLineVisible: Boolean? = null,

    @JsonAdapter(ColorAdapter::class)
    override var baseLineColor: ColorWrapper? = null,

    override var baseLineWidth: LineWidth? = null,
    override var baseLineStyle: LineStyle? = null,

    @JsonAdapter(ColorAdapter::class)
    override var upColor: ColorWrapper? = null,

    @JsonAdapter(ColorAdapter::class)
    override var downColor: ColorWrapper? = null,

    override var openVisible: Boolean? = null,
    override var thinBars: Boolean? = null,
    override var overlay: Boolean? = null,
    override var scaleMargins: PriceScaleMargins? = null,
    override var priceScaleId: PriceScaleId? = null,
    override var autoscaleInfoProvider: Plugin? = null,
    override var visible: Boolean? = null
) : SeriesOptionsCommon, BarStyleOptions

inline fun barSeriesOptions(init: BarSeriesOptions.() -> Unit): BarSeriesOptions {
    return BarSeriesOptions().apply(init)
}