package com.tradingview.lightweightcharts.example.app.view.charts

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.tradingview.lightweightcharts.api.chart.models.ImageMimeType
import com.tradingview.lightweightcharts.api.chart.models.ScreenshotOptions
import com.tradingview.lightweightcharts.api.chart.models.color.surface.SolidColor
import com.tradingview.lightweightcharts.api.interfaces.ChartApi
import com.tradingview.lightweightcharts.api.interfaces.SeriesApi
import com.tradingview.lightweightcharts.api.options.enums.ConflationPriority
import com.tradingview.lightweightcharts.api.options.enums.PriceScaleSide
import com.tradingview.lightweightcharts.api.options.models.AreaSeriesOptions
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
import com.tradingview.lightweightcharts.api.series.models.LineData
import com.tradingview.lightweightcharts.api.series.models.LogicalRange
import com.tradingview.lightweightcharts.api.series.models.PriceRange
import com.tradingview.lightweightcharts.api.series.models.PriceScaleId
import com.tradingview.lightweightcharts.api.series.models.Time
import com.tradingview.lightweightcharts.example.app.R
import com.tradingview.lightweightcharts.example.app.databinding.FragmentV5ShowcaseBinding
import com.tradingview.lightweightcharts.example.app.view.util.ITitleFragment
import com.tradingview.lightweightcharts.example.app.view.util.ScreenshotShare
import com.tradingview.lightweightcharts.example.app.view.util.chartColor
import com.tradingview.lightweightcharts.view.ChartsView
import kotlin.math.sin

class V5ScalesAndEventsFragment : Fragment(), ITitleFragment {
    override val fragmentTitleRes = R.string.v5_scales_and_events

    private lateinit var binding: FragmentV5ShowcaseBinding
    private lateinit var chartApi: ChartApi
    private lateinit var signalSeries: SeriesApi
    private lateinit var signalData: List<LineData>
    private var isReady = false
    private var fixedPriceRange = false
    private var crosshairLocked = false

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
        binding.chipPrimary.text = getString(R.string.v5_logical_range)
        binding.chipSecondary.text = getString(R.string.v5_price_range)
        binding.chipTertiary.text = getString(R.string.v5_crosshair)
        binding.chipQuaternary.text = getString(R.string.v5_screenshot)

        binding.chipPrimary.setOnClickListener {
            if (::chartApi.isInitialized) {
                val range = LogicalRange(from = 980f, to = 1_180f)
                chartApi.timeScale.setVisibleLogicalRange(range)
                chartApi.timeScale.timeToIndex(signalData[1_050].time, findNearest = true) { index ->
                    chartApi.timeScale.getVisibleLogicalRange { visible ->
                        showStatus("Logical range ${visible?.from?.toInt()}..${visible?.to?.toInt()}, index: $index")
                    }
                }
            }
        }
        binding.chipSecondary.setOnClickListener {
            if (::chartApi.isInitialized) {
                val priceScale = chartApi.priceScale(PriceScaleId.RIGHT)
                fixedPriceRange = !fixedPriceRange
                if (fixedPriceRange) {
                    priceScale.setAutoScale(false)
                    priceScale.setVisibleRange(PriceRange(from = 92f, to = 124f))
                    priceScale.getVisibleRange { range ->
                        showStatus("Price range ${range?.from?.toInt()}..${range?.to?.toInt()}")
                    }
                } else {
                    priceScale.setAutoScale(true)
                    showStatus("Price scale autoscale restored")
                }
            }
        }
        binding.chipTertiary.setOnClickListener {
            if (::chartApi.isInitialized && ::signalSeries.isInitialized) {
                crosshairLocked = !crosshairLocked
                if (crosshairLocked) {
                    val point = signalData[1_200]
                    chartApi.setCrosshairPosition(point.value, point.time, signalSeries)
                    signalSeries.lastValueData(globalLast = true) { last ->
                        showStatus("Crosshair locked, last value: ${last.text.orEmpty()}")
                    }
                } else {
                    chartApi.clearCrosshairPosition()
                    showStatus("Crosshair cleared")
                }
            }
        }
        binding.chipQuaternary.setOnClickListener {
            if (::chartApi.isInitialized) {
                showStatus("Preparing screenshot...")
                chartApi.takeScreenshot(
                    ScreenshotOptions(
                        mimeType = ImageMimeType.PNG,
                        addTopLayer = true,
                        includeCrosshair = true,
                    )
                ) { bitmap ->
                    showStatus("Sharing screenshot: ${bitmap.width}x${bitmap.height}")
                    ScreenshotShare.share(requireContext(), bitmap, "v5-scales-and-events.png")
                }
            }
        }
    }

    private fun setupChart(api: ChartApi) {
        chartApi = api
        signalData = createDenseSignalData()

        api.applyOptions {
            layout = layoutOptions {
                background = SolidColor(chartColor(R.color.chart_signal_background))
                textColor = chartColor(R.color.chart_text_secondary)
                attributionLogo = false
            }
            defaultVisiblePriceScaleId = PriceScaleSide.RIGHT
            hoveredSeriesOnTop = true
            rightPriceScale = priceScaleOptions {
                minimumWidth = 72
                ensureEdgeTickMarksVisible = true
                tickMarkDensity = 1.4f
                textColor = chartColor(R.color.chart_text_bright)
            }
            timeScale = timeScaleOptions {
                enableConflation = true
                conflationThresholdFactor = 0.75f
                precomputeConflationOnInit = true
                precomputeConflationPriority = ConflationPriority.USER_VISIBLE
                rightOffsetPixels = 20f
                maxBarSpacing = 18f
                minimumHeight = 38
                tickMarkMaxCharacterLength = 8
            }
            crosshair = crosshairOptions {
                mode = CrosshairMode.MAGNET_OHLC
                doNotSnapToHiddenSeriesIndices = true
            }
            grid = gridOptions {
                vertLines = gridLineOptions { color = chartColor(R.color.chart_grid_slate, alpha = 24) }
                horzLines = gridLineOptions { color = chartColor(R.color.chart_grid_slate, alpha = 36) }
            }
        }

        api.addSeries(
            type = SeriesType.AREA,
            options = AreaSeriesOptions(
                title = "Conflated signal",
                topColor = chartColor(R.color.chart_series_emerald, alpha = 112),
                bottomColor = chartColor(R.color.chart_series_emerald, alpha = 12),
                lineColor = chartColor(R.color.chart_series_emerald),
                lineWidth = LineWidth.TWO,
                relativeGradient = true,
                conflationThresholdFactor = 0.6f,
            ),
        ) { series ->
            signalSeries = series
            series.setData(signalData)
        }

        api.addSeries(
            type = SeriesType.LINE,
            options = LineSeriesOptions(
                title = "Hidden snap target",
                color = chartColor(R.color.chart_series_orange),
                lineWidth = LineWidth.ONE,
                visible = false,
            ),
        ) { hiddenSeries ->
            hiddenSeries.setData(signalData.filterIndexed { index, _ -> index % 12 == 0 })
            reportInitialState(api)
        }

        api.subscribeCrosshairMove { params ->
            val hovered = params.hoveredInfo?.objectKind
                ?: params.hoveredInfo?.type
                ?: params.hoveredSeries
                ?: "none"
            binding.tvDebugValues.text =
                "Crosshair pane=${params.paneIndex}, logical=${params.logicalFloat ?: params.logical}, hovered=$hovered"
        }
        api.subscribeDblClick { params ->
            showStatus("Double click pane=${params.paneIndex}, logical=${params.logicalFloat ?: params.logical}")
        }
        api.timeScale.subscribeVisibleLogicalRangeChange { range ->
            if (range != null && !crosshairLocked) {
                binding.tvDebugValues.text = "Visible logical ${range.from.toInt()}..${range.to.toInt()}"
            }
        }
    }

    private fun reportInitialState(api: ChartApi) {
        api.priceScale(PriceScaleId.RIGHT).applyOptions {
            minimumWidth = 72
            ensureEdgeTickMarksVisible = true
        }
        api.timeScale.setVisibleLogicalRange(LogicalRange(from = 900f, to = 1_240f))
        showStatus("Loaded ${signalData.size} points with conflation enabled")
    }

    private fun createDenseSignalData(): List<LineData> {
        return (0 until POINT_COUNT).map { index ->
            val value = 104f +
                sin(index / 19.0).toFloat() * 7f +
                sin(index / 71.0).toFloat() * 4f +
                index * 0.0025f
            LineData(
                time = Time.Utc(START_TIME + index * MINUTE_SECONDS),
                value = value,
            )
        }
    }

    private fun showStatus(message: String) {
        binding.tvDebugValues.text = message
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
    }

    private fun chartColor(colorRes: Int, alpha: Int? = null) = requireContext().chartColor(colorRes, alpha)

    companion object {
        private const val POINT_COUNT = 5_200
        private const val START_TIME = 1_704_067_200L
        private const val MINUTE_SECONDS = 60L
    }
}
