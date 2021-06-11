package com.tradingview.lightweightcharts.api.series.models

import androidx.annotation.ColorInt
import com.google.gson.annotations.JsonAdapter
import com.tradingview.lightweightcharts.api.series.common.SeriesData
import com.tradingview.lightweightcharts.api.series.models.ColorWrapper.*

data class HistogramData(
    override val time: Time,
    val value: Float,
    /**
     * Optional color value for certain data item. If missed, color from HistogramSeriesOptions is used
     */
    val color: ColorWrapper? = null
): SeriesData