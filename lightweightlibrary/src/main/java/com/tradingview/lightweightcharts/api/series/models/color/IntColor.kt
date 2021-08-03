package com.tradingview.lightweightcharts.api.series.models.color

import com.google.gson.*
import com.tradingview.lightweightcharts.api.series.exception.ColorParseException
import com.tradingview.lightweightcharts.help.isString
import java.lang.reflect.Type
import kotlin.contracts.ExperimentalContracts

class IntColor(val value: Int): Colorable

class IntColorAdapter : JsonSerializer<IntColor>, JsonDeserializer<IntColor> {
    override fun serialize(
        src: IntColor?,
        typeOfSrc: Type?,
        context: JsonSerializationContext?
    ): JsonElement {
        return when (src) {
            is IntColor -> JsonPrimitive(src.value.toHexString())
            else -> JsonNull.INSTANCE
        }
    }

    @ExperimentalContracts
    override fun deserialize(
        json: JsonElement?,
        typeOfT: Type?,
        context: JsonDeserializationContext?
    ): IntColor? {
        return when {
            json.isString() -> json.asString.toColor()?.let { IntColor(it) }
            else -> null
        }
    }
}

fun Int.toIntColor(): IntColor {
    return IntColor(this)
}

fun String.toIntColor(): IntColor {
    val color = this.toColor()
        ?: throw ColorParseException("Color is empty")
    return IntColor(color)
}