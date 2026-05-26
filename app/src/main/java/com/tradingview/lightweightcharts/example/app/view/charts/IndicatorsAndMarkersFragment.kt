package com.tradingview.lightweightcharts.example.app.view.charts

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
import com.tradingview.lightweightcharts.api.options.models.AreaSeriesOptions
import com.tradingview.lightweightcharts.api.options.models.HistogramSeriesOptions
import com.tradingview.lightweightcharts.api.options.models.PriceScaleMargins
import com.tradingview.lightweightcharts.api.options.models.PriceScaleOptions
import com.tradingview.lightweightcharts.api.options.models.SeriesMarkersOptions
import com.tradingview.lightweightcharts.api.options.models.gridLineOptions
import com.tradingview.lightweightcharts.api.options.models.gridOptions
import com.tradingview.lightweightcharts.api.options.models.layoutOptions
import com.tradingview.lightweightcharts.api.options.models.priceScaleMargins
import com.tradingview.lightweightcharts.api.options.models.priceScaleOptions
import com.tradingview.lightweightcharts.api.series.common.SeriesData
import com.tradingview.lightweightcharts.api.series.enums.LineWidth
import com.tradingview.lightweightcharts.api.series.enums.SeriesMarkerPosition
import com.tradingview.lightweightcharts.api.series.enums.SeriesMarkerShape
import com.tradingview.lightweightcharts.api.series.enums.SeriesMarkerZOrder
import com.tradingview.lightweightcharts.api.series.models.LineData
import com.tradingview.lightweightcharts.api.series.models.PriceFormat
import com.tradingview.lightweightcharts.api.series.models.PriceScaleId
import com.tradingview.lightweightcharts.api.series.models.SeriesMarker
import com.tradingview.lightweightcharts.example.app.R
import com.tradingview.lightweightcharts.example.app.databinding.LayoutChartFragmentBinding
import com.tradingview.lightweightcharts.example.app.model.Data
import com.tradingview.lightweightcharts.example.app.view.util.ITitleFragment
import com.tradingview.lightweightcharts.example.app.view.util.chartColor
import com.tradingview.lightweightcharts.example.app.viewmodel.VolumeStudyViewModel
import com.tradingview.lightweightcharts.view.ChartsView

class IndicatorsAndMarkersFragment : Fragment(), ITitleFragment {
    override val fragmentTitleRes = R.string.indicators_and_markers

    private val vm by lazy { ViewModelProvider(this)[VolumeStudyViewModel::class.java] }

    private lateinit var binding: LayoutChartFragmentBinding

    private var areaSeries: MutableList<SeriesApi> = mutableListOf()
    private var volumeSeries: MutableList<SeriesApi> = mutableListOf()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        return LayoutChartFragmentBinding.inflate(inflater, container, false)
            .also { binding = it }.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        observeViewModelData()
        subscribeOnChartReady(binding.chartsView)
        applyChartOptions()
    }

    private fun observeViewModelData() = vm.run {
        areaSeriesData.observe(viewLifecycleOwner) { data ->
            createAreaSeriesWithData(data, binding.chartsView.api) { series ->
                this@IndicatorsAndMarkersFragment.areaSeries.clear()
                this@IndicatorsAndMarkersFragment.areaSeries.add(series)
            }
        }
        volumeSeriesData.observe(viewLifecycleOwner) { data ->
            createVolumeSeriesWithData(data, binding.chartsView.api) { series ->
                this@IndicatorsAndMarkersFragment.volumeSeries.clear()
                this@IndicatorsAndMarkersFragment.volumeSeries.add(series)
            }
        }
    }

    private fun subscribeOnChartReady(view: ChartsView) {
        view.subscribeOnChartStateChange { state ->
            when (state) {
                is ChartsView.State.Preparing -> Unit
                is ChartsView.State.Ready -> {
                    Toast.makeText(context, "Chart ${view.id} is ready", Toast.LENGTH_SHORT).show()
                }

                is ChartsView.State.Error -> {
                    Toast.makeText(context, state.exception.localizedMessage, Toast.LENGTH_LONG).show()
                }
            }
        }
    }

    private fun applyChartOptions() {
        binding.chartsView.api.applyOptions {
            layout = layoutOptions {
                background = SolidColor(chartColor(R.color.chart_dark_background))
                textColor = chartColor(R.color.chart_text_primary)
            }
            rightPriceScale = priceScaleOptions {
                scaleMargins = priceScaleMargins {
                    top = 0.3f
                    bottom = 0.25f
                }
                borderVisible = false
            }
            grid = gridOptions {
                vertLines = gridLineOptions {
                    color = chartColor(R.color.chart_grid_dark, alpha = 0)
                }
                horzLines = gridLineOptions {
                    color = chartColor(R.color.chart_grid_dark, alpha = 153)
                }
            }
        }
    }

    private fun createAreaSeriesWithData(
        data: Data,
        chartApi: ChartApi,
        onSeriesCreated: (SeriesApi) -> Unit,
    ) {
        chartApi.addAreaSeries(
            options = AreaSeriesOptions(
                topColor = chartColor(R.color.chart_series_cyan, alpha = 143),
                bottomColor = chartColor(R.color.chart_series_cyan, alpha = 10),
                lineColor = chartColor(R.color.chart_series_cyan),
                lineWidth = LineWidth.TWO,
            ),
            onSeriesCreated = { api ->
                api.setData(data.list)
                api.createSeriesMarkers(
                    data = createIndicatorMarkers(data.list),
                    options = SeriesMarkersOptions(zOrder = SeriesMarkerZOrder.TOP, autoScale = true),
                ) {}
                onSeriesCreated(api)
            }
        )
    }

    private fun createVolumeSeriesWithData(
        data: Data,
        chartApi: ChartApi,
        onSeriesCreated: (SeriesApi) -> Unit,
    ) {
        chartApi.addHistogramSeries(
            options = HistogramSeriesOptions(
                color = chartColor(R.color.chart_series_green),
                priceFormat = PriceFormat.priceFormatBuiltIn(
                    type = PriceFormat.Type.VOLUME,
                    precision = 1,
                    minMove = 1f,
                ),
                priceScaleId = PriceScaleId(""),
            ),
            onSeriesCreated = { api ->
                api.priceScale().applyOptions(
                    PriceScaleOptions().apply {
                        scaleMargins = PriceScaleMargins(
                            top = 0.8f,
                            bottom = 0f,
                        )
                    }
                )

                api.setData(data.list)
                onSeriesCreated(api)
            }
        )
    }

    private fun createIndicatorMarkers(data: List<SeriesData>): List<SeriesMarker> {
        if (data.size < 55) {
            return emptyList()
        }

        val firstSignal = data[18] as LineData
        val secondSignal = data[34] as LineData
        val thirdSignal = data[49] as LineData

        return listOf(
            SeriesMarker(
                time = firstSignal.time,
                position = SeriesMarkerPosition.BELOW_BAR,
                color = chartColor(R.color.chart_marker_blue),
                shape = SeriesMarkerShape.ARROW_UP,
                text = "RSI",
            ),
            SeriesMarker(
                time = secondSignal.time,
                position = SeriesMarkerPosition.ABOVE_BAR,
                color = chartColor(R.color.chart_marker_pink),
                shape = SeriesMarkerShape.ARROW_DOWN,
                text = "Exit",
            ),
            SeriesMarker(
                time = thirdSignal.time,
                position = SeriesMarkerPosition.AT_PRICE_MIDDLE,
                price = thirdSignal.value,
                color = chartColor(R.color.chart_marker_orange),
                shape = SeriesMarkerShape.CIRCLE,
                text = "Signal",
            ),
        )
    }

    private fun chartColor(colorRes: Int, alpha: Int? = null) = requireContext().chartColor(colorRes, alpha)
}
