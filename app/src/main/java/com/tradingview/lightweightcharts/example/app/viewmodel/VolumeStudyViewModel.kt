package com.tradingview.lightweightcharts.example.app.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tradingview.lightweightcharts.example.app.model.Data
import com.tradingview.lightweightcharts.example.app.model.SeriesDataType
import com.tradingview.lightweightcharts.example.app.repository.DynamicRepository
import com.tradingview.lightweightcharts.example.app.repository.StaticRepository
import kotlinx.coroutines.launch

class VolumeStudyViewModel: ViewModel() {
    private val staticRepository = StaticRepository()

    val areaSeriesData: LiveData<Data>
        get() = areaData

    val volumeSeriesData: LiveData<Data>
        get() = volumeData

    private val areaData: MutableLiveData<Data> by lazy {
        MutableLiveData<Data>().also {
            loadAreaData()
        }
    }

    private val volumeData: MutableLiveData<Data> by lazy {
        MutableLiveData<Data>().also {
            loadVolumeData()
        }
    }

    private fun loadAreaData() {
        viewModelScope.launch {
            val barData = staticRepository.getVolumeStudyAreaData()
            areaData.postValue(Data(barData, SeriesDataType.LINE))
        }
    }

    private fun loadVolumeData() {
        viewModelScope.launch {
            val barData = staticRepository.getVolumeStudySeriesData()
            volumeData.postValue(Data(barData, SeriesDataType.HISTOGRAM))
        }
    }

}