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
import com.tradingview.lightweightcharts.api.options.models.CandlestickSeriesOptions
import com.tradingview.lightweightcharts.api.options.models.gridLineOptions
import com.tradingview.lightweightcharts.api.options.models.gridOptions
import com.tradingview.lightweightcharts.api.options.models.layoutOptions
import com.tradingview.lightweightcharts.api.options.models.priceScaleOptions
import com.tradingview.lightweightcharts.api.options.models.timeScaleOptions
import com.tradingview.lightweightcharts.api.series.common.SeriesData
import com.tradingview.lightweightcharts.api.series.enums.SeriesMarkerPosition
import com.tradingview.lightweightcharts.api.series.enums.SeriesMarkerShape
import com.tradingview.lightweightcharts.api.series.models.BarData
import com.tradingview.lightweightcharts.api.series.models.SeriesMarker
import com.tradingview.lightweightcharts.example.app.R
import com.tradingview.lightweightcharts.example.app.databinding.LayoutChartFragmentBinding
import com.tradingview.lightweightcharts.example.app.model.Data
import com.tradingview.lightweightcharts.example.app.view.util.ITitleFragment
import com.tradingview.lightweightcharts.example.app.view.util.chartColor
import com.tradingview.lightweightcharts.example.app.viewmodel.SeriesMarkersViewModel
import com.tradingview.lightweightcharts.view.ChartsView
import kotlin.math.floor

class SeriesDataFragment : Fragment(), ITitleFragment {
    override val fragmentTitleRes = R.string.data

    private lateinit var viewModel: SeriesMarkersViewModel

    private lateinit var binding: LayoutChartFragmentBinding

    private var series: MutableList<SeriesApi> = mutableListOf()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        return LayoutChartFragmentBinding.inflate(inflater, container, false)
            .also { binding = it }.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        provideViewModel()
        observeViewModelData()
        subscribeOnChartReady(binding.chartsView)
        applyChartOptions()
    }

    private fun provideViewModel() {
        viewModel = ViewModelProvider(this).get(SeriesMarkersViewModel::class.java)
    }

    private fun observeViewModelData() {
        viewModel.seriesData.observe(viewLifecycleOwner) { data ->
            createSeriesWithData(data, binding.chartsView.api) { series ->
                this.series.clear()
                this.series.add(series)

                series.setMarkers(createMarkers(data.list))
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
                background = SolidColor(chartColor(R.color.chart_white))
                textColor = chartColor(R.color.chart_black)
            }
            timeScale = timeScaleOptions {
                timeVisible = true
                borderColor = chartColor(R.color.chart_text_primary)
            }
            rightPriceScale = priceScaleOptions {
                borderColor = chartColor(R.color.chart_text_primary)
            }
            grid = gridOptions {
                horzLines = gridLineOptions {
                    color = chartColor(R.color.chart_light_grid)
                }
                vertLines = gridLineOptions {
                    color = chartColor(R.color.chart_light_grid)
                }
            }
        }
    }

    private fun createSeriesWithData(
        data: Data,
        chartApi: ChartApi,
        onSeriesCreated: (SeriesApi) -> Unit,
    ) {
        chartApi.addCandlestickSeries(
            options = CandlestickSeriesOptions(
                upColor = chartColor(R.color.chart_series_green),
                downColor = chartColor(R.color.chart_series_red),
                wickUpColor = chartColor(R.color.chart_series_green),
                wickDownColor = chartColor(R.color.chart_series_red),
                borderVisible = false,
            ),
            onSeriesCreated = { api ->
                api.setData(data.list)
                onSeriesCreated(api)
            }
        )
    }

    private fun createMarkers(seriesDataList: List<SeriesData>): List<SeriesMarker> {
        if (seriesDataList.size < 48) {
            return emptyList()
        }

        val datesForMarkers = seriesDataList.subList(
            seriesDataList.size - 39,
            seriesDataList.size - 18,
        )

        val indexOfMinPrice = datesForMarkers
            .indexOfFirst { data ->
                (data as BarData).low == datesForMarkers.minOf { (it as BarData).low }
            }

        val indexOfMaxPrice = datesForMarkers
            .indexOfLast { data ->
                (data as BarData).high == datesForMarkers.maxOf { (it as BarData).high }
            }

        return listOf(
            SeriesMarker(
                time = seriesDataList[seriesDataList.size - 48].time,
                position = SeriesMarkerPosition.ABOVE_BAR,
                color = chartColor(R.color.chart_marker_orange),
                shape = SeriesMarkerShape.CIRCLE,
                text = "D",
            ),
            SeriesMarker(
                time = datesForMarkers[indexOfMinPrice].time,
                position = SeriesMarkerPosition.BELOW_BAR,
                color = chartColor(R.color.chart_marker_blue),
                shape = SeriesMarkerShape.ARROW_UP,
                text = "Buy @ ${floor((datesForMarkers[indexOfMinPrice] as BarData).low - 2)}",
            ),
            SeriesMarker(
                time = datesForMarkers[indexOfMaxPrice].time,
                position = SeriesMarkerPosition.ABOVE_BAR,
                color = chartColor(R.color.chart_marker_pink),
                shape = SeriesMarkerShape.ARROW_DOWN,
                text = "Sell @ ${floor((datesForMarkers[indexOfMaxPrice] as BarData).high + 2)}",
            ),
        )
    }

    private fun chartColor(colorRes: Int, alpha: Int? = null) = requireContext().chartColor(colorRes, alpha)
}
