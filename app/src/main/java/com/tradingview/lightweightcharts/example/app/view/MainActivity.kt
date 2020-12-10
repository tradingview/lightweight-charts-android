package com.tradingview.lightweightcharts.example.app.view

import android.os.Bundle
import android.view.*
import android.webkit.WebView
import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.drawerlayout.widget.DrawerLayout
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.google.android.material.navigation.NavigationView
import com.tradingview.lightweightcharts.api.interfaces.ChartApi
import com.tradingview.lightweightcharts.api.interfaces.SeriesApi
import com.tradingview.lightweightcharts.api.options.models.*
import com.tradingview.lightweightcharts.api.series.common.SeriesData
import com.tradingview.lightweightcharts.api.series.enums.*
import com.tradingview.lightweightcharts.api.series.models.*
import com.tradingview.lightweightcharts.example.app.*
import com.tradingview.lightweightcharts.example.app.model.Data
import com.tradingview.lightweightcharts.example.app.model.SeriesDataType
import com.tradingview.lightweightcharts.example.app.plugins.AutoscaleInfoProvider
import com.tradingview.lightweightcharts.example.app.plugins.TickMarkFormatter
import com.tradingview.lightweightcharts.example.app.viewmodel.*
import com.tradingview.lightweightcharts.runtime.plugins.DateTimeFormat
import com.tradingview.lightweightcharts.runtime.plugins.Eval
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

    private var leftSeries: MutableList<SeriesApi<SeriesData>> = mutableListOf()
    private var rightSeries: MutableList<SeriesApi<SeriesData>> = mutableListOf()
    private var realtimeDataJob: Job? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        WebView.setWebContentsDebuggingEnabled(true)
        setContentView(R.layout.activity_main)
        initializeNavigationDrawer()

        viewModel = ViewModelProvider(this).get(MainViewModel::class.java)

        viewModel.seriesData.observe(this, { data ->
            setSeriesData(data, PriceScaleId.LEFT, firstChartApi) {
                leftSeries.add(it as SeriesApi<SeriesData>)
            }
            setSeriesData(data, PriceScaleId.RIGHT, secondChartApi) {
                rightSeries.add(it as SeriesApi<SeriesData>)
            }
        })

        subscribeOnChartReady(charts_view)
        subscribeOnChartReady(charts_view_second)

        firstChartApi.applyOptions {
            layout = layoutOptions {
                backgroundColor = "#eeeeee"
                textColor = "#000000"
            }
            grid = gridOptions {
                vertLines = gridLineOptions {
                    color = "#c2c2c2"
                }
                horzLines = gridLineOptions {
                    color = "#c2c2c2"
                }
            }
            rightPriceScale = priceScaleOptions {
                visible = false
            }
            leftPriceScale = priceScaleOptions {
                visible = true
                autoScale = true
                scaleMargins = priceScaleMargins {
                    top = 0.2f
                    bottom = 0.2f
                }
            }
            crosshair = crosshairOptions {
                mode = CrosshairMode.NORMAL
                vertLine = crosshairLineOptions {
                    color = "#555555"
                    labelBackgroundColor = "#555555"
                }
                horzLine = crosshairLineOptions {
                    color = "#555555"
                    labelBackgroundColor = "#555555"
                }
            }
            handleScroll = handleScrollOptions {
                horzTouchDrag = true
                vertTouchDrag = false
            }
            handleScale = handleScaleOptions {
                axisPressedMouseMove = axisPressedMouseMoveOptions {
                    time = true
                    price = false
                }
            }
            timeScale = timeScaleOptions {
                tickMarkFormatter = TickMarkFormatter()
            }
            localization = localizationOptions {
                locale = "ru-RU"
                //priceFormatter = Eval("function() { return 123 }")
                priceFormatter = PriceFormatter(template = "{price:#2:#3}$")
                timeFormatter = TimeFormatter(
                    locale = "ru-RU",
                    dateTimeFormat = DateTimeFormat.DATE_TIME
                )
            }
        }
    }

    private fun setSeriesData(
        data: Data,
        priceScale: PriceScaleId,
        chartApi: ChartApi,
        onSeriesCreated: (SeriesApi<*>) -> Unit
    ) {
        when (data.type) {
            SeriesDataType.AREA -> chartApi.addAreaSeries(
                options = AreaSeriesOptions(
                    priceFormat = PriceFormat.priceFormatCustom(
                        PriceFormatter("{price}$!"),
                        0.02f
                    ),
                    priceScaleId = priceScale,
                    autoscaleInfoProvider = AutoscaleInfoProvider()
                ),
                onSeriesCreated = { api ->
                    api.setData(data.list.map { it as LineData })
                    api.setMarkers(
                        listOf(
                            SeriesMarker(
                                time = data.list[0].time,
                                position = SeriesMarkerPosition.ABOVE_BAR,
                                color = "black",
                                shape = SeriesMarkerShape.ARROW_DOWN,
                                text = "Example",
                                size = 2
                            )
                        )
                    )
                    api.createPriceLine(
                        PriceLineOptions(
                            price = 44.1f,
                            color = "green",
                            lineWidth = LineWidth.TWO,
                            lineStyle = LineStyle.Solid,
                            axisLabelVisible = true,
                            title = "P/L 500"
                        )
                    )
                    onSeriesCreated(api)
                }
            )

            SeriesDataType.LINE -> chartApi.addLineSeries(
                options = LineSeriesOptions(
                    priceScaleId = priceScale
                ),
                onSeriesCreated = { api ->
                    api.setData(data.list.map { it as LineData })
                    onSeriesCreated(api)
                }
            )

            SeriesDataType.BAR -> chartApi.addBarSeries(
                options = BarSeriesOptions(
                    priceScaleId = priceScale
                ),
                onSeriesCreated = { api ->
                    api.setData(data.list.map { it as BarData })
                    onSeriesCreated(api)
                }
            )

            SeriesDataType.CANDLESTICK -> chartApi.addCandlestickSeries(
                options = CandlestickSeriesOptions(
                    priceScaleId = priceScale
                ),
                onSeriesCreated = { api ->
                    api.setData(data.list.map { it as BarData })
                    onSeriesCreated(api)
                }
            )

            SeriesDataType.HISTOGRAM -> chartApi.addHistogramSeries(
                options = HistogramSeriesOptions(
                    priceScaleId = priceScale
                ),
                onSeriesCreated = { api ->
                    api.setData(data.list.map { it as HistogramData })
                    onSeriesCreated(api)
                }
            )
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.data_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when {
            item.itemId == R.id.static_data -> {
                lifecycleScope.launchWhenResumed {
                    realtimeDataJob?.cancelAndJoin()
                    viewModel.selectSeries(SeriesDataType.AREA)
                }
                return true
            }
            item.itemId == R.id.real_time_data -> {
                clearSeries()
                viewModel.selectSeries(SeriesDataType.AREA)
                realtimeDataJob = lifecycleScope.launchWhenResumed {
                    viewModel.seriesFlow.collect {
                        leftSeries.lastOrNull()?.update(it)
                        rightSeries.lastOrNull()?.update(it)
                    }
                }
                return true
            }
            item.itemId == R.id.clear_series -> {
                lifecycleScope.launchWhenResumed {
                    clearSeries()
                    realtimeDataJob?.cancelAndJoin()
                }
                return true
            }
            actionBar.onOptionsItemSelected(item) -> return true
            else -> return super.onOptionsItemSelected(item)
        }
    }

    private fun clearSeries() {
        leftSeries.forEach(firstChartApi::removeSeries)
        leftSeries.clear()

        rightSeries.forEach(secondChartApi::removeSeries)
        rightSeries.clear()
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