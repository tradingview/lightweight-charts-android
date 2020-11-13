package com.tradingview.lightweightcharts.api.interfaces

import com.tradingview.lightweightcharts.api.series.models.BarData
import com.tradingview.lightweightcharts.api.series.models.HistogramData
import com.tradingview.lightweightcharts.api.series.models.LineData
import com.tradingview.lightweightcharts.api.series.models.MouseEventParams
import com.tradingview.lightweightcharts.api.series.models.TimeRange
import com.tradingview.lightweightcharts.api.options.models.*

interface ChartApi {

    object Func {
        const val RESIZE = "resize"
        const val PRINT = "print"
        const val SUBSCRIBE_ON_CLICK = "subscribeOnClick"
        const val SUBSCRIBE_CROSSHAIR_MOVE = "subscribeCrosshairMove"
        const val SUBSCRIBE_VISIBLE_TIME_RANGE_CHANGE = "subscribeVisibleTimeRangeChange"
        const val REMOVE = "remove"
        const val REMOVE_SERIES = "removeSeries"
        const val TIME_SCALE = "timeScale"
        const val PRICE_SCALE = "priceScale"
        const val APPLY_OPTIONS = "chartApplyOptions"
        const val CHART_OPTIONS = "chartOptions"
    }

    object Params {
        const val UUID = "uuid"
        const val TEXT = "text"
        const val HEIGHT = "height"
        const val WIDTH = "width"
        const val OPTIONS = "options"
        const val FORCE_REPAINT = "forceRepaint"
    }

    /**
     * Removes the chart object including all DOM elements.
     * This is an irreversible operation, you cannot do anything with the chart after removing it.
     */
    fun remove()

    /**
     * Sets fixed size of the chart. By default chart takes up 100% of its container
     * @param height - target height of the chart
     * @param width - target width of the chart
     * @param forceRepaint - true to initiate resize immediately.
     *                       One could need this to get screenshot immediately after resize
     */
    fun resize(height: Float, width: Float, forceRepaint: Boolean = false)

    /**
     * Creates an area series with specified parameters
     * @param options - customization parameters of the series being created
     * @returns an interface of the created series
     */
    fun addAreaSeries(
            options: AreaSeriesOptions = AreaSeriesOptions(),
            block: (api: SeriesApi<LineData>) -> Unit
    )

    /**
     * Creates a bar series with specified parameters
     * @param options - customization parameters of the series being created
     * @returns an interface of the created series
     */
    fun addBarSeries(
            options: BarSeriesOptions = BarSeriesOptions(),
            block: (api: SeriesApi<BarData>) -> Unit
    )

    /**
     * Creates a candlestick series with specified parameters
     * @param options - customization parameters of the series being created
     * @returns an interface of the created series
     */
    fun addCandlestickSeries(
            options: CandlestickSeriesOptions = CandlestickSeriesOptions(),
            block: (api: SeriesApi<BarData>) -> Unit
    )

    /**
     * Creates a histogram series with specified parameters
     * @param options - customization parameters of the series being created
     * @returns an interface of the created series
     */
    fun addHistogramSeries(
            options: HistogramSeriesOptions = HistogramSeriesOptions(),
            block: (api: SeriesApi<HistogramData>) -> Unit
    )

    /**
     * Creates a line series with specified parameters
     * @param options - customization parameters of the series being created
     * @returns an interface of the created series
     */
    fun addLineSeries(
            options: LineSeriesOptions = LineSeriesOptions(),
            block: (api: SeriesApi<LineData>) -> Unit
    )

    /**
     * Removes a series of any type.
     * This is an irreversible operation, you cannot do anything with the series after removing it
     */
    fun removeSeries(seriesApi: SeriesApi<*>, block: () -> Unit = {})

    /**
     * Adds a subscription to mouse click event
     */
    fun subscribeClick(block: (params: MouseEventParams?) -> Unit)

    /**
     * Removes mouse click subscription
     */
    fun unsubscribeClick(block: (params: MouseEventParams?) -> Unit)

    /**
     * Adds a subscription to crosshair movement to receive notifications on crosshair movements
     */
    fun subscribeCrosshairMove(block: (params: MouseEventParams?) -> Unit)

    /**
     * Removes a subscription on crosshair movement
     */
    fun unsubscribeCrosshairMove(block: (params: MouseEventParams?) -> Unit)

    /**
     * Adds a subscription to visible range changes
     * to receive notification about visible range of data changes
     */
    fun subscribeVisibleTimeRangeChange(block: (params: TimeRange?) -> Unit)

    /**
     * Removes a subscription to visible range changes
     */
    fun unsubscribeVisibleTimeRangeChange(block: (params: TimeRange?) -> Unit)

    /**
     * Returns API to manipulate the price scale
     * @returns - target API
     */
    fun priceScale(): PriceScaleApi

    /**
     * Returns API to manipulate the time scale
     * @returns - target API
     */
    fun timeScale(): TimeScaleApi

    /**
     * Applies new options to the chart
     * @param options - any subset of chart options
     */
    fun applyOptions(options: ChartOptions, onApply: () -> Unit = {})

    /**
     * Returns currently applied options
     * @returns - full set of currently applied options, including defaults
     */

    fun options(block: (options: ChartOptions?) -> Unit)

}