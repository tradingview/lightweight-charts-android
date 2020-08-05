package com.tradingview.lightweightcharts.api.series.enums

import com.google.gson.annotations.SerializedName

enum class LineType {
    @SerializedName("0")
    SIMPLE,
    @SerializedName("1")
    WITH_STEPS
}