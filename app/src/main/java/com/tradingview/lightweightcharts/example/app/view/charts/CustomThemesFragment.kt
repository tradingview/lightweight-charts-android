package com.tradingview.lightweightcharts.example.app.view.charts

import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.LinearLayout
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.tradingview.lightweightcharts.api.interfaces.ChartApi
import com.tradingview.lightweightcharts.api.interfaces.SeriesApi
import com.tradingview.lightweightcharts.api.options.models.*
import com.tradingview.lightweightcharts.api.series.enums.LineWidth
import com.tradingview.lightweightcharts.api.series.models.PriceScaleId
import com.tradingview.lightweightcharts.example.app.R
import com.tradingview.lightweightcharts.example.app.model.Data
import com.tradingview.lightweightcharts.example.app.viewmodel.BaseViewModel
import com.tradingview.lightweightcharts.example.app.viewmodel.CustomThemesViewModel
import com.tradingview.lightweightcharts.view.ChartsView
import kotlinx.android.synthetic.main.layout_chart_fragment.*

class CustomThemesFragment: Fragment() {

    companion object {
        const val BUTTON_WIDTH = 360
        const val BUTTON_HEIGHT = 180
    }

    private lateinit var viewModel: CustomThemesViewModel

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
        viewModel = ViewModelProvider(this).get(CustomThemesViewModel::class.java)
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

    private val darkThemeOptions: ChartOptions.() -> Unit = {
        layout = layoutOptions {
            backgroundColor = Color.parseColor("#2B2B43")
            textColor = Color.parseColor("#D9D9D9")
        }
        watermark = watermarkOptions {
            color = Color.argb(0, 0, 0, 0)
        }
        crosshair = crosshairOptions {
            vertLine = crosshairLineOptions {
                color = Color.parseColor("#758696")
                labelBackgroundColor = Color.parseColor("#758696")
            }
            horzLine = crosshairLineOptions {
                color = Color.parseColor("#758696")
                labelBackgroundColor = Color.parseColor("#758696")
            }
        }
        grid = gridOptions {
            vertLines = gridLineOptions {
                color = Color.parseColor("#2B2B43")
            }
            horzLines = gridLineOptions {
                color = Color.parseColor("#363C4E")
            }
        }
    }

    private val lightThemeOptions: ChartOptions.() -> Unit = {
        layout = layoutOptions {
            backgroundColor = Color.WHITE
            textColor = Color.parseColor("#191919")
        }
        watermark = watermarkOptions {
            color = Color.argb(0, 0, 0, 0)
        }
        grid = gridOptions {
            vertLines = gridLineOptions {
                visible = false
            }
            horzLines = gridLineOptions {
                color = Color.parseColor("#f0f3fa")
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
                        topColor = Color.argb(143, 33, 150, 243),
                        bottomColor = Color.argb(10, 33, 150, 243),
                        lineColor = Color.argb(204, 33, 150, 243),
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

    private fun enableButtons(view: View) {
        mapOf(
                "Dark" to darkThemeOptions,
                "Light" to lightThemeOptions
        ).forEach {
            createButton(it.key) { applyThemeOptions(it.value) }
        }
    }

    protected fun createButton(buttonText: String, onClick: () -> Unit) {

        if (switcher.visibility != View.VISIBLE) {
            switcher.visibility = View.VISIBLE
        }

        val button = Button(context).apply {
            layoutParams = ViewGroup.LayoutParams(BUTTON_WIDTH, BUTTON_HEIGHT)
            text = buttonText
            setOnClickListener { onClick.invoke() }
        }

        switcher.addView(button)
    }

    private fun applyThemeOptions(theme: ChartOptions.() -> Unit) {
        chartApi.applyOptions {
            theme.invoke(this)
        }
    }
}