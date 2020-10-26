package com.tradingview.lightweightcharts.runtime.plugins

open class Plugin(
    val name: String,
    val file: String,
    val configurationParams: Any? = null
)