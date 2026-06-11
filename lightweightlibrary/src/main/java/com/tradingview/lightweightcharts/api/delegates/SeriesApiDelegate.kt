package com.tradingview.lightweightcharts.api.delegates

import com.tradingview.lightweightcharts.api.interfaces.PriceFormatterApi
import com.tradingview.lightweightcharts.api.interfaces.PriceScaleApi
import com.tradingview.lightweightcharts.api.interfaces.SeriesApi
import com.tradingview.lightweightcharts.api.interfaces.SeriesMarkersApi
import com.tradingview.lightweightcharts.api.interfaces.SeriesApi.Func.APPLY_OPTIONS
import com.tradingview.lightweightcharts.api.interfaces.SeriesApi.Func.BARS_IN_LOGICAL_RANGE
import com.tradingview.lightweightcharts.api.interfaces.SeriesApi.Func.COORDINATE_TO_PRICE
import com.tradingview.lightweightcharts.api.interfaces.SeriesApi.Func.DATA as SERIES_DATA
import com.tradingview.lightweightcharts.api.interfaces.SeriesApi.Func.CREATE_SERIES_MARKERS_COMPAT
import com.tradingview.lightweightcharts.api.interfaces.SeriesApi.Func.CREATE_PRICE_LINE
import com.tradingview.lightweightcharts.api.interfaces.SeriesApi.Func.DATA_BY_INDEX_SERIES
import com.tradingview.lightweightcharts.api.interfaces.SeriesApi.Func.GET_MARKERS_SERIES
import com.tradingview.lightweightcharts.api.interfaces.SeriesApi.Func.GET_PANE
import com.tradingview.lightweightcharts.api.interfaces.SeriesApi.Func.LAST_VALUE_DATA
import com.tradingview.lightweightcharts.api.interfaces.SeriesApi.Func.MOVE_TO_PANE
import com.tradingview.lightweightcharts.api.interfaces.SeriesApi.Func.POP
import com.tradingview.lightweightcharts.api.interfaces.SeriesApi.Func.PRICE_SCALE_SERIES
import com.tradingview.lightweightcharts.api.interfaces.SeriesApi.Func.PRICE_LINES
import com.tradingview.lightweightcharts.api.interfaces.SeriesApi.Func.PRICE_TO_COORDINATE
import com.tradingview.lightweightcharts.api.interfaces.SeriesApi.Func.REMOVE_PRICE_LINE
import com.tradingview.lightweightcharts.api.interfaces.SeriesApi.Func.SERIES_ORDER
import com.tradingview.lightweightcharts.api.interfaces.SeriesApi.Func.SERIES_TYPE
import com.tradingview.lightweightcharts.api.interfaces.SeriesApi.Func.SET_SERIES_ORDER
import com.tradingview.lightweightcharts.api.interfaces.SeriesApi.Func.SET_MARKERS
import com.tradingview.lightweightcharts.api.interfaces.SeriesApi.Func.SET_SERIES
import com.tradingview.lightweightcharts.api.interfaces.SeriesApi.Func.SUBSCRIBE_DATA_CHANGED
import com.tradingview.lightweightcharts.api.interfaces.SeriesApi.Func.UPDATE
import com.tradingview.lightweightcharts.api.interfaces.SeriesApi.Params.BAR
import com.tradingview.lightweightcharts.api.interfaces.SeriesApi.Params.COORDINATE
import com.tradingview.lightweightcharts.api.interfaces.SeriesApi.Params.COUNT
import com.tradingview.lightweightcharts.api.interfaces.SeriesApi.Params.DATA as DATA_PARAM
import com.tradingview.lightweightcharts.api.interfaces.SeriesApi.Params.GLOBAL_LAST
import com.tradingview.lightweightcharts.api.interfaces.SeriesApi.Params.LINE_ID
import com.tradingview.lightweightcharts.api.interfaces.SeriesApi.Params.LOGICAL_INDEX
import com.tradingview.lightweightcharts.api.interfaces.SeriesApi.Params.MISMATCH_DIRECTION
import com.tradingview.lightweightcharts.api.interfaces.SeriesApi.Params.OPTIONS
import com.tradingview.lightweightcharts.api.interfaces.SeriesApi.Params.ORDER
import com.tradingview.lightweightcharts.api.interfaces.SeriesApi.Params.PANE_INDEX
import com.tradingview.lightweightcharts.api.interfaces.SeriesApi.Params.PRICE
import com.tradingview.lightweightcharts.api.interfaces.SeriesApi.Params.RANGE
import com.tradingview.lightweightcharts.api.interfaces.SeriesApi.Params.SERIES_UUID
import com.tradingview.lightweightcharts.api.options.enums.MismatchDirection
import com.tradingview.lightweightcharts.api.options.models.AreaSeriesOptions
import com.tradingview.lightweightcharts.api.options.models.BarSeriesOptions
import com.tradingview.lightweightcharts.api.options.models.BaselineSeriesOptions
import com.tradingview.lightweightcharts.api.options.models.CandlestickSeriesOptions
import com.tradingview.lightweightcharts.api.options.models.HistogramSeriesOptions
import com.tradingview.lightweightcharts.api.options.models.LineSeriesOptions
import com.tradingview.lightweightcharts.api.options.models.PriceLineOptions
import com.tradingview.lightweightcharts.api.options.models.SeriesMarkersOptions
import com.tradingview.lightweightcharts.api.options.models.SeriesOptionsCommon
import com.tradingview.lightweightcharts.api.serializer.AreaSeriesOptionsDeserializer
import com.tradingview.lightweightcharts.api.serializer.BarsInfoDeserializer
import com.tradingview.lightweightcharts.api.serializer.BarSeriesOptionsDeserializer
import com.tradingview.lightweightcharts.api.serializer.BaselineSeriesOptionsDeserializer
import com.tradingview.lightweightcharts.api.serializer.ClassSimpleDeserializer
import com.tradingview.lightweightcharts.api.serializer.ClassListDeserializer
import com.tradingview.lightweightcharts.api.serializer.CandlestickSeriesOptionsDeserializer
import com.tradingview.lightweightcharts.api.serializer.Deserializer
import com.tradingview.lightweightcharts.api.serializer.HistogramSeriesOptionsDeserializer
import com.tradingview.lightweightcharts.api.serializer.LastValueDataDeserializer
import com.tradingview.lightweightcharts.api.serializer.LineSeriesOptionsDeserializer
import com.tradingview.lightweightcharts.api.serializer.PrimitiveSerializer
import com.tradingview.lightweightcharts.api.serializer.SeriesMarkersDeserializer
import com.tradingview.lightweightcharts.api.serializer.SeriesTypeDeserializer
import com.tradingview.lightweightcharts.api.series.common.PriceLine
import com.tradingview.lightweightcharts.api.series.common.PriceLineDelegate
import com.tradingview.lightweightcharts.api.series.common.SeriesData
import com.tradingview.lightweightcharts.api.series.enums.SeriesType
import com.tradingview.lightweightcharts.api.series.models.BarsInfo
import com.tradingview.lightweightcharts.api.series.models.LastValueData
import com.tradingview.lightweightcharts.api.series.models.LogicalRange
import com.tradingview.lightweightcharts.api.series.models.SeriesMarker
import com.tradingview.lightweightcharts.runtime.controller.WebMessageController
import com.tradingview.lightweightcharts.runtime.version.ChartRuntimeObject

class SeriesApiDelegate<T : SeriesOptionsCommon>(
    override val uuid: String,
    private val controller: WebMessageController,
    private val optionsDeserializer: Deserializer<out T>,
) : SeriesApi, ChartRuntimeObject {
    private val dataChangedCallbacks = mutableMapOf<() -> Unit, (Unit) -> Unit>()

    companion object {
        fun optionsDeserializer(type: SeriesType): Deserializer<out SeriesOptionsCommon> {
            return when (type) {
                SeriesType.LINE -> LineSeriesOptionsDeserializer()
                SeriesType.AREA -> AreaSeriesOptionsDeserializer()
                SeriesType.BAR -> BarSeriesOptionsDeserializer()
                SeriesType.CANDLESTICK -> CandlestickSeriesOptionsDeserializer()
                SeriesType.HISTOGRAM -> HistogramSeriesOptionsDeserializer()
                SeriesType.BASELINE -> BaselineSeriesOptionsDeserializer()
            }
        }
    }

    override fun getVersion(): Int {
        return controller.hashCode()
    }

    override fun setData(data: List<SeriesData>) {
        controller.callFunction(
            SET_SERIES,
            mapOf(
                SERIES_UUID to uuid,
                DATA_PARAM to data
            )
        )
    }

    override fun <T : SeriesData> data(clazz: Class<T>, dataReceived: (List<T>) -> Unit) {
        controller.callFunction(
            SERIES_DATA,
            mapOf(SERIES_UUID to uuid),
            callback = dataReceived,
            deserializer = ClassListDeserializer(clazz)
        )
    }

    override fun priceToCoordinate(price: Float, onCoordinateReceived: (Float?) -> Unit) {
        controller.callFunction<Float?>(
            PRICE_TO_COORDINATE,
            mapOf(
                SERIES_UUID to uuid,
                PRICE to price
            ),
            callback = onCoordinateReceived,
            PrimitiveSerializer.FloatDeserializer
        )
    }

    override fun coordinateToPrice(coordinate: Float, onPriceReceived: (Float?) -> Unit) {
        controller.callFunction(
            COORDINATE_TO_PRICE,
            mapOf(
                SERIES_UUID to uuid,
                COORDINATE to coordinate
            ),
            callback = onPriceReceived,
            PrimitiveSerializer.FloatDeserializer
        )
    }

    override fun barsInLogicalRange(range: LogicalRange, onBarsInfoReceived: (BarsInfo?) -> Unit) {
        controller.callFunction(
            BARS_IN_LOGICAL_RANGE,
            mapOf(
                SERIES_UUID to uuid,
                RANGE to range
            ),
            callback = onBarsInfoReceived,
            deserializer = BarsInfoDeserializer()
        )
    }

    override fun priceFormatter(): PriceFormatterApi {
        return PriceFormatterApiDelegate(uuid, controller)
    }

    override fun applyOptions(options: SeriesOptionsCommon) {
        controller.callFunction(
            APPLY_OPTIONS,
            mapOf(
                SERIES_UUID to uuid,
                OPTIONS to options
            )
        )
    }

    override fun options(onOptionsReceived: (SeriesOptionsCommon) -> Unit) {
        controller.callFunction(
            OPTIONS,
            mapOf(SERIES_UUID to uuid),
            callback = onOptionsReceived,
            deserializer = optionsDeserializer
        )
    }

    override fun priceScale(): PriceScaleApi {
        val uuid = controller.callFunction(
            PRICE_SCALE_SERIES,
            mapOf(
                SERIES_UUID to uuid,
            )
        )
        return PriceScaleApiDelegate(uuid, controller)
    }

    override fun <T : SeriesData> dataByIndex(
        clazz: Class<T>,
        logicalIndex: Int,
        direction: MismatchDirection,
        dataReceived: (T) -> Unit,
    ) {
        controller.callFunction(
            DATA_BY_INDEX_SERIES,
            mapOf(
                SERIES_UUID to uuid,
                LOGICAL_INDEX to logicalIndex,
                MISMATCH_DIRECTION to direction.value
            ),
            callback = dataReceived,
            deserializer = ClassSimpleDeserializer(clazz)
        )
    }

    override fun setMarkers(data: List<SeriesMarker>) {
        controller.callFunction(
            SET_MARKERS,
            mapOf(
                SERIES_UUID to uuid,
                DATA_PARAM to data
            )
        )
    }

    override fun createSeriesMarkers(
        data: List<SeriesMarker>,
        options: SeriesMarkersOptions?,
        onMarkersCreated: (SeriesMarkersApi) -> Unit
    ) {
        val params = mutableMapOf<String, Any>(
            SERIES_UUID to uuid,
            DATA_PARAM to data
        )
        options?.let { params[OPTIONS] = it }
        controller.callFunction(
            CREATE_SERIES_MARKERS_COMPAT,
            params,
            { markersUuid -> onMarkersCreated(SeriesMarkersApiDelegate(markersUuid, controller)) },
            PrimitiveSerializer.StringDeserializer
        )
    }

    override fun markers(markersReceived: (List<SeriesMarker>) -> Unit) {
        controller.callFunction(
            GET_MARKERS_SERIES,
            mapOf(
                SERIES_UUID to uuid,
            ),
            callback = markersReceived,
            deserializer = SeriesMarkersDeserializer()
        )
    }

    override fun createPriceLine(options: PriceLineOptions): PriceLine {
        val uuid = controller.callFunction(
            CREATE_PRICE_LINE,
            mapOf(
                SERIES_UUID to uuid,
                OPTIONS to options
            )
        )
        return PriceLineDelegate(
            uuid,
            controller
        )
    }

    override fun removePriceLine(line: PriceLine) {
        controller.callFunction(
            REMOVE_PRICE_LINE,
            mapOf(
                SERIES_UUID to uuid,
                LINE_ID to line.uuid
            )
        )
    }

    override fun priceLines(linesReceived: (List<PriceLine>) -> Unit) {
        controller.callFunction(
            PRICE_LINES,
            mapOf(SERIES_UUID to uuid),
            callback = { lineIds ->
                linesReceived(lineIds.map { lineId -> PriceLineDelegate(lineId, controller) })
            },
            deserializer = object : Deserializer<List<String>>() {
                override fun deserialize(json: com.google.gson.JsonElement): List<String>? {
                    return json.asJsonArray.map { it.asString }
                }
            }
        )
    }

    override fun update(bar: SeriesData) {
        update(bar, false)
    }

    override fun update(bar: SeriesData, historicalUpdate: Boolean) {
        controller.callFunction(
            UPDATE,
            mapOf(
                SERIES_UUID to uuid,
                BAR to bar,
                "historicalUpdate" to historicalUpdate
            )
        )
    }

    override fun subscribeDataChanged(onDataChanged: () -> Unit) {
        if (dataChangedCallbacks.containsKey(onDataChanged)) {
            return
        }

        val wrapper: (Unit) -> Unit = { onDataChanged() }
        dataChangedCallbacks[onDataChanged] = wrapper
        controller.callSubscribe(
            SUBSCRIBE_DATA_CHANGED,
            mapOf(SERIES_UUID to uuid),
            callback = wrapper,
            deserializer = object : Deserializer<Unit>() {
                override fun deserialize(json: com.google.gson.JsonElement): Unit? = Unit
            }
        )
    }

    override fun unsubscribeDataChanged(onDataChanged: () -> Unit) {
        val wrapper = dataChangedCallbacks.remove(onDataChanged) ?: return
        controller.callUnsubscribe(
            SUBSCRIBE_DATA_CHANGED,
            subscription = wrapper
        )
    }

    internal fun unsubscribeAllDataChanged() {
        val wrappers = dataChangedCallbacks.values.toList()
        dataChangedCallbacks.clear()
        wrappers.forEach { wrapper ->
            controller.callUnsubscribe(
                SUBSCRIBE_DATA_CHANGED,
                subscription = wrapper
            )
        }
    }

    override fun <T : SeriesData> pop(clazz: Class<T>, count: Int, dataReceived: (List<T>) -> Unit) {
        controller.callFunction(
            POP,
            mapOf(
                SERIES_UUID to uuid,
                COUNT to count
            ),
            callback = dataReceived,
            deserializer = ClassListDeserializer(clazz)
        )
    }

    override fun seriesType(onSeriesTypeReceived: (SeriesType) -> Unit) {
        controller.callFunction(
            SERIES_TYPE,
            mapOf(SERIES_UUID to uuid),
            callback = onSeriesTypeReceived,
            deserializer = SeriesTypeDeserializer()
        )
    }

    override fun seriesOrder(onSeriesOrderReceived: (Int) -> Unit) {
        controller.callFunction(
            SERIES_ORDER,
            mapOf(SERIES_UUID to uuid),
            callback = onSeriesOrderReceived,
            deserializer = PrimitiveSerializer.IntDeserializer
        )
    }

    override fun setSeriesOrder(order: Int) {
        controller.callFunction(
            SET_SERIES_ORDER,
            mapOf(SERIES_UUID to uuid, ORDER to order)
        )
    }

    override fun moveToPane(paneIndex: Int) {
        controller.callFunction(
            MOVE_TO_PANE,
            mapOf(SERIES_UUID to uuid, PANE_INDEX to paneIndex)
        )
    }

    override fun getPane(onPaneReceived: (com.tradingview.lightweightcharts.api.interfaces.PaneApi) -> Unit) {
        controller.callFunction(
            GET_PANE,
            mapOf(SERIES_UUID to uuid),
            callback = { paneUuid -> onPaneReceived(PaneApiDelegate(paneUuid, controller)) },
            deserializer = PrimitiveSerializer.StringDeserializer
        )
    }

    override fun lastValueData(globalLast: Boolean, onDataReceived: (LastValueData) -> Unit) {
        controller.callFunction(
            LAST_VALUE_DATA,
            mapOf(SERIES_UUID to uuid, GLOBAL_LAST to globalLast),
            callback = onDataReceived,
            deserializer = LastValueDataDeserializer()
        )
    }
}
