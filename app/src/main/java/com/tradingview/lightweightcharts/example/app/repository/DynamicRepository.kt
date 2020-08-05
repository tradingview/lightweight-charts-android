package com.tradingview.lightweightcharts.example.app.repository

import com.tradingview.lightweightcharts.api.series.common.SeriesData
import com.tradingview.lightweightcharts.api.series.models.LineData
import com.tradingview.lightweightcharts.api.series.models.Time
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import java.util.*

class DynamicRepository {

    fun getListSeriesData(): Flow<SeriesData> {
        return flow {
            val date = Date()
            while (true) {
                delay(1000)
                date.time = date.time + 86000L * 1000L
                val value = 40.00f + Random().nextInt(5) - 5
                emit(LineData(Time.Utc.fromDate(date), value))
            }
        }
    }
}