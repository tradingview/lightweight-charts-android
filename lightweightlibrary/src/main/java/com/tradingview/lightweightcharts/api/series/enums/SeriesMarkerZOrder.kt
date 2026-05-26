package com.tradingview.lightweightcharts.api.series.enums

import com.google.gson.annotations.SerializedName

enum class SeriesMarkerZOrder {
    @SerializedName("top")
    TOP,

    @SerializedName("aboveSeries")
    ABOVE_SERIES,

    @SerializedName("normal")
    NORMAL,
}
