package com.tradingview.lightweightcharts.example.app.repository

import com.tradingview.lightweightcharts.api.options.models.PriceLineOptions
import com.tradingview.lightweightcharts.api.series.common.SeriesData
import com.tradingview.lightweightcharts.api.series.models.SeriesMarker
import com.tradingview.lightweightcharts.example.app.data.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class StaticRepository {
    suspend fun getListSeriesMarker(): MutableList<SeriesMarker> {
        return withContext(Dispatchers.IO) {
            return@withContext listSeriesMarker()
        }
    }

    suspend fun getListBarSeriesData(): MutableList<SeriesData> {
        return withContext(Dispatchers.IO) {
            return@withContext listBarSeriesData()
        }
    }

    suspend fun getListLineSeriesData(): MutableList<SeriesData> {
        return withContext(Dispatchers.IO) {
            return@withContext listLineSeriesData()
        }
    }

    suspend fun getListAreaSeriesData(): MutableList<SeriesData> {
        return withContext(Dispatchers.IO) {
            return@withContext listAreaSeriesData()
        }
    }

    suspend fun getListHistogramSeriesData(): MutableList<SeriesData> {
        return withContext(Dispatchers.IO) {
            return@withContext listHistogramSeriesData()
        }
    }

    suspend fun getListCandlestickSeriesData(): MutableList<SeriesData> {
        return withContext(Dispatchers.IO) {
            return@withContext listCandlestickSeriesData()
        }
    }

    suspend fun getPriceLineOptions(): PriceLineOptions {
        return withContext(Dispatchers.IO) {
            return@withContext priceLineOptions()
        }
    }
}