package com.tradingview.lightweightcharts.api.delegates

import com.tradingview.lightweightcharts.api.interfaces.PriceFormatterApi
import com.tradingview.lightweightcharts.api.interfaces.PriceFormatterApi.Func.PRICE_FORMATTER_FORMAT
import com.tradingview.lightweightcharts.api.interfaces.SeriesApi.Params.PRICE
import com.tradingview.lightweightcharts.api.interfaces.SeriesApi.Params.SERIES_UUID
import com.tradingview.lightweightcharts.api.serializer.PrimitiveSerializer
import com.tradingview.lightweightcharts.runtime.controller.WebMessageController

class PriceFormatterApiDelegate(
    private val seriesId: String,
    private val controller: WebMessageController,
) : PriceFormatterApi {

    override fun format(price: Float, onFormatted: (String) -> Unit) {
        controller.callFunction(
            PRICE_FORMATTER_FORMAT,
            mapOf(
                SERIES_UUID to seriesId,
                PRICE to price
            ),
            callback = onFormatted,
            deserializer = PrimitiveSerializer.StringDeserializer
        )
    }
}
