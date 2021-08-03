package com.tradingview.lightweightcharts.api.options.common

import com.tradingview.lightweightcharts.api.series.enums.LineStyle
import com.tradingview.lightweightcharts.api.series.enums.LineType
import com.tradingview.lightweightcharts.api.series.enums.LineWidth
import com.tradingview.lightweightcharts.api.series.models.color.IntColor

interface AreaStyleOptions {
    val topColor: IntColor?
    val bottomColor: IntColor?
    val lineColor: IntColor?
    val lineStyle: LineStyle?
    val lineWidth: LineWidth?
    val lineType: LineType?
    val crosshairMarkerVisible: Boolean?
    val crosshairMarkerRadius: Float?
    val crosshairMarkerBorderColor: IntColor?
    val crosshairMarkerBackgroundColor: IntColor?
}