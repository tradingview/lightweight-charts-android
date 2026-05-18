package com.tradingview.lightweightcharts.api.series.enums

import com.google.gson.annotations.SerializedName

enum class SeriesMarkerPosition {
    @SerializedName("aboveBar")
    ABOVE_BAR,

    @SerializedName("belowBar")
    BELOW_BAR,

    @SerializedName("inBar")
    IN_BAR,

    @SerializedName("atPriceTop")
    AT_PRICE_TOP,

    @SerializedName("atPriceBottom")
    AT_PRICE_BOTTOM,

    @SerializedName("atPriceMiddle")
    AT_PRICE_MIDDLE,
}
