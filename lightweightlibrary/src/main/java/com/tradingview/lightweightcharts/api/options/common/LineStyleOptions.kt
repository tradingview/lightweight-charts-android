package com.tradingview.lightweightcharts.api.options.common

import androidx.annotation.ColorInt
import com.google.gson.annotations.JsonAdapter
import com.tradingview.lightweightcharts.api.series.enums.LineStyle
import com.tradingview.lightweightcharts.api.series.enums.LineType
import com.tradingview.lightweightcharts.api.series.enums.LineWidth
import com.tradingview.lightweightcharts.api.series.models.ColorAdapter
import com.tradingview.lightweightcharts.api.series.models.IntColor

interface LineStyleOptions {
    val color: IntColor?
    val lineStyle: LineStyle?
    val lineWidth: LineWidth?
    val lineType: LineType?
    val crosshairMarkerVisible: Boolean?
    val crosshairMarkerRadius: Float?
    val crosshairMarkerBorderColor: IntColor?
    val crosshairMarkerBackgroundColor: IntColor?
}