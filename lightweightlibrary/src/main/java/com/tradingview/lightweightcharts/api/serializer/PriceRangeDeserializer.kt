package com.tradingview.lightweightcharts.api.serializer

import com.google.gson.JsonElement
import com.google.gson.JsonSyntaxException
import com.tradingview.lightweightcharts.api.series.models.PriceRange

class PriceRangeDeserializer : Deserializer<PriceRange>() {
    override fun deserialize(json: JsonElement): PriceRange? {
        return try {
            gson.fromJson(json, PriceRange::class.java)
        } catch (_: JsonSyntaxException) {
            null
        }
    }
}
