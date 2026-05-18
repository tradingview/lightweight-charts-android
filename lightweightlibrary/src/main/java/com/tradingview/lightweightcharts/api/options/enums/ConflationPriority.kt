package com.tradingview.lightweightcharts.api.options.enums

import com.google.gson.annotations.SerializedName

enum class ConflationPriority {
    @SerializedName("background")
    BACKGROUND,

    @SerializedName("user-visible")
    USER_VISIBLE,

    @SerializedName("user-blocking")
    USER_BLOCKING,
}
