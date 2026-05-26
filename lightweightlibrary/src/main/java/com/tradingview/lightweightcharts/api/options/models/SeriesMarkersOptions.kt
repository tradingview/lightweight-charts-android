package com.tradingview.lightweightcharts.api.options.models

import com.tradingview.lightweightcharts.api.series.enums.SeriesMarkerZOrder

data class SeriesMarkersOptions(
    val zOrder: SeriesMarkerZOrder? = null,
    val autoScale: Boolean? = null,
)

inline fun seriesMarkersOptions(init: SeriesMarkersOptions.() -> Unit): SeriesMarkersOptions {
    return SeriesMarkersOptions().apply(init)
}
