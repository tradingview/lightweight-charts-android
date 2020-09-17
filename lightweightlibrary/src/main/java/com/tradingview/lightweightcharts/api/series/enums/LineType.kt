package com.tradingview.lightweightcharts.api.series.enums

import com.google.gson.*
import com.tradingview.lightweightcharts.help.isNumber
import java.lang.reflect.Type
import kotlin.contracts.ExperimentalContracts

enum class LineType(val value: Int) {
    SIMPLE(0),
    WITH_STEPS(1);

    companion object {
        fun from(value: Int): LineType = values().first { it.value == value }
    }

    class LineTypeAdapter : JsonSerializer<LineType>, JsonDeserializer<LineType> {
        override fun serialize(
            src: LineType?,
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
        ): LineType {
            return when {
                json.isNumber() -> from(json.asInt)
                else -> SIMPLE
            }
        }
    }
}