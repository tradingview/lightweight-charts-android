package com.tradingview.lightweightcharts.api.options.enums

import com.google.gson.annotations.SerializedName

enum class ColorSpace {
    @SerializedName("srgb")
    SRGB,

    @SerializedName("display-p3")
    DISPLAY_P3,
}
