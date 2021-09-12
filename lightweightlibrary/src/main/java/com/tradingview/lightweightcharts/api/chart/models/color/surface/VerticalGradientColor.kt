package com.tradingview.lightweightcharts.api.chart.models.color.surface

import com.tradingview.lightweightcharts.api.chart.models.color.IntColor

class VerticalGradientColor(
    val topColor: IntColor,
    val bottomColor: IntColor
) : SurfaceColor {
    override val type: SurfaceColor.ColorType = SurfaceColor.ColorType.VERTICAL_GRADIENT
}