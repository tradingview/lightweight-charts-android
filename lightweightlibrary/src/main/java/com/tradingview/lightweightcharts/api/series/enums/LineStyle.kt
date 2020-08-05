package com.tradingview.lightweightcharts.api.series.enums

import com.google.gson.annotations.SerializedName

enum class LineStyle {
    @SerializedName("0")
    Solid,
    @SerializedName("1")
    Dotted,
    @SerializedName("2")
    Dashed,
    @SerializedName("3")
    LargeDashed,
    @SerializedName("4")
    SparseDotted
}