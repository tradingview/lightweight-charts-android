package com.tradingview.lightweightcharts.api.delegates

import com.tradingview.lightweightcharts.api.interfaces.SeriesMarkersApi
import com.tradingview.lightweightcharts.api.interfaces.SeriesMarkersApi.Func.SERIES_MARKERS_APPLY_OPTIONS
import com.tradingview.lightweightcharts.api.interfaces.SeriesMarkersApi.Func.SERIES_MARKERS_DETACH
import com.tradingview.lightweightcharts.api.interfaces.SeriesMarkersApi.Func.SERIES_MARKERS_GET
import com.tradingview.lightweightcharts.api.interfaces.SeriesMarkersApi.Func.SERIES_MARKERS_SET
import com.tradingview.lightweightcharts.api.interfaces.SeriesMarkersApi.Params.DATA
import com.tradingview.lightweightcharts.api.interfaces.SeriesMarkersApi.Params.MARKERS_ID
import com.tradingview.lightweightcharts.api.interfaces.SeriesMarkersApi.Params.OPTIONS
import com.tradingview.lightweightcharts.api.options.models.SeriesMarkersOptions
import com.tradingview.lightweightcharts.api.serializer.SeriesMarkersDeserializer
import com.tradingview.lightweightcharts.api.series.models.SeriesMarker
import com.tradingview.lightweightcharts.runtime.controller.WebMessageController
import com.tradingview.lightweightcharts.runtime.version.ChartRuntimeObject

class SeriesMarkersApiDelegate(
    override val uuid: String,
    private val controller: WebMessageController,
) : SeriesMarkersApi, ChartRuntimeObject {

    override fun getVersion(): Int = controller.hashCode()

    override fun setMarkers(data: List<SeriesMarker>) {
        controller.callFunction(SERIES_MARKERS_SET, mapOf(MARKERS_ID to uuid, DATA to data))
    }

    override fun markers(markersReceived: (List<SeriesMarker>) -> Unit) {
        controller.callFunction(
            SERIES_MARKERS_GET,
            mapOf(MARKERS_ID to uuid),
            callback = markersReceived,
            deserializer = SeriesMarkersDeserializer()
        )
    }

    override fun applyOptions(options: SeriesMarkersOptions) {
        controller.callFunction(SERIES_MARKERS_APPLY_OPTIONS, mapOf(MARKERS_ID to uuid, OPTIONS to options))
    }

    override fun detach() {
        controller.callFunction(SERIES_MARKERS_DETACH, mapOf(MARKERS_ID to uuid))
    }
}
