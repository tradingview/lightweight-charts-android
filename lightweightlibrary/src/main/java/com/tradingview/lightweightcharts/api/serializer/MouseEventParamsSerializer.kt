package com.tradingview.lightweightcharts.api.serializer

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.tradingview.lightweightcharts.api.series.models.BarPrices
import com.tradingview.lightweightcharts.api.series.models.MouseEventParams
import org.json.JSONObject

class MouseEventParamsSerializer: Serializer<MouseEventParams>() {

    override val gson: Gson by lazy {
        GsonBuilder()
            .registerTypeAdapter(BarPrices::class.java, BarPricesTypedAdapter())
            .create()
    }

    override fun serialize(any: Any?): MouseEventParams? {
        return when (any) {
            is Map<*, *> -> gson.fromJson(
                JSONObject(any).toString(),
                MouseEventParams::class.java
            )
            else -> null
        }
    }

}
