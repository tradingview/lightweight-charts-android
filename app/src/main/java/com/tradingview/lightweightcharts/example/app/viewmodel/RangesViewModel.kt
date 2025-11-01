package com.tradingview.lightweightcharts.example.app.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.tradingview.lightweightcharts.api.series.enums.SeriesType
import com.tradingview.lightweightcharts.example.app.model.Data
import com.tradingview.lightweightcharts.example.app.repository.HistoricalDataRepository
import kotlinx.coroutines.launch

class RangesViewModel(val app: Application) : AndroidViewModel(app) {

    private val rep = HistoricalDataRepository(app)

    val seriesDailyAreaData: LiveData<Data> get() = dailyAreaData
    private val dailyAreaData = MutableLiveData<Data>()

    val seriesWeeklyAreaData: LiveData<Data> get() = weeklyAreaData
    private val weeklyAreaData = MutableLiveData<Data>()

    val seriesMonthlyAreaData: LiveData<Data> get() = monthlyAreaData
    private val monthlyAreaData = MutableLiveData<Data>()

    val seriesYearlyAreaData: LiveData<Data> get() = yearlyBarData
    private val yearlyBarData = MutableLiveData<Data>()


    fun init() {
        loadData()
    }

    private fun loadData() {
        viewModelScope.launch {
            val daily = rep.getDailyData().take(1000).toList()
            dailyAreaData.postValue(Data(daily, SeriesType.BAR))

            val weekly = rep.getWeeklyData().take(1000).toList()
            weeklyAreaData.postValue(Data(weekly, SeriesType.BAR))


            val monthly = rep.getMonthlyData().take(1000).toList()
            monthlyAreaData.postValue(Data(monthly, SeriesType.BAR))


            val yearly = rep.getYearlyData().toList()
            yearlyBarData.postValue(Data(yearly, SeriesType.BAR))

        }
    }
}