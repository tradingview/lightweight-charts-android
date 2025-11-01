package com.tradingview.lightweightcharts.example.app.view.util

import android.content.res.Resources
import androidx.annotation.ArrayRes
import com.tradingview.lightweightcharts.api.chart.models.color.IntColor
import com.tradingview.lightweightcharts.api.chart.models.color.toIntColor
import com.tradingview.lightweightcharts.api.series.enums.LineStyle
import kotlin.random.Random

fun Resources.randomColor(@ArrayRes arrayRes: Int): IntColor {
    return getIntArray(arrayRes).random().toIntColor()
}


fun Resources.randomColorTransparent(@ArrayRes arrayRes: Int): IntColor {
    val alfaMask = (Random.nextInt(0x50) shl 24) or 0xffffff
    val color = getIntArray(arrayRes).random() and alfaMask
    return color.toIntColor()
}

fun Random.nextFloat(st: Float, end: Float): Float {
    val r = nextFloat()
    return r * (end - st) + st
}


fun LineStyle.Companion.random(): LineStyle =
    LineStyle.values().toList().random()
