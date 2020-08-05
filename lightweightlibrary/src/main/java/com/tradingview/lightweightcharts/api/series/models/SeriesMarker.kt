package com.tradingview.lightweightcharts.api.series.models

import com.tradingview.lightweightcharts.api.series.enums.SeriesMarkerPosition
import com.tradingview.lightweightcharts.api.series.enums.SeriesMarkerShape

data class SeriesMarker(
    val time: Time? = null,
    val position: SeriesMarkerPosition? = null,
    val shape: SeriesMarkerShape? = null,
    val color: String? = null,
    val id: String? = null
)
