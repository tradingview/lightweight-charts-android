package com.tradingview.lightweightcharts.api.serializer

import com.tradingview.lightweightcharts.api.options.models.ChartOptions
import org.json.JSONObject

class ChartOptionsSerializer: Serializer<ChartOptions>() {

    override fun serialize(any: Any?): ChartOptions? {
        return when (any) {
            is Map<*, *> -> gson.fromJson(
                JSONObject(any).toString(),
                ChartOptions::class.java
            )
            else -> null
        }
    }

}
