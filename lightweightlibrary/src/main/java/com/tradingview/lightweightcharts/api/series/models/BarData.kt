package com.tradingview.lightweightcharts.api.series.models

import com.tradingview.lightweightcharts.api.series.common.SeriesData

data class BarData(
    override val time: Time,
    val open: Float,
    val high: Float,
    val low: Float,
    val close: Float
): SeriesData