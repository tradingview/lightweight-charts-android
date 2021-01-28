package com.tradingview.lightweightcharts.example.app.view.charts

import android.graphics.Color
import androidx.lifecycle.ViewModelProvider
import com.tradingview.lightweightcharts.api.options.models.crosshairOptions
import com.tradingview.lightweightcharts.api.options.models.layoutOptions
import com.tradingview.lightweightcharts.api.options.models.priceScaleOptions
import com.tradingview.lightweightcharts.api.options.models.timeScaleOptions
import com.tradingview.lightweightcharts.api.series.enums.CrosshairMode
import com.tradingview.lightweightcharts.example.app.viewmodel.BarChartViewModel


class BarChartFragment: BaseFragment<BarChartViewModel>() {

    override fun provideViewModel() {
        viewModel = ViewModelProvider(this).get(BarChartViewModel::class.java)
    }

    override fun applyChartOptions() {
        firstChartApi.applyOptions {
            layout = layoutOptions {
                backgroundColor = Color.WHITE
                textColor = Color.argb(1, 33, 56, 77)
            }
            crosshair = crosshairOptions {
                mode = CrosshairMode.NORMAL
            }
            rightPriceScale = priceScaleOptions {
                borderColor = Color.argb(1, 197, 203, 206)
            }
            timeScale = timeScaleOptions {
                borderColor = Color.argb(1, 197, 203, 206)
            }
        }
    }
}