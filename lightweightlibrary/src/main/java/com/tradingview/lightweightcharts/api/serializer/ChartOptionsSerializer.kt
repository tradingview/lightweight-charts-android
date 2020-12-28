package com.tradingview.lightweightcharts.api.serializer

import com.google.gson.JsonElement
import com.google.gson.JsonSyntaxException
import com.tradingview.lightweightcharts.api.options.models.ChartOptions

class ChartOptionsSerializer: Serializer<ChartOptions>() {

    override fun serialize(json: JsonElement): ChartOptions? {
        return try {
            gson.fromJson(json, ChartOptions::class.java)
        } catch (e: JsonSyntaxException) {
            null
        }
    }
}
