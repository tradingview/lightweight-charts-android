package com.tradingview.lightweightcharts.example.app.repository

import com.tradingview.lightweightcharts.api.series.common.SeriesData
import com.tradingview.lightweightcharts.api.series.models.CandlestickData
import com.tradingview.lightweightcharts.api.series.models.OhlcData
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
        val lastData = data.list.last() as OhlcData
        var lastClose = lastData.close
        var lastHigh = lastData.high
        var lastLow = lastData.low
        var lastOpen = lastData.open
        var lastIndex = data.list.size - 2

        var targetIndex = lastIndex + 105 + (Math.random() + 30).roundToInt()
        var targetPrice = getRandomPrice()

        var currentIndex = lastIndex + 1
        var ticksInCurrentBar = 0;

        return flow {
            val date = Date()
            while (true) {
                var currentCandlestickData: OhlcData

                delay(200)
                val deltaY = targetPrice - lastClose
                val deltaX = targetIndex - lastIndex
                val angle = deltaY / deltaX
                val basePrice = lastClose + (currentIndex - lastIndex) * angle
                val noise = (0.1f - Math.random().toFloat() * 0.2f) + 1.0f
                val noisedPrice = basePrice * noise

                if (ticksInCurrentBar == 0) {
                    currentCandlestickData = CandlestickData(
                            time = Time.Utc.fromDate(date),
                            open = noisedPrice,
                            high = noisedPrice,
                            low = noisedPrice,
                            close = noisedPrice,
                    )
                } else {
                    currentCandlestickData = CandlestickData(
                            time = Time.Utc.fromDate(date),
                            open = lastOpen,
                            high = lastHigh.coerceAtLeast(noisedPrice),
                            low = lastLow.coerceAtMost(noisedPrice),
                            close = noisedPrice,
                    )
                }

                emit(currentCandlestickData)

                lastOpen = currentCandlestickData.open
                lastHigh = currentCandlestickData.high
                lastLow = currentCandlestickData.low
                lastClose = currentCandlestickData.close

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
        return 10 + (Math.random() * 1000).roundToInt() / 100
    }
}