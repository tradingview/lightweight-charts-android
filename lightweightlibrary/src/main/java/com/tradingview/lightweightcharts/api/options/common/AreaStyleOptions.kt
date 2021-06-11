package com.tradingview.lightweightcharts.api.options.common

import com.tradingview.lightweightcharts.api.series.enums.LineStyle
import com.tradingview.lightweightcharts.api.series.enums.LineType
import com.tradingview.lightweightcharts.api.series.enums.LineWidth
import com.tradingview.lightweightcharts.api.series.models.ColorWrapper
import com.tradingview.lightweightcharts.api.series.models.ColorWrapper.IntColor

interface AreaStyleOptions {
    val topColor: ColorWrapper?
    val bottomColor: ColorWrapper?
    val lineColor: ColorWrapper?
    val lineStyle: LineStyle?
    val lineWidth: LineWidth?
    val lineType: LineType?
    val crosshairMarkerVisible: Boolean?
    val crosshairMarkerRadius: Float?
    val crosshairMarkerBorderColor: ColorWrapper?
    val crosshairMarkerBackgroundColor: ColorWrapper?
}