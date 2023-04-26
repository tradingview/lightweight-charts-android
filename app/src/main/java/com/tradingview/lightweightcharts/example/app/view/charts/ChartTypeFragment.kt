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
import com.tradingview.lightweightcharts.api.interfaces.ChartApi
import com.tradingview.lightweightcharts.api.interfaces.SeriesApi
import com.tradingview.lightweightcharts.api.options.enums.TrackingModeExitMode
import com.tradingview.lightweightcharts.api.options.models.TrackingModeOptions
import com.tradingview.lightweightcharts.api.options.models.applyAreaSeriesOptions
import com.tradingview.lightweightcharts.api.options.models.applyBarSeriesOptions
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
import com.tradingview.lightweightcharts.example.app.view.util.nextFloat
import com.tradingview.lightweightcharts.example.app.view.util.randomColor
import com.tradingview.lightweightcharts.example.app.view.util.randomColorTransparent
import com.tradingview.lightweightcharts.example.app.viewmodel.BarChartViewModel
import com.tradingview.lightweightcharts.view.ChartsView
import kotlin.random.Random

class ChartTypeFragment : Fragment(), ITitleFragment {
    override val fragmentTitleRes = R.string.chart_type

    private val vm by lazy { ViewModelProvider(this)[BarChartViewModel::class.java] }

    private lateinit var binding: FragmentChartTypeBinding
    private val chartApi get() = binding.chartsView.api
    private var curSeries: SeriesApi? = null

    private val typeChips get() = binding.run { listOf(chipCandles, chipLine, chipBars, chipArea) }
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
                        curSeries = null
                        chartTypeIndex = 0
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

        vm.seriesBarData.observe(viewLifecycleOwner) {
            refreshUI()
        }

    }


    private fun refreshUI() {
        if (!isAdded || chartTypeIndex == null || vm.seriesBarData.value == null)
            return

        chartApi.applyRandOptions()
        typeChips.forEachIndexed { index, chip ->
            chip.isChecked = index == chartTypeIndex
        }

        curSeries?.let {
            chartApi.removeSeries(it)
            curSeries = null
        }
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
                upColor = resources.randomColor(R.array.green_array)
                downColor = resources.randomColor(R.array.red_array)
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
                color = resources.randomColor(R.array.blue_array)
            },
            onSeriesCreated = { series ->
                curSeries = series
                series.setData(vm.seriesAreaData.value!!.list)
            }
        )
    }

    private fun addBarsSeries() {
        chartApi.addBarSeries { series ->
            curSeries = series
            series.applyBarSeriesOptions {
                priceScaleId = PriceScaleId.RIGHT
                thinBars = true
                upColor = resources.randomColor(R.array.green_array)
                downColor = resources.randomColor(R.array.red_array)
            }
            series.setData(vm.seriesBarData.value!!.list)
        }

    }

    private fun addAreaSeries() {
        chartApi.addAreaSeries { series ->
            curSeries = series
            series.setData(vm.seriesAreaData.value!!.list)

            series.applyAreaSeriesOptions {
                priceScaleId = PriceScaleId.RIGHT
                lineColor = resources.randomColor(R.array.green_array)
                topColor = resources.randomColor(R.array.green_array)
                bottomColor = resources.randomColorTransparent(R.array.white_array)
            }
        }
    }

    private fun ChartApi.applyRandOptions() = applyOptions {
        handleScale = handleScaleOptions {
            kineticScroll = kineticScrollOptions {
                touch = false
                mouse = false
            }
        }
        layout = layoutOptions {
            background = SolidColor(Color.WHITE)
            textColor = resources.randomColor(R.array.blue_array)
        }
        crosshair = crosshairOptions {
            mode = CrosshairMode.NORMAL
        }
        rightPriceScale = priceScaleOptions {
            borderColor = resources.randomColor(R.array.white_array)
        }
        timeScale = timeScaleOptions {
            borderColor = resources.randomColor(R.array.white_array)
            fixRightEdge = true
            minBarSpacing = Random.nextFloat(0.5f, 1.5f)
        }
        trackingMode = TrackingModeOptions(exitMode = TrackingModeExitMode.ON_TOUCH_END)
    }
}