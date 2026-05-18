package com.tradingview.lightweightcharts.api.series.enums

import com.google.gson.annotations.SerializedName

enum class SeriesType {
    @SerializedName("Line")
    LINE,

    @SerializedName("Area")
    AREA,

    @SerializedName("Candlestick")
    CANDLESTICK,

    @SerializedName("Bar")
    BAR,

    @SerializedName("Histogram")
    HISTOGRAM,

    @SerializedName("Baseline")
    BASELINE
}
