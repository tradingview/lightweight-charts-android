package com.tradingview.lightweightcharts.example.app.view.charts

import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.View.VISIBLE
import android.view.ViewGroup
import android.widget.Button
import android.widget.LinearLayout
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.tradingview.lightweightcharts.api.interfaces.ChartApi
import com.tradingview.lightweightcharts.api.interfaces.SeriesApi
import com.tradingview.lightweightcharts.api.options.models.*
import com.tradingview.lightweightcharts.api.series.enums.*
import com.tradingview.lightweightcharts.api.series.models.LineData
import com.tradingview.lightweightcharts.api.series.models.PriceFormat
import com.tradingview.lightweightcharts.api.series.models.PriceScaleId
import com.tradingview.lightweightcharts.api.series.models.SeriesMarker
import com.tradingview.lightweightcharts.example.app.R
import com.tradingview.lightweightcharts.example.app.model.Data
import com.tradingview.lightweightcharts.example.app.plugins.AutoscaleInfoProvider
import com.tradingview.lightweightcharts.example.app.viewmodel.BaseViewModel
import com.tradingview.lightweightcharts.example.app.viewmodel.CustomPriceFormatterViewModel
import com.tradingview.lightweightcharts.runtime.plugins.PriceFormatter
import com.tradingview.lightweightcharts.view.ChartsView
import kotlinx.android.synthetic.main.layout_chart_fragment.*

class CustomPriceFormatterFragment: Fragment() {

    companion object {
        const val BUTTON_WIDTH = 360
        const val BUTTON_HEIGHT = 180
    }

    private lateinit var viewModel: CustomPriceFormatterViewModel

    private val chartApi: ChartApi by lazy { charts_view.api }
    private var series: MutableList<SeriesApi> = mutableListOf()
    private val switcher: LinearLayout by lazy { switcher_ll }

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
        enableButtons(view)
    }

    private fun provideViewModel() {
        viewModel = ViewModelProvider(this).get(CustomPriceFormatterViewModel::class.java)
    }

    private fun observeViewModelData() {
        viewModel.seriesData.observe(this, { data ->
            createSeriesWithData(data, PriceScaleId.RIGHT, chartApi) { series ->
                this.series.forEach(chartApi::removeSeries)
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

    private fun applyChartOptions() {
        chartApi.applyOptions {
            layout = layoutOptions {
                backgroundColor = Color.BLACK
                textColor = Color.argb(204, 255, 255, 255)
            }
            localization = localizationOptions {
                priceFormatter = PriceFormatter(template = "\${price:#2:#2}")
            }
            crosshair = crosshairOptions {
                mode = CrosshairMode.NORMAL
            }
            rightPriceScale = priceScaleOptions {
                borderColor = Color.argb(204, 255, 255, 255)
            }
            timeScale = timeScaleOptions {
                borderColor = Color.argb(204, 255, 255, 255)
            }
            grid = gridOptions {
                vertLines = gridLineOptions {
                    color = Color.argb(51, 255, 255, 255)
                }
                horzLines = gridLineOptions {
                    color = Color.argb(51, 255, 255, 255)
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
        chartApi.addAreaSeries(
            options = AreaSeriesOptions(
                topColor = Color.argb(128, 21, 101, 192),
                bottomColor = Color.argb(128, 21, 101, 192),
                lineColor = Color.argb(204, 255, 255, 255),
                lineWidth = LineWidth.TWO,
            ),
            onSeriesCreated = { api ->
                api.setData(data.list)
                onSeriesCreated(api)
            }
        )
    }

    private fun enableButtons(view: View) {
        mapOf(
                "Dollar" to "\${price:#2:#2}",
                "Pound" to "\u00A3{price:#2:#2}"
        ).forEach {
            createButton(it.key) { applyPriceFormat(it.value) }
        }
    }

    private fun createButton(buttonText: String, onClick: () -> Unit) {

        if (switcher.visibility != VISIBLE) {
            switcher.visibility = VISIBLE
        }

        val button = Button(context).apply {
            layoutParams = ViewGroup.LayoutParams(BUTTON_WIDTH, BUTTON_HEIGHT)
            text = buttonText
            setOnClickListener { onClick.invoke() }
        }

        switcher.addView(button)
    }

    private fun applyPriceFormat(template: String) {
        chartApi.applyOptions {
            localization = localizationOptions {
                priceFormatter = PriceFormatter(template = template)
            }
        }
    }
}