package com.tradingview.lightweightcharts.api.series.models

import com.tradingview.lightweightcharts.api.series.enums.SeriesMarkerPosition
import com.tradingview.lightweightcharts.api.series.enums.SeriesMarkerShape
import com.tradingview.lightweightcharts.api.chart.models.color.IntColor
import com.tradingview.lightweightcharts.api.series.enums.SeriesMarkerTextPosition

data class SeriesMarker(
    val time: Time? = null,
    val position: SeriesMarkerPosition? = null,
    val shape: SeriesMarkerShape? = null,
    /**
     * size multiplier of the marker, the shape is hidden when set to 0, default value is 1
     */
    val size: Int? = null,

    val color: IntColor? = null,

    val borderVisible: Boolean? = null,
    val borderColor: IntColor? = null,
    /**
     * the width of the border relative to the marker width
     */
    val borderWidth: Float? = null,

    val id: String? = null,
    val text: String? = null,
    val textColor: IntColor? = null,
    val textPosition: SeriesMarkerTextPosition? = null
)
