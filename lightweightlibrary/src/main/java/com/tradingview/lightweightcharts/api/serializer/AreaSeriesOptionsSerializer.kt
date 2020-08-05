package com.tradingview.lightweightcharts.api.serializer

import com.tradingview.lightweightcharts.api.options.models.AreaSeriesOptions
import org.json.JSONObject

class AreaSeriesOptionsSerializer : Serializer<AreaSeriesOptions>() {
    override fun serialize(any: Any?): AreaSeriesOptions? {
        return when (any) {
            is Map<*, *> -> gson.fromJson(
                JSONObject(any).toString(),
                AreaSeriesOptions::class.java
            )
            else -> null
        }
    }
}
