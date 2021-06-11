package com.tradingview.lightweightcharts.api.options.common

import com.tradingview.lightweightcharts.api.series.models.ColorWrapper

interface HistogramStyleOptions {
    val color: ColorWrapper?
    val base: Float?
}