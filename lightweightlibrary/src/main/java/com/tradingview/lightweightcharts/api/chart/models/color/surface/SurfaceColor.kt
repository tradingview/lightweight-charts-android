package com.tradingview.lightweightcharts.api.chart.models.color.surface

interface SurfaceColor {
    val type: ColorType

    enum class ColorType {
        SOLID, VERTICAL_GRADIENT
    }
}