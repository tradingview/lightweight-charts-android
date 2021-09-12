package com.tradingview.lightweightcharts.api.chart.models.color.surface

import com.tradingview.lightweightcharts.api.chart.models.color.IntColor

class SolidColor(val color: IntColor) : SurfaceColor {
    override val type: SurfaceColor.ColorType = SurfaceColor.ColorType.SOLID
}