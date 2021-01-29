package com.tradingview.lightweightcharts.example.app.view.charts

import android.graphics.Color
import androidx.lifecycle.ViewModelProvider
import com.tradingview.lightweightcharts.api.interfaces.ChartApi
import com.tradingview.lightweightcharts.api.interfaces.SeriesApi
import com.tradingview.lightweightcharts.api.options.models.*
import com.tradingview.lightweightcharts.api.series.enums.CrosshairMode
import com.tradingview.lightweightcharts.api.series.models.PriceScaleId
import com.tradingview.lightweightcharts.example.app.model.Data
import com.tradingview.lightweightcharts.example.app.viewmodel.BarChartViewModel


class BarChartFragment: BaseFragment<BarChartViewModel>() {

    override fun provideViewModel() {
        viewModel = ViewModelProvider(this).get(BarChartViewModel::class.java)
    }

    override fun applyChartOptions() {
        chartApi.applyOptions {
            layout = layoutOptions {
                backgroundColor = Color.WHITE
                textColor = Color.argb(255, 33, 56, 77)
            }
            crosshair = crosshairOptions {
                mode = CrosshairMode.NORMAL
            }
            rightPriceScale = priceScaleOptions {
                borderColor = Color.argb(255, 197, 203, 206)
            }
            timeScale = timeScaleOptions {
                borderColor = Color.argb(255, 197, 203, 206)
            }
        }
    }

    override fun createSeriesWithData(
        data: Data,
        priceScale: PriceScaleId,
        chartApi: ChartApi,
        onSeriesCreated: (SeriesApi) -> Unit
    ) {
        chartApi.addBarSeries(
            options = BarSeriesOptions(
                priceScaleId = priceScale,
                thinBars = true,
                downColor = Color.BLACK,
                upColor = Color.BLACK,
            ),
            onSeriesCreated = { api ->
                api.setData(data.list)
                onSeriesCreated(api)
            }
        )
    }
}