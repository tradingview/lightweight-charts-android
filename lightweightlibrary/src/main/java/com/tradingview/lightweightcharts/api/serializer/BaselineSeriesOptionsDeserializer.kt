package com.tradingview.lightweightcharts.api.serializer

import com.google.gson.JsonElement
import com.google.gson.JsonSyntaxException
import com.tradingview.lightweightcharts.api.options.models.BaselineSeriesOptions

class BaselineSeriesOptionsDeserializer : Deserializer<BaselineSeriesOptions>() {
    override fun deserialize(json: JsonElement): BaselineSeriesOptions? {
        return try {
            gson.fromJson(json, BaselineSeriesOptions::class.java)
        } catch (_: JsonSyntaxException) {
            null
        }
    }
}
