package com.tradingview.lightweightcharts.api.options.common

import com.tradingview.lightweightcharts.api.series.enums.LineStyle
import com.tradingview.lightweightcharts.api.series.enums.LineType
import com.tradingview.lightweightcharts.api.series.enums.LineWidth
import com.tradingview.lightweightcharts.api.series.models.color.Colorable

interface LineStyleOptions {
    val color: Colorable?
    val lineStyle: LineStyle?
    val lineWidth: LineWidth?
    val lineType: LineType?
    val crosshairMarkerVisible: Boolean?
    val crosshairMarkerRadius: Float?
    val crosshairMarkerBorderColor: Colorable?
    val crosshairMarkerBackgroundColor: Colorable?
}