package com.tradingview.lightweightcharts.api.serializer

import com.google.gson.JsonElement
import com.google.gson.JsonSyntaxException
import com.tradingview.lightweightcharts.api.series.models.LogicalRange

class LogicalRangeDeserializer : Deserializer<LogicalRange>() {
    override fun deserialize(json: JsonElement): LogicalRange? {
        return try {
            gson.fromJson(json, LogicalRange::class.java)
        } catch (_: JsonSyntaxException) {
            null
        }
    }
}
