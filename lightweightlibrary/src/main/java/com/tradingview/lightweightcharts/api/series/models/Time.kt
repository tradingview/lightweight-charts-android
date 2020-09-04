package com.tradingview.lightweightcharts.api.series.models

import com.google.gson.*
import java.lang.IllegalStateException
import java.lang.reflect.Type
import java.text.SimpleDateFormat
import java.util.*

sealed class Time {
    data class Utc(val timestamp: Long) : Time() {
        companion object {
            fun fromDate(date: Date): Utc = Utc(date.time / 1000)
        }

        override val date: Date
            get() = Date(timestamp * 1000)
    }
    data class BusinessDay(val year: Int, val month: Int, val day: Int): Time() {
        override val date: Date
            get() = Calendar
                .getInstance()
                .apply { set(year, month, day) }
                .time
    }
    data class StringTime(val source: String, val locale: Locale = Locale.getDefault()): Time() {
        override val date: Date
            get() = SimpleDateFormat("yyyy-MM-dd", locale).parse(source)
                ?: throw IllegalStateException("Time format is not supported")
    }

    abstract val date: Date

    class TimeSerializer : JsonSerializer<Time> {
        override fun serialize(
            src: Time?,
            typeOfSrc: Type?,
            context: JsonSerializationContext?
        ): JsonElement {
            return when(src) {
                is Utc -> JsonPrimitive(src.timestamp)
                is BusinessDay -> JsonObject().apply {
                    addProperty("year", src.year)
                    addProperty("month", src.month)
                    addProperty("day", src.day)
                }
                is StringTime -> JsonPrimitive(src.source)
                else -> throw IllegalStateException("Unsupported time type")
            }
        }
    }
    class TimeDeserializer : JsonDeserializer<Time> {
        override fun deserialize(
            json: JsonElement?,
            typeOfT: Type?,
            context: JsonDeserializationContext?
        ): Time {
            return when {
                json is JsonPrimitive && json.isNumber -> Utc(json.asLong)
                json is JsonPrimitive && json.isString -> StringTime(json.asString)
                json is JsonObject -> BusinessDay(
                        json["year"].asInt,
                        json["month"].asInt,
                        json["day"].asInt
                )
                else -> throw IllegalStateException("Unsupported time type")
            }
        }
    }
}
