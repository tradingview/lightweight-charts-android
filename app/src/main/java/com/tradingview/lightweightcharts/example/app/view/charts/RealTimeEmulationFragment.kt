package com.tradingview.lightweightcharts.example.app.view.charts

import com.tradingview.lightweightcharts.api.options.models.crosshairOptions
import com.tradingview.lightweightcharts.api.series.enums.CrosshairMode
import com.tradingview.lightweightcharts.example.app.view.charts.BaseFragment

class RealTimeEmulationFragment: BaseFragment() {

    override fun applyChartOptions() {
        firstChartApi.applyOptions {
            crosshair = crosshairOptions {
                mode = CrosshairMode.NORMAL
            }
        }
    }
}