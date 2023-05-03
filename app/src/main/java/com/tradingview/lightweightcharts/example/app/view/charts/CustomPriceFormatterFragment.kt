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
import com.tradingview.lightweightcharts.api.options.models.AreaSeriesOptions
import com.tradingview.lightweightcharts.api.options.models.crosshairOptions
import com.tradingview.lightweightcharts.api.options.models.gridLineOptions
import com.tradingview.lightweightcharts.api.options.models.gridOptions
import com.tradingview.lightweightcharts.api.options.models.layoutOptions
import com.tradingview.lightweightcharts.api.options.models.localizationOptions
import com.tradingview.lightweightcharts.api.options.models.priceScaleOptions
import com.tradingview.lightweightcharts.api.options.models.timeScaleOptions
import com.tradingview.lightweightcharts.api.series.enums.CrosshairMode
import com.tradingview.lightweightcharts.api.series.enums.LineWidth
import com.tradingview.lightweightcharts.api.series.models.PriceScaleId
import com.tradingview.lightweightcharts.example.app.R
import com.tradingview.lightweightcharts.example.app.databinding.LayoutPriceFormatterChartFragmentBinding
import com.tradingview.lightweightcharts.example.app.model.Data
import com.tradingview.lightweightcharts.example.app.view.util.ITitleFragment
import com.tradingview.lightweightcharts.example.app.viewmodel.CustomPriceFormatterViewModel
import com.tradingview.lightweightcharts.runtime.plugins.PriceFormatter
import com.tradingview.lightweightcharts.view.ChartsView

class CustomPriceFormatterFragment : Fragment(), ITitleFragment {
    override val fragmentTitleRes = R.string.custom_formator

    private lateinit var viewModel: CustomPriceFormatterViewModel

    private lateinit var binding: LayoutPriceFormatterChartFragmentBinding
    private var series: MutableList<SeriesApi> = mutableListOf()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        return LayoutPriceFormatterChartFragmentBinding.inflate(inflater, container, false)
            .also { binding = it }.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        provideViewModel()
        observeViewModelData()
        subscribeOnChartReady(binding.chartsView)
        applyChartOptions()
        binding.dollarBtn.setOnClickListener { applyPriceFormat("\${price:#2:#2}") }
        binding.poundBtn.setOnClickListener { applyPriceFormat("\u00A3{price:#2:#2}") }
    }

    private fun provideViewModel() {
        viewModel = ViewModelProvider(this).get(CustomPriceFormatterViewModel::class.java)
    }

    private fun observeViewModelData() {
        viewModel.seriesData.observe(viewLifecycleOwner) { data ->
            createSeriesWithData(data, PriceScaleId.RIGHT, binding.chartsView.api) { series ->
                this.series.clear()
                this.series.add(series)
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
                background = SolidColor(Color.BLACK.toIntColor())
                textColor = Color.argb(204, 255, 255, 255).toIntColor()
            }
            localization = localizationOptions {
                priceFormatter = PriceFormatter(template = "\${price:#2:#2}")
            }
            crosshair = crosshairOptions {
                mode = CrosshairMode.NORMAL
            }
            rightPriceScale = priceScaleOptions {
                borderColor = Color.argb(204, 255, 255, 255).toIntColor()
            }
            timeScale = timeScaleOptions {
                borderColor = Color.argb(204, 255, 255, 255).toIntColor()
            }
            grid = gridOptions {
                vertLines = gridLineOptions {
                    color = Color.argb(51, 255, 255, 255).toIntColor()
                }
                horzLines = gridLineOptions {
                    color = Color.argb(51, 255, 255, 255).toIntColor()
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
        chartApi.addAreaSeries(
            options = AreaSeriesOptions(
                topColor = Color.argb(128, 21, 101, 192).toIntColor(),
                bottomColor = Color.argb(128, 21, 101, 192).toIntColor(),
                lineColor = Color.argb(204, 255, 255, 255).toIntColor(),
                lineWidth = LineWidth.TWO,
            ),
            onSeriesCreated = { api ->
                api.setData(data.list)
                onSeriesCreated(api)
            }
        )
    }

    private fun applyPriceFormat(template: String) {
        binding.chartsView.api.applyOptions {
            localization = localizationOptions {
                priceFormatter = PriceFormatter(template = template)
            }
        }
    }
}