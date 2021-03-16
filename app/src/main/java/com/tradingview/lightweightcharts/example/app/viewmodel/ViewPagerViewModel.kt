package com.tradingview.lightweightcharts.example.app.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tradingview.lightweightcharts.example.app.model.Data
import com.tradingview.lightweightcharts.example.app.model.SeriesDataType
import com.tradingview.lightweightcharts.example.app.repository.StaticRepository
import kotlinx.coroutines.launch

class ViewPagerViewModel : ViewModel() {
    private val staticRepository = StaticRepository()
    private var dataType: SeriesDataType = SeriesDataType.AREA

    private val data: MutableLiveData<Data> by lazy {
        MutableLiveData<Data>().also {
            loadData()
        }
    }

    val seriesData: LiveData<Data>
        get() = data

    private fun loadData() {
        viewModelScope.launch {
            when(dataType) {
                SeriesDataType.AREA -> {
                    val areaSeriesData = staticRepository.getListAreaSeriesData()
                    data.postValue(Data(areaSeriesData, SeriesDataType.AREA))
                }
            }
        }
    }
}