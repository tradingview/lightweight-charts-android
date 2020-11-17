package com.tradingview.lightweightcharts.api.series.models

open class PriceScaleId(val value: String) {
    class Left : PriceScaleId("left")
    class Right : PriceScaleId("left")
    class Custom(id: String) : PriceScaleId(id)
}