package com.tradingview.lightweightcharts.api.series.models

import com.google.gson.annotations.SerializedName
import com.tradingview.lightweightcharts.runtime.plugins.Plugin

data class PriceFormat(
    val formatter: Plugin? = null,
    val type: Type? = null,
    val precision: Int? = null,
    val minMove: Float? = null,
    val base: Float? = null,
    val tickmarksFormatter: Plugin? = null,
) {

    enum class Type {
        @SerializedName("price")
        PRICE,

        @SerializedName("volume")
        VOLUME,

        @SerializedName("percent")
        PERCENT,

        @SerializedName("custom")
        CUSTOM
    }

    companion object {

        fun priceFormatBuiltIn(type: Type, precision: Int, minMove: Float): PriceFormat {
            return PriceFormat(type = type, precision = precision, minMove = minMove)
        }

        fun priceFormatBuiltIn(type: Type, precision: Int, minMove: Float, base: Float): PriceFormat {
            return PriceFormat(type = type, precision = precision, minMove = minMove, base = base)
        }

        fun priceFormatCustom(formatter: Plugin, minMove: Float): PriceFormat {
            return PriceFormat(formatter = formatter, minMove = minMove, type = Type.CUSTOM)
        }
    }
}
