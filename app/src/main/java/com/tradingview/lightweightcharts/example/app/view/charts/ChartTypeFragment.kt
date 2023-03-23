package com.tradingview.lightweightcharts.example.app.view.charts

import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.tradingview.lightweightcharts.api.chart.models.color.surface.SolidColor
import com.tradingview.lightweightcharts.api.chart.models.color.toIntColor
import com.tradingview.lightweightcharts.api.interfaces.ChartApi
import com.tradingview.lightweightcharts.api.interfaces.SeriesApi
import com.tradingview.lightweightcharts.api.options.enums.TrackingModeExitMode
import com.tradingview.lightweightcharts.api.options.models.TrackingModeOptions
import com.tradingview.lightweightcharts.api.options.models.applyAreaSeriesOptions
import com.tradingview.lightweightcharts.api.options.models.applyBarSeriesOptions
import com.tradingview.lightweightcharts.api.options.models.barSeriesOptions
import com.tradingview.lightweightcharts.api.options.models.candlestickSeriesOptions
import com.tradingview.lightweightcharts.api.options.models.crosshairOptions
import com.tradingview.lightweightcharts.api.options.models.handleScaleOptions
import com.tradingview.lightweightcharts.api.options.models.kineticScrollOptions
import com.tradingview.lightweightcharts.api.options.models.layoutOptions
import com.tradingview.lightweightcharts.api.options.models.lineSeriesOptions
import com.tradingview.lightweightcharts.api.options.models.priceScaleOptions
import com.tradingview.lightweightcharts.api.options.models.timeScaleOptions
import com.tradingview.lightweightcharts.api.series.enums.CrosshairMode
import com.tradingview.lightweightcharts.api.series.models.PriceScaleId
import com.tradingview.lightweightcharts.example.app.R
import com.tradingview.lightweightcharts.example.app.databinding.FragmentChartTypeBinding
import com.tradingview.lightweightcharts.example.app.view.util.ITitleFragment
import com.tradingview.lightweightcharts.example.app.viewmodel.BarChartViewModel
import com.tradingview.lightweightcharts.view.ChartsView

class ChartTypeFragment : Fragment(), ITitleFragment {
    override val fragmentTitleRes = R.string.chart_type

    private val vm by lazy { ViewModelProvider(this)[BarChartViewModel::class.java] }

    private lateinit var binding: FragmentChartTypeBinding
    private val chartApi get() = binding.chartsView.api
    private val typeChips get() = binding.run { listOf(chipCandles, chipLine, chipBars, chipArea) }
    private var curSeries: SeriesApi? = null

    private var chartTypeIndex: Int? = null
        set(value) {
            field = value
            refreshUI()
        }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        return FragmentChartTypeBinding.inflate(inflater, container, false).also { binding = it }.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        vm.init()
        binding.chartsView.subscribeOnChartStateChange { state ->
            when (state) {
                is ChartsView.State.Preparing -> Unit
                is ChartsView.State.Ready -> {
                    vm.seriesBarData.observe(viewLifecycleOwner) { data ->
                        chartTypeIndex = 0
                        chartApi.applyDefOptions()
                    }
                }
                is ChartsView.State.Error -> {
                    Toast.makeText(context, state.exception.localizedMessage, Toast.LENGTH_LONG).show()
                }
            }
        }

        typeChips.forEachIndexed { index, chip ->
            chip.setOnClickListener {
                chartTypeIndex = index
            }
        }

    }


    private fun refreshUI() {
        if (chartTypeIndex == null)
            return

        typeChips.forEachIndexed { index, chip ->
            chip.isChecked = index == chartTypeIndex
        }

        curSeries?.let { chartApi.removeSeries(it) }
        when (chartTypeIndex) {
            0 -> addCandleSeries()
            1 -> addLineSeries()
            2 -> addBarsSeries()
            3 -> addAreaSeries()
        }
    }

    private fun addCandleSeries() {
        chartApi.addCandlestickSeries(
            options = candlestickSeriesOptions {
                priceScaleId = PriceScaleId.RIGHT
            },
            onSeriesCreated = { series ->
                curSeries = series
                series.setData(vm.seriesBarData.value!!.list)
            }
        )
    }

    private fun addLineSeries() {
        chartApi.addLineSeries(
            options = lineSeriesOptions {
                priceScaleId = PriceScaleId.RIGHT
            },
            onSeriesCreated = { series ->
                curSeries = series
                series.setData(vm.seriesAreaData.value!!.list)
            }
        )
    }

    private fun addBarsSeries() {

        chartApi.addBarSeries(
            options = barSeriesOptions {
                priceScaleId = PriceScaleId.RIGHT
                thinBars = true
                downColor = Color.BLACK.toIntColor()
                upColor = Color.BLACK.toIntColor()
            },
            onSeriesCreated = { series ->
                curSeries = series
                series.applyBarSeriesOptions {
                    priceScaleId = PriceScaleId.LEFT
                    thinBars = true
                    downColor = Color.BLACK.toIntColor()
                    upColor = Color.BLACK.toIntColor()
                }
                series.setData(vm.seriesBarData.value!!.list)
            }
        )
    }

    private fun addAreaSeries() {
        chartApi.addAreaSeries { series ->
            curSeries = series
            series.setData(vm.seriesAreaData.value!!.list)

            series.applyAreaSeriesOptions {
                priceScaleId = PriceScaleId.LEFT
            }
        }
    }

    private fun ChartApi.applyDefOptions() = applyOptions {
        handleScale = handleScaleOptions {
            kineticScroll = kineticScrollOptions {
                touch = false
                mouse = false
            }
        }
        layout = layoutOptions {
            background = SolidColor(Color.WHITE)
            textColor = resources.getColor(R.color.blue_1).toIntColor()
        }
        crosshair = crosshairOptions {
            mode = CrosshairMode.NORMAL
        }
        rightPriceScale = priceScaleOptions {
            borderColor = resources.getColor(R.color.gray_1).toIntColor()
        }
        timeScale = timeScaleOptions {
            borderColor = resources.getColor(R.color.gray_1).toIntColor()
            fixRightEdge = true
            minBarSpacing = 0.7f
        }
        trackingMode = TrackingModeOptions(exitMode = TrackingModeExitMode.ON_TOUCH_END)
    }
}