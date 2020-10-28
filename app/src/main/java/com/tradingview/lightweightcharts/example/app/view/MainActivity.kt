package com.tradingview.lightweightcharts.example.app.view

import android.os.Bundle
import android.view.*
import android.webkit.WebView
import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.drawerlayout.widget.DrawerLayout
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.google.android.material.navigation.NavigationView
import com.tradingview.lightweightcharts.api.interfaces.ChartApi
import com.tradingview.lightweightcharts.api.interfaces.SeriesApi
import com.tradingview.lightweightcharts.api.options.models.*
import com.tradingview.lightweightcharts.api.series.common.SeriesData
import com.tradingview.lightweightcharts.api.series.enums.CrosshairMode
import com.tradingview.lightweightcharts.api.series.models.BarData
import com.tradingview.lightweightcharts.api.series.models.HistogramData
import com.tradingview.lightweightcharts.api.series.models.LineData
import com.tradingview.lightweightcharts.api.series.models.PriceFormat
import com.tradingview.lightweightcharts.example.app.*
import com.tradingview.lightweightcharts.example.app.model.Data
import com.tradingview.lightweightcharts.example.app.model.SeriesDataType
import com.tradingview.lightweightcharts.example.app.viewmodel.*
import com.tradingview.lightweightcharts.runtime.plugins.DateTimeFormat
import com.tradingview.lightweightcharts.runtime.plugins.PriceFormatter
import com.tradingview.lightweightcharts.runtime.plugins.TimeFormatter
import com.tradingview.lightweightcharts.view.ChartsView
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.coroutines.Job
import kotlinx.coroutines.cancelAndJoin
import kotlinx.coroutines.flow.collect

class MainActivity : AppCompatActivity() {
    private lateinit var viewModel: MainViewModel

    private lateinit var drawer: DrawerLayout
    private lateinit var actionBar: ActionBarDrawerToggle
    private lateinit var navigationView: NavigationView
    private val context: MainActivity = this
    private val firstChartApi: ChartApi by lazy { charts_view.api }
    private val secondChartApi: ChartApi by lazy { charts_view_second.api }

    private var currentSeriesApiFirstChart: SeriesApi<SeriesData>? = null
    private var currentSeriesApiSecondChart: SeriesApi<SeriesData>? = null
    private var realtimeDataJob: Job? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        WebView.setWebContentsDebuggingEnabled(true)
        setContentView(R.layout.activity_main)
        initializeNavigationDrawer()

        viewModel = ViewModelProvider(this).get(MainViewModel::class.java)

        viewModel.seriesData.observe(this, Observer { data ->
            currentSeriesApiFirstChart = setSeriesData(data, currentSeriesApiFirstChart, firstChartApi)
            currentSeriesApiSecondChart = setSeriesData(data, currentSeriesApiSecondChart, secondChartApi)
        })

        subscribeOnChartReady(charts_view)
        subscribeOnChartReady(charts_view_second)

        firstChartApi.applyOptions(
            ChartOptions(
                layout = LayoutOptions(
                    backgroundColor = "#eeeeee",
                    textColor = "#000000"
                ),
                grid = GridOptions(
                    GridLineOptions(
                        "#c2c2c2"
                    ),
                    GridLineOptions(
                        "#c2c2c2"
                    )
                ),
                priceScale = PriceScaleOptions(
                    autoScale = true,
                    scaleMargins = PriceScaleMargins(
                        top = 0.2f, bottom = 0.2f
                    ),
                    borderVisible = false
                ),
                timeScale = TimeScaleOptions(
                    borderVisible = false,
                    timeVisible = true,
                    secondsVisible = true
                ),
                crosshair = CrosshairOptions(
                    CrosshairMode.NORMAL,
                    CrosshairLineOptions(
                        color = "#555555",
                        labelBackgroundColor = "#555555"
                    ),
                    CrosshairLineOptions(
                        color = "#555555",
                        labelBackgroundColor = "#555555"
                    )
                ),
                handleScroll = HandleScrollOptions(
                    horzTouchDrag = true,
                    vertTouchDrag = false
                ),
                localization = LocalizationOptions(
                    locale = "ru-RU",
                    priceFormatter = PriceFormatter(template = "{price}$"),
                    timeFormatter = TimeFormatter(
                        locale = "ru-RU",
                        dateTimeFormat = DateTimeFormat.DATE_TIME
                    )
                )
            )
        )
    }

    private fun setSeriesData(data: Data, currentSeries: SeriesApi<*>?, chartApi: ChartApi): SeriesApi<SeriesData> {
        currentSeries?.let(chartApi::removeSeries)

        when (data.type) {
            SeriesDataType.AREA -> {
                val seriesApi = chartApi.addAreaSeries(AreaSeriesOptions(
                    priceFormat = PriceFormat.priceFormatBuiltIn(PriceFormat.Type.VOLUME, 1, 0.02f)
                ))
                seriesApi.setData(data.list.map { it as LineData })
                return seriesApi as SeriesApi<SeriesData>
            }
            SeriesDataType.LINE -> {
                val seriesApi = chartApi.addLineSeries()
                seriesApi.setData(data.list.map { it as LineData })
                return seriesApi as SeriesApi<SeriesData>
            }
            SeriesDataType.BAR -> {
                val seriesApi = chartApi.addBarSeries()
                seriesApi.setData(data.list.map { it as BarData })
                return seriesApi as SeriesApi<SeriesData>
            }
            SeriesDataType.CANDLESTICK -> {
                val seriesApi = chartApi.addCandlestickSeries()
                seriesApi.setData(data.list.map { it as BarData })
                return seriesApi as SeriesApi<SeriesData>
            }
            SeriesDataType.HISTOGRAM -> {
                val seriesApi = chartApi.addHistogramSeries()
                seriesApi.setData(data.list.map { it as HistogramData })
                return seriesApi as SeriesApi<SeriesData>
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.data_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when  {
            item.itemId == R.id.static_data -> {
                lifecycleScope.launchWhenResumed {
                    realtimeDataJob?.cancelAndJoin()
                    viewModel.selectSeries(SeriesDataType.AREA)
                }
                return true
            }
            item.itemId == R.id.real_time_data -> {
                viewModel.selectSeries(SeriesDataType.AREA)
                realtimeDataJob = lifecycleScope.launchWhenResumed {
                    viewModel.seriesFlow.collect {
                        currentSeriesApiFirstChart?.update(it)
                        currentSeriesApiSecondChart?.update(it)
                    }
                }
                return true
            }
            actionBar.onOptionsItemSelected(item) -> return true
            else -> return super.onOptionsItemSelected(item)
        }
    }

    private fun subscribeOnChartReady(view: ChartsView) {
        view.subscribeOnChartStateChange { state ->
            when (state) {
                is ChartsView.State.Preparing -> Unit
                is ChartsView.State.Ready -> {
                    Toast.makeText(this, "Chart ${view.id} is ready", Toast.LENGTH_SHORT).show()
                }
                is ChartsView.State.Error -> {
                    Toast.makeText(this, state.exception.localizedMessage, Toast.LENGTH_LONG).show()
                }
            }
        }
    }

    private fun initializeNavigationDrawer() {
        drawer = findViewById(R.id.drawer_layout)
        actionBar = ActionBarDrawerToggle(context, drawer_layout, R.string.open, R.string.close)
        navigationView = findViewById(R.id.navigation_view)

        drawer.addDrawerListener(actionBar)
        actionBar.syncState()
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        navigationView.setNavigationItemSelectedListener { menuItem ->
            lifecycleScope.launchWhenResumed {
                realtimeDataJob?.cancelAndJoin()
                viewModel.loadData()
                when (menuItem.itemId) {
                    R.id.menu_area_series -> viewModel.selectSeries(SeriesDataType.AREA)
                    R.id.menu_bar_prices -> viewModel.selectSeries(SeriesDataType.BAR)
                    R.id.menu_histogram_time -> viewModel.selectSeries(SeriesDataType.HISTOGRAM)
                    R.id.menu_candlestick_time -> viewModel.selectSeries(SeriesDataType.CANDLESTICK)
                    R.id.menu_line_time -> viewModel.selectSeries(SeriesDataType.LINE)
                }
            }
            drawer.closeDrawers()
            true
        }
    }
}