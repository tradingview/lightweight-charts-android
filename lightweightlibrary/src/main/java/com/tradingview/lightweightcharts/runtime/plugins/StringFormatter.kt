package com.tradingview.lightweightcharts.runtime.plugins

class StringFormatter(formatterParams: FormatterParams) : Plugin(
    "stringFormatter",
    "scripts/plugins/string-formatter/main.js",
    formatterParams
)

data class FormatterParams(
    val template: String
)