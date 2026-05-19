package com.tradingview.lightweightcharts.example.app.repository

import com.tradingview.lightweightcharts.api.series.common.SeriesData
import com.tradingview.lightweightcharts.api.series.models.CandlestickData
import com.tradingview.lightweightcharts.api.series.models.OhlcData
import com.tradingview.lightweightcharts.api.series.models.Time
import com.tradingview.lightweightcharts.example.app.model.Data
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale
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
        var ticksInCurrentBar = 0

        return flow {
            val date = Date(lastData.time.date.time + DAY_MS)
            val stringTimeLocale = (lastData.time as? Time.StringTime)?.locale ?: Locale.getDefault()
            val stringDateFormat = SimpleDateFormat(STRING_TIME_FORMAT, stringTimeLocale)
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
                        time = date.toSeriesTime(lastData.time, stringDateFormat),
                        open = noisedPrice,
                        high = noisedPrice,
                        low = noisedPrice,
                        close = noisedPrice,
                    )
                } else {
                    currentCandlestickData = CandlestickData(
                        time = date.toSeriesTime(lastData.time, stringDateFormat),
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
                    date.time += DAY_MS
                    currentIndex++
                    ticksInCurrentBar = 0
                    if (currentIndex == 5000) {
                        onEmulationComplete.invoke()
                        return@flow
                    }

                    if (currentIndex == targetIndex) {
                        // change trend
                        lastClose = noisedPrice
                        lastIndex = currentIndex
                        targetIndex = (lastIndex + 5 + (Math.random() + 30).roundToInt())
                        targetPrice = getRandomPrice()
                    }
                }
            }
        }
    }

    private fun Date.toSeriesTime(seedTime: Time, stringDateFormat: SimpleDateFormat): Time {
        return when (seedTime) {
            is Time.Utc -> Time.Utc.fromDate(this)
            is Time.StringTime -> Time.StringTime(stringDateFormat.format(this), seedTime.locale)
            is Time.BusinessDay -> Calendar.getInstance()
                .apply { time = this@toSeriesTime }
                .run {
                    Time.BusinessDay(
                        year = get(Calendar.YEAR),
                        month = get(Calendar.MONTH) + 1,
                        day = get(Calendar.DAY_OF_MONTH),
                    )
                }
        }
    }

    private fun getRandomPrice(): Int {
        return 10 + (Math.random() * 1000).roundToInt() / 100
    }

    private companion object {
        private const val DAY_MS = 86_400_000L
        private const val STRING_TIME_FORMAT = "yyyy-MM-dd"
    }
}
