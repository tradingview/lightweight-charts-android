package com.tradingview.lightweightcharts.api.series.models

import androidx.annotation.ColorInt
import com.google.gson.annotations.JsonAdapter
import com.tradingview.lightweightcharts.api.series.common.SeriesData

data class HistogramData(
    override val time: Time,
    val value: Float,
    /**
     * Optional color value for certain data item. If missed, color from HistogramSeriesOptions is used
     */
    @ColorInt
    @JsonAdapter(ColorAdapter::class)
    val color: IntColor? = null
): SeriesData