package com.tradingview.lightweightcharts.api.options.models

import com.tradingview.lightweightcharts.api.series.enums.LineStyle
import com.tradingview.lightweightcharts.api.series.enums.LineType
import com.tradingview.lightweightcharts.api.series.enums.LineWidth
import com.tradingview.lightweightcharts.api.options.common.AreaStyleOptions
import com.tradingview.lightweightcharts.api.series.models.PriceFormat

data class AreaSeriesOptions(
    override val title: String? = null,
    override val lastValueVisible: Boolean? = null,
    override val priceLineVisible: Boolean? = null,
    override val priceLineWidth: LineWidth? = null,
    override val priceLineColor: String? = null,
    override val priceLineStyle: LineStyle? = null,
    override val priceFormat: PriceFormat? = null,
    override val baseLineVisible: Boolean? = null,
    override val baseLineColor: String? = null,
    override val baseLineWidth: LineWidth? = null,
    override val baseLineStyle: LineStyle? = null,

    override val topColor: String? = null,
    override val bottomColor: String? = null,
    override val lineColor: String? = null,
    override val lineStyle: LineStyle? = null,
    override val lineWidth: LineWidth? = null,
    override val lineType: LineType? = null,
    override val crosshairMarkerVisible: Boolean? = null,
    override val crosshairMarkerRadius: Float? = null,
    override val overlay: Boolean = false,
    override val scaleMargins: PriceScaleMargins? = null
) : SeriesOptionsCommon, AreaStyleOptions