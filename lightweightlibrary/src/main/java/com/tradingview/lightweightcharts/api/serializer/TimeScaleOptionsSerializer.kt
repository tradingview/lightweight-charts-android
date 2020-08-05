package com.tradingview.lightweightcharts.api.serializer

import com.tradingview.lightweightcharts.api.options.models.TimeScaleOptions
import org.json.JSONObject

class TimeScaleOptionsSerializer: Serializer<TimeScaleOptions>() {

    override fun serialize(any: Any?): TimeScaleOptions? {
        return when (any) {
            is Map<*, *> -> gson.fromJson(
                JSONObject(any).toString(),
                TimeScaleOptions::class.java
            )
            else -> null
        }
    }

}
