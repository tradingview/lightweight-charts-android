package com.tradingview.lightweightcharts.api.interfaces

import android.graphics.Bitmap
import com.tradingview.lightweightcharts.api.options.models.*
import com.tradingview.lightweightcharts.api.series.models.*

interface ChartApi {

    object Func {
        const val SUBSCRIBE_ON_CLICK = "subscribeOnClick"
        const val SUBSCRIBE_CROSSHAIR_MOVE = "subscribeCrosshairMove"
        const val REMOVE = "remove"
        const val REMOVE_SERIES = "removeSeries"
        const val PRICE_SCALE = "priceScale"
        const val APPLY_OPTIONS = "chartApplyOptions"
        const val CHART_OPTIONS = "chartOptions"
        const val TAKE_SCREENSHOT = "takeScreenshot"
    }

    object Params {
        const val UUID = "uuid"
        const val TEXT = "text"
        const val OPTIONS = "options"
    }

    /**
     * Returns API to manipulate the time scale
     * @return target API
     */
    val timeScale: TimeScaleApi

    /**
     * Removes the chart object including all DOM elements.
     * This is an irreversible operation, you cannot do anything with the chart after removing it.
     */
    fun remove()

    /**
     * Creates an area series with specified parameters
     * @param options - customization parameters of the series being created
     * @return an interface of the created series
     */
    fun addAreaSeries(
            options: AreaSeriesOptions = AreaSeriesOptions(),
            onSeriesCreated: (api: SeriesApi) -> Unit
    )

    /**
     * Creates a bar series with specified parameters
     * @param options - customization parameters of the series being created
     * @return an interface of the created series
     */
    fun addBarSeries(
            options: BarSeriesOptions = BarSeriesOptions(),
            onSeriesCreated: (api: SeriesApi) -> Unit
    )

    /**
     * Creates a candlestick series with specified parameters
     * @param options - customization parameters of the series being created
     * @return an interface of the created series
     */
    fun addCandlestickSeries(
            options: CandlestickSeriesOptions = CandlestickSeriesOptions(),
            onSeriesCreated: (api: SeriesApi) -> Unit
    )

    /**
     * Creates a histogram series with specified parameters
     * @param options - customization parameters of the series being created
     * @return an interface of the created series
     */
    fun addHistogramSeries(
            options: HistogramSeriesOptions = HistogramSeriesOptions(),
            onSeriesCreated: (api: SeriesApi) -> Unit
    )

    /**
     * Creates a line series with specified parameters
     * @param options - customization parameters of the series being created
     * @return an interface of the created series
     */
    fun addLineSeries(
            options: LineSeriesOptions = LineSeriesOptions(),
            onSeriesCreated: (api: SeriesApi) -> Unit
    )

    /**
     * Removes a series of any type.
     * This is an irreversible operation, you cannot do anything with the series after removing it
     */
    fun removeSeries(seriesApi: SeriesApi, onSeriesDeleted: () -> Unit = {})

    /**
     * Adds a subscription to mouse click event
     */
    fun subscribeClick(onClicked: (params: MouseEventParams) -> Unit)

    /**
     * Removes mouse click subscription
     */
    fun unsubscribeClick(onClicked: (params: MouseEventParams) -> Unit)

    /**
     * Adds a subscription to crosshair movement to receive notifications on crosshair movements
     */
    fun subscribeCrosshairMove(onCrosshairMoved: (params: MouseEventParams) -> Unit)

    /**
     * Removes a subscription on crosshair movement
     */
    fun unsubscribeCrosshairMove(onCrosshairMoved: (params: MouseEventParams) -> Unit)

    /**
     * Returns API to manipulate the price scale
     * @return target API
     */
    fun priceScale(id: PriceScaleId): PriceScaleApi

    @Deprecated("Using ChartApi.priceScale() method without arguments " +
            "has been deprecated, pass valid price scale id instead")
    fun priceScale(): PriceScaleApi

    /**
     * Applies new options to the chart
     * @param options - any subset of chart options
     */
    fun applyOptions(options: ChartOptions)

    /**
     * Applies new options to the chart
     * @param options - any subset of chart options
     */
    fun applyOptions(options: ChartOptions.() -> Unit) {
        applyOptions(ChartOptions().apply(options))
    }

    /**
     * Returns currently applied options
     * @return - full set of currently applied options, including defaults
     */
    fun options(onOptionsReceived: (options: ChartOptions) -> Unit)

    /**
     * Returns bitmap with captured chart
     * @return bitmap with captured chart
     */
    fun takeScreenshot(onScreenshotReady: (Bitmap) -> Unit)
}