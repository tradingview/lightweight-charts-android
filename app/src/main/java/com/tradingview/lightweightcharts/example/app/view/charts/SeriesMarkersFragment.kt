package com.tradingview.lightweightcharts.example.app.view.charts

import android.graphics.Color
import androidx.lifecycle.ViewModelProvider
import com.tradingview.lightweightcharts.api.options.models.*
import com.tradingview.lightweightcharts.example.app.viewmodel.BarChartViewModel
import com.tradingview.lightweightcharts.example.app.viewmodel.SeriesMarkersViewModel

class SeriesMarkersFragment: BaseFragment<SeriesMarkersViewModel>() {

    override fun provideViewModel() {
        viewModel = ViewModelProvider(this).get(SeriesMarkersViewModel::class.java)
    }

    override fun applyChartOptions() {
        firstChartApi.applyOptions {
            layout = layoutOptions {
                backgroundColor = Color.WHITE
                textColor = Color.BLACK
            }
            timeScale = timeScaleOptions {
                timeVisible = true
                borderColor = Color.parseColor("#D1D4DC")
            }
            rightPriceScale = priceScaleOptions {
                borderColor = Color.parseColor("#D1D4DC")
            }
            grid = gridOptions {
                horzLines = gridLineOptions {
                    color = Color.parseColor("#F0F3FA")
                }
                vertLines = gridLineOptions {
                    color = Color.parseColor("#F0F3FA")
                }
            }
        }
    }
}