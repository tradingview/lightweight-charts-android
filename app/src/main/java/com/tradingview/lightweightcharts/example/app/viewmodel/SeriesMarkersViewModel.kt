package com.tradingview.lightweightcharts.example.app.viewmodel

import android.graphics.Color
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tradingview.lightweightcharts.api.series.enums.SeriesMarkerPosition
import com.tradingview.lightweightcharts.api.series.enums.SeriesMarkerShape
import com.tradingview.lightweightcharts.api.series.models.BarData
import com.tradingview.lightweightcharts.api.series.models.SeriesMarker
import com.tradingview.lightweightcharts.example.app.model.Data
import com.tradingview.lightweightcharts.example.app.model.SeriesDataType
import com.tradingview.lightweightcharts.example.app.repository.StaticRepository
import kotlinx.coroutines.launch
import kotlin.math.floor

class SeriesMarkersViewModel: ViewModel() {
    private val staticRepository = StaticRepository()

    val seriesData: LiveData<Data>
        get() = data

    private val data: MutableLiveData<Data> by lazy {
        MutableLiveData<Data>().also {
            loadData()
        }
    }

    private fun loadData() {
        viewModelScope.launch {
            val barData = staticRepository.getSeriesMarkersSeriesData()
            data.postValue(Data(barData, SeriesDataType.CANDLESTICK))
        }
    }

    fun createMarkers(): List<SeriesMarker> {
        val seriesDataList = data.value?.list
        val datesForMarkers = seriesDataList?.subList(seriesDataList.size - 39, seriesDataList.size - 18)
        var indexOfMinPrice = 0
        var indexOfMaxPrice = 0

        for (i in datesForMarkers!!.indices) {
            if ((datesForMarkers[i] as BarData).low < (datesForMarkers[indexOfMinPrice] as BarData).low) {
                indexOfMinPrice = i
            }

            if ((datesForMarkers[i] as BarData).high > (datesForMarkers[indexOfMaxPrice] as BarData).high) {
                indexOfMaxPrice = i
            }
        }

        val markers = mutableListOf<SeriesMarker>()

        for (i in datesForMarkers.indices) {
            val markerBar = when (i) {
                indexOfMaxPrice -> SeriesMarker(
                            time = datesForMarkers[i].time,
                            position = SeriesMarkerPosition.ABOVE_BAR,
                            color = Color.parseColor("#e91e63"),
                            shape = SeriesMarkerShape.ARROW_DOWN,
                            text = "Sell @ ${floor((datesForMarkers[i] as BarData).high + 2)}"
                )

                indexOfMinPrice -> SeriesMarker(
                        time = datesForMarkers[i].time,
                        position = SeriesMarkerPosition.BELOW_BAR,
                        color = Color.parseColor("#2196F3"),
                        shape = SeriesMarkerShape.ARROW_UP,
                        text = "Buy @ ${floor((datesForMarkers[i] as BarData).low - 2)}"
                )

                else -> continue
            }

            markers.add(markerBar)
        }

        val circleMarker = SeriesMarker(
                time = seriesDataList.get(seriesDataList.size - 48)?.time,
                position = SeriesMarkerPosition.ABOVE_BAR,
                color = Color.parseColor("#f68410"),
                shape = SeriesMarkerShape.CIRCLE,
                text = "D"
        )

        markers.add(circleMarker)

        return markers
    }
}