package com.tradingview.lightweightcharts.api.options.common

import com.tradingview.lightweightcharts.api.options.models.PriceScaleMargins

interface SeriesSpecificOptions {
    val overlay: Boolean
    val scaleMargins: PriceScaleMargins?
}
