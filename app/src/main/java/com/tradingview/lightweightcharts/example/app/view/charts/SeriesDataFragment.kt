package com.tradingview.lightweightcharts.example.app.view.charts

import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.tradingview.lightweightcharts.api.chart.models.color.IntColor
import com.tradingview.lightweightcharts.api.chart.models.color.surface.SolidColor
import com.tradingview.lightweightcharts.api.chart.models.color.toIntColor
import com.tradingview.lightweightcharts.api.interfaces.ChartApi
import com.tradingview.lightweightcharts.api.interfaces.SeriesApi
import com.tradingview.lightweightcharts.api.options.models.CandlestickSeriesOptions
import com.tradingview.lightweightcharts.api.options.models.gridLineOptions
import com.tradingview.lightweightcharts.api.options.models.gridOptions
import com.tradingview.lightweightcharts.api.options.models.layoutOptions
import com.tradingview.lightweightcharts.api.options.models.priceScaleOptions
import com.tradingview.lightweightcharts.api.options.models.timeScaleOptions
import com.tradingview.lightweightcharts.api.series.models.PriceScaleId
import com.tradingview.lightweightcharts.example.app.R
import com.tradingview.lightweightcharts.example.app.databinding.LayoutChartFragmentBinding
import com.tradingview.lightweightcharts.example.app.model.Data
import com.tradingview.lightweightcharts.example.app.view.util.ITitleFragment
import com.tradingview.lightweightcharts.example.app.viewmodel.SeriesMarkersViewModel
import com.tradingview.lightweightcharts.view.ChartsView

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
            createSeriesWithData(data, PriceScaleId.RIGHT, binding.chartsView.api) { series ->
                this.series.clear()
                this.series.add(series)

                series.setMarkers(viewModel.markers)
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
                background = SolidColor(IntColor(Color.WHITE))
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
        onSeriesCreated: (SeriesApi) -> Unit,
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