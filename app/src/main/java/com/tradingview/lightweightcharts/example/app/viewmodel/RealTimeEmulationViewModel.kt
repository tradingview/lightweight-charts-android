package com.tradingview.lightweightcharts.example.app.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tradingview.lightweightcharts.api.series.common.SeriesData
import com.tradingview.lightweightcharts.api.series.enums.SeriesType
import com.tradingview.lightweightcharts.example.app.model.Data
import com.tradingview.lightweightcharts.example.app.repository.DynamicRepository
import com.tradingview.lightweightcharts.example.app.repository.StaticRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch

class RealTimeEmulationViewModel : ViewModel() {

    private val staticRepository = StaticRepository()
    private val dynamicRepository = DynamicRepository()

    val seriesFlow: Flow<SeriesData>
        get() = dynamicRepository
            .getListSeriesData(emulationData ?: data.value!!) {
                loadData()
            }
            .onEach(::rememberEmittedBar)

    val seriesData: LiveData<Data>
        get() = data

    private val data: MutableLiveData<Data> by lazy {
        MutableLiveData<Data>().also {
            loadData()
        }
    }

    private var emulationData: Data? = null

    private fun loadData() {
        viewModelScope.launch {
            val barData = staticRepository.getRealTimeEmulationSeriesData()
            val loadedData = Data(barData, SeriesType.CANDLESTICK)
            emulationData = loadedData
            data.postValue(loadedData)
        }
    }

    private fun rememberEmittedBar(bar: SeriesData) {
        val currentData = emulationData ?: data.value ?: return
        val nextList = currentData.list.toMutableList()
        val lastBar = nextList.lastOrNull()
        if (lastBar?.time?.date?.time == bar.time.date.time && nextList.isNotEmpty()) {
            nextList[nextList.lastIndex] = bar
        } else {
            nextList.add(bar)
        }
        emulationData = currentData.copy(list = nextList)
    }
}
