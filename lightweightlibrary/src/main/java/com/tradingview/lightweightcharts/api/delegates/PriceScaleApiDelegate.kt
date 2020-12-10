package com.tradingview.lightweightcharts.api.delegates

import com.tradingview.lightweightcharts.api.interfaces.PriceScaleApi
import com.tradingview.lightweightcharts.api.interfaces.PriceScaleApi.Func.APPLY_OPTIONS
import com.tradingview.lightweightcharts.api.interfaces.PriceScaleApi.Func.WIDTH
import com.tradingview.lightweightcharts.api.interfaces.PriceScaleApi.Func.OPTIONS
import com.tradingview.lightweightcharts.api.interfaces.PriceScaleApi.Params.OPTIONS_PARAM
import com.tradingview.lightweightcharts.api.interfaces.PriceScaleApi.Params.UUID
import com.tradingview.lightweightcharts.api.interfaces.SeriesApi.Params.PRICE_SCALE_ID
import com.tradingview.lightweightcharts.api.options.models.PriceScaleOptions
import com.tradingview.lightweightcharts.api.serializer.PriceScaleOptionsSerializer
import com.tradingview.lightweightcharts.runtime.controller.WebMessageController

class PriceScaleApiDelegate(
    override val uuid: String,
    private val controller: WebMessageController
): PriceScaleApi {

    override fun applyOptions(options: PriceScaleOptions) {
        controller.callFunction(
            APPLY_OPTIONS,
            mapOf(
                PRICE_SCALE_ID to uuid,
                OPTIONS_PARAM to options
            )
        )
    }

    override fun options(onOptionsReceived: (PriceScaleOptions?) -> Unit) {
        controller.callFunction(
            OPTIONS,
            mapOf(PRICE_SCALE_ID to uuid),
            callback = onOptionsReceived,
            serializer = PriceScaleOptionsSerializer()
        )
    }

    override fun width(onWidthReceived: (Float) -> Unit) {
        controller.callFunction(
            WIDTH,
            mapOf(PRICE_SCALE_ID to uuid),
            callback = onWidthReceived
        )
    }
}