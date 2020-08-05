package com.tradingview.lightweightcharts.api.options.models

data class LayoutOptions(
    /** Background color of the chart area and the scales */
    val backgroundColor: String? = null,
    /** Color of a text on the scales */
    val textColor: String? = null,
    /** Font size of a text on the scales in pixels  */
    val fontSize: Int? = null,
    /** Font family of a text on the scales */
    val fontFamily: String? = null
)