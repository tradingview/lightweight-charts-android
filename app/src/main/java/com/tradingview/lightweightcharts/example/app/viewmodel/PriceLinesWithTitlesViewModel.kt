package com.tradingview.lightweightcharts.example.app.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tradingview.lightweightcharts.api.series.enums.SeriesType
import com.tradingview.lightweightcharts.api.series.models.LineData
import com.tradingview.lightweightcharts.example.app.model.Data
import com.tradingview.lightweightcharts.example.app.repository.StaticRepository
import kotlinx.coroutines.launch

class PriceLinesWithTitlesViewModel: ViewModel() {

    private val staticRepository = StaticRepository()

    var minimumPrice: Float = 0f
    var maximumPrice: Float = 0f
    var avgPrice: Float = 0f

    val seriesData: LiveData<Data>
        get() = data

    private val data: MutableLiveData<Data> by lazy {
        MutableLiveData<Data>().also {
            loadData()
        }
    }

    private fun loadData() {
        viewModelScope.launch {
            val barData = staticRepository.getPriceLinesWithTitlesSeriesData()
            data.postValue(Data(barData, SeriesType.LINE))
        }
    }

    fun fetchPrices() {
        val seriesDataList = data.value?.list
        minimumPrice = (seriesDataList?.get(0) as LineData).value
        maximumPrice = minimumPrice;
        for (i in seriesDataList.indices) {
            val price = (seriesDataList[i] as LineData).value;
            if (price > maximumPrice) {
                maximumPrice = price;
            }
            if (price < minimumPrice) {
                minimumPrice = price;
            }
        }
        avgPrice = (maximumPrice + minimumPrice) / 2;
    }



}