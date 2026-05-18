package com.tradingview.lightweightcharts.api.options.models

import com.tradingview.lightweightcharts.api.chart.models.color.IntColor
import com.tradingview.lightweightcharts.api.options.enums.HorizontalAlignment
import com.tradingview.lightweightcharts.api.options.enums.VerticalAlignment

data class TextWatermarkOptions(
    var visible: Boolean? = null,
    var horzAlign: HorizontalAlignment? = null,
    var vertAlign: VerticalAlignment? = null,
    var lines: List<TextWatermarkLineOptions>? = null,
)

data class TextWatermarkLineOptions(
    var text: String? = null,
    var color: IntColor? = null,
    var fontSize: Int? = null,
    var lineHeight: Int? = null,
    var fontStyle: String? = null,
    var fontFamily: String? = null,
)

inline fun textWatermarkOptions(init: TextWatermarkOptions.() -> Unit): TextWatermarkOptions {
    return TextWatermarkOptions().apply(init)
}

inline fun textWatermarkLineOptions(init: TextWatermarkLineOptions.() -> Unit): TextWatermarkLineOptions {
    return TextWatermarkLineOptions().apply(init)
}
