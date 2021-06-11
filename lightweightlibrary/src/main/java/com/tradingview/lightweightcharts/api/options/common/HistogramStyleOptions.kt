package com.tradingview.lightweightcharts.api.options.common

import com.tradingview.lightweightcharts.api.series.models.ColorWrapper
import com.tradingview.lightweightcharts.api.series.models.ColorWrapper.IntColor

interface HistogramStyleOptions {
    val color: ColorWrapper?
    val base: Float?
}