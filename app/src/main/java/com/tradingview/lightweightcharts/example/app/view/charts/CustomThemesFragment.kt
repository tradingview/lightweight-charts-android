package com.tradingview.lightweightcharts.example.app.view.charts

import android.graphics.Color
import androidx.lifecycle.ViewModelProvider
import com.tradingview.lightweightcharts.api.options.models.*
import com.tradingview.lightweightcharts.example.app.viewmodel.CustomThemesViewModel

class CustomThemesFragment: BaseFragment<CustomThemesViewModel>() {

    override fun provideViewModel() {
        viewModel = ViewModelProvider(this).get(CustomThemesViewModel::class.java)
    }

    var isDarkTheme: Boolean = false

    val darkThemeOptions: ChartOptions.() -> Unit = {
        layout = layoutOptions {
            backgroundColor = Color.parseColor("#2B2B43")
            textColor = Color.parseColor("#D9D9D9")
        }
        watermark = watermarkOptions {
            color = Color.argb(0, 0, 0, 0)
        }
        crosshair = crosshairOptions {
            vertLine = crosshairLineOptions {
                color = Color.parseColor("#758696")
                labelBackgroundColor = Color.parseColor("#758696")
            }
            horzLine = crosshairLineOptions {
                color = Color.parseColor("#758696")
                labelBackgroundColor = Color.parseColor("#758696")
            }
        }
        grid = gridOptions {
            vertLines = gridLineOptions {
                color = Color.parseColor("#2B2B43")
            }
            horzLines = gridLineOptions {
                color = Color.parseColor("#363C4E")
            }
        }
    }

    val lightThemeOptions: ChartOptions.() -> Unit = {
        layout = layoutOptions {
            backgroundColor = Color.WHITE
            textColor = Color.parseColor("#191919")
        }
        watermark = watermarkOptions {
            color = Color.argb(0, 0, 0, 0)
        }
        grid = gridOptions {
            vertLines = gridLineOptions {
                visible = false
            }
            horzLines = gridLineOptions {
                color = Color.parseColor("#f0f3fa")
            }
        }
    }

    override fun applyChartOptions() {
        chartApi.applyOptions {
            if (isDarkTheme) {
                darkThemeOptions
            } else {
                lightThemeOptions
            }
        }
    }
}