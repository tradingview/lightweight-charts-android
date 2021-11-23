package com.tradingview.lightweightcharts.api.options.common

import com.tradingview.lightweightcharts.api.chart.models.color.Colorable
import com.tradingview.lightweightcharts.api.series.enums.LastPriceAnimationMode
import com.tradingview.lightweightcharts.api.series.enums.LineStyle
import com.tradingview.lightweightcharts.api.series.enums.LineWidth
import com.tradingview.lightweightcharts.api.series.models.BaseValueType

/**
 * Represents style options for a baseline series.
 */
interface BaselineStyleOptions {
    val baseValue: BaseValueType

    val topFillColor1: Colorable

    val topFillColor2: Colorable

    val topLineColor: Colorable

    val bottomFillColor1: Colorable

    val bottomFillColor2: Colorable

    val bottomLineColor: Colorable

    val lineWidth: LineWidth

    val lineStyle: LineStyle

    val crosshairMarkerVisible: Boolean

    val crosshairMarkerRadius: Float

    val crosshairMarkerBorderColor: Colorable

    val crosshairMarkerBackgroundColor: Colorable

    val lastPriceAnimation: LastPriceAnimationMode
}