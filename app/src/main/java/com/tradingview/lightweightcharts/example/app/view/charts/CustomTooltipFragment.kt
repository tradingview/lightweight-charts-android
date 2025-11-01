package com.tradingview.lightweightcharts.example.app.view.charts

import android.graphics.Color
import android.os.Bundle
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.tradingview.lightweightcharts.api.chart.models.color.surface.SolidColor
import com.tradingview.lightweightcharts.api.chart.models.color.toIntColor
import com.tradingview.lightweightcharts.api.interfaces.SeriesApi
import com.tradingview.lightweightcharts.api.options.models.AreaSeriesOptions
import com.tradingview.lightweightcharts.api.options.models.crosshairLineOptions
import com.tradingview.lightweightcharts.api.options.models.crosshairOptions
import com.tradingview.lightweightcharts.api.options.models.gridLineOptions
import com.tradingview.lightweightcharts.api.options.models.gridOptions
import com.tradingview.lightweightcharts.api.options.models.layoutOptions
import com.tradingview.lightweightcharts.api.options.models.priceScaleMargins
import com.tradingview.lightweightcharts.api.options.models.priceScaleOptions
import com.tradingview.lightweightcharts.api.options.models.timeScaleOptions
import com.tradingview.lightweightcharts.api.series.enums.LineWidth
import com.tradingview.lightweightcharts.api.series.models.MouseEventParams
import com.tradingview.lightweightcharts.example.app.R
import com.tradingview.lightweightcharts.example.app.view.util.ITitleFragment
import com.tradingview.lightweightcharts.example.app.view.util.Tooltip
import com.tradingview.lightweightcharts.example.app.viewmodel.FloatingTooltipViewModel
import com.tradingview.lightweightcharts.view.ChartsView
import java.text.SimpleDateFormat

class CustomTooltipFragment : Fragment(), ITitleFragment {
    override val fragmentTitleRes = R.string.custom_tooltips

    private val viewModelProvider get() = ViewModelProvider(this)
    private lateinit var viewModel: FloatingTooltipViewModel

    private lateinit var areaSeries: SeriesApi

    private val dateFormat by lazy { SimpleDateFormat.getDateInstance() }

    private val tooltip get() = requireView().findViewById<Tooltip>(R.id.tooltip)
    private val chartsView get() = requireView().findViewById<ChartsView>(R.id.charts_view)

    private val chartApi get() = chartsView.api

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.layout_chart_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = viewModelProvider[FloatingTooltipViewModel::class.java]
        viewModel.seriesData.observe(viewLifecycleOwner) { data ->
            chartApi.addAreaSeries(
                options = AreaSeriesOptions(
                    topColor = Color.argb(143, 0, 150, 136).toIntColor(),
                    bottomColor = Color.argb(10, 0, 150, 136).toIntColor(),
                    lineColor = Color.argb(255, 0, 150, 136).toIntColor(),
                    lineWidth = LineWidth.TWO,
                ),
                onSeriesCreated = { api ->
                    areaSeries = api
                    areaSeries.setData(data.list)
                }
            )
        }
        applyChartOptions()
        chartApi.subscribeCrosshairMove(onCrosshairMove)
    }

    override fun onDestroyView() {
        chartApi.unsubscribeCrosshairMove(onCrosshairMove)
        super.onDestroyView()
    }

    private fun applyChartOptions() {
        chartApi.applyOptions {
            layout = layoutOptions {
                background = SolidColor(Color.WHITE)
                textColor = Color.parseColor("#333333").toIntColor()
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
                    color = Color.parseColor("#eeeeee").toIntColor()
                }
                vertLines = gridLineOptions {
                    color = Color.WHITE.toIntColor()
                }
            }
        }
    }


    private val onCrosshairMove: (MouseEventParams) -> Unit = onCrosshairMove@{ mouseEventParams ->
        val prices = mouseEventParams.seriesData
        if (prices.isNullOrEmpty()) {
            tooltip.visibility = View.GONE
            return@onCrosshairMove
        }

        tooltip.visibility = View.VISIBLE

        val price = prices.first().prices.value ?: 0f

        val crosshairDate = mouseEventParams.time!!.date
        val time = dateFormat.format(crosshairDate)

        areaSeries.priceToCoordinate(price) { coordinate ->
            if (coordinate == null) {
                return@priceToCoordinate
            }

            tooltip.translationX = dpToPx(mouseEventParams.point?.x).toFloat()
            tooltip.translationY = dpToPx(coordinate).toFloat()
            tooltip.setPrice(price.toString())
            tooltip.setDate(time)
            tooltip.setSymbolName("Apple Inc.")
        }
    }

    private fun dpToPx(value: Float?): Int {
        return value?.let {
            TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, it, resources.displayMetrics).toInt()
        } ?: 0
    }
}