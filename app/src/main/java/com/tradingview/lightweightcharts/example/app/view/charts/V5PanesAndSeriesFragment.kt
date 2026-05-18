package com.tradingview.lightweightcharts.example.app.view.charts

import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.tradingview.lightweightcharts.api.chart.models.color.surface.SolidColor
import com.tradingview.lightweightcharts.api.chart.models.color.toIntColor
import com.tradingview.lightweightcharts.api.interfaces.ChartApi
import com.tradingview.lightweightcharts.api.interfaces.PaneApi
import com.tradingview.lightweightcharts.api.interfaces.SeriesApi
import com.tradingview.lightweightcharts.api.interfaces.pop
import com.tradingview.lightweightcharts.api.options.enums.PriceScaleSide
import com.tradingview.lightweightcharts.api.options.models.CandlestickSeriesOptions
import com.tradingview.lightweightcharts.api.options.models.HistogramSeriesOptions
import com.tradingview.lightweightcharts.api.options.models.LineSeriesOptions
import com.tradingview.lightweightcharts.api.options.models.crosshairOptions
import com.tradingview.lightweightcharts.api.options.models.gridLineOptions
import com.tradingview.lightweightcharts.api.options.models.gridOptions
import com.tradingview.lightweightcharts.api.options.models.layoutOptions
import com.tradingview.lightweightcharts.api.options.models.priceScaleOptions
import com.tradingview.lightweightcharts.api.options.models.timeScaleOptions
import com.tradingview.lightweightcharts.api.series.enums.CrosshairMode
import com.tradingview.lightweightcharts.api.series.enums.LineWidth
import com.tradingview.lightweightcharts.api.series.enums.SeriesType
import com.tradingview.lightweightcharts.api.series.models.CandlestickData
import com.tradingview.lightweightcharts.api.series.models.HistogramData
import com.tradingview.lightweightcharts.api.series.models.LineData
import com.tradingview.lightweightcharts.api.series.models.PriceFormat
import com.tradingview.lightweightcharts.api.series.models.PriceScaleId
import com.tradingview.lightweightcharts.api.series.models.Time
import com.tradingview.lightweightcharts.example.app.R
import com.tradingview.lightweightcharts.example.app.databinding.FragmentV5ShowcaseBinding
import com.tradingview.lightweightcharts.example.app.view.util.ITitleFragment
import com.tradingview.lightweightcharts.view.ChartsView
import kotlin.math.abs
import kotlin.math.sin

class V5PanesAndSeriesFragment : Fragment(), ITitleFragment {
    override val fragmentTitleRes = R.string.v5_panes_and_series

    private lateinit var binding: FragmentV5ShowcaseBinding
    private lateinit var candleSeries: SeriesApi
    private lateinit var movingAverageSeries: SeriesApi
    private lateinit var volumeSeries: SeriesApi
    private lateinit var leftScaleSeries: SeriesApi
    private var preservedPane: PaneApi? = null
    private var isReady = false
    private var movingAveragePaneIndex = MAIN_PANE_INDEX
    private var movingAverageOnTop = true
    private var preservedPaneCompact = true

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        return FragmentV5ShowcaseBinding.inflate(inflater, container, false)
            .also { binding = it }.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        configureActions()
        binding.tvDebugValues.text = getString(R.string.v5_waiting_for_chart)
        binding.chartsView.subscribeOnChartStateChange { state ->
            when (state) {
                is ChartsView.State.Ready -> if (!isReady) {
                    isReady = true
                    setupChart(binding.chartsView.api)
                }
                is ChartsView.State.Error -> showStatus(state.exception.localizedMessage.orEmpty())
                is ChartsView.State.Preparing -> Unit
            }
        }
    }

    private fun configureActions() {
        binding.chipPrimary.text = getString(R.string.v5_move_series)
        binding.chipSecondary.text = getString(R.string.v5_series_order)
        binding.chipTertiary.text = getString(R.string.v5_pane_size)
        binding.chipQuaternary.text = getString(R.string.v5_pop)

        binding.chipPrimary.setOnClickListener {
            if (::movingAverageSeries.isInitialized) {
                val nextTargetIndex = (movePaneTargets.indexOfFirst { it.index == movingAveragePaneIndex } + 1)
                    .floorMod(movePaneTargets.size)
                val nextTarget = movePaneTargets[nextTargetIndex]
                movingAveragePaneIndex = nextTarget.index
                movingAverageSeries.moveToPane(nextTarget.index)
                showStatus("MA moved to pane ${nextTarget.index}: ${nextTarget.label}")
            }
        }
        binding.chipSecondary.setOnClickListener {
            if (::movingAverageSeries.isInitialized) {
                movingAverageOnTop = !movingAverageOnTop
                movingAverageSeries.setSeriesOrder(if (movingAverageOnTop) TOP_SERIES_ORDER else BOTTOM_SERIES_ORDER)
                movingAverageSeries.seriesOrder { order -> showStatus("MA series order: $order") }
            }
        }
        binding.chipTertiary.setOnClickListener {
            binding.chartsView.api.paneSize(MAIN_PANE_INDEX) { mainSize ->
                binding.chartsView.api.paneSize(VOLUME_PANE_INDEX) { volumeSize ->
                    binding.chartsView.api.paneSize(LEFT_SCALE_PANE_INDEX) { leftScaleSize ->
                        preservedPane?.let { pane ->
                            preservedPaneCompact = !preservedPaneCompact
                            pane.setHeight(
                                if (preservedPaneCompact) {
                                    PRESERVED_PANE_COMPACT_HEIGHT
                                } else {
                                    PRESERVED_PANE_EXPANDED_HEIGHT
                                }
                            )
                            pane.paneIndex { preservedPaneIndex ->
                                pane.getHeight { preservedPaneHeight ->
                                    pane.getSeries { preservedPaneSeries ->
                                        pane.preserveEmptyPane { preserve ->
                                            updateStatus(
                                                "Pane 0: ${mainSize.width.toInt()}x${mainSize.height.toInt()}, " +
                                                    "pane 1: ${volumeSize.width.toInt()}x${volumeSize.height.toInt()}, " +
                                                    "pane 2L: ${leftScaleSize.width.toInt()}x${leftScaleSize.height.toInt()}, " +
                                                    "pane $preservedPaneIndex: ${preservedPaneHeight}px, " +
                                                    "series=${preservedPaneSeries.size}, preserve=$preserve",
                                                showToast = false,
                                            )
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
        binding.chipQuaternary.setOnClickListener {
            if (::candleSeries.isInitialized) {
                candleSeries.pop<CandlestickData>(3) { popped ->
                    showStatus("Popped ${popped.size} candles from the main series")
                }
            }
        }
    }

    private fun setupChart(chartApi: ChartApi) {
        val candles = createCandleData()
        val volume = createVolumeData(candles)
        val movingAverage = createMovingAverage(candles)
        val leftScaleSignal = createLeftScaleSignal(candles)

        chartApi.applyOptions {
            layout = layoutOptions {
                background = SolidColor(Color.parseColor("#11131A").toIntColor())
                textColor = Color.parseColor("#D1D4DC").toIntColor()
                attributionLogo = false
            }
            leftPriceScale = priceScaleOptions {
                visible = true
                borderVisible = true
                minimumWidth = 54
                textColor = Color.parseColor("#FB7185").toIntColor()
            }
            defaultVisiblePriceScaleId = PriceScaleSide.RIGHT
            hoveredSeriesOnTop = true
            crosshair = crosshairOptions {
                mode = CrosshairMode.MAGNET_OHLC
                doNotSnapToHiddenSeriesIndices = true
            }
            grid = gridOptions {
                vertLines = gridLineOptions { color = Color.argb(32, 120, 144, 156).toIntColor() }
                horzLines = gridLineOptions { color = Color.argb(44, 120, 144, 156).toIntColor() }
            }
            timeScale = timeScaleOptions {
                rightOffsetPixels = 16f
                maxBarSpacing = 22f
                minimumHeight = 34
                tickMarkMaxCharacterLength = 10
            }
        }

        chartApi.addSeries(
            type = SeriesType.CANDLESTICK,
            options = CandlestickSeriesOptions(
                priceFormat = PriceFormat.priceFormatBuiltIn(
                    type = PriceFormat.Type.PRICE,
                    precision = 2,
                    minMove = 0.01f,
                    base = 100f,
                ),
            ),
            paneIndex = MAIN_PANE_INDEX,
        ) { series ->
            candleSeries = series
            series.setData(candles)
        }

        chartApi.addSeries(
            type = SeriesType.LINE,
            options = LineSeriesOptions(
                title = "MA 8",
                color = Color.parseColor("#F5C542").toIntColor(),
                lineWidth = LineWidth.TWO,
                pointMarkersVisible = true,
                pointMarkersRadius = 2f,
            ),
            paneIndex = MAIN_PANE_INDEX,
        ) { series ->
            movingAverageSeries = series
            series.setData(movingAverage)
            series.setSeriesOrder(TOP_SERIES_ORDER)
        }

        chartApi.addPane(preserveEmptyPane = true) { pane ->
            pane.setStretchFactor(0.32f)
            pane.addSeries(
                type = SeriesType.HISTOGRAM,
                options = HistogramSeriesOptions(
                    title = "Volume",
                    color = Color.parseColor("#4B7BEC").toIntColor(),
                    priceFormat = PriceFormat.priceFormatBuiltIn(
                        type = PriceFormat.Type.VOLUME,
                        precision = 0,
                        minMove = 1f,
                    ),
                ),
            ) { series ->
                volumeSeries = series
                series.setData(volume)
                createLeftScalePane(chartApi, leftScaleSignal)
            }
        }
    }

    private fun createLeftScalePane(chartApi: ChartApi, signal: List<LineData>) {
        chartApi.addPane(preserveEmptyPane = true) { pane ->
            pane.setStretchFactor(0.26f)
            pane.addSeries(
                type = SeriesType.LINE,
                options = LineSeriesOptions(
                    title = "Left scale signal",
                    color = Color.parseColor("#FB7185").toIntColor(),
                    lineWidth = LineWidth.TWO,
                    priceScaleId = PriceScaleId.LEFT,
                    priceLineVisible = true,
                    pointMarkersVisible = true,
                    pointMarkersRadius = 2f,
                    priceFormat = PriceFormat.priceFormatBuiltIn(
                        type = PriceFormat.Type.PRICE,
                        precision = 1,
                        minMove = 0.1f,
                    ),
                ),
            ) { series ->
                leftScaleSeries = series
                series.setData(signal)
                series.priceScale().applyOptions {
                    visible = true
                    borderVisible = true
                    textColor = Color.parseColor("#FB7185").toIntColor()
                    minimumWidth = 54
                }
                createPreservedPane(chartApi)
            }
        }
    }

    private fun createPreservedPane(chartApi: ChartApi) {
        chartApi.addPane(preserveEmptyPane = true) { pane ->
            preservedPane = pane
            pane.setStretchFactor(0.18f)
            pane.setHeight(PRESERVED_PANE_COMPACT_HEIGHT)
            binding.chartsView.postDelayed({ reportPaneState(chartApi) }, PANE_STATE_REPORT_DELAY_MS)
        }
    }

    private fun reportPaneState(chartApi: ChartApi) {
        chartApi.panes { panes ->
            leftScaleSeries.priceScale().width { leftScaleWidth ->
                volumeSeries.lastValueData(globalLast = true) { last ->
                    preservedPane?.getSeries { preservedPaneSeries ->
                        showStatus(
                            "Panes: ${panes.size}, left scale width: ${leftScaleWidth.toInt()}px, " +
                                "preserved pane series: ${preservedPaneSeries.size}, " +
                                "last volume label: ${last.text.orEmpty()}"
                        )
                    }
                }
            }
        }
    }

    private fun createCandleData(): List<CandlestickData> {
        var previousClose = 101f
        return (0 until POINT_COUNT).map { index ->
            val open = previousClose
            val wave = sin(index / 4.0).toFloat()
            val close = open + wave * 1.8f + if (index % 9 == 0) 1.2f else -0.25f
            val high = maxOf(open, close) + 1.4f + abs(wave)
            val low = minOf(open, close) - 1.2f - abs(wave * 0.8f)
            previousClose = close
            CandlestickData(
                time = Time.Utc(START_TIME + index * DAY_SECONDS),
                open = open,
                high = high,
                low = low,
                close = close,
            )
        }
    }

    private fun createVolumeData(candles: List<CandlestickData>): List<HistogramData> {
        return candles.mapIndexed { index, candle ->
            val rising = candle.close >= candle.open
            HistogramData(
                time = candle.time,
                value = (18_000 + index * 210 + abs(candle.close - candle.open) * 9_000).toFloat(),
                color = Color.parseColor(if (rising) "#26A69A" else "#EF5350").toIntColor(),
            )
        }
    }

    private fun createMovingAverage(candles: List<CandlestickData>): List<LineData> {
        return candles.mapIndexed { index, candle ->
            val window = candles.subList(maxOf(0, index - 7), index + 1)
            LineData(
                time = candle.time,
                value = window.map { it.close }.average().toFloat(),
            )
        }
    }

    private fun createLeftScaleSignal(candles: List<CandlestickData>): List<LineData> {
        return candles.mapIndexed { index, candle ->
            val momentum = (candle.close - candle.open) * 18f
            val oscillator = sin(index / 5.0).toFloat() * 12f
            LineData(
                time = candle.time,
                value = 50f + momentum + oscillator,
            )
        }
    }

    private fun showStatus(message: String) {
        updateStatus(message, showToast = true)
    }

    private fun updateStatus(message: String, showToast: Boolean) {
        binding.tvDebugValues.text = message
        if (showToast) {
            Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
        }
    }

    companion object {
        private const val MAIN_PANE_INDEX = 0
        private const val VOLUME_PANE_INDEX = 1
        private const val LEFT_SCALE_PANE_INDEX = 2
        private const val PRESERVED_PANE_INDEX = 3
        private const val TOP_SERIES_ORDER = 2
        private const val BOTTOM_SERIES_ORDER = 0
        private const val POINT_COUNT = 72
        private const val START_TIME = 1_704_067_200L
        private const val DAY_SECONDS = 86_400L
        private const val PRESERVED_PANE_COMPACT_HEIGHT = 72
        private const val PRESERVED_PANE_EXPANDED_HEIGHT = 120
        private const val PANE_STATE_REPORT_DELAY_MS = 120L
    }

    private data class MovePaneTarget(val index: Int, val label: String)

    private val movePaneTargets = listOf(
        MovePaneTarget(MAIN_PANE_INDEX, "main candles"),
        MovePaneTarget(VOLUME_PANE_INDEX, "volume histogram"),
        MovePaneTarget(LEFT_SCALE_PANE_INDEX, "left price scale signal"),
        MovePaneTarget(PRESERVED_PANE_INDEX, "preserved empty pane"),
    )

    private fun Int.floorMod(divisor: Int): Int = ((this % divisor) + divisor) % divisor
}
