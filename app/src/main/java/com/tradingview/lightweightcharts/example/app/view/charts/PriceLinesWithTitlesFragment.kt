package com.tradingview.lightweightcharts.example.app.view.charts

import android.graphics.Color
import androidx.lifecycle.ViewModelProvider
import com.tradingview.lightweightcharts.api.options.models.*
import com.tradingview.lightweightcharts.api.series.enums.LineStyle
import com.tradingview.lightweightcharts.api.series.enums.LineWidth
import com.tradingview.lightweightcharts.example.app.viewmodel.BarChartViewModel
import com.tradingview.lightweightcharts.example.app.viewmodel.PriceLinesWithTitlesViewModel

class PriceLinesWithTitlesFragment: BaseFragment<PriceLinesWithTitlesViewModel>() {

    override fun provideViewModel() {
        viewModel = ViewModelProvider(this).get(PriceLinesWithTitlesViewModel::class.java)
    }

    override fun applyChartOptions() {
        firstChartApi.applyOptions {
            layout = layoutOptions {
                textColor = Color.parseColor("#d1d4dc")
                backgroundColor = Color.BLACK
            }
            rightPriceScale = priceScaleOptions {
                scaleMargins = priceScaleMargins {
                    top = 0.3f
                    bottom = 0.25f
                }
            }
            crosshair = crosshairOptions {
                vertLine = crosshairLineOptions {
                    width = LineWidth.FOUR
                    color = Color.argb(0.1f, 224f, 227f, 235f)
                    style = LineStyle.SOLID
                }
                horzLine = crosshairLineOptions {
                    visible = false
                    labelVisible = false
                }
            }
            grid = gridOptions {
                vertLines = gridLineOptions {
                    color = Color.argb(0, 42, 46, 57)
                }
                horzLines = gridLineOptions {
                    color = Color.argb(0, 42, 46, 57)
                }
            }
            handleScroll = handleScrollOptions {
                vertTouchDrag = false
            }
        }
    }
}