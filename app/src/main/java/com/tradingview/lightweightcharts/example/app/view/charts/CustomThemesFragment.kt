package com.tradingview.lightweightcharts.example.app.view.charts

import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.tradingview.lightweightcharts.api.chart.models.color.toIntColor
import com.tradingview.lightweightcharts.api.interfaces.ChartApi
import com.tradingview.lightweightcharts.api.interfaces.SeriesApi
import com.tradingview.lightweightcharts.api.options.models.*
import com.tradingview.lightweightcharts.api.series.enums.LineWidth
import com.tradingview.lightweightcharts.api.series.models.PriceScaleId
import com.tradingview.lightweightcharts.example.app.R
import com.tradingview.lightweightcharts.example.app.model.Data
import com.tradingview.lightweightcharts.example.app.viewmodel.CustomThemesViewModel
import com.tradingview.lightweightcharts.view.ChartsView
import kotlinx.android.synthetic.main.layout_chart_fragment.charts_view
import kotlinx.android.synthetic.main.layout_themes_chart_fragment.*

class CustomThemesFragment: Fragment() {

    private lateinit var viewModel: CustomThemesViewModel

    private var series: MutableList<SeriesApi> = mutableListOf()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.layout_themes_chart_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        provideViewModel()
        observeViewModelData()
        subscribeOnChartReady(charts_view)
        applyChartOptions()
        dark_theme_btn.setOnClickListener { applyThemeOptions(darkThemeOptions) }
        light_theme_btn.setOnClickListener { applyThemeOptions(lightThemeOptions) }
    }

    private fun provideViewModel() {
        viewModel = ViewModelProvider(this).get(CustomThemesViewModel::class.java)
    }

    private fun observeViewModelData() {
        viewModel.seriesData.observe(viewLifecycleOwner, { data ->
            createSeriesWithData(data, PriceScaleId.RIGHT, charts_view.api) { series ->
                this.series.clear()
                this.series.add(series)
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

    private val darkThemeOptions: ChartOptions.() -> Unit = {
        layout = layoutOptions {
            backgroundColor = Color.parseColor("#2B2B43").toIntColor()
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
            backgroundColor = Color.WHITE.toIntColor()
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
            onSeriesCreated: (SeriesApi) -> Unit
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
        charts_view.api.applyOptions {
            theme.invoke(this)
        }
    }
}