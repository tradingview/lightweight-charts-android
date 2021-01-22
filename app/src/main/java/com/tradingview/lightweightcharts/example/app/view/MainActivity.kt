package com.tradingview.lightweightcharts.example.app.view

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
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
import com.tradingview.lightweightcharts.api.options.enums.HorizontalAlignment
import com.tradingview.lightweightcharts.api.options.enums.VerticalAlignment
import com.tradingview.lightweightcharts.api.options.models.*
import com.tradingview.lightweightcharts.api.series.enums.*
import com.tradingview.lightweightcharts.api.series.models.*
import com.tradingview.lightweightcharts.example.app.R
import com.tradingview.lightweightcharts.example.app.model.Data
import com.tradingview.lightweightcharts.example.app.model.SeriesDataType
import com.tradingview.lightweightcharts.example.app.plugins.AutoscaleInfoProvider
import com.tradingview.lightweightcharts.example.app.plugins.TickMarkFormatter
import com.tradingview.lightweightcharts.example.app.viewmodel.MainViewModel
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

    private var leftSeries: MutableList<SeriesApi> = mutableListOf()
    private var rightSeries: MutableList<SeriesApi> = mutableListOf()
    private var realtimeDataJob: Job? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        WebView.setWebContentsDebuggingEnabled(true)
        setContentView(R.layout.activity_main)
        initializeNavigationDrawer()

        viewModel = ViewModelProvider(this).get(MainViewModel::class.java)

        viewModel.seriesData.observe(this, { data ->
            createSeriesWithData(data, PriceScaleId.LEFT, firstChartApi) { series ->
                leftSeries.forEach(firstChartApi::removeSeries)
                leftSeries.clear()
                leftSeries.add(series)
            }
            createSeriesWithData(data, PriceScaleId.RIGHT, secondChartApi) { series ->
                rightSeries.forEach(secondChartApi::removeSeries)
                rightSeries.clear()
                rightSeries.add(series)
            }
        })

        subscribeOnChartReady(charts_view)
        subscribeOnChartReady(charts_view_second)

        firstChartApi.applyOptions {
            layout = layoutOptions {
                backgroundColor = Color.LTGRAY
                textColor = Color.BLACK
            }
            grid = gridOptions {
                vertLines = gridLineOptions {
                    color = Color.rgb(0xA0, 0xA0, 0xA0)
                }
                horzLines = gridLineOptions {
                    //aRGB color
                    color = 0xFFB0B0B0.toInt()
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
                    color = Color.DKGRAY
                    labelBackgroundColor = Color.DKGRAY
                }
                horzLine = crosshairLineOptions {
                    color = Color.DKGRAY
                    labelBackgroundColor = Color.DKGRAY
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
            watermark = watermarkOptions {
                visible = true
                color = Color.argb(102, 11, 94, 29)
                text = "TradingView Watermark Example"
                fontSize = 24
                fontStyle = "italic"
                horzAlign = HorizontalAlignment.LEFT
                vertAlign = VerticalAlignment.TOP
            }
        }
    }


    private fun createSeriesWithData(
        data: Data,
        priceScale: PriceScaleId,
        chartApi: ChartApi,
        onSeriesCreated: (SeriesApi) -> Unit
    ) {
        when (data.type) {
            SeriesDataType.AREA -> chartApi.addAreaSeries(
                options = AreaSeriesOptions(
                    priceFormat = PriceFormat.priceFormatCustom(
                        PriceFormatter("{price}$!"),
                        0.02f
                    ),
                    priceScaleId = priceScale,
                    autoscaleInfoProvider = AutoscaleInfoProvider(),
                    priceLineSource = PriceLineSource.LAST_BAR
                ),
                onSeriesCreated = { api ->
                    api.setData(data.list.map { it as LineData })
                    api.setMarkers(
                        listOf(
                            SeriesMarker(
                                time = data.list[0].time,
                                position = SeriesMarkerPosition.ABOVE_BAR,
                                color = Color.BLACK,
                                shape = SeriesMarkerShape.ARROW_DOWN,
                                text = "Example",
                                size = 2
                            )
                        )
                    )
                    val options = PriceLineOptions(
                        price = 44.1f,
                        //css color green
                        color = Color.rgb(0,128,0),
                        lineWidth = LineWidth.TWO,
                        lineStyle = LineStyle.SOLID,
                        axisLabelVisible = true,
                        title = "P/L 500"
                    )
                    val priceLine = api.createPriceLine(options)
                    api.removePriceLine(priceLine)
                    onSeriesCreated(api)
                }
            )

            SeriesDataType.LINE -> chartApi.addLineSeries(
                options = LineSeriesOptions(
                    priceScaleId = priceScale
                ),
                onSeriesCreated = { api ->
                    api.setData(data.list)
                    onSeriesCreated(api)
                }
            )

            SeriesDataType.BAR -> chartApi.addBarSeries(
                options = BarSeriesOptions(
                    priceScaleId = priceScale
                ),
                onSeriesCreated = { api ->
                    api.setData(data.list)
                    onSeriesCreated(api)
                }
            )

            SeriesDataType.CANDLESTICK -> chartApi.addCandlestickSeries(
                options = CandlestickSeriesOptions(
                    priceScaleId = priceScale
                ),
                onSeriesCreated = { api ->
                    api.setData(data.list)
                    onSeriesCreated(api)
                }
            )

            SeriesDataType.HISTOGRAM -> chartApi.addHistogramSeries(
                options = HistogramSeriesOptions(
                    priceScaleId = priceScale
                ),
                onSeriesCreated = { api ->
                    api.setData(data.list)
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
                    R.id.menu_bar_chart -> viewModel.selectSeries(SeriesDataType.BAR)
                    R.id.menu_custom_price_formatter -> viewModel.selectSeries(SeriesDataType.BAR)
                    R.id.menu_custom_themes -> viewModel.selectSeries(SeriesDataType.HISTOGRAM)
                    R.id.menu_floating_tooltip -> viewModel.selectSeries(SeriesDataType.CANDLESTICK)
                    R.id.menu_volume_study -> viewModel.selectSeries(SeriesDataType.LINE)
                    R.id.menu_real_time_emulation -> viewModel.selectSeries(SeriesDataType.LINE)
                    R.id.menu_series_markers -> viewModel.selectSeries(SeriesDataType.LINE)
                    R.id.menu_price_lines_with_titles -> viewModel.selectSeries(SeriesDataType.LINE)
                    R.id.menu_view_pager -> {
                        startActivity(Intent(this@MainActivity, ViewPagerActivity::class.java))
                    }
                }
            }
            drawer.closeDrawers()
            true
        }
    }
}