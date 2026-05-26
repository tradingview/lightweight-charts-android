package com.tradingview.lightweightcharts.api.delegates

import com.tradingview.lightweightcharts.api.interfaces.PaneApi
import com.tradingview.lightweightcharts.api.interfaces.PaneApi.Func.PANE_ADD_SERIES
import com.tradingview.lightweightcharts.api.interfaces.PaneApi.Func.PANE_GET_HEIGHT
import com.tradingview.lightweightcharts.api.interfaces.PaneApi.Func.PANE_GET_SERIES
import com.tradingview.lightweightcharts.api.interfaces.PaneApi.Func.PANE_GET_STRETCH_FACTOR
import com.tradingview.lightweightcharts.api.interfaces.PaneApi.Func.PANE_INDEX
import com.tradingview.lightweightcharts.api.interfaces.PaneApi.Func.PANE_MOVE_TO
import com.tradingview.lightweightcharts.api.interfaces.PaneApi.Func.PANE_PRESERVE_EMPTY
import com.tradingview.lightweightcharts.api.interfaces.PaneApi.Func.PANE_PRICE_SCALE
import com.tradingview.lightweightcharts.api.interfaces.PaneApi.Func.PANE_SET_HEIGHT
import com.tradingview.lightweightcharts.api.interfaces.PaneApi.Func.PANE_SET_PRESERVE_EMPTY
import com.tradingview.lightweightcharts.api.interfaces.PaneApi.Func.PANE_SET_STRETCH_FACTOR
import com.tradingview.lightweightcharts.api.interfaces.PaneApi.Params.HEIGHT
import com.tradingview.lightweightcharts.api.interfaces.PaneApi.Params.OPTIONS
import com.tradingview.lightweightcharts.api.interfaces.PaneApi.Params.PANE_INDEX as PANE_INDEX_PARAM
import com.tradingview.lightweightcharts.api.interfaces.PaneApi.Params.PANE_ID
import com.tradingview.lightweightcharts.api.interfaces.PaneApi.Params.PRESERVE
import com.tradingview.lightweightcharts.api.interfaces.PaneApi.Params.PRICE_SCALE_ID
import com.tradingview.lightweightcharts.api.interfaces.PaneApi.Params.SERIES_TYPE
import com.tradingview.lightweightcharts.api.interfaces.PaneApi.Params.STRETCH_FACTOR
import com.tradingview.lightweightcharts.api.interfaces.PriceScaleApi
import com.tradingview.lightweightcharts.api.interfaces.SeriesApi
import com.tradingview.lightweightcharts.api.options.models.SeriesOptionsCommon
import com.tradingview.lightweightcharts.api.serializer.Deserializer
import com.tradingview.lightweightcharts.api.serializer.PrimitiveSerializer
import com.tradingview.lightweightcharts.api.series.enums.SeriesType
import com.tradingview.lightweightcharts.api.series.models.PriceScaleId
import com.tradingview.lightweightcharts.runtime.controller.WebMessageController
import com.tradingview.lightweightcharts.runtime.version.ChartRuntimeObject

class PaneApiDelegate(
    override val uuid: String,
    private val controller: WebMessageController,
) : PaneApi, ChartRuntimeObject {

    override fun getVersion(): Int = controller.hashCode()

    override fun addSeries(
        type: SeriesType,
        options: SeriesOptionsCommon?,
        onSeriesCreated: (api: SeriesApi) -> Unit
    ) {
        val params = mutableMapOf<String, Any>(
            PANE_ID to uuid,
            SERIES_TYPE to type,
        )
        options?.let { params[OPTIONS] = it }
        controller.callFunction(
            PANE_ADD_SERIES,
            params,
            { seriesUuid -> onSeriesCreated(SeriesApiDelegate(seriesUuid, controller, SeriesApiDelegate.optionsDeserializer(type))) },
            PrimitiveSerializer.StringDeserializer
        )
    }

    override fun priceScale(id: PriceScaleId): PriceScaleApi {
        val priceScaleUuid = controller.callFunction(
            PANE_PRICE_SCALE,
            mapOf(PANE_ID to uuid, PRICE_SCALE_ID to id.value)
        )
        return PriceScaleApiDelegate(priceScaleUuid, controller)
    }

    override fun getHeight(onHeightReceived: (Int) -> Unit) {
        controller.callFunction(
            PANE_GET_HEIGHT,
            mapOf(PANE_ID to uuid),
            callback = onHeightReceived,
            deserializer = PrimitiveSerializer.IntDeserializer
        )
    }

    override fun setHeight(height: Int) {
        controller.callFunction(PANE_SET_HEIGHT, mapOf(PANE_ID to uuid, HEIGHT to height))
    }

    override fun moveTo(paneIndex: Int) {
        controller.callFunction(PANE_MOVE_TO, mapOf(PANE_ID to uuid, PANE_INDEX_PARAM to paneIndex))
    }

    override fun paneIndex(onPaneIndexReceived: (Int) -> Unit) {
        controller.callFunction(
            PANE_INDEX,
            mapOf(PANE_ID to uuid),
            callback = onPaneIndexReceived,
            deserializer = PrimitiveSerializer.IntDeserializer
        )
    }

    override fun getSeries(onSeriesReceived: (List<SeriesApi>) -> Unit) {
        controller.callFunction(
            PANE_GET_SERIES,
            mapOf(PANE_ID to uuid),
            callback = { seriesReferences ->
                onSeriesReceived(
                    seriesReferences.map { reference ->
                        SeriesApiDelegate(
                            reference.uuid,
                            controller,
                            SeriesApiDelegate.optionsDeserializer(reference.type)
                        )
                    }
                )
            },
            deserializer = object : Deserializer<List<PaneSeriesReference>>() {
                override fun deserialize(json: com.google.gson.JsonElement): List<PaneSeriesReference>? {
                    return json.asJsonArray.map { element ->
                        val item = element.asJsonObject
                        PaneSeriesReference(
                            uuid = item["uuid"].asString,
                            type = gson.fromJson(item["seriesType"], SeriesType::class.java)
                        )
                    }
                }
            }
        )
    }

    override fun setPreserveEmptyPane(preserve: Boolean) {
        controller.callFunction(PANE_SET_PRESERVE_EMPTY, mapOf(PANE_ID to uuid, PRESERVE to preserve))
    }

    override fun preserveEmptyPane(onPreserveReceived: (Boolean) -> Unit) {
        controller.callFunction(
            PANE_PRESERVE_EMPTY,
            mapOf(PANE_ID to uuid),
            callback = onPreserveReceived,
            deserializer = PrimitiveSerializer.BooleanDeserializer
        )
    }

    override fun getStretchFactor(onStretchFactorReceived: (Float) -> Unit) {
        controller.callFunction(
            PANE_GET_STRETCH_FACTOR,
            mapOf(PANE_ID to uuid),
            callback = onStretchFactorReceived,
            deserializer = PrimitiveSerializer.FloatDeserializer
        )
    }

    override fun setStretchFactor(stretchFactor: Float) {
        controller.callFunction(PANE_SET_STRETCH_FACTOR, mapOf(PANE_ID to uuid, STRETCH_FACTOR to stretchFactor))
    }

    private data class PaneSeriesReference(
        val uuid: String,
        val type: SeriesType,
    )
}
