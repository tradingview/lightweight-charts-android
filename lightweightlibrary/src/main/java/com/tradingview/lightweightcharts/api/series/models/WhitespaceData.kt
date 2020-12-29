package com.tradingview.lightweightcharts.api.series.models

import com.tradingview.lightweightcharts.api.series.common.SeriesData

data class WhitespaceData(
    override val time: Time
): SeriesData
