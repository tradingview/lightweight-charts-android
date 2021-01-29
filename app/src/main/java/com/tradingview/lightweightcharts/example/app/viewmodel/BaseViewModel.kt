package com.tradingview.lightweightcharts.example.app.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tradingview.lightweightcharts.api.series.common.SeriesData
import com.tradingview.lightweightcharts.example.app.model.Data
import com.tradingview.lightweightcharts.example.app.model.SeriesDataType
import com.tradingview.lightweightcharts.example.app.repository.DynamicRepository
import com.tradingview.lightweightcharts.example.app.repository.StaticRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch

abstract class BaseViewModel : ViewModel() {
    protected val staticRepository = StaticRepository()
    private val dynamicRepository = DynamicRepository()
    private var dataType: SeriesDataType = SeriesDataType.AREA

    protected val data: MutableLiveData<Data> by lazy {
        MutableLiveData<Data>().also {
            loadData()
        }
    }

    val seriesData: LiveData<Data>
        get() = data

    val seriesFlow: Flow<SeriesData>
        get() = dynamicRepository.getListSeriesData()

    abstract fun loadData()

    fun selectSeries(seriesDataType: SeriesDataType) {
        dataType = seriesDataType
        loadData()
    }
}