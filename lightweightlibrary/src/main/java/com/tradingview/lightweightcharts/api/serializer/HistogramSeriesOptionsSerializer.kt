package com.tradingview.lightweightcharts.api.serializer

import com.tradingview.lightweightcharts.api.options.models.HistogramSeriesOptions
import org.json.JSONObject

class HistogramSeriesOptionsSerializer : Serializer<HistogramSeriesOptions>() {
    override fun serialize(any: Any?): HistogramSeriesOptions? {
        return when (any) {
            is Map<*, *> -> gson.fromJson(
                JSONObject(any).toString(),
                HistogramSeriesOptions::class.java
            )
            else -> null
        }
    }
}