package com.tradingview.lightweightcharts.example.app.view.charts

import android.graphics.Color
import com.tradingview.lightweightcharts.api.options.models.*

class FloatingTooltipFragment: BaseFragment() {

    override fun applyChartOptions() {
        firstChartApi.applyOptions {
            layout = layoutOptions {
                backgroundColor = Color.WHITE
                textColor = Color.parseColor("#333")
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
                    color = Color.parseColor("#eee")
                }
                vertLines = gridLineOptions {
                    color = Color.WHITE
                }
            }
        }
    }
}