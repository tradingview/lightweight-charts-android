package com.tradingview.lightweightcharts.example.app.view.charts

import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.tradingview.lightweightcharts.api.interfaces.ChartApi
import com.tradingview.lightweightcharts.api.interfaces.SeriesApi
import com.tradingview.lightweightcharts.api.options.models.*
import com.tradingview.lightweightcharts.api.series.models.PriceScaleId
import com.tradingview.lightweightcharts.api.series.models.color.IntColor
import com.tradingview.lightweightcharts.api.series.models.color.toIntColor
import com.tradingview.lightweightcharts.example.app.R
import com.tradingview.lightweightcharts.example.app.model.Data
import com.tradingview.lightweightcharts.example.app.viewmodel.SeriesMarkersViewModel
import com.tradingview.lightweightcharts.view.ChartsView
import kotlinx.android.synthetic.main.layout_chart_fragment.*

class SeriesMarkersFragment: Fragment() {

    private lateinit var viewModel: SeriesMarkersViewModel

    private val chartApi: ChartApi by lazy { charts_view.api }
    private var series: MutableList<SeriesApi> = mutableListOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        provideViewModel()
        observeViewModelData()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.layout_chart_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        subscribeOnChartReady(charts_view)
        applyChartOptions()
    }

    private fun provideViewModel() {
        viewModel = ViewModelProvider(this).get(SeriesMarkersViewModel::class.java)
    }

    private fun observeViewModelData() {
        viewModel.seriesData.observe(this, { data ->
            createSeriesWithData(data, PriceScaleId.RIGHT, chartApi) { series ->
                this.series.forEach(chartApi::removeSeries)
                this.series.clear()
                this.series.add(series)

                series.setMarkers(viewModel.createMarkers())
            }
        })
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
        chartApi.applyOptions {
            layout = layoutOptions {
                backgroundColor = IntColor(Color.WHITE)
                textColor = IntColor(Color.BLACK)
            }
            timeScale = timeScaleOptions {
                timeVisible = true
                borderColor = "#D1D4DC".toIntColor()
            }
            rightPriceScale = priceScaleOptions {
                borderColor = "#D1D4DC".toIntColor()
            }
            grid = gridOptions {
                horzLines = gridLineOptions {
                    color = "#F0F3FA".toIntColor()
                }
                vertLines = gridLineOptions {
                    color = "#F0F3FA".toIntColor()
                }
            }
        }
    }

    private fun createSeriesWithData(
            data: Data,
            priceScale: PriceScaleId,
            chartApi: ChartApi,
            onSeriesCreated: (SeriesApi) -> Unit
    ) {
        chartApi.addCandlestickSeries(
                options = CandlestickSeriesOptions(
                    upColor = IntColor(Color.argb(255, 38, 166, 154)),
                    downColor = IntColor(Color.argb(255, 255, 82, 82)),
                    wickUpColor = IntColor(Color.argb(255, 38, 166, 154)),
                    wickDownColor = IntColor(Color.argb(255, 255, 82, 82)),
                    borderVisible = false,
                ),
                onSeriesCreated = { api ->
                    api.setData(data.list)
                    onSeriesCreated(api)
                }
        )

    }
}