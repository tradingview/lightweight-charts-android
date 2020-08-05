package com.tradingview.lightweightcharts.api.serializer

import com.tradingview.lightweightcharts.api.options.models.PriceLineOptions
import org.json.JSONObject

class PriceLineOptionsSerializer: Serializer<PriceLineOptions>() {

    override fun serialize(any: Any?): PriceLineOptions? {
        return when (any) {
            is Map<*, *> -> gson.fromJson(
                JSONObject(any).toString(),
                PriceLineOptions::class.java
            )
            else -> null
        }
    }

}