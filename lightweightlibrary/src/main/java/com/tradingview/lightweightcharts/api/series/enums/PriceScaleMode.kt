package com.tradingview.lightweightcharts.api.series.enums

import com.google.gson.*
import com.tradingview.lightweightcharts.help.isNumber
import java.lang.reflect.Type
import kotlin.contracts.ExperimentalContracts

enum class PriceScaleMode(val value: Int) {
    NORMAL(0),
    LOGARITHMIC(1),
    PERCENTAGE(2),
    INDEXED_TO_100(3);

    companion object {
        fun from(value: Int): PriceScaleMode = values().first { it.value == value }
    }

    class PriceScaleModeAdapter : JsonSerializer<PriceScaleMode>, JsonDeserializer<PriceScaleMode> {
        override fun serialize(
            src: PriceScaleMode?,
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
        ): PriceScaleMode {
            return when {
                json.isNumber() -> from(json.asInt)
                else -> NORMAL
            }
        }
    }
}