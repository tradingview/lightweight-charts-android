package com.tradingview.lightweightcharts.api.serializer

import com.google.gson.JsonElement
import com.google.gson.JsonSyntaxException
import com.tradingview.lightweightcharts.api.options.models.BarSeriesOptions
import org.json.JSONObject

class BarSeriesOptionsSerializer : Serializer<BarSeriesOptions>() {
    override fun serialize(json: JsonElement): BarSeriesOptions? {
        return try {
            gson.fromJson(json, BarSeriesOptions::class.java)
        } catch (e: JsonSyntaxException) {
            null
        }
    }
}
