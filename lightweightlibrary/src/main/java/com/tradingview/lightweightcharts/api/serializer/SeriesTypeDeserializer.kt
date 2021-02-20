package com.tradingview.lightweightcharts.api.serializer

import com.google.gson.JsonElement
import com.tradingview.lightweightcharts.api.series.enums.SeriesType
import com.tradingview.lightweightcharts.help.isString
import java.util.*
import kotlin.contracts.ExperimentalContracts

class SeriesTypeDeserializer: Deserializer<SeriesType>() {

    @ExperimentalContracts
    override fun deserialize(json: JsonElement): SeriesType? {
        if (json.isString()) {
            return SeriesType.valueOf(json.asString.toUpperCase(Locale.ENGLISH))
        }
        return null
    }
}