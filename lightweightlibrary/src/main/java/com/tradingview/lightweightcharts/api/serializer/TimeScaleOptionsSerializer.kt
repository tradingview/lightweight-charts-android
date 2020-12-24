package com.tradingview.lightweightcharts.api.serializer

import com.google.gson.JsonElement
import com.google.gson.JsonSyntaxException
import com.tradingview.lightweightcharts.api.options.models.TimeScaleOptions
import org.json.JSONObject

class TimeScaleOptionsSerializer: Serializer<TimeScaleOptions>() {

    override fun serialize(json: JsonElement): TimeScaleOptions? {
        return try {
            gson.fromJson(json, TimeScaleOptions::class.java)
        } catch (e: JsonSyntaxException) {
            null
        }
    }
}
