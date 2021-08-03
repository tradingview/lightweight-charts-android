package com.tradingview.lightweightcharts.example.app.repository

import com.tradingview.lightweightcharts.api.series.common.SeriesData
import com.tradingview.lightweightcharts.api.series.models.BarData
import com.tradingview.lightweightcharts.api.series.models.Time
import com.tradingview.lightweightcharts.example.app.model.Data
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import java.util.*
import kotlin.math.roundToInt

class DynamicRepository {

    @Suppress("LongMethod")
    fun getListSeriesData(data: Data, onEmulationComplete: () -> Unit): Flow<SeriesData> {
        var lastClose = (data.list.last() as BarData).close
        var lastHigh = (data.list.last() as BarData).high
        var lastLow = (data.list.last() as BarData).low
        var lastOpen = (data.list.last() as BarData).open
        var lastIndex = data.list.size - 2

        var targetIndex = lastIndex + 105 + (Math.random() + 30).roundToInt()
        var targetPrice = getRandomPrice()

        var currentIndex = lastIndex + 1
        var ticksInCurrentBar = 0;

        return flow {
            val date = Date()
            while (true) {
                var currentBarData: BarData

                delay(200)
                val deltaY = targetPrice - lastClose
                val deltaX = targetIndex - lastIndex
                val angle = deltaY / deltaX
                val basePrice = lastClose + (currentIndex - lastIndex) * angle
                val noise = (0.1f - Math.random().toFloat() * 0.2f) + 1.0f
                val noisedPrice = basePrice * noise

                if (ticksInCurrentBar == 0) {
                    currentBarData = BarData(
                            time = Time.Utc.fromDate(date),
                            open = noisedPrice,
                            high = noisedPrice,
                            low = noisedPrice,
                            close = noisedPrice,
                    )
                } else {
                    currentBarData = BarData(
                            time = Time.Utc.fromDate(date),
                            open = lastOpen,
                            high = lastHigh.coerceAtLeast(noisedPrice),
                            low = lastLow.coerceAtMost(noisedPrice),
                            close = noisedPrice,
                    )
                }

                emit(currentBarData)

                lastOpen = currentBarData.open
                lastHigh = currentBarData.high
                lastLow = currentBarData.low
                lastClose = currentBarData.close

                if (++ticksInCurrentBar == 5) {
                    date.time = date.time + 86000L * 1000L
                    currentIndex++;
                    ticksInCurrentBar = 0
                    if (currentIndex == 5000) {
                        onEmulationComplete.invoke()
                        return@flow
                    }

                    if (currentIndex == targetIndex) {
                        // change trend
                        lastClose = noisedPrice;
                        lastIndex = currentIndex;
                        targetIndex = (lastIndex + 5 + (Math.random() + 30).roundToInt())
                        targetPrice = getRandomPrice();
                    }
                }
            }
        }
    }

    private fun getRandomPrice(): Int {
        return 10 + (Math.random() * 10000).roundToInt() / 100
    }
}