package com.tradingview.lightweightcharts.api.serializer

import com.tradingview.lightweightcharts.api.options.models.PriceScaleOptions
import org.json.JSONObject

class PriceScaleOptionsSerializer: Serializer<PriceScaleOptions>() {

    override fun serialize(any: Any?): PriceScaleOptions? {
        return when (any) {
            is Map<*, *> -> gson.fromJson(
                JSONObject(any).toString(),
                PriceScaleOptions::class.java
            )
            else -> null
        }
    }

}
