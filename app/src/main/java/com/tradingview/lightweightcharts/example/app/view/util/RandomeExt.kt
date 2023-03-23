package com.tradingview.lightweightcharts.example.app.view.util

import android.content.res.Resources
import androidx.annotation.ArrayRes
import com.tradingview.lightweightcharts.api.chart.models.color.IntColor
import com.tradingview.lightweightcharts.api.chart.models.color.toIntColor
import kotlin.random.Random

fun Resources.randomColor(@ArrayRes arrayRes: Int): IntColor {
    return getIntArray(arrayRes).random().toIntColor()
}


fun Random.nextFloat(st: Float, end: Float): Float {
    val r = nextFloat()
    return r * (end - st) + st
}