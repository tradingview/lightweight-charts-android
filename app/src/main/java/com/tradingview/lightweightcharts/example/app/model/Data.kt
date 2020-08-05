package com.tradingview.lightweightcharts.example.app.model

import com.tradingview.lightweightcharts.api.series.common.SeriesData

data class Data(
    val list: List<SeriesData>,
    val type: SeriesDataType
)
enum class SeriesDataType {
    BAR, AREA, LINE, CANDLESTICK, HISTOGRAM
}