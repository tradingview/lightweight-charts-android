package com.tradingview.lightweightcharts.api.options.common

import com.tradingview.lightweightcharts.api.series.models.color.IntColor

interface HistogramStyleOptions {
    val color: IntColor?
    val base: Float?
}