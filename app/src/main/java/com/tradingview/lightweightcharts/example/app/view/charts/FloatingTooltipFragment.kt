package com.tradingview.lightweightcharts.example.app.view.charts

import android.graphics.Color
import androidx.lifecycle.ViewModelProvider
import com.tradingview.lightweightcharts.api.options.models.*
import com.tradingview.lightweightcharts.example.app.viewmodel.FloatingTooltipViewModel

class FloatingTooltipFragment: BaseFragment<FloatingTooltipViewModel>() {

    override fun provideViewModel() {
        viewModel = ViewModelProvider(this).get(FloatingTooltipViewModel::class.java)
    }

    override fun applyChartOptions() {
        chartApi.applyOptions {
            layout = layoutOptions {
                backgroundColor = Color.WHITE
                textColor = Color.parseColor("#333333")
            }
            crosshair = crosshairOptions {
                vertLine = crosshairLineOptions {
                    visible = false
                }
            }
            rightPriceScale = priceScaleOptions {
                scaleMargins = priceScaleMargins {
                    top = 0.2f
                    bottom = 0.2f
                }
                borderVisible = false
            }
            timeScale = timeScaleOptions {
                borderVisible = false
            }
            grid = gridOptions {
                horzLines = gridLineOptions {
                    color = Color.parseColor("#eeeeee")
                }
                vertLines = gridLineOptions {
                    color = Color.WHITE
                }
            }
        }
    }
}