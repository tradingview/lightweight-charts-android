package com.tradingview.lightweightcharts.example.app.view.charts

import android.graphics.Color
import com.tradingview.lightweightcharts.api.options.models.*
import com.tradingview.lightweightcharts.api.series.enums.CrosshairMode
import com.tradingview.lightweightcharts.runtime.plugins.PriceFormatter

class CustomPriceFormatterFragment: BaseFragment() {

    override fun applyChartOptions() {
        firstChartApi.applyOptions {
            layout = layoutOptions {
                backgroundColor = Color.WHITE
                textColor = Color.argb(255, 255, 255, 0.8)
            }
            localization = localizationOptions {
                priceFormatter = PriceFormatter(template = "{price:#2}$")
            }
            crosshair = crosshairOptions {
                mode = CrosshairMode.NORMAL
            }
            rightPriceScale = priceScaleOptions {
                borderColor = Color.argb(255, 255, 255, 0.8)
            }
            timeScale = timeScaleOptions {
                borderColor = Color.argb(255, 255, 255, 0.8)
            }
            grid = gridOptions {
                vertLines = Color.argb(255, 255, 255, 0.2)
                horzLines = Color.argb(255, 255, 255, 0.2)
            }
        }
    }
}