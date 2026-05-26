package com.tradingview.lightweightcharts.api.chart.models

data class ScreenshotOptions(
    val mimeType: ImageMimeType,
    val addTopLayer: Boolean = false,
    val includeCrosshair: Boolean = false,
)
