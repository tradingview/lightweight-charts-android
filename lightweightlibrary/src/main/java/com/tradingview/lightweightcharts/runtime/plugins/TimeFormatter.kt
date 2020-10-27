package com.tradingview.lightweightcharts.runtime.plugins

class TimeFormatter(locale: String) : Plugin(
    name = "timeFormatter",
    file = "scripts/plugins/time-formatter/main.js",
    configurationParams = TimeFormatterParams(locale)
)

data class TimeFormatterParams(
    val locale: String
)