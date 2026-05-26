package com.tradingview.lightweightcharts.api.options.models

import com.tradingview.lightweightcharts.api.chart.models.color.IntColor
import com.tradingview.lightweightcharts.api.chart.models.color.surface.SurfaceColor

data class LayoutOptions(

    /**
     * Chart and scales background color.
     */
    var background: SurfaceColor? = null,

    /**
     * Color of a text on the scales
     */
    var textColor: IntColor? = null,

    /**
     * Font size of a text on the scales in pixels
     */
    var fontSize: Int? = null,

    /**
     * Font family of a text on the scales
     */
    var fontFamily: String? = null,

    /**
     * Whether to show the TradingView attribution logo.
     */
    var attributionLogo: Boolean? = null,

    /**
     * Options for chart layout panes.
     */
    var panes: LayoutPanesOptions? = null,
)

inline fun layoutOptions(init: LayoutOptions.() -> Unit): LayoutOptions {
    return LayoutOptions().apply(init)
}
