package com.tradingview.lightweightcharts.api.serializer

import com.google.gson.JsonElement
import com.google.gson.JsonSyntaxException
import com.tradingview.lightweightcharts.api.options.models.PriceLineOptions
import com.tradingview.lightweightcharts.api.series.common.PriceFormatter
import org.json.JSONObject

class PriceLineOptionsSerializer: Serializer<PriceLineOptions>() {
    override fun serialize(json: JsonElement): PriceLineOptions? {
        return try {
            gson.fromJson(json, PriceLineOptions::class.java)
        } catch (e: JsonSyntaxException) {
            null
        }
    }
}