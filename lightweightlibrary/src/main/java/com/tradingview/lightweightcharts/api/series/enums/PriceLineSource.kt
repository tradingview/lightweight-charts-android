package com.tradingview.lightweightcharts.api.series.enums

import com.google.gson.*
import com.tradingview.lightweightcharts.help.isNumber
import java.lang.reflect.Type
import kotlin.contracts.ExperimentalContracts


enum class PriceLineSource(val value: Int) {
    LAST_BAR(0),
    LAST_VISIBLE(1);

    companion object {
        fun from(value: Int): PriceLineSource = values().first { it.value == value }
    }

    class PriceLineSourceAdapter : JsonSerializer<PriceLineSource>, JsonDeserializer<PriceLineSource> {
        override fun serialize(
            src: PriceLineSource?,
            typeOfSrc: Type?,
            context: JsonSerializationContext?
        ): JsonElement {
            return JsonPrimitive(src?.value ?: 0)
        }

        @ExperimentalContracts
        override fun deserialize(
            json: JsonElement?,
            typeOfT: Type?,
            context: JsonDeserializationContext?
        ): PriceLineSource {
            return when {
                json.isNumber() -> from(json.asInt)
                else -> LAST_BAR
            }
        }
    }
}