package com.tradingview.lightweightcharts.api.options.common

import com.tradingview.lightweightcharts.api.series.models.color.Colorable

interface HistogramStyleOptions {
    val color: Colorable?
    val base: Float?
}