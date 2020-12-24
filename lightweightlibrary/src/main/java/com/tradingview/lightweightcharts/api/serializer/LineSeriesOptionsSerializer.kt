package com.tradingview.lightweightcharts.api.serializer

import com.google.gson.JsonElement
import com.google.gson.JsonSyntaxException
import com.tradingview.lightweightcharts.api.options.models.LineSeriesOptions
import org.json.JSONObject

class LineSeriesOptionsSerializer: Serializer<LineSeriesOptions>() {
    override fun serialize(json: JsonElement): LineSeriesOptions? {
        return try {
            gson.fromJson(json, LineSeriesOptions::class.java)
        } catch (e: JsonSyntaxException) {
            null
        }
    }
}