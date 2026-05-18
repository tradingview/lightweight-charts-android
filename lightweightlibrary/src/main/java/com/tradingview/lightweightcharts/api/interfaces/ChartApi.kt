package com.tradingview.lightweightcharts.api.interfaces

import android.graphics.Bitmap
import com.tradingview.lightweightcharts.api.chart.models.ImageMimeType
import com.tradingview.lightweightcharts.api.chart.models.ScreenshotOptions
import com.tradingview.lightweightcharts.api.options.common.BaselineStyleOptions
import com.tradingview.lightweightcharts.api.options.models.*
import com.tradingview.lightweightcharts.api.series.enums.SeriesType
import com.tradingview.lightweightcharts.api.series.models.*

interface ChartApi {

    object Func {
        const val SUBSCRIBE_ON_CLICK = "subscribeOnClick"
        const val SUBSCRIBE_CROSSHAIR_MOVE = "subscribeCrosshairMove"
        const val REMOVE = "remove"
        const val REMOVE_SERIES = "removeSeries"
        const val PRICE_SCALE = "priceScale"
        const val RESIZE = "resize"
        const val APPLY_OPTIONS = "chartApplyOptions"
        const val CHART_OPTIONS = "chartOptions"
        const val TAKE_SCREENSHOT = "takeScreenshot"
        const val SUBSCRIBE_DBL_CLICK = "subscribeDblClick"
        const val SET_CROSSHAIR_POSITION = "setCrosshairPosition"
        const val CLEAR_CROSSHAIR_POSITION = "clearCrosshairPosition"
        const val PANE_SIZE = "paneSize"
        const val AUTO_SIZE_ACTIVE = "autoSizeActive"
    }

    object Params {
        const val UUID = "uuid"
        const val TEXT = "text"
        const val OPTIONS = "options"
        const val MIME = "mimeType"
        const val WIDTH = "width"
        const val HEIGHT = "height"
        const val FORCE_REPAINT = "forceRepaint"
        const val ADD_TOP_LAYER = "addTopLayer"
        const val INCLUDE_CROSSHAIR = "includeCrosshair"
        const val PRICE = "price"
        const val HORIZONTAL_POSITION = "horizontalPosition"
        const val SERIES_UUID = "seriesId"
        const val PANE_INDEX = "paneIndex"
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
     * Resizes chart to fixed dimensions. If autoSize is active, the core may ignore dimensions.
     */
    fun resize(width: Int, height: Int, forceRepaint: Boolean = false)

    /**
     * Returns all panes in the chart.
     */
    fun panes(onPanesReceived: (List<PaneApi>) -> Unit)

    /**
     * Adds a pane to the chart.
     */
    fun addPane(preserveEmptyPane: Boolean = false, onPaneCreated: (PaneApi) -> Unit)

    fun removePane(index: Int)

    fun swapPanes(first: Int, second: Int)

    fun paneSize(paneIndex: Int = 0, onPaneSizeReceived: (android.util.SizeF) -> Unit)

    fun autoSizeActive(onAutoSizeActiveReceived: (Boolean) -> Unit)

    /**
     * Creates an area series with specified parameters
     * @param options customization parameters of the series being created
     * @return an interface of the created series
     */
    fun addAreaSeries(
        options: AreaSeriesOptions = AreaSeriesOptions(),
        onSeriesCreated: (api: SeriesApi) -> Unit
    )

    /**
     * Creates a bar series with specified parameters
     * @param options customization parameters of the series being created
     * @param onSeriesCreated returns an interface of the created series
     */
    fun addBarSeries(
        options: BarSeriesOptions = BarSeriesOptions(),
        onSeriesCreated: (api: SeriesApi) -> Unit
    )

    /**
     * Creates a candlestick series with specified parameters
     * @param options customization parameters of the series being created
     * @param onSeriesCreated returns an interface of the created series
     */
    fun addCandlestickSeries(
        options: CandlestickSeriesOptions = CandlestickSeriesOptions(),
        onSeriesCreated: (api: SeriesApi) -> Unit
    )

    /**
     * Creates a histogram series with specified parameters
     * @param options customization parameters of the series being created
     * @param onSeriesCreated returns an interface of the created series
     */
    fun addHistogramSeries(
        options: HistogramSeriesOptions = HistogramSeriesOptions(),
        onSeriesCreated: (api: SeriesApi) -> Unit
    )

    /**
     * Creates a line series with specified parameters
     * @param options customization parameters of the series being created
     * @param onSeriesCreated returns an interface of the created series
     */
    fun addLineSeries(
        options: LineSeriesOptions = LineSeriesOptions(),
        onSeriesCreated: (api: SeriesApi) -> Unit
    )

    /**
     * A baseline chart is another way of displaying quantitative data.
     * It's basically two colored areas (top and bottom) between
     * the line connecting all data points and the baseline line.
     * A baseline series has a crosshair marker - a round mark
     * which is moving along the series' line
     * while the cursor is moving on a chart along the time scale.
     *
     * @param options customization parameters of the series being created
     * @param onSeriesCreated returns an interface of the created series
     */
    fun addBaselineSeries(
        options: BaselineSeriesOptions = BaselineSeriesOptions(),
        onSeriesCreated: (api: SeriesApi) -> Unit
    )

    @Deprecated(
        message = "Use BaselineSeriesOptions to include v5 common series options.",
        level = DeprecationLevel.WARNING
    )
    fun addBaselineSeries(
        options: BaselineStyleOptions,
        onSeriesCreated: (api: SeriesApi) -> Unit
    )

    /**
     * v5-native series creation API.
     */
    fun addSeries(
        type: SeriesType,
        options: SeriesOptionsCommon? = null,
        paneIndex: Int? = null,
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
     * Adds a subscription to double click event.
     */
    fun subscribeDblClick(onDoubleClicked: (params: MouseEventParams) -> Unit)

    /**
     * Removes double click subscription.
     */
    fun unsubscribeDblClick(onDoubleClicked: (params: MouseEventParams) -> Unit)

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

    /**
     * Returns API to manipulate a price scale on a specific pane.
     */
    fun priceScale(id: PriceScaleId, paneIndex: Int): PriceScaleApi

    @Deprecated(
        "Using ChartApi.priceScale() method without arguments " +
            "has been deprecated, pass valid price scale id instead"
    )
    fun priceScale(): PriceScaleApi

    /**
     * Applies new options to the chart
     * @param options any subset of chart options
     */
    fun applyOptions(options: ChartOptions)

    /**
     * Applies new options to the chart
     * @param options any subset of chart options
     */
    fun applyOptions(options: ChartOptions.() -> Unit) {
        applyOptions(ChartOptions().apply(options))
    }

    /**
     * Returns currently applied options
     * @param onOptionsReceived returns full set of currently applied options, including defaults
     */
    fun options(onOptionsReceived: (options: ChartOptions) -> Unit)

    /**
     * Taking screenshot of the chart and returns a bitmap with captured chart
     * @param mimeType type of a bitmap
     * @param onScreenshotReady returns a bitmap with captured chart
     */
    fun takeScreenshot(mimeType: ImageMimeType, onScreenshotReady: (Bitmap) -> Unit)

    fun takeScreenshot(options: ScreenshotOptions, onScreenshotReady: (Bitmap) -> Unit)

    fun setCrosshairPosition(price: Float, horizontalPosition: Time, seriesApi: SeriesApi)

    fun clearCrosshairPosition()

    fun createTextWatermark(
        options: TextWatermarkOptions,
        paneIndex: Int = 0,
        onWatermarkCreated: (WatermarkApi) -> Unit
    )

    fun createImageWatermark(
        imageUrl: String,
        options: ImageWatermarkOptions,
        paneIndex: Int = 0,
        onWatermarkCreated: (WatermarkApi) -> Unit
    )
}
