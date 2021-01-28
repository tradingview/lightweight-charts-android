package com.tradingview.lightweightcharts.example.app.viewmodel

import androidx.lifecycle.viewModelScope
import com.tradingview.lightweightcharts.example.app.model.Data
import com.tradingview.lightweightcharts.example.app.model.SeriesDataType
import kotlinx.coroutines.launch

class PriceLinesWithTitlesViewModel: BaseViewModel() {

    override fun loadData() {
        viewModelScope.launch {
            val barData = staticRepository.getPriceLinesWithTitlesSeriesData()
            data.postValue(Data(barData, SeriesDataType.LINE))
        }
    }
}