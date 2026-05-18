package com.tradingview.lightweightcharts.api.serializer

import com.google.gson.JsonElement
import com.google.gson.JsonSyntaxException
import com.tradingview.lightweightcharts.api.series.common.SeriesData

class ClassListDeserializer<T : SeriesData>(private val clazz: Class<T>) : Deserializer<List<T>>() {

    override fun deserialize(json: JsonElement): List<T>? {
        return try {
            json.asJsonArray.map { gson.fromJson(it, clazz) }
        } catch (_: JsonSyntaxException) {
            null
        } catch (_: IllegalStateException) {
            null
        }
    }
}
