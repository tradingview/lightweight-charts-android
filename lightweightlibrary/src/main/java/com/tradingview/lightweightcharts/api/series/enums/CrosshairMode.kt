package com.tradingview.lightweightcharts.api.series.enums

import com.google.gson.*
import java.lang.reflect.Type


enum class CrosshairMode(val value: Int) {
    NORMAL(0),
    MAGNET(1);
    companion object {
        fun fromValue(value: Int): CrosshairMode? {
            return when (value) {
                0 -> NORMAL
                1 -> MAGNET
                else -> null
            }
        }
    }

    class CrosshairModeAdapter : JsonSerializer<CrosshairMode>, JsonDeserializer<CrosshairMode> {
        override fun serialize(
            src: CrosshairMode?,
            typeOfSrc: Type?,
            context: JsonSerializationContext?
        ): JsonElement {
            return src?.let { JsonPrimitive(it.value) } ?: JsonNull.INSTANCE
        }

        override fun deserialize(
            json: JsonElement?,
            typeOfT: Type?,
            context: JsonDeserializationContext?
        ): CrosshairMode? {
            return when {
                json is JsonPrimitive && json.isNumber -> fromValue(json.asInt)
                else -> null
            }
        }
    }
}