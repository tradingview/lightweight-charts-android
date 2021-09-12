package com.tradingview.lightweightcharts.api.series.enums

import com.google.gson.*
import com.tradingview.lightweightcharts.help.isNumber
import java.lang.reflect.Type
import kotlin.contracts.ExperimentalContracts

enum class LastPriceAnimationMode(val value: Int) {
    DISABLED(0),

    CONTINUOUS(1),

    ON_DATA_UPDATE(2);

    companion object {
        fun from(value: Int) = values().first { it.value == value }
    }

    class LastPriceAnimationModeAdapter :
        JsonSerializer<LastPriceAnimationMode>,
        JsonDeserializer<LastPriceAnimationMode> {

        override fun serialize(
            src: LastPriceAnimationMode?,
            typeOfSrc: Type?,
            context: JsonSerializationContext?
        ): JsonElement {
            return src?.let { JsonPrimitive(it.value) } ?: JsonNull.INSTANCE
        }

        @ExperimentalContracts
        override fun deserialize(
            json: JsonElement?,
            typeOfT: Type?,
            context: JsonDeserializationContext?
        ): LastPriceAnimationMode {
            return when {
                json.isNumber() -> from(json.asInt)
                else -> DISABLED
            }
        }
    }
}