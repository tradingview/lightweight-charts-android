package com.tradingview.lightweightcharts.api.serializer

import com.google.gson.JsonElement
import com.google.gson.JsonSyntaxException
import com.tradingview.lightweightcharts.api.options.models.PriceScaleOptions

class PriceScaleOptionsSerializer: Serializer<PriceScaleOptions>() {
    override fun serialize(json: JsonElement): PriceScaleOptions? {
        return try {
            gson.fromJson(json, PriceScaleOptions::class.java)
        } catch (e: JsonSyntaxException) {
            null
        }
    }
}
