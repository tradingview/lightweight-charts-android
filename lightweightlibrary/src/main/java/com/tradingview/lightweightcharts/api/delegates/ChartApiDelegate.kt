package com.tradingview.lightweightcharts.api.delegates

import android.graphics.Bitmap
import android.util.SizeF
import com.tradingview.lightweightcharts.api.chart.models.ImageMimeType
import com.tradingview.lightweightcharts.api.chart.models.ScreenshotOptions
import com.tradingview.lightweightcharts.api.interfaces.ChartApi
import com.tradingview.lightweightcharts.api.interfaces.ChartApi.Func.APPLY_OPTIONS
import com.tradingview.lightweightcharts.api.interfaces.ChartApi.Func.AUTO_SIZE_ACTIVE
import com.tradingview.lightweightcharts.api.interfaces.ChartApi.Func.CHART_OPTIONS
import com.tradingview.lightweightcharts.api.interfaces.ChartApi.Func.CLEAR_CROSSHAIR_POSITION
import com.tradingview.lightweightcharts.api.interfaces.ChartApi.Func.PANE_SIZE
import com.tradingview.lightweightcharts.api.interfaces.ChartApi.Func.PRICE_SCALE
import com.tradingview.lightweightcharts.api.interfaces.ChartApi.Func.REMOVE
import com.tradingview.lightweightcharts.api.interfaces.ChartApi.Func.REMOVE_SERIES
import com.tradingview.lightweightcharts.api.interfaces.ChartApi.Func.RESIZE
import com.tradingview.lightweightcharts.api.interfaces.ChartApi.Func.SET_CROSSHAIR_POSITION
import com.tradingview.lightweightcharts.api.interfaces.ChartApi.Func.SUBSCRIBE_CROSSHAIR_MOVE
import com.tradingview.lightweightcharts.api.interfaces.ChartApi.Func.SUBSCRIBE_DBL_CLICK
import com.tradingview.lightweightcharts.api.interfaces.ChartApi.Func.SUBSCRIBE_ON_CLICK
import com.tradingview.lightweightcharts.api.interfaces.ChartApi.Func.TAKE_SCREENSHOT
import com.tradingview.lightweightcharts.api.interfaces.ChartApi.Params.ADD_TOP_LAYER
import com.tradingview.lightweightcharts.api.interfaces.ChartApi.Params.FORCE_REPAINT
import com.tradingview.lightweightcharts.api.interfaces.ChartApi.Params.HEIGHT
import com.tradingview.lightweightcharts.api.interfaces.ChartApi.Params.HORIZONTAL_POSITION
import com.tradingview.lightweightcharts.api.interfaces.ChartApi.Params.INCLUDE_CROSSHAIR
import com.tradingview.lightweightcharts.api.interfaces.ChartApi.Params.MIME
import com.tradingview.lightweightcharts.api.interfaces.ChartApi.Params.OPTIONS
import com.tradingview.lightweightcharts.api.interfaces.ChartApi.Params.PRICE
import com.tradingview.lightweightcharts.api.interfaces.ChartApi.Params.WIDTH
import com.tradingview.lightweightcharts.api.interfaces.PaneApi
import com.tradingview.lightweightcharts.api.interfaces.PaneApi.Func.ADD_PANE
import com.tradingview.lightweightcharts.api.interfaces.PaneApi.Func.GET_PANES
import com.tradingview.lightweightcharts.api.interfaces.PaneApi.Func.REMOVE_PANE
import com.tradingview.lightweightcharts.api.interfaces.PaneApi.Func.SWAP_PANES
import com.tradingview.lightweightcharts.api.interfaces.PaneApi.Params.FIRST
import com.tradingview.lightweightcharts.api.interfaces.PaneApi.Params.PANE_INDEX
import com.tradingview.lightweightcharts.api.interfaces.PaneApi.Params.PRESERVE_EMPTY_PANE
import com.tradingview.lightweightcharts.api.interfaces.PaneApi.Params.SECOND
import com.tradingview.lightweightcharts.api.interfaces.PaneApi.Params.SERIES_TYPE
import com.tradingview.lightweightcharts.api.interfaces.PriceScaleApi
import com.tradingview.lightweightcharts.api.interfaces.PriceScaleApi.Params.PRICE_SCALE_ID
import com.tradingview.lightweightcharts.api.interfaces.SeriesApi
import com.tradingview.lightweightcharts.api.interfaces.SeriesApi.Func.ADD_AREA_SERIES
import com.tradingview.lightweightcharts.api.interfaces.SeriesApi.Func.ADD_BAR_SERIES
import com.tradingview.lightweightcharts.api.interfaces.SeriesApi.Func.ADD_BASELINE_SERIES
import com.tradingview.lightweightcharts.api.interfaces.SeriesApi.Func.ADD_CANDLESTICK_SERIES
import com.tradingview.lightweightcharts.api.interfaces.SeriesApi.Func.ADD_HISTOGRAM_SERIES
import com.tradingview.lightweightcharts.api.interfaces.SeriesApi.Func.ADD_LINE_SERIES
import com.tradingview.lightweightcharts.api.interfaces.SeriesApi.Params.SERIES_UUID
import com.tradingview.lightweightcharts.api.interfaces.TimeScaleApi
import com.tradingview.lightweightcharts.api.interfaces.WatermarkApi
import com.tradingview.lightweightcharts.api.interfaces.WatermarkApi.Func.CREATE_IMAGE_WATERMARK
import com.tradingview.lightweightcharts.api.interfaces.WatermarkApi.Func.CREATE_TEXT_WATERMARK
import com.tradingview.lightweightcharts.api.interfaces.WatermarkApi.Params.IMAGE_URL
import com.tradingview.lightweightcharts.api.options.common.BaselineStyleOptions
import com.tradingview.lightweightcharts.api.options.models.*
import com.tradingview.lightweightcharts.api.serializer.*
import com.tradingview.lightweightcharts.api.series.enums.SeriesType
import com.tradingview.lightweightcharts.api.series.models.*
import com.tradingview.lightweightcharts.runtime.controller.WebMessageController
import com.tradingview.lightweightcharts.runtime.version.ChartRuntimeObject

class ChartApiDelegate(
    private val controller: WebMessageController
) : ChartApi, ChartRuntimeObject {

    override fun getVersion(): Int {
        return controller.hashCode()
    }

    override val timeScale: TimeScaleApi = TimeScaleApiDelegate(controller)

    override fun resize(width: Int, height: Int, forceRepaint: Boolean) {
        controller.callFunction(
            RESIZE,
            mapOf(
                WIDTH to width,
                HEIGHT to height,
                FORCE_REPAINT to forceRepaint
            )
        )
    }

    override fun panes(onPanesReceived: (List<PaneApi>) -> Unit) {
        controller.callFunction(
            GET_PANES,
            callback = { paneIds ->
                onPanesReceived(paneIds.map { paneId -> PaneApiDelegate(paneId, controller) })
            },
            deserializer = object : Deserializer<List<String>>() {
                override fun deserialize(json: com.google.gson.JsonElement): List<String>? {
                    return json.asJsonArray.map { it.asString }
                }
            }
        )
    }

    override fun addPane(preserveEmptyPane: Boolean, onPaneCreated: (PaneApi) -> Unit) {
        controller.callFunction(
            ADD_PANE,
            mapOf(PRESERVE_EMPTY_PANE to preserveEmptyPane),
            { paneId -> onPaneCreated(PaneApiDelegate(paneId, controller)) },
            PrimitiveSerializer.StringDeserializer
        )
    }

    override fun removePane(index: Int) {
        controller.callFunction(
            REMOVE_PANE,
            mapOf(PANE_INDEX to index)
        )
    }

    override fun swapPanes(first: Int, second: Int) {
        controller.callFunction(
            SWAP_PANES,
            mapOf(
                FIRST to first,
                SECOND to second
            )
        )
    }

    override fun paneSize(paneIndex: Int, onPaneSizeReceived: (SizeF) -> Unit) {
        controller.callFunction(
            PANE_SIZE,
            mapOf(PANE_INDEX to paneIndex),
            callback = onPaneSizeReceived,
            deserializer = SizeDeserializer()
        )
    }

    override fun autoSizeActive(onAutoSizeActiveReceived: (Boolean) -> Unit) {
        controller.callFunction(
            AUTO_SIZE_ACTIVE,
            callback = onAutoSizeActiveReceived,
            deserializer = PrimitiveSerializer.BooleanDeserializer
        )
    }

    override fun subscribeCrosshairMove(onCrosshairMoved: (params: MouseEventParams) -> Unit) {
        controller.callSubscribe(
            SUBSCRIBE_CROSSHAIR_MOVE,
            callback = onCrosshairMoved,
            deserializer = MouseEventParamsDeserializer()
        )
    }

    override fun unsubscribeCrosshairMove(onCrosshairMoved: (params: MouseEventParams) -> Unit) {
        controller.callUnsubscribe(
            SUBSCRIBE_CROSSHAIR_MOVE,
            subscription = onCrosshairMoved
        )
    }

    override fun subscribeClick(onClicked: (params: MouseEventParams) -> Unit) {
        controller.callSubscribe(
            SUBSCRIBE_ON_CLICK,
            callback = onClicked,
            deserializer = MouseEventParamsDeserializer()
        )
    }

    override fun subscribeDblClick(onDoubleClicked: (params: MouseEventParams) -> Unit) {
        controller.callSubscribe(
            SUBSCRIBE_DBL_CLICK,
            callback = onDoubleClicked,
            deserializer = MouseEventParamsDeserializer()
        )
    }

    override fun unsubscribeDblClick(onDoubleClicked: (params: MouseEventParams) -> Unit) {
        controller.callUnsubscribe(
            SUBSCRIBE_DBL_CLICK,
            subscription = onDoubleClicked
        )
    }

    override fun unsubscribeClick(onClicked: (params: MouseEventParams) -> Unit) {
        controller.callUnsubscribe(
            SUBSCRIBE_ON_CLICK,
            subscription = onClicked
        )
    }

    override fun addAreaSeries(
        options: AreaSeriesOptions,
        onSeriesCreated: (api: SeriesApi) -> Unit
    ) {
        controller.callFunction(
            ADD_AREA_SERIES,
            mapOf(OPTIONS to options),
            { uuid ->
                onSeriesCreated(
                    SeriesApiDelegate(
                        uuid,
                        controller,
                        AreaSeriesOptionsDeserializer()
                    )
                )
            },
            PrimitiveSerializer.StringDeserializer
        )
    }

    override fun addBarSeries(
        options: BarSeriesOptions,
        onSeriesCreated: (api: SeriesApi) -> Unit
    ) {
        controller.callFunction(
            ADD_BAR_SERIES,
            mapOf(OPTIONS to options),
            { uuid ->
                onSeriesCreated(
                    SeriesApiDelegate(
                        uuid,
                        controller,
                        BarSeriesOptionsDeserializer()
                    )
                )
            },
            PrimitiveSerializer.StringDeserializer
        )
    }

    override fun addCandlestickSeries(
        options: CandlestickSeriesOptions,
        onSeriesCreated: (api: SeriesApi) -> Unit
    ) {
        controller.callFunction(
            ADD_CANDLESTICK_SERIES,
            mapOf(OPTIONS to options),
            { uuid ->
                onSeriesCreated(
                    SeriesApiDelegate(
                        uuid,
                        controller,
                        CandlestickSeriesOptionsDeserializer()
                    )
                )
            },
            PrimitiveSerializer.StringDeserializer
        )
    }

    override fun addHistogramSeries(
        options: HistogramSeriesOptions,
        onSeriesCreated: (api: SeriesApi) -> Unit
    ) {
        controller.callFunction(
            ADD_HISTOGRAM_SERIES,
            mapOf(OPTIONS to options),
            { uuid ->
                onSeriesCreated(
                    SeriesApiDelegate(
                        uuid,
                        controller,
                        HistogramSeriesOptionsDeserializer()
                    )
                )
            },
            PrimitiveSerializer.StringDeserializer
        )
    }

    override fun addLineSeries(
        options: LineSeriesOptions,
        onSeriesCreated: (api: SeriesApi) -> Unit
    ) {
        controller.callFunction(
            ADD_LINE_SERIES,
            mapOf(OPTIONS to options),
            { uuid ->
                onSeriesCreated(
                    SeriesApiDelegate(
                        uuid,
                        controller,
                        LineSeriesOptionsDeserializer()
                    )
                )
            },
            PrimitiveSerializer.StringDeserializer
        )
    }

    override fun addBaselineSeries(
        options: BaselineSeriesOptions,
        onSeriesCreated: (api: SeriesApi) -> Unit
    ) {
        addBaselineSeriesInternal(options, onSeriesCreated)
    }

    @Deprecated(
        message = "Use BaselineSeriesOptions to include v5 common series options.",
        level = DeprecationLevel.WARNING
    )
    override fun addBaselineSeries(
        options: BaselineStyleOptions,
        onSeriesCreated: (api: SeriesApi) -> Unit
    ) {
        addBaselineSeriesInternal(options, onSeriesCreated)
    }

    private fun addBaselineSeriesInternal(
        options: Any,
        onSeriesCreated: (api: SeriesApi) -> Unit
    ) {
        controller.callFunction(
            ADD_BASELINE_SERIES,
            mapOf(OPTIONS to options),
            { uuid ->
                onSeriesCreated(
                    SeriesApiDelegate(
                        uuid,
                        controller,
                        BaselineSeriesOptionsDeserializer()
                    )
                )
            },
            PrimitiveSerializer.StringDeserializer
        )
    }

    override fun addSeries(
        type: SeriesType,
        options: SeriesOptionsCommon?,
        paneIndex: Int?,
        onSeriesCreated: (api: SeriesApi) -> Unit
    ) {
        val params = mutableMapOf<String, Any>(SERIES_TYPE to type)
        options?.let { params[OPTIONS] = it }
        paneIndex?.let { params[PANE_INDEX] = it }
        controller.callFunction(
            "addSeries",
            params,
            { uuid ->
                onSeriesCreated(
                    SeriesApiDelegate(
                        uuid,
                        controller,
                        SeriesApiDelegate.optionsDeserializer(type)
                    )
                )
            },
            PrimitiveSerializer.StringDeserializer
        )
    }

    override fun remove() {
        controller.clearSubscriptions()
        controller.callFunction(REMOVE)
    }

    override fun removeSeries(seriesApi: SeriesApi, onSeriesDeleted: () -> Unit) {
        if (seriesApi is ChartRuntimeObject) {
            require(seriesApi.getVersion() == getVersion()) {
                "The object should be removed by the same ChartApi as it was created"
            }
        }
        if (seriesApi is SeriesApiDelegate<*>) {
            seriesApi.unsubscribeAllDataChanged()
        }

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

    override fun priceScale(id: PriceScaleId, paneIndex: Int): PriceScaleApi {
        val uuid = controller.callFunction(
            PRICE_SCALE,
            mapOf(
                PRICE_SCALE_ID to id.value,
                PANE_INDEX to paneIndex
            )
        )
        return PriceScaleApiDelegate(uuid, controller)
    }

    @Deprecated(
        "Using ChartApi.priceScale() method without arguments " +
            "has been deprecated, pass valid price scale id instead"
    )
    override fun priceScale(): PriceScaleApi {
        return priceScale(PriceScaleId.RIGHT)
    }

    override fun applyOptions(options: ChartOptions) {
        controller.callFunction(
            APPLY_OPTIONS,
            mapOf(OPTIONS to options)
        )
    }

    override fun options(onOptionsReceived: (options: ChartOptions) -> Unit) {
        controller.callFunction(
            CHART_OPTIONS,
            callback = onOptionsReceived,
            deserializer = ChartOptionsDeserializer()
        )
    }

    override fun takeScreenshot(mimeType: ImageMimeType, onScreenshotReady: (Bitmap) -> Unit) {
        controller.callFunction(
            TAKE_SCREENSHOT,
            mapOf(MIME to mimeType),
            callback = onScreenshotReady,
            deserializer = BitmapDeserializer()
        )
    }

    override fun takeScreenshot(options: ScreenshotOptions, onScreenshotReady: (Bitmap) -> Unit) {
        controller.callFunction(
            TAKE_SCREENSHOT,
            mapOf(
                MIME to options.mimeType,
                ADD_TOP_LAYER to options.addTopLayer,
                INCLUDE_CROSSHAIR to options.includeCrosshair
            ),
            callback = onScreenshotReady,
            deserializer = BitmapDeserializer()
        )
    }

    override fun setCrosshairPosition(price: Float, horizontalPosition: Time, seriesApi: SeriesApi) {
        if (seriesApi is ChartRuntimeObject) {
            require(seriesApi.getVersion() == getVersion()) {
                "The series should belong to the same ChartApi"
            }
        }

        controller.callFunction(
            SET_CROSSHAIR_POSITION,
            mapOf(
                PRICE to price,
                HORIZONTAL_POSITION to horizontalPosition,
                SERIES_UUID to seriesApi.uuid
            )
        )
    }

    override fun clearCrosshairPosition() {
        controller.callFunction(CLEAR_CROSSHAIR_POSITION)
    }

    override fun createTextWatermark(
        options: TextWatermarkOptions,
        paneIndex: Int,
        onWatermarkCreated: (WatermarkApi) -> Unit
    ) {
        controller.callFunction(
            CREATE_TEXT_WATERMARK,
            mapOf(
                PANE_INDEX to paneIndex,
                OPTIONS to options
            ),
            { watermarkId ->
                onWatermarkCreated(WatermarkApiDelegate(watermarkId, controller, WatermarkApiDelegate.Kind.TEXT))
            },
            PrimitiveSerializer.StringDeserializer
        )
    }

    override fun createImageWatermark(
        imageUrl: String,
        options: ImageWatermarkOptions,
        paneIndex: Int,
        onWatermarkCreated: (WatermarkApi) -> Unit
    ) {
        controller.callFunction(
            CREATE_IMAGE_WATERMARK,
            mapOf(
                PANE_INDEX to paneIndex,
                IMAGE_URL to imageUrl,
                OPTIONS to options
            ),
            { watermarkId ->
                onWatermarkCreated(WatermarkApiDelegate(watermarkId, controller, WatermarkApiDelegate.Kind.IMAGE))
            },
            PrimitiveSerializer.StringDeserializer
        )
    }
}
