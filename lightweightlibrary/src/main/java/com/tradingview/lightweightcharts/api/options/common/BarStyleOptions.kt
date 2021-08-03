package com.tradingview.lightweightcharts.api.options.common

import com.tradingview.lightweightcharts.api.series.models.color.Colorable

interface BarStyleOptions {
    val upColor: Colorable?
    val downColor: Colorable?
    val openVisible: Boolean?
    val thinBars: Boolean?
}