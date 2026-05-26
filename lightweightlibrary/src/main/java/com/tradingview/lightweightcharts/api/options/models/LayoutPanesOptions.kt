package com.tradingview.lightweightcharts.api.options.models

data class LayoutPanesOptions(
    var enableResize: Boolean? = null,
    var separatorColor: String? = null,
    var separatorHoverColor: String? = null,
)

inline fun layoutPanesOptions(init: LayoutPanesOptions.() -> Unit): LayoutPanesOptions {
    return LayoutPanesOptions().apply(init)
}
