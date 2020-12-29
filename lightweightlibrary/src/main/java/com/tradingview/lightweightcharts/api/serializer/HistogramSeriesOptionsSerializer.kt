package com.tradingview.lightweightcharts.api.serializer

import com.google.gson.JsonElement
import com.google.gson.JsonSyntaxException
import com.tradingview.lightweightcharts.api.options.models.HistogramSeriesOptions

class HistogramSeriesOptionsSerializer : Serializer<HistogramSeriesOptions>() {
    override fun serialize(json: JsonElement): HistogramSeriesOptions? {
        return try {
            gson.fromJson(json, HistogramSeriesOptions::class.java)
        } catch (e: JsonSyntaxException) {
            null
        }
    }
}