package com.tradingview.lightweightcharts.api.serializer

import com.tradingview.lightweightcharts.api.series.common.PriceFormatter
import org.json.JSONObject

class PriceFormatterSerializer : Serializer<PriceFormatter>() {
    override fun serialize(any: Any?): PriceFormatter? {
        return when (any) {
            is Map<*, *> -> gson.fromJson(
                JSONObject(any).toString(),
                PriceFormatter::class.java
            )
            else -> null
        }
    }
}
