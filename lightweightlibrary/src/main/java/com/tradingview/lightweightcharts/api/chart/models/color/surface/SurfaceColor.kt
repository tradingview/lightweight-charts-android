package com.tradingview.lightweightcharts.api.chart.models.color.surface

import com.google.gson.JsonDeserializationContext
import com.google.gson.JsonDeserializer
import com.google.gson.JsonElement
import com.google.gson.annotations.SerializedName
import com.tradingview.lightweightcharts.api.serializer.gson.GsonProvider
import java.lang.IllegalStateException
import java.lang.reflect.Type

interface SurfaceColor {
    val type: ColorType

    enum class ColorType {

        @SerializedName("solid")
        SOLID,

        @SerializedName("gradient")
        VERTICAL_GRADIENT
    }

    class SurfaceColorAdapter : JsonDeserializer<SurfaceColor?> {
        override fun deserialize(
            json: JsonElement?,
            typeOfT: Type?,
            context: JsonDeserializationContext?
        ): SurfaceColor? {
            if (json == null || json.isJsonNull) {
                return null
            }

            val gson = GsonProvider.newInstance()
            val jsonObject = json.asJsonObject
            return when (jsonObject["type"].asString) {
                "solid" -> gson.fromJson(json, SolidColor::class.java)
                "gradient" -> gson.fromJson(json, VerticalGradientColor::class.java)
                else -> throw IllegalStateException("Unknown type of color")
            }
        }
    }
}