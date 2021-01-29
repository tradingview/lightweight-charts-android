package com.tradingview.lightweightcharts.example.app.view.charts

import androidx.lifecycle.ViewModelProvider
import com.tradingview.lightweightcharts.api.options.models.crosshairOptions
import com.tradingview.lightweightcharts.api.series.enums.CrosshairMode
import com.tradingview.lightweightcharts.example.app.viewmodel.RealTimeEmulationViewModel
import kotlinx.coroutines.Job

class RealTimeEmulationFragment: BaseFragment<RealTimeEmulationViewModel>() {

    override fun provideViewModel() {
        viewModel = ViewModelProvider(this).get(RealTimeEmulationViewModel::class.java)
    }

    private var realtimeDataJob: Job? = null

    override fun applyChartOptions() {
        chartApi.applyOptions {
            crosshair = crosshairOptions {
                mode = CrosshairMode.NORMAL
            }
        }
    }
}