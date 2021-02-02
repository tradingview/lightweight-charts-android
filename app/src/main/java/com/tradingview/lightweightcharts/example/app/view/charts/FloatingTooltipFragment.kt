package com.tradingview.lightweightcharts.example.app.view.charts

import android.content.res.Resources
import android.graphics.Color
import android.util.Log
import android.util.TypedValue
import android.widget.LinearLayout
import androidx.core.view.children
import androidx.lifecycle.ViewModelProvider
import com.tradingview.lightweightcharts.api.interfaces.ChartApi
import com.tradingview.lightweightcharts.api.interfaces.SeriesApi
import com.tradingview.lightweightcharts.api.options.models.*
import com.tradingview.lightweightcharts.api.series.enums.LineWidth
import com.tradingview.lightweightcharts.api.series.models.LineData
import com.tradingview.lightweightcharts.api.series.models.PriceScaleId
import com.tradingview.lightweightcharts.api.series.models.Time
import com.tradingview.lightweightcharts.example.app.model.Data
import com.tradingview.lightweightcharts.example.app.view.util.Tooltip
import com.tradingview.lightweightcharts.example.app.viewmodel.FloatingTooltipViewModel


class FloatingTooltipFragment: BaseFragment<FloatingTooltipViewModel>() {

    companion object {
        const val TOOLTIP_WIDTH = 300
        const val TOOLTIP_HEIGHT = 300
    }

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

        attachTooltipToCrosshair()
    }

    private fun attachTooltipToCrosshair() {
        val layoutParams = LinearLayout.LayoutParams(TOOLTIP_WIDTH, TOOLTIP_HEIGHT)
        val displayMetrics = context!!.resources.displayMetrics
        val dpScreenHeight = displayMetrics.heightPixels / displayMetrics.density
        val tooltip = Tooltip(context!!)
        tooltip.setSymbolName("Apple Inc.")

        chartApi.subscribeCrosshairMove { event ->

            if (event.seriesPrices.isNullOrEmpty()) {
                return@subscribeCrosshairMove
            }

            val price = event.seriesPrices!!.firstOrNull()?.prices?.value ?: 0f
            val time = "${(event.time as Time.BusinessDay).year}-" +
                    "${(event.time as Time.BusinessDay).month}-" +
                    "${(event.time as Time.BusinessDay).day}"

            series.first().priceToCoordinate(price) { coordinate ->

                if (coordinate == null) {
                    return@priceToCoordinate
                }

                if (chartFragment.children.last() is Tooltip) {
                    chartFragment.removeViewAt(chartFragment.childCount - 1)
                }

                val coordinateY = 0f.coerceAtLeast((dpScreenHeight - TOOLTIP_HEIGHT).coerceAtMost(coordinate))

                layoutParams.leftMargin = dpToPx(event.point?.x)
                layoutParams.topMargin = dpToPx(coordinateY)

                tooltip.layoutParams = layoutParams
                tooltip.setPrice(price.toString())
                tooltip.setDate(time)
                chartFragment.addView(tooltip, chartFragment.childCount)
            }
        }
    }

    private fun dpToPx(value: Float?): Int {
        return value?.let {
            TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, it, resources.displayMetrics).toInt()
        } ?: 0
    }

    override fun createSeriesWithData(
            data: Data,
            priceScale: PriceScaleId,
            chartApi: ChartApi,
            onSeriesCreated: (SeriesApi) -> Unit
    ) {
        chartApi.addAreaSeries(
                options = AreaSeriesOptions(
                        topColor = Color.argb(143, 0, 150, 136),
                        bottomColor = Color.argb(10, 0, 150, 136),
                        lineColor = Color.argb(255, 0, 150, 136),
                        lineWidth = LineWidth.TWO,
                ),
                onSeriesCreated = { api ->
                    api.setData(data.list)
                    onSeriesCreated(api)
                }
        )
    }
}