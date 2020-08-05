package com.tradingview.lightweightcharts.api.serializer

import com.tradingview.lightweightcharts.api.options.models.LineSeriesOptions
import org.json.JSONObject

class LineSeriesOptionsSerializer: Serializer<LineSeriesOptions>() {
    override fun serialize(any: Any?): LineSeriesOptions? {
        return when (any) {
            is Map<*, *> -> gson.fromJson(
                JSONObject(any).toString(),
                LineSeriesOptions::class.java
            )
            else -> null
        }
    }
}