package com.tradingview.lightweightcharts.example.app.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tradingview.lightweightcharts.api.series.enums.SeriesType
import com.tradingview.lightweightcharts.example.app.model.Data
import com.tradingview.lightweightcharts.example.app.repository.StaticRepository
import kotlinx.coroutines.launch

class BarChartViewModel : ViewModel() {

    private val staticRepository = StaticRepository()

    val seriesBarData: LiveData<Data> get() = barData
    private val barData = MutableLiveData<Data>()

    val seriesAreaData: LiveData<Data> get() = areaData
    private val areaData = MutableLiveData<Data>()

    fun init() {
        loadData()
    }

    private fun loadData() {
        viewModelScope.launch {
            val barData = staticRepository.getBarChartSeriesData()
            this@BarChartViewModel.barData.postValue(Data(barData, SeriesType.BAR))


            val areaData = staticRepository.getVolumeStudyAreaData()
            this@BarChartViewModel.areaData.postValue(Data(areaData, SeriesType.AREA))
        }
    }
}