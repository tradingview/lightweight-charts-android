package com.tradingview.lightweightcharts.api.chart.models.color.surface

import com.google.gson.annotations.SerializedName

interface SurfaceColor {
    val type: ColorType

    enum class ColorType {

        @SerializedName("solid")
        SOLID,

        @SerializedName("gradient")
        VERTICAL_GRADIENT
    }
}