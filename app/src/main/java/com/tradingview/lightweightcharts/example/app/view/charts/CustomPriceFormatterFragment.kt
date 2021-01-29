package com.tradingview.lightweightcharts.example.app.view.charts

import android.graphics.Color
import androidx.lifecycle.ViewModelProvider
import com.tradingview.lightweightcharts.api.interfaces.ChartApi
import com.tradingview.lightweightcharts.api.interfaces.SeriesApi
import com.tradingview.lightweightcharts.api.options.models.*
import com.tradingview.lightweightcharts.api.series.enums.*
import com.tradingview.lightweightcharts.api.series.models.LineData
import com.tradingview.lightweightcharts.api.series.models.PriceFormat
import com.tradingview.lightweightcharts.api.series.models.PriceScaleId
import com.tradingview.lightweightcharts.api.series.models.SeriesMarker
import com.tradingview.lightweightcharts.example.app.model.Data
import com.tradingview.lightweightcharts.example.app.plugins.AutoscaleInfoProvider
import com.tradingview.lightweightcharts.example.app.viewmodel.CustomPriceFormatterViewModel
import com.tradingview.lightweightcharts.runtime.plugins.PriceFormatter

class CustomPriceFormatterFragment: BaseFragment<CustomPriceFormatterViewModel>() {

    var formatters = hashMapOf(
        "Dollar" to "\${price:#2:#2}",
        "Pound" to "\\u00A3{price:#2:#2}"
    )

    override fun provideViewModel() {
        viewModel = ViewModelProvider(this).get(CustomPriceFormatterViewModel::class.java)
    }

    override fun applyChartOptions() {
        chartApi.applyOptions {
            layout = layoutOptions {
                backgroundColor = Color.BLACK
                textColor = Color.argb(204, 255, 255, 255)
            }
            localization = localizationOptions {
                priceFormatter = PriceFormatter(template = "\${price:#2:#2}")
            }
            crosshair = crosshairOptions {
                mode = CrosshairMode.NORMAL
            }
            rightPriceScale = priceScaleOptions {
                borderColor = Color.argb(204, 255, 255, 255)
            }
            timeScale = timeScaleOptions {
                borderColor = Color.argb(204, 255, 255, 255)
            }
            grid = gridOptions {
                vertLines = gridLineOptions {
                    color = Color.argb(51, 255, 255, 255)
                }
                horzLines = gridLineOptions {
                    color = Color.argb(51, 255, 255, 255)
                }
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
                topColor = Color.argb(128, 21, 101, 192),
                bottomColor = Color.argb(128, 21, 101, 192),
                lineColor = Color.argb(204, 255, 255, 255),
                lineWidth = LineWidth.TWO,
            ),
            onSeriesCreated = { api ->
                api.setData(data.list)
                onSeriesCreated(api)
            }
        )
    }

    private fun applyPriceFormat(template: String) {
        chartApi.applyOptions {
            localization = localizationOptions {
                priceFormatter = PriceFormatter(template = template)
            }
        }
    }
}