package com.tradingview.lightweightcharts.runtime.plugins

class PriceFormatter(template: String) : Plugin(
    name = "priceFormatter",
    file = "scripts/plugins/price-formatter/main.js",
    configurationParams = FormatterParams(template)
)

data class FormatterParams(
    val template: String
)