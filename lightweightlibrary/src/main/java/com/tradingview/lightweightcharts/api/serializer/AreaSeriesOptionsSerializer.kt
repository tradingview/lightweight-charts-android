package com.tradingview.lightweightcharts.api.serializer

import com.google.gson.JsonElement
import com.google.gson.JsonSyntaxException
import com.tradingview.lightweightcharts.api.options.models.AreaSeriesOptions

class AreaSeriesOptionsSerializer : Serializer<AreaSeriesOptions>() {
    override fun serialize(json: JsonElement): AreaSeriesOptions? {
        return try {
            gson.fromJson(json, AreaSeriesOptions::class.java)
        } catch (e: JsonSyntaxException) {
            null
        }
    }
}
