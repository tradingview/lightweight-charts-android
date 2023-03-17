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
import com.tradingview.lightweightcharts.api.options.models.ChartOptions
import com.tradingview.lightweightcharts.api.options.models.crosshairLineOptions
import com.tradingview.lightweightcharts.api.options.models.crosshairOptions
import com.tradingview.lightweightcharts.api.options.models.gridLineOptions
import com.tradingview.lightweightcharts.api.options.models.gridOptions
import com.tradingview.lightweightcharts.api.options.models.layoutOptions
import com.tradingview.lightweightcharts.api.options.models.watermarkOptions
import com.tradingview.lightweightcharts.api.series.enums.LineWidth
import com.tradingview.lightweightcharts.api.series.models.PriceScaleId
import com.tradingview.lightweightcharts.example.app.databinding.LayoutThemesChartFragmentBinding
import com.tradingview.lightweightcharts.example.app.model.Data
import com.tradingview.lightweightcharts.example.app.viewmodel.CustomThemesViewModel
import com.tradingview.lightweightcharts.view.ChartsView

class CustomThemesFragment : Fragment() {

    private lateinit var viewModel: CustomThemesViewModel

    private lateinit var binding: LayoutThemesChartFragmentBinding

    private var series: MutableList<SeriesApi> = mutableListOf()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        return LayoutThemesChartFragmentBinding.inflate(inflater, container, false)
            .also { binding = it }.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        provideViewModel()
        observeViewModelData()
        subscribeOnChartReady(binding.chartsView)
        applyChartOptions()
        binding.darkThemeBtn.setOnClickListener { applyThemeOptions(darkThemeOptions) }
        binding.lightThemeBtn.setOnClickListener { applyThemeOptions(lightThemeOptions) }
    }

    private fun provideViewModel() {
        viewModel = ViewModelProvider(this).get(CustomThemesViewModel::class.java)
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

    private val darkThemeOptions: ChartOptions.() -> Unit = {
        layout = layoutOptions {
            background = SolidColor(Color.parseColor("#2B2B43").toIntColor())
            textColor = Color.parseColor("#D9D9D9").toIntColor()
        }
        watermark = watermarkOptions {
            color = Color.argb(0, 0, 0, 0).toIntColor()
        }
        crosshair = crosshairOptions {
            vertLine = crosshairLineOptions {
                color = Color.parseColor("#758696").toIntColor()
                labelBackgroundColor = Color.parseColor("#758696").toIntColor()
            }
            horzLine = crosshairLineOptions {
                color = Color.parseColor("#758696").toIntColor()
                labelBackgroundColor = Color.parseColor("#758696").toIntColor()
            }
        }
        grid = gridOptions {
            vertLines = gridLineOptions {
                color = Color.parseColor("#2B2B43").toIntColor()
            }
            horzLines = gridLineOptions {
                color = Color.parseColor("#363C4E").toIntColor()
            }
        }
    }

    private val lightThemeOptions: ChartOptions.() -> Unit = {
        layout = layoutOptions {
            background = SolidColor(Color.WHITE.toIntColor())
            textColor = Color.parseColor("#191919").toIntColor()
        }
        watermark = watermarkOptions {
            color = Color.argb(0, 0, 0, 0).toIntColor()
        }
        grid = gridOptions {
            vertLines = gridLineOptions {
                visible = false
            }
            horzLines = gridLineOptions {
                color = Color.parseColor("#f0f3fa").toIntColor()
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
                topColor = Color.argb(143, 33, 150, 243).toIntColor(),
                bottomColor = Color.argb(10, 33, 150, 243).toIntColor(),
                lineColor = Color.argb(204, 33, 150, 243).toIntColor(),
                lineWidth = LineWidth.TWO,
            ),
            onSeriesCreated = { api ->
                api.setData(data.list)
                onSeriesCreated(api)
            }
        )
    }

    private fun applyChartOptions() {
        applyThemeOptions(darkThemeOptions)
    }

    private fun applyThemeOptions(theme: ChartOptions.() -> Unit) {
        binding.chartsView.api.applyOptions {
            theme.invoke(this)
        }
    }
}