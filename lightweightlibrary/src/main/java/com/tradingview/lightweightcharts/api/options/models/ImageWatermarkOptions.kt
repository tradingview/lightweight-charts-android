package com.tradingview.lightweightcharts.api.options.models

data class ImageWatermarkOptions(
    var alpha: Float? = null,
    var maxWidth: Int? = null,
    var maxHeight: Int? = null,
    var padding: Int? = null,
)

inline fun imageWatermarkOptions(init: ImageWatermarkOptions.() -> Unit): ImageWatermarkOptions {
    return ImageWatermarkOptions().apply(init)
}
