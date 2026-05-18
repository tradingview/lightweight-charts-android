package com.tradingview.lightweightcharts.api.serializer

import com.google.gson.*
import com.tradingview.lightweightcharts.api.series.models.MouseEventParams

class MouseEventParamsDeserializer : Deserializer<MouseEventParams>() {

    override fun deserialize(json: JsonElement): MouseEventParams? {
        return try {
            val normalized = normalizeLogicalIndex(json)
            gson.fromJson(normalized, MouseEventParams::class.java)
        } catch (_: JsonSyntaxException) {
            null
        }
    }

    private fun normalizeLogicalIndex(json: JsonElement): JsonElement {
        if (!json.isJsonObject) {
            return json
        }

        val jsonObject = json.asJsonObject.deepCopy()
        val logicalElement = jsonObject.get("logical")
        if (logicalElement?.isJsonPrimitive == true && logicalElement.asJsonPrimitive.isNumber) {
            val logical = logicalElement.asFloat
            if (!jsonObject.has("logicalFloat")) {
                jsonObject.addProperty("logicalFloat", logical)
            }
            jsonObject.addProperty("logical", logical.toInt())
        }

        return jsonObject
    }
}
