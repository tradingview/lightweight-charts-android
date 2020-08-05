package com.tradingview.lightweightcharts.api.series.models

import com.tradingview.lightweightcharts.api.series.common.SeriesData

data class LineData(
    override val time: Time,
    /**
     * Price value of data item
     */
    val value: Float
): SeriesData
