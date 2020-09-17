package com.tradingview.lightweightcharts.api.serializer

import com.google.gson.*
import com.tradingview.lightweightcharts.api.serializer.adapter.GsonProvider
import com.tradingview.lightweightcharts.api.series.models.BarPrices
import com.tradingview.lightweightcharts.api.series.models.MouseEventParams
import com.tradingview.lightweightcharts.api.series.models.Time
import org.json.JSONObject

class MouseEventParamsSerializer: Serializer<MouseEventParams>() {

    override val gson: Gson by lazy { GsonProvider.newInstance() }

    override fun serialize(any: Any?): MouseEventParams? {
        return when (any) {
            is Map<*, *> -> gson.fromJson<MouseEventParams>(
                JSONObject(any).toString(),
                MouseEventParams::class.java
            )
            else -> null
        }
    }
}
