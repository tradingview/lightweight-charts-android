package com.tradingview.lightweightcharts.api.options.common

import com.tradingview.lightweightcharts.api.series.models.IntColor

interface BarStyleOptions {
    val upColor: IntColor?
    val downColor: IntColor?
    val openVisible: Boolean?
    val thinBars: Boolean?
}