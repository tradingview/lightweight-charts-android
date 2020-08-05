package com.tradingview.lightweightcharts.api.serializer

import com.tradingview.lightweightcharts.api.options.models.BarSeriesOptions
import org.json.JSONObject

class BarSeriesOptionsSerializer : Serializer<BarSeriesOptions>() {
    override fun serialize(any: Any?): BarSeriesOptions? {
        return when (any) {
            is Map<*, *> -> gson.fromJson(
                JSONObject(any).toString(),
                BarSeriesOptions::class.java
            )
            else -> null
        }
    }
}
