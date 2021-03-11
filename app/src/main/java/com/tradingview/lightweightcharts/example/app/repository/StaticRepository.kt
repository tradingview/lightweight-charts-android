package com.tradingview.lightweightcharts.example.app.repository

import com.tradingview.lightweightcharts.api.options.models.PriceLineOptions
import com.tradingview.lightweightcharts.api.series.common.SeriesData
import com.tradingview.lightweightcharts.example.app.data.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class StaticRepository {
    suspend fun getBarChartSeriesData(): MutableList<SeriesData> {
        return withContext(Dispatchers.IO) {
            return@withContext listBarChartSeriesData()
        }
    }

    suspend fun getCustomPriceFormatterSeriesData(): MutableList<SeriesData> {
        return withContext(Dispatchers.IO) {
            return@withContext listCustomPriceFormatterSeriesData()
        }
    }

    suspend fun getCustomThemesSeriesData(): MutableList<SeriesData> {
        return withContext(Dispatchers.IO) {
            return@withContext listCustomThemesSeriesData()
        }
    }

    suspend fun getFloatingTooltipSeriesData(): MutableList<SeriesData> {
        return withContext(Dispatchers.IO) {
            return@withContext listFloatingTooltipSeriesData()
        }
    }

    suspend fun getPriceLinesWithTitlesSeriesData(): MutableList<SeriesData> {
        return withContext(Dispatchers.IO) {
            return@withContext listPriceLinesWithTitlesSeriesData()
        }
    }

    suspend fun getRealTimeEmulationSeriesData(): MutableList<SeriesData> {
        return withContext(Dispatchers.IO) {
            return@withContext listRealTimeEmulationSeriesData()
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
}