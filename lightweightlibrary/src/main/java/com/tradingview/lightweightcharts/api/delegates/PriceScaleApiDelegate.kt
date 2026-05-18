package com.tradingview.lightweightcharts.api.delegates

import com.tradingview.lightweightcharts.api.interfaces.PriceScaleApi
import com.tradingview.lightweightcharts.api.interfaces.PriceScaleApi.Func.APPLY_OPTIONS
import com.tradingview.lightweightcharts.api.interfaces.PriceScaleApi.Func.GET_VISIBLE_RANGE
import com.tradingview.lightweightcharts.api.interfaces.PriceScaleApi.Func.OPTIONS
import com.tradingview.lightweightcharts.api.interfaces.PriceScaleApi.Func.SET_AUTO_SCALE
import com.tradingview.lightweightcharts.api.interfaces.PriceScaleApi.Func.SET_VISIBLE_RANGE
import com.tradingview.lightweightcharts.api.interfaces.PriceScaleApi.Func.WIDTH
import com.tradingview.lightweightcharts.api.interfaces.PriceScaleApi.Params.CALLER
import com.tradingview.lightweightcharts.api.interfaces.PriceScaleApi.Params.ON
import com.tradingview.lightweightcharts.api.interfaces.PriceScaleApi.Params.OPTIONS_PARAM
import com.tradingview.lightweightcharts.api.interfaces.PriceScaleApi.Params.RANGE
import com.tradingview.lightweightcharts.api.options.models.PriceScaleOptions
import com.tradingview.lightweightcharts.api.serializer.PriceScaleOptionsDeserializer
import com.tradingview.lightweightcharts.api.serializer.PriceRangeDeserializer
import com.tradingview.lightweightcharts.api.serializer.PrimitiveSerializer
import com.tradingview.lightweightcharts.api.series.models.PriceRange
import com.tradingview.lightweightcharts.runtime.controller.WebMessageController
import com.tradingview.lightweightcharts.runtime.version.ChartRuntimeObject

class PriceScaleApiDelegate(
    override val uuid: String,
    private val controller: WebMessageController
) : PriceScaleApi, ChartRuntimeObject {

    override fun getVersion(): Int {
        return controller.hashCode()
    }

    override fun applyOptions(options: PriceScaleOptions) {
        controller.callFunction(
            APPLY_OPTIONS,
            mapOf(
                CALLER to uuid,
                OPTIONS_PARAM to options
            )
        )
    }

    override fun options(onOptionsReceived: (PriceScaleOptions) -> Unit) {
        controller.callFunction(
            OPTIONS,
            mapOf(CALLER to uuid),
            callback = onOptionsReceived,
            deserializer = PriceScaleOptionsDeserializer()
        )
    }

    override fun width(onWidthReceived: (Float) -> Unit) {
        controller.callFunction(
            WIDTH,
            mapOf(CALLER to uuid),
            callback = onWidthReceived,
            deserializer = PrimitiveSerializer.FloatDeserializer
        )
    }

    override fun setVisibleRange(range: PriceRange) {
        controller.callFunction(
            SET_VISIBLE_RANGE,
            mapOf(
                CALLER to uuid,
                RANGE to range
            )
        )
    }

    override fun getVisibleRange(onRangeReceived: (PriceRange?) -> Unit) {
        controller.callFunction(
            GET_VISIBLE_RANGE,
            mapOf(CALLER to uuid),
            callback = onRangeReceived,
            deserializer = PriceRangeDeserializer()
        )
    }

    override fun setAutoScale(on: Boolean) {
        controller.callFunction(
            SET_AUTO_SCALE,
            mapOf(
                CALLER to uuid,
                ON to on
            )
        )
    }
}
