package com.tradingview.lightweightcharts.example.app.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.tradingview.lightweightcharts.api.series.common.SeriesData
import com.tradingview.lightweightcharts.example.app.model.Data
import com.tradingview.lightweightcharts.example.app.model.SeriesDataType
import com.tradingview.lightweightcharts.example.app.repository.DynamicRepository
import com.tradingview.lightweightcharts.example.app.repository.StaticRepository
import kotlinx.coroutines.flow.Flow

abstract class BaseViewModel : ViewModel() {
    private val dynamicRepository = DynamicRepository()
    private var dataType: SeriesDataType = SeriesDataType.AREA

    protected val data: MutableLiveData<Data> by lazy {
        MutableLiveData<Data>().also {
            loadData()
        }
    }

    val seriesData: LiveData<Data>
        get() = data

    abstract fun loadData()

}