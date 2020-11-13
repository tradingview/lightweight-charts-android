package com.tradingview.lightweightcharts.api.delegates

import com.tradingview.lightweightcharts.api.interfaces.ChartApi.Func.APPLY_OPTIONS
import com.tradingview.lightweightcharts.api.interfaces.ChartApi.Func.CHART_OPTIONS
import com.tradingview.lightweightcharts.api.interfaces.ChartApi.Func.PRICE_SCALE
import com.tradingview.lightweightcharts.api.interfaces.ChartApi.Func.PRINT
import com.tradingview.lightweightcharts.api.interfaces.ChartApi.Func.REMOVE
import com.tradingview.lightweightcharts.api.interfaces.ChartApi.Func.REMOVE_SERIES
import com.tradingview.lightweightcharts.api.interfaces.ChartApi.Func.RESIZE
import com.tradingview.lightweightcharts.api.interfaces.ChartApi.Func.SUBSCRIBE_CROSSHAIR_MOVE
import com.tradingview.lightweightcharts.api.interfaces.ChartApi.Func.SUBSCRIBE_ON_CLICK
import com.tradingview.lightweightcharts.api.interfaces.ChartApi.Params.FORCE_REPAINT
import com.tradingview.lightweightcharts.api.interfaces.ChartApi.Params.HEIGHT
import com.tradingview.lightweightcharts.api.interfaces.ChartApi.Params.OPTIONS
import com.tradingview.lightweightcharts.api.interfaces.ChartApi.Params.TEXT
import com.tradingview.lightweightcharts.api.interfaces.ChartApi.Params.WIDTH
import com.tradingview.lightweightcharts.api.interfaces.SeriesApi.Func.ADD_AREA_SERIES
import com.tradingview.lightweightcharts.api.interfaces.SeriesApi.Func.ADD_BAR_SERIES
import com.tradingview.lightweightcharts.api.interfaces.SeriesApi.Func.ADD_CANDLESTICK_SERIES
import com.tradingview.lightweightcharts.api.interfaces.SeriesApi.Func.ADD_HISTOGRAM_SERIES
import com.tradingview.lightweightcharts.api.interfaces.SeriesApi.Func.ADD_LINE_SERIES
import com.tradingview.lightweightcharts.api.interfaces.SeriesApi.Params.SERIES_ID
import com.tradingview.lightweightcharts.api.interfaces.ChartApi
import com.tradingview.lightweightcharts.api.interfaces.PriceScaleApi
import com.tradingview.lightweightcharts.api.interfaces.SeriesApi
import com.tradingview.lightweightcharts.runtime.controller.WebMessageController
import com.tradingview.lightweightcharts.api.options.models.*
import com.tradingview.lightweightcharts.api.serializer.*
import com.tradingview.lightweightcharts.api.series.models.*

class ChartApiDelegate(
    private val controller: WebMessageController
): ChartApi {

    override val timeScale = TimeScaleApiDelegate(controller)

    override fun subscribeCrosshairMove(block: (params: MouseEventParams?) -> Unit) {
        controller.callSubscribe(
            SUBSCRIBE_CROSSHAIR_MOVE,
            callback = block,
            serializer = MouseEventParamsSerializer()
        )
    }

    override fun unsubscribeCrosshairMove(block: (params: MouseEventParams?) -> Unit) {
        controller.callUnsubscribe(
            SUBSCRIBE_CROSSHAIR_MOVE,
            callback = block
        )
    }

    override fun subscribeClick(block: (params: MouseEventParams?) -> Unit) {
        controller.callSubscribe(
            SUBSCRIBE_ON_CLICK,
            callback = block,
            serializer = MouseEventParamsSerializer()
        )
    }

    override fun unsubscribeClick(block: (params: MouseEventParams?) -> Unit) {
        controller.callUnsubscribe(
            SUBSCRIBE_ON_CLICK,
            callback = block
        )
    }

    override fun resize(height: Float, width: Float, forceRepaint: Boolean) {
        controller.callFunction(
            RESIZE,
            mapOf(
                HEIGHT to height,
                WIDTH to width,
                FORCE_REPAINT to forceRepaint
            )
        )
    }

    override fun addAreaSeries(
            options: AreaSeriesOptions,
            block: (api: SeriesApi<LineData>) -> Unit
    ) {
        controller.callFunction<String>(
            ADD_AREA_SERIES,
            mapOf(OPTIONS to options),
            { uuid ->
                if (uuid != null) {
                    block(SeriesApiDelegate(
                            uuid,
                            controller,
                            AreaSeriesOptionsSerializer()
                    ))
                }
            }
        )
    }

    override fun addBarSeries(
            options: BarSeriesOptions,
            block: (api: SeriesApi<BarData>) -> Unit
    ) {
        controller.callFunction<String>(
            ADD_BAR_SERIES,
            mapOf(OPTIONS to options),
            { uuid ->
                if (uuid != null) {
                    block(SeriesApiDelegate(
                            uuid,
                            controller,
                            BarSeriesOptionsSerializer()
                    ))
                }
            }
        )
    }

    override fun addCandlestickSeries(
            options: CandlestickSeriesOptions,
            block: (api: SeriesApi<BarData>) -> Unit
    ) {
        controller.callFunction<String>(
            ADD_CANDLESTICK_SERIES,
            mapOf(OPTIONS to options),
            { uuid ->
                if (uuid != null) {
                    block(SeriesApiDelegate(
                            uuid,
                            controller,
                            CandlestickSeriesOptionsSerializer()
                    ))
                }
            }
        )
    }

    override fun addHistogramSeries(
            options: HistogramSeriesOptions,
            block: (api: SeriesApi<HistogramData>) -> Unit
    ) {
        controller.callFunction<String>(
            ADD_HISTOGRAM_SERIES,
            mapOf(OPTIONS to options),
            { uuid ->
                if (uuid != null) {
                    block(SeriesApiDelegate(
                            uuid,
                            controller,
                            HistogramSeriesOptionsSerializer()
                    ))
                }
            }
        )
    }

    override fun addLineSeries(
            options: LineSeriesOptions,
            block: (api: SeriesApi<LineData>) -> Unit
    ) {
        controller.callFunction<String>(
            ADD_LINE_SERIES,
            mapOf(OPTIONS to options),
            { uuid ->
                if (uuid != null) {
                    block(SeriesApiDelegate(
                            uuid,
                            controller,
                            LineSeriesOptionsSerializer()
                    ))
                }
            },
        )
    }

    override fun remove() {
        controller.callFunction(REMOVE)
    }

    override fun removeSeries(seriesApi: SeriesApi<*>, block: () -> Unit) {
        controller.callFunction<Unit>(
            REMOVE_SERIES,
            mapOf(SERIES_ID to seriesApi.uuid),
            { block() }
        )
    }

    override fun priceScale(): PriceScaleApi {
        val uuid = controller.callFunction(PRICE_SCALE)
        return PriceScaleApiDelegate(uuid, controller)
    }

    override fun applyOptions(options: ChartOptions, onApply: () -> Unit) {
        controller.callFunction<Unit>(
                APPLY_OPTIONS,
                mapOf(OPTIONS to options),
                { onApply() }
        )
    }

    override fun options(block: (options: ChartOptions?) -> Unit) {
        controller.callFunction(
            CHART_OPTIONS,
            callback = block,
            serializer = ChartOptionsSerializer()
        )
    }

    fun print(text: String) {
        controller.callFunction(PRINT, mapOf(TEXT to text))
    }

}