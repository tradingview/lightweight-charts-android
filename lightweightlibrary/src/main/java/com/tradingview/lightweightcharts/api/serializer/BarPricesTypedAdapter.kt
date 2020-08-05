package com.tradingview.lightweightcharts.api.serializer

import com.google.gson.*
import com.tradingview.lightweightcharts.api.series.models.BarPrice
import com.tradingview.lightweightcharts.api.series.models.BarPrices
import java.lang.reflect.Type

class BarPricesTypedAdapter : JsonDeserializer<BarPrices> {

    override fun deserialize(
        json: JsonElement?,
        typeOfT: Type?,
        context: JsonDeserializationContext?
    ): BarPrices {
        json?.asJsonObject?.apply {
            return BarPrices(get("id")?.asString ?: "", generate(this))
        }
        throw JsonSyntaxException("BarPrice syntax exception")
    }

    private fun generate(json: JsonObject): BarPrice {
        val prices = json.get("prices")
        return if (prices.isJsonObject) {
            Gson().fromJson(prices, BarPrice::class.java)
        } else {
            BarPrice(value = prices.asFloat)
        }

    }

}
