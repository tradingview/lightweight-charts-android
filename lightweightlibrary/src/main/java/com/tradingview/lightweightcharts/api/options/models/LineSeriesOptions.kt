package com.tradingview.lightweightcharts.api.options.models

import androidx.annotation.ColorInt
import com.google.gson.annotations.JsonAdapter
import com.tradingview.lightweightcharts.api.series.enums.LineStyle
import com.tradingview.lightweightcharts.api.series.enums.LineType
import com.tradingview.lightweightcharts.api.series.enums.LineWidth
import com.tradingview.lightweightcharts.api.options.common.LineStyleOptions
import com.tradingview.lightweightcharts.api.series.enums.PriceLineSource
import com.tradingview.lightweightcharts.api.series.models.ColorAdapter
import com.tradingview.lightweightcharts.api.series.models.IntColor
import com.tradingview.lightweightcharts.api.series.models.PriceFormat
import com.tradingview.lightweightcharts.api.series.models.PriceScaleId
import com.tradingview.lightweightcharts.runtime.plugins.Plugin

data class LineSeriesOptions(
    override var title: String? = null,
    override var lastValueVisible: Boolean? = null,
    override var priceLineVisible: Boolean? = null,
    override var priceLineSource: PriceLineSource? = null,
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
    override var color: IntColor? = null,

    override var lineStyle: LineStyle? = null,
    override var lineWidth: LineWidth? = null,
    override var lineType: LineType? = null,
    override var crosshairMarkerVisible: Boolean? = null,
    override var crosshairMarkerRadius: Float? = null,

    @ColorInt
    @JsonAdapter(ColorAdapter::class)
    override val crosshairMarkerBorderColor: IntColor? = null,

    @ColorInt
    @JsonAdapter(ColorAdapter::class)
    override val crosshairMarkerBackgroundColor: IntColor? = null,

    override var overlay: Boolean? = null,
    override var scaleMargins: PriceScaleMargins? = null,
    override var priceScaleId: PriceScaleId? = null,
    override var autoscaleInfoProvider: Plugin? = null,
    override var visible: Boolean? = null
) : SeriesOptionsCommon, LineStyleOptions

inline fun lineSeriesOptions(init: LineSeriesOptions.() -> Unit): LineSeriesOptions {
    return LineSeriesOptions().apply(init)
}