package com.tradingview.lightweightcharts.api.options.models

import com.tradingview.lightweightcharts.api.options.common.AreaStyleOptions
import com.tradingview.lightweightcharts.api.series.enums.LineStyle
import com.tradingview.lightweightcharts.api.series.enums.LineType
import com.tradingview.lightweightcharts.api.series.enums.LineWidth
import com.tradingview.lightweightcharts.api.series.enums.PriceLineSource
import com.tradingview.lightweightcharts.api.series.models.color.Colorable
import com.tradingview.lightweightcharts.api.series.models.PriceFormat
import com.tradingview.lightweightcharts.api.series.models.PriceScaleId
import com.tradingview.lightweightcharts.api.series.models.color.IntColor
import com.tradingview.lightweightcharts.runtime.plugins.Plugin

data class AreaSeriesOptions(
    override var title: String? = null,
    override var lastValueVisible: Boolean? = null,
    override var priceLineVisible: Boolean? = null,
    override var priceLineSource: PriceLineSource? = null,
    override var priceLineWidth: LineWidth? = null,

    override var priceLineColor: IntColor? = null,

    override var priceLineStyle: LineStyle? = null,
    override var priceFormat: PriceFormat? = null,
    override var baseLineVisible: Boolean? = null,

    override var baseLineColor: Colorable? = null,

    override var baseLineWidth: LineWidth? = null,
    override var baseLineStyle: LineStyle? = null,

    override var topColor: Colorable? = null,

    override var bottomColor: Colorable? = null,

    override var lineColor: Colorable? = null,

    override var lineStyle: LineStyle? = null,
    override var lineWidth: LineWidth? = null,
    override var lineType: LineType? = null,
    override var crosshairMarkerVisible: Boolean? = null,
    override var crosshairMarkerRadius: Float? = null,

    override var crosshairMarkerBorderColor: Colorable? = null,

    override var crosshairMarkerBackgroundColor: Colorable? = null,

    override var overlay: Boolean? = null,
    override var scaleMargins: PriceScaleMargins? = null,
    override var priceScaleId: PriceScaleId? = null,
    override var autoscaleInfoProvider: Plugin? = null,
    override var visible: Boolean? = null
) : SeriesOptionsCommon, AreaStyleOptions

inline fun areaSeriesOptions(init: AreaSeriesOptions.() -> Unit): AreaSeriesOptions {
    return AreaSeriesOptions().apply(init)
}