package com.tradingview.lightweightcharts.api.serializer

import com.tradingview.lightweightcharts.api.series.models.Time
import com.tradingview.lightweightcharts.api.series.models.TimeRange

class TimeRangeSerializer: Serializer<TimeRange>() {

    override fun serialize(any: Any?): TimeRange? {
        if (any !is Map<*, *>) {
            return null
        }

        if (any["from"] is Double) {
            val from = any["from"] as Double
            val to = any["to"] as Double
            return TimeRange(Time.Utc(from.toLong()), Time.Utc(to.toLong()))
        }

        if (any["from"] is String) {
            val from = any["from"] as String
            val to = any["to"] as String
            return TimeRange(Time.StringTime(from), Time.StringTime(to))
        }

        if (any["from"] is Map<*, *>) {
            fun parseBusinessDay(map: Map<*, *>): Time.BusinessDay {
                return Time.BusinessDay(map["year"] as Int, map["month"] as Int, map["day"] as Int)
            }

            val from = (any["from"] as Map<*, *>).let(::parseBusinessDay)
            val to = (any["to"] as Map<*, *>).let(::parseBusinessDay)

            return TimeRange(from, to)
        }

        return null
    }

}
