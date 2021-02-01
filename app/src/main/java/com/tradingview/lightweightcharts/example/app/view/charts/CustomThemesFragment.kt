package com.tradingview.lightweightcharts.example.app.view.charts

import android.graphics.Color
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.LinearLayout
import androidx.lifecycle.ViewModelProvider
import com.tradingview.lightweightcharts.api.interfaces.ChartApi
import com.tradingview.lightweightcharts.api.interfaces.SeriesApi
import com.tradingview.lightweightcharts.api.options.models.*
import com.tradingview.lightweightcharts.api.series.enums.LineWidth
import com.tradingview.lightweightcharts.api.series.models.PriceScaleId
import com.tradingview.lightweightcharts.example.app.R
import com.tradingview.lightweightcharts.example.app.model.Data
import com.tradingview.lightweightcharts.example.app.viewmodel.CustomThemesViewModel

class CustomThemesFragment: BaseFragment<CustomThemesViewModel>() {

    override fun provideViewModel() {
        viewModel = ViewModelProvider(this).get(CustomThemesViewModel::class.java)
    }

    private val darkThemeOptions: ChartOptions.() -> Unit = {
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

    private val lightThemeOptions: ChartOptions.() -> Unit = {
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

    override fun createSeriesWithData(
            data: Data,
            priceScale: PriceScaleId,
            chartApi: ChartApi,
            onSeriesCreated: (SeriesApi) -> Unit
    ) {
        chartApi.addAreaSeries(
                options = AreaSeriesOptions(
                        topColor = Color.argb(143, 33, 150, 243),
                        bottomColor = Color.argb(10, 33, 150, 243),
                        lineColor = Color.argb(204, 33, 150, 243),
                        lineWidth = LineWidth.TWO,
                ),
                onSeriesCreated = { api ->
                    api.setData(data.list)
                    onSeriesCreated(api)
                }
        )
    }

    override fun applyChartOptions() {
        applyThemeOptions(darkThemeOptions)
    }

    override fun enableButtons(view: View) {
        mapOf(
                "Dark" to darkThemeOptions,
                "Light" to lightThemeOptions
        ).forEach {
            createButton(it.key) { applyThemeOptions(it.value) }
        }
    }

    private fun applyThemeOptions(theme: ChartOptions.() -> Unit) {
        chartApi.applyOptions {
            theme.invoke(this)
        }
    }
}