package com.tradingview.lightweightcharts.runtime.plugins

class PriceFormatter(formatterParams: FormatterParams) : Plugin(
    name = "priceFormatter",
    file = "scripts/plugins/price-formatter/main.js",
    configurationParams = formatterParams
)

data class FormatterParams(
    val template: String
)