package com.tradingview.lightweightcharts.example.app.repository

import com.tradingview.lightweightcharts.api.options.models.PriceLineOptions
import com.tradingview.lightweightcharts.api.series.common.SeriesData
import com.tradingview.lightweightcharts.example.app.data.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class StaticRepository {
    suspend fun getBarChartSeriesData(): MutableList<SeriesData> {
        return withContext(Dispatchers.IO) {
            return@withContext listBarChartSeriesBarData()
        }
    }

    suspend fun getCustomPriceFormatterSeriesData(): MutableList<SeriesData> {
        return withContext(Dispatchers.IO) {
            return@withContext listCustomPriceFormatterSeriesLineData()
        }
    }

    suspend fun getCustomThemesSeriesData(): MutableList<SeriesData> {
        return withContext(Dispatchers.IO) {
            return@withContext listCustomThemesSeriesLineData()
        }
    }

    suspend fun getFloatingTooltipSeriesData(): MutableList<SeriesData> {
        return withContext(Dispatchers.IO) {
            return@withContext listFloatingTooltipSeriesLineData()
        }
    }

    suspend fun getPriceLinesWithTitlesSeriesData(): MutableList<SeriesData> {
        return withContext(Dispatchers.IO) {
            return@withContext listPriceLinesWithTitlesSeriesLineData()
        }
    }

    suspend fun getRealTimeEmulationSeriesData(): MutableList<SeriesData> {
        return withContext(Dispatchers.IO) {
            return@withContext listRealTimeEmulationSeriesCandlestickData()
        }
    }

    suspend fun getVolumeStudyAreaData(): MutableList<SeriesData> {
        return withContext(Dispatchers.IO) {
            return@withContext listVolumeStudyAreaData()
        }
    }

    suspend fun getVolumeStudySeriesData(): MutableList<SeriesData> {
        return withContext(Dispatchers.IO) {
            return@withContext listVolumeStudySeriesData()
        }
    }

    suspend fun getSeriesMarkersSeriesData(): MutableList<SeriesData> {
        return withContext(Dispatchers.IO) {
            return@withContext listSeriesMarkersSeriesData()
        }
    }

    suspend fun getPriceLineOptions(): PriceLineOptions {
        return withContext(Dispatchers.IO) {
            return@withContext priceLineOptions()
        }
    }

    suspend fun getListAreaSeriesData(): MutableList<SeriesData> {
        return withContext(Dispatchers.IO) {
            return@withContext listAreaSeriesData()
        }
    }
}