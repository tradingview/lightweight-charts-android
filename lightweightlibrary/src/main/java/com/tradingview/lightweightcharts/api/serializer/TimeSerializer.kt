package com.tradingview.lightweightcharts.api.serializer

import com.google.gson.JsonElement
import com.google.gson.JsonSyntaxException
import com.tradingview.lightweightcharts.api.series.models.Time

class TimeSerializer : Serializer<Time>() {
    override fun serialize(json: JsonElement): Time? {
        return try {
            gson.fromJson(json, Time::class.java)
        } catch (e: JsonSyntaxException) {
            null
        }
    }
}