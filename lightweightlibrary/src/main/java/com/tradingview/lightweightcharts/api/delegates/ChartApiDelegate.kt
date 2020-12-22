package com.tradingview.lightweightcharts.api.delegates

import com.tradingview.lightweightcharts.api.interfaces.ChartApi.Func.APPLY_OPTIONS
import com.tradingview.lightweightcharts.api.interfaces.ChartApi.Func.CHART_OPTIONS
import com.tradingview.lightweightcharts.api.interfaces.ChartApi.Func.PRICE_SCALE
import com.tradingview.lightweightcharts.api.interfaces.ChartApi.Func.PRINT
import com.tradingview.lightweightcharts.api.interfaces.ChartApi.Func.REMOVE
import com.tradingview.lightweightcharts.api.interfaces.ChartApi.Func.REMOVE_SERIES
import com.tradingview.lightweightcharts.api.interfaces.ChartApi.Func.SUBSCRIBE_CROSSHAIR_MOVE
import com.tradingview.lightweightcharts.api.interfaces.ChartApi.Func.SUBSCRIBE_ON_CLICK
import com.tradingview.lightweightcharts.api.interfaces.ChartApi.Params.OPTIONS
import com.tradingview.lightweightcharts.api.interfaces.ChartApi.Params.TEXT
import com.tradingview.lightweightcharts.api.interfaces.SeriesApi.Func.ADD_AREA_SERIES
import com.tradingview.lightweightcharts.api.interfaces.SeriesApi.Func.ADD_BAR_SERIES
import com.tradingview.lightweightcharts.api.interfaces.SeriesApi.Func.ADD_CANDLESTICK_SERIES
import com.tradingview.lightweightcharts.api.interfaces.SeriesApi.Func.ADD_HISTOGRAM_SERIES
import com.tradingview.lightweightcharts.api.interfaces.SeriesApi.Func.ADD_LINE_SERIES
import com.tradingview.lightweightcharts.api.interfaces.SeriesApi.Params.SERIES_UUID
import com.tradingview.lightweightcharts.api.interfaces.ChartApi
import com.tradingview.lightweightcharts.api.interfaces.PriceScaleApi
import com.tradingview.lightweightcharts.api.interfaces.SeriesApi
import com.tradingview.lightweightcharts.api.interfaces.SeriesApi.Params.PRICE_SCALE_ID
import com.tradingview.lightweightcharts.runtime.controller.WebMessageController
import com.tradingview.lightweightcharts.api.options.models.*
import com.tradingview.lightweightcharts.api.serializer.*
import com.tradingview.lightweightcharts.api.series.models.*

class ChartApiDelegate(
    private val controller: WebMessageController
) : ChartApi {

    override val timeScale = TimeScaleApiDelegate(controller)

    override fun subscribeCrosshairMove(onCrosshairMove: (params: MouseEventParams?) -> Unit) {
        controller.callSubscribe(
            SUBSCRIBE_CROSSHAIR_MOVE,
            callback = onCrosshairMove,
            serializer = MouseEventParamsSerializer()
        )
    }

    override fun unsubscribeCrosshairMove(funLink: (params: MouseEventParams?) -> Unit) {
        controller.callUnsubscribe(
            SUBSCRIBE_CROSSHAIR_MOVE,
            callback = funLink
        )
    }

    override fun subscribeClick(onClick: (params: MouseEventParams?) -> Unit) {
        controller.callSubscribe(
            SUBSCRIBE_ON_CLICK,
            callback = onClick,
            serializer = MouseEventParamsSerializer()
        )
    }

    override fun unsubscribeClick(funLink: (params: MouseEventParams?) -> Unit) {
        controller.callUnsubscribe(
            SUBSCRIBE_ON_CLICK,
            callback = funLink
        )
    }

    override fun addAreaSeries(
        options: AreaSeriesOptions,
        onSeriesCreated: (api: SeriesApi) -> Unit
    ) {
        controller.callFunction<String>(
            ADD_AREA_SERIES,
            mapOf(OPTIONS to options),
            { uuid ->
                onSeriesCreated(
                    SeriesApiDelegate(
                        uuid,
                        controller,
                        AreaSeriesOptionsSerializer()
                    )
                )
            }
        )
    }

    override fun addBarSeries(
        options: BarSeriesOptions,
        onSeriesCreated: (api: SeriesApi) -> Unit
    ) {
        controller.callFunction<String>(
            ADD_BAR_SERIES,
            mapOf(OPTIONS to options),
            { uuid ->
                onSeriesCreated(
                    SeriesApiDelegate(
                        uuid,
                        controller,
                        BarSeriesOptionsSerializer()
                    )
                )
            }
        )
    }

    override fun addCandlestickSeries(
        options: CandlestickSeriesOptions,
        onSeriesCreated: (api: SeriesApi) -> Unit
    ) {
        controller.callFunction<String>(
            ADD_CANDLESTICK_SERIES,
            mapOf(OPTIONS to options),
            { uuid ->
                onSeriesCreated(
                    SeriesApiDelegate(
                        uuid,
                        controller,
                        CandlestickSeriesOptionsSerializer()
                    )
                )
            }
        )
    }

    override fun addHistogramSeries(
        options: HistogramSeriesOptions,
        onSeriesCreated: (api: SeriesApi) -> Unit
    ) {
        controller.callFunction<String>(
            ADD_HISTOGRAM_SERIES,
            mapOf(OPTIONS to options),
            { uuid ->
                onSeriesCreated(
                    SeriesApiDelegate(
                        uuid,
                        controller,
                        HistogramSeriesOptionsSerializer()
                    )
                )
            }
        )
    }

    override fun addLineSeries(
        options: LineSeriesOptions,
        onSeriesCreated: (api: SeriesApi) -> Unit
    ) {
        controller.callFunction<String>(
            ADD_LINE_SERIES,
            mapOf(OPTIONS to options),
            { uuid ->
                onSeriesCreated(
                    SeriesApiDelegate(
                        uuid,
                        controller,
                        LineSeriesOptionsSerializer()
                    )
                )
            },
        )
    }

    override fun remove() {
        controller.callFunction(REMOVE)
    }

    override fun removeSeries(seriesApi: SeriesApi, onSeriesDeleted: () -> Unit) {
        controller.callFunction(
            REMOVE_SERIES,
            mapOf(SERIES_UUID to seriesApi.uuid),
            onSeriesDeleted
        )
    }

    override fun priceScale(id: PriceScaleId): PriceScaleApi {
        val uuid = controller.callFunction(
            PRICE_SCALE,
            mapOf(PRICE_SCALE_ID to id.value)
        )
        return PriceScaleApiDelegate(uuid, controller)
    }

    @Deprecated("Using ChartApi.priceScale() method without arguments " +
            "has been deprecated, pass valid price scale id instead")
    override fun priceScale(): PriceScaleApi {
        val uuid = controller.callFunction(PRICE_SCALE)
        return PriceScaleApiDelegate(uuid, controller)
    }

    override fun applyOptions(options: ChartOptions, onApply: () -> Unit) {
        controller.callFunction(
            APPLY_OPTIONS,
            mapOf(OPTIONS to options),
            onApply
        )
    }

    override fun options(onOptionsReceived: (options: ChartOptions) -> Unit) {
        controller.callFunction(
            CHART_OPTIONS,
            callback = onOptionsReceived,
            serializer = ChartOptionsSerializer()
        )
    }

    fun print(text: String) {
        controller.callFunction(PRINT, mapOf(TEXT to text))
    }

}