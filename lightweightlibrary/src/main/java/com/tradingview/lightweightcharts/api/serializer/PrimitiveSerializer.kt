package com.tradingview.lightweightcharts.api.serializer

import com.google.gson.JsonElement

sealed class PrimitiveSerializer {
    object StringSerializer: Serializer<String>() {
        override fun serialize(json: JsonElement): String? {
            if (json.isJsonNull) {
                return null
            }
            return json.asString
        }
    }

    object FloatSerializer: Serializer<Float>() {
        override fun serialize(json: JsonElement): Float? {
            if (json.isJsonNull) {
                return null
            }
            return json.asFloat
        }
    }

    object IntSerializer: Serializer<Int>() {
        override fun serialize(json: JsonElement): Int? {
            if (json.isJsonNull) {
                return null
            }
            return json.asInt
        }
    }

    object DoubleSerializer: Serializer<Double>() {
        override fun serialize(json: JsonElement): Double? {
            if (json.isJsonNull) {
                return null
            }
            return json.asDouble
        }
    }

    object NullSerializer: Serializer<Any>() {
        override fun serialize(json: JsonElement): Any? {
            return null
        }
    }
}

