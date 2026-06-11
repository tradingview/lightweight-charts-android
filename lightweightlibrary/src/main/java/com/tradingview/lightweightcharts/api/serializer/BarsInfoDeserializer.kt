package com.tradingview.lightweightcharts.api.serializer

import com.google.gson.JsonElement
import com.tradingview.lightweightcharts.api.series.models.BarsInfo
import com.tradingview.lightweightcharts.api.series.models.Time
import com.tradingview.lightweightcharts.help.isNumber
import com.tradingview.lightweightcharts.help.isString

class BarsInfoDeserializer : Deserializer<BarsInfo>() {

    override fun deserialize(json: JsonElement): BarsInfo? {
        if (!json.isJsonObject) {
            return null
        }
        val jsonObject = json.asJsonObject
        val barsBefore = jsonObject.get("barsBefore") ?: return null
        val barsAfter = jsonObject.get("barsAfter") ?: return null

        return BarsInfo(
            from = jsonObject.get("from")?.let(::parseTime),
            to = jsonObject.get("to")?.let(::parseTime),
            barsBefore = barsBefore.asFloat,
            barsAfter = barsAfter.asFloat,
        )
    }

    private fun parseTime(value: JsonElement): Time? {
        return when {
            value.isJsonNull -> null
            value.isNumber() -> Time.Utc(value.asLong)
            value.isString() -> value.asString.toLongOrNull()?.let(Time::Utc)
                ?: Time.StringTime(value.asString)
            value.isJsonObject -> {
                val date = value.asJsonObject
                runCatching {
                    Time.BusinessDay(
                        date.get("year").asInt,
                        date.get("month").asInt,
                        date.get("day").asInt
                    )
                }.getOrNull()
            }
            else -> null
        }
    }
}
