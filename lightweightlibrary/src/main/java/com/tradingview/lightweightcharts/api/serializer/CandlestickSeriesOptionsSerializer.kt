package com.tradingview.lightweightcharts.api.serializer

import com.tradingview.lightweightcharts.api.options.models.CandlestickSeriesOptions
import org.json.JSONObject

class CandlestickSeriesOptionsSerializer : Serializer<CandlestickSeriesOptions>() {
    override fun serialize(any: Any?): CandlestickSeriesOptions? {
        return when (any) {
            is Map<*, *> -> gson.fromJson(
                JSONObject(any).toString(),
                CandlestickSeriesOptions::class.java
            )
            else -> null
        }
    }
}