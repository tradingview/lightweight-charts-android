package com.tradingview.lightweightcharts.example.app.view.charts

import android.graphics.Color
import androidx.lifecycle.ViewModelProvider
import com.tradingview.lightweightcharts.api.options.models.*
import com.tradingview.lightweightcharts.api.series.enums.CrosshairMode
import com.tradingview.lightweightcharts.example.app.viewmodel.BarChartViewModel
import com.tradingview.lightweightcharts.example.app.viewmodel.CustomPriceFormatterViewModel
import com.tradingview.lightweightcharts.runtime.plugins.PriceFormatter

class CustomPriceFormatterFragment: BaseFragment<CustomPriceFormatterViewModel>() {

    override fun provideViewModel() {
        viewModel = ViewModelProvider(this).get(CustomPriceFormatterViewModel::class.java)
    }

    override fun applyChartOptions() {
        firstChartApi.applyOptions {
            layout = layoutOptions {
                backgroundColor = Color.WHITE
                textColor = Color.argb(0.8f, 255f, 255f, 255f)
            }
            localization = localizationOptions {
                priceFormatter = PriceFormatter(template = "{price:#2}$")
            }
            crosshair = crosshairOptions {
                mode = CrosshairMode.NORMAL
            }
            rightPriceScale = priceScaleOptions {
                borderColor = Color.argb(0.8f, 255f, 255f, 255f)
            }
            timeScale = timeScaleOptions {
                borderColor = Color.argb(0.8f, 255f, 255f, 255f)
            }
            grid = gridOptions {
                vertLines = gridLineOptions {
                    color = Color.argb(0.2f, 255f, 255f, 255f)
                }
                horzLines = gridLineOptions {
                    color = Color.argb(0.2f, 255f, 255f, 255f)
                }
            }
        }
    }
}