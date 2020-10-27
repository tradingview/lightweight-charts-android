package com.tradingview.lightweightcharts.runtime.plugins

class StringFormatter(formatterParams: FormatterParams) : Plugin(
    name = "stringFormatter",
    file = "scripts/plugins/string-formatter/main.js",
    configurationParams = formatterParams
)

data class FormatterParams(
    val template: String
)