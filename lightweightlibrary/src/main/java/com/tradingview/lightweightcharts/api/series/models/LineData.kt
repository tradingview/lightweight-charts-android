package com.tradingview.lightweightcharts.api.series.models

data class LineData(
    override val time: Time,
    override val value: Float,
): SingleValueData
