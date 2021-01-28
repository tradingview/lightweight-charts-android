package com.tradingview.lightweightcharts.example.app.view.charts

import android.graphics.Color
import com.tradingview.lightweightcharts.api.options.models.*

class SeriesMarkersFragment: BaseFragment() {

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