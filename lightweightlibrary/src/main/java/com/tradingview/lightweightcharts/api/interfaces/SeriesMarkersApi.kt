package com.tradingview.lightweightcharts.api.interfaces

import com.tradingview.lightweightcharts.api.options.models.SeriesMarkersOptions
import com.tradingview.lightweightcharts.api.series.models.SeriesMarker

interface SeriesMarkersApi {
    object Func {
        const val CREATE_SERIES_MARKERS = "createSeriesMarkers"
        const val SERIES_MARKERS_SET = "seriesMarkersSet"
        const val SERIES_MARKERS_GET = "seriesMarkersGet"
        const val SERIES_MARKERS_APPLY_OPTIONS = "seriesMarkersApplyOptions"
        const val SERIES_MARKERS_DETACH = "seriesMarkersDetach"
    }

    object Params {
        const val MARKERS_ID = "markersId"
        const val SERIES_UUID = "seriesId"
        const val DATA = "data"
        const val OPTIONS = "options"
    }

    val uuid: String

    fun setMarkers(data: List<SeriesMarker>)

    fun markers(markersReceived: (List<SeriesMarker>) -> Unit)

    fun applyOptions(options: SeriesMarkersOptions)

    fun detach()
}
