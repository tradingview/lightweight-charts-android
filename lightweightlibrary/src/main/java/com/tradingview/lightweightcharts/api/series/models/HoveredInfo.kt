package com.tradingview.lightweightcharts.api.series.models

data class HoveredInfo(
    val type: String? = null,
    val sourceKind: String? = null,
    val objectKind: String? = null,
    val series: String? = null,
    val objectId: String? = null,
    val paneIndex: Int? = null,
)
