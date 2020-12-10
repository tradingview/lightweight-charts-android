package com.tradingview.lightweightcharts.api.series.models

import com.tradingview.lightweightcharts.api.series.enums.SeriesMarkerPosition
import com.tradingview.lightweightcharts.api.series.enums.SeriesMarkerShape

data class SeriesMarker(
    val time: Time? = null,
    val position: SeriesMarkerPosition? = null,
    val shape: SeriesMarkerShape? = null,
    /**
     * size multiplier of the marker, the shape is hidden when set to 0, default value is 1
     */
    val size: Int? = null,
    val color: String? = null,
    val id: String? = null,
    val text: String? = null
)
