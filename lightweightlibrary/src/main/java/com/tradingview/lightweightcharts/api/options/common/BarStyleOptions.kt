package com.tradingview.lightweightcharts.api.options.common

import com.tradingview.lightweightcharts.api.series.models.ColorWrapper

interface BarStyleOptions {
    val upColor: ColorWrapper?
    val downColor: ColorWrapper?
    val openVisible: Boolean?
    val thinBars: Boolean?
}