package com.tradingview.lightweightcharts.example.app.viewmodel

import android.graphics.Color
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tradingview.lightweightcharts.api.series.enums.SeriesMarkerPosition
import com.tradingview.lightweightcharts.api.series.enums.SeriesMarkerShape
import com.tradingview.lightweightcharts.api.series.enums.SeriesType
import com.tradingview.lightweightcharts.api.series.models.BarData
import com.tradingview.lightweightcharts.api.series.models.SeriesMarker
import com.tradingview.lightweightcharts.api.chart.models.color.toIntColor
import com.tradingview.lightweightcharts.example.app.model.Data
import com.tradingview.lightweightcharts.example.app.repository.StaticRepository
import kotlinx.coroutines.launch
import kotlin.math.floor

class SeriesMarkersViewModel: ViewModel() {
    private val staticRepository = StaticRepository()

    val seriesData: LiveData<Data>
        get() = data

    val markers: List<SeriesMarker>
        get() = createMarkers()

    private val data: MutableLiveData<Data> by lazy {
        MutableLiveData<Data>().also {
            loadData()
        }
    }

    private fun loadData() {
        viewModelScope.launch {
            val barData = staticRepository.getSeriesMarkersSeriesData()
            data.postValue(Data(barData, SeriesType.CANDLESTICK))
        }
    }

    private fun createMarkers(): List<SeriesMarker> {
        val seriesDataList = data.value?.list
            ?: return emptyList()

        val datesForMarkers = seriesDataList.subList(
            seriesDataList.size - 39,
            seriesDataList.size - 18
        )

        val indexOfMinPrice = datesForMarkers
            .indexOfFirst { data ->
                (data as BarData).low == datesForMarkers.minOf { (it as BarData).low }
            }

        val indexOfMaxPrice = datesForMarkers
            .indexOfLast { data ->
                (data as BarData).high == datesForMarkers.maxOf { (it as BarData).high }
            }

        return listOf(
            SeriesMarker(
                time = seriesDataList[seriesDataList.size - 48].time,
                position = SeriesMarkerPosition.ABOVE_BAR,
                color = Color.parseColor("#f68410").toIntColor(),
                shape = SeriesMarkerShape.CIRCLE,
                text = "D"
            ),
            SeriesMarker(
                time = datesForMarkers[indexOfMinPrice].time,
                position = SeriesMarkerPosition.BELOW_BAR,
                color = Color.parseColor("#2196F3").toIntColor(),
                shape = SeriesMarkerShape.ARROW_UP,
                text = "Buy @ ${floor((datesForMarkers[indexOfMinPrice] as BarData).low - 2)}"
            ),
            SeriesMarker(
                time = datesForMarkers[indexOfMaxPrice].time,
                position = SeriesMarkerPosition.ABOVE_BAR,
                color = Color.parseColor("#e91e63").toIntColor(),
                shape = SeriesMarkerShape.ARROW_DOWN,
                text = "Sell @ ${floor((datesForMarkers[indexOfMaxPrice] as BarData).high + 2)}"
            )
        )
    }
}