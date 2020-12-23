package com.tradingview.lightweightcharts.api.series.models

import android.graphics.Color
import com.google.gson.*
import com.tradingview.lightweightcharts.help.isString
import java.lang.reflect.Type
import kotlin.contracts.ExperimentalContracts

typealias IntColor = Int

class ColorAdapter : JsonSerializer<IntColor>, JsonDeserializer<IntColor> {
    override fun serialize(
        src: IntColor?,
        typeOfSrc: Type?,
        context: JsonSerializationContext?
    ): JsonElement {
        return src?.let { JsonPrimitive(it.toHexString()) } ?: JsonNull.INSTANCE
    }

    @ExperimentalContracts
    override fun deserialize(
        json: JsonElement?,
        typeOfT: Type?,
        context: JsonDeserializationContext?
    ): IntColor? {
         return when {
             json.isString() -> Color.parseColor(json.asString)
             else -> null
        }
    }
}

private fun IntColor.toHexString(): String {
    val alpha = String.format("%02x", Color.alpha(this))
    val red = String.format("%02x", Color.red(this))
    val green = String.format("%02x", Color.green(this))
    val blue = String.format("%02x", Color.blue(this))

    return "#$red$green$blue$alpha"
}