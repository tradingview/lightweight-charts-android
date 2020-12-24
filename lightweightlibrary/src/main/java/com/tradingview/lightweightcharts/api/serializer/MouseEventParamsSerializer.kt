package com.tradingview.lightweightcharts.api.serializer

import com.google.gson.*
import com.tradingview.lightweightcharts.api.serializer.gson.GsonProvider
import com.tradingview.lightweightcharts.api.series.models.MouseEventParams
import org.json.JSONObject

class MouseEventParamsSerializer: Serializer<MouseEventParams>() {

    override fun serialize(json: JsonElement): MouseEventParams? {
        return try {
            gson.fromJson(json, MouseEventParams::class.java)
        } catch (e: JsonSyntaxException) {
            null
        }
    }
}
