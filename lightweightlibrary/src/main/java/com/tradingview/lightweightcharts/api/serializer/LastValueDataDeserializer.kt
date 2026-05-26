package com.tradingview.lightweightcharts.api.serializer

import com.google.gson.JsonElement
import com.google.gson.JsonSyntaxException
import com.tradingview.lightweightcharts.api.series.models.LastValueData

class LastValueDataDeserializer : Deserializer<LastValueData>() {
    override fun deserialize(json: JsonElement): LastValueData? {
        return try {
            gson.fromJson(json, LastValueData::class.java)
        } catch (_: JsonSyntaxException) {
            null
        }
    }
}
