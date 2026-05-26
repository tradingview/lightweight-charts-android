package com.tradingview.lightweightcharts.example.app.view.charts

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.tradingview.lightweightcharts.api.chart.models.color.surface.SolidColor
import com.tradingview.lightweightcharts.api.interfaces.ChartApi
import com.tradingview.lightweightcharts.api.interfaces.SeriesApi
import com.tradingview.lightweightcharts.api.interfaces.SeriesMarkersApi
import com.tradingview.lightweightcharts.api.interfaces.WatermarkApi
import com.tradingview.lightweightcharts.api.options.enums.HorizontalAlignment
import com.tradingview.lightweightcharts.api.options.enums.VerticalAlignment
import com.tradingview.lightweightcharts.api.options.models.BarSeriesOptions
import com.tradingview.lightweightcharts.api.options.models.ImageWatermarkOptions
import com.tradingview.lightweightcharts.api.options.models.LineSeriesOptions
import com.tradingview.lightweightcharts.api.options.models.SeriesMarkersOptions
import com.tradingview.lightweightcharts.api.options.models.TextWatermarkLineOptions
import com.tradingview.lightweightcharts.api.options.models.TextWatermarkOptions
import com.tradingview.lightweightcharts.api.options.models.crosshairOptions
import com.tradingview.lightweightcharts.api.options.models.gridLineOptions
import com.tradingview.lightweightcharts.api.options.models.gridOptions
import com.tradingview.lightweightcharts.api.options.models.layoutOptions
import com.tradingview.lightweightcharts.api.series.common.SeriesData
import com.tradingview.lightweightcharts.api.series.enums.CrosshairMode
import com.tradingview.lightweightcharts.api.series.enums.LineWidth
import com.tradingview.lightweightcharts.api.series.enums.SeriesMarkerPosition
import com.tradingview.lightweightcharts.api.series.enums.SeriesMarkerShape
import com.tradingview.lightweightcharts.api.series.enums.SeriesMarkerZOrder
import com.tradingview.lightweightcharts.api.series.enums.SeriesType
import com.tradingview.lightweightcharts.api.series.models.BarData
import com.tradingview.lightweightcharts.api.series.models.LineData
import com.tradingview.lightweightcharts.api.series.models.SeriesMarker
import com.tradingview.lightweightcharts.example.app.R
import com.tradingview.lightweightcharts.example.app.data.listSeriesMarkersSeriesData
import com.tradingview.lightweightcharts.example.app.databinding.FragmentV5ShowcaseBinding
import com.tradingview.lightweightcharts.example.app.view.util.ChartWatermarkTypography
import com.tradingview.lightweightcharts.example.app.view.util.ITitleFragment
import com.tradingview.lightweightcharts.example.app.view.util.chartColor
import com.tradingview.lightweightcharts.example.app.view.util.chartWatermarkTypography
import com.tradingview.lightweightcharts.view.ChartsView
import kotlin.math.floor

class V5MarkersAndWatermarksFragment : Fragment(), ITitleFragment {
    override val fragmentTitleRes = R.string.v5_markers_and_watermarks

    private lateinit var binding: FragmentV5ShowcaseBinding
    private lateinit var priceSeries: SeriesApi
    private lateinit var markers: List<SeriesMarker>
    private var markersApi: SeriesMarkersApi? = null
    private var textWatermarkApi: WatermarkApi? = null
    private var imageWatermarkApi: WatermarkApi? = null
    private var isReady = false
    private var markersVisible = true
    private var markerZOrderIndex = 0
    private var textWatermarkLarge = true
    private var imageWatermarkOpaque = false
    private lateinit var watermarkTypography: ChartWatermarkTypography

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
        binding.chipPrimary.text = getString(R.string.v5_toggle_markers)
        updateMarkerZOrderChip()
        binding.chipTertiary.text = getString(R.string.v5_text_watermark)
        binding.chipQuaternary.text = getString(R.string.v5_image_watermark)

        binding.chipPrimary.setOnClickListener {
            markersApi?.let { api ->
                markersVisible = !markersVisible
                api.setMarkers(if (markersVisible) markers else emptyList())
                showStatus("Markers ${if (markersVisible) "shown" else "hidden"}")
            }
        }
        binding.chipSecondary.setOnClickListener {
            markersApi?.let { api ->
                markerZOrderIndex = (markerZOrderIndex + 1) % MARKER_Z_ORDERS.size
                val zOrder = MARKER_Z_ORDERS[markerZOrderIndex]
                api.applyOptions(SeriesMarkersOptions(zOrder = zOrder, autoScale = true))
                updateMarkerZOrderChip()
                showStatus(markerZOrderStatus(zOrder))
            }
        }
        binding.chipTertiary.setOnClickListener {
            textWatermarkApi?.let { watermark ->
                textWatermarkLarge = !textWatermarkLarge
                watermark.applyTextOptions(createTextWatermarkOptions(textWatermarkLarge))
                showStatus("Text watermark font: ${if (textWatermarkLarge) 52 else 30}")
            }
        }
        binding.chipQuaternary.setOnClickListener {
            imageWatermarkApi?.let { watermark ->
                imageWatermarkOpaque = !imageWatermarkOpaque
                watermark.applyImageOptions(
                    ImageWatermarkOptions(
                        alpha = if (imageWatermarkOpaque) 0.38f else 0.18f,
                        maxWidth = 180,
                        maxHeight = 90,
                        padding = 18,
                    )
                )
                showStatus("Image watermark alpha: ${if (imageWatermarkOpaque) "0.38" else "0.18"}")
            }
        }
    }

    private fun setupChart(chartApi: ChartApi) {
        val data = listSeriesMarkersSeriesData().take(80)
        markers = createMarkers(data)
        watermarkTypography = requireContext().chartWatermarkTypography()

        chartApi.applyOptions {
            layout = layoutOptions {
                background = SolidColor(chartColor(R.color.chart_dark_background))
                textColor = chartColor(R.color.chart_text_primary)
                attributionLogo = true
            }
            crosshair = crosshairOptions {
                mode = CrosshairMode.MAGNET_OHLC
            }
            grid = gridOptions {
                vertLines = gridLineOptions { color = chartColor(R.color.white_1, alpha = 20) }
                horzLines = gridLineOptions { color = chartColor(R.color.white_1, alpha = 32) }
            }
        }

        chartApi.addSeries(
            type = SeriesType.BAR,
            options = BarSeriesOptions(
                title = "Price with primitive markers",
            ),
        ) { series ->
            priceSeries = series
            series.setData(data)
            series.createSeriesMarkers(
                data = markers,
                options = SeriesMarkersOptions(zOrder = MARKER_Z_ORDERS[markerZOrderIndex], autoScale = true),
            ) { api ->
                markersApi = api
                api.markers { currentMarkers ->
                    showStatus("Native marker primitive: ${currentMarkers.size} markers. ${markerZOrderStatus(MARKER_Z_ORDERS[markerZOrderIndex])}")
                }
            }
        }

        chartApi.addSeries(
            type = SeriesType.LINE,
            options = LineSeriesOptions(
                title = "Z-order reference line",
                color = chartColor(R.color.white_1),
                lineWidth = LineWidth.FOUR,
                priceLineVisible = false,
                lastValueVisible = false,
                pointMarkersVisible = false,
            ),
        ) { series ->
            series.setData(createReferenceLineData(data))
        }

        chartApi.createTextWatermark(
            options = createTextWatermarkOptions(textWatermarkLarge),
            paneIndex = 0,
        ) { watermark ->
            textWatermarkApi = watermark
        }

        chartApi.createImageWatermark(
            imageUrl = IMAGE_WATERMARK,
            options = ImageWatermarkOptions(
                alpha = 0.18f,
                maxWidth = 180,
                maxHeight = 90,
                padding = 18,
            ),
            paneIndex = 0,
        ) { watermark ->
            imageWatermarkApi = watermark
        }
    }

    private fun createMarkers(data: List<SeriesData>): List<SeriesMarker> {
        val aboveBar = data[8] as BarData
        val belowBar = data[24] as BarData
        val inBar = data[38] as BarData
        val priceBar = data[52] as BarData
        val zOrderBar = data[58] as BarData

        return listOf(
            SeriesMarker(
                time = aboveBar.time,
                position = SeriesMarkerPosition.ABOVE_BAR,
                color = chartColor(R.color.chart_marker_orange),
                shape = SeriesMarkerShape.CIRCLE,
                size = 2,
                id = "above-bar",
                text = "aboveBar",
            ),
            SeriesMarker(
                time = belowBar.time,
                position = SeriesMarkerPosition.BELOW_BAR,
                color = chartColor(R.color.chart_marker_blue),
                shape = SeriesMarkerShape.ARROW_UP,
                id = "below-bar",
                text = "belowBar ${floor(belowBar.low)}",
            ),
            SeriesMarker(
                time = inBar.time,
                position = SeriesMarkerPosition.IN_BAR,
                color = chartColor(R.color.chart_marker_pink),
                shape = SeriesMarkerShape.SQUARE,
                id = "in-bar",
                text = "inBar",
            ),
            SeriesMarker(
                time = priceBar.time,
                position = SeriesMarkerPosition.AT_PRICE_TOP,
                price = priceBar.high + 2f,
                color = chartColor(R.color.chart_marker_purple),
                shape = SeriesMarkerShape.SQUARE,
                id = "at-price-top",
                text = "atPriceTop",
            ),
            SeriesMarker(
                time = priceBar.time,
                position = SeriesMarkerPosition.AT_PRICE_MIDDLE,
                price = (priceBar.high + priceBar.low) / 2f,
                color = chartColor(R.color.chart_marker_teal),
                shape = SeriesMarkerShape.CIRCLE,
                id = "at-price-middle",
                text = "atPriceMiddle",
            ),
            SeriesMarker(
                time = priceBar.time,
                position = SeriesMarkerPosition.AT_PRICE_BOTTOM,
                price = priceBar.low - 2f,
                color = chartColor(R.color.chart_marker_green),
                shape = SeriesMarkerShape.SQUARE,
                id = "at-price-bottom",
                text = "atPriceBottom",
            ),
            SeriesMarker(
                time = zOrderBar.time,
                position = SeriesMarkerPosition.AT_PRICE_MIDDLE,
                price = zOrderBar.close,
                color = chartColor(R.color.chart_marker_yellow),
                shape = SeriesMarkerShape.SQUARE,
                size = 7,
                id = "z-order-probe",
                text = "layer probe",
            ),
        )
    }

    private fun createReferenceLineData(data: List<SeriesData>): List<LineData> {
        return data.map { item ->
            val bar = item as BarData
            LineData(time = bar.time, value = bar.close)
        }
    }

    private fun markerZOrderStatus(zOrder: SeriesMarkerZOrder): String {
        return when (zOrder) {
            SeriesMarkerZOrder.NORMAL -> "Z-order NORMAL: white line can draw over the yellow layer probe"
            SeriesMarkerZOrder.ABOVE_SERIES -> "Z-order ABOVE_SERIES: markers draw above series, below top-layer primitives"
            SeriesMarkerZOrder.TOP -> "Z-order TOP: markers draw above the thick reference line"
        }
    }

    private fun updateMarkerZOrderChip() {
        binding.chipSecondary.text = when (MARKER_Z_ORDERS[markerZOrderIndex]) {
            SeriesMarkerZOrder.NORMAL -> "Z: normal"
            SeriesMarkerZOrder.ABOVE_SERIES -> "Z: above"
            SeriesMarkerZOrder.TOP -> "Z: top"
        }
    }

    private fun createTextWatermarkOptions(large: Boolean): TextWatermarkOptions {
        return TextWatermarkOptions(
            visible = true,
            horzAlign = HorizontalAlignment.CENTER,
            vertAlign = VerticalAlignment.CENTER,
            lines = listOf(
                TextWatermarkLineOptions(
                    text = "Lightweight Charts 5.2",
                    color = chartColor(R.color.white_1, alpha = 56),
                    fontSize = watermarkTypography.titleFontSize(large),
                    lineHeight = watermarkTypography.titleLineHeight(large),
                    fontStyle = watermarkTypography.titleFontStyle,
                    fontFamily = watermarkTypography.fontFamily,
                ),
                TextWatermarkLineOptions(
                    text = "primitive markers + watermarks",
                    color = chartColor(R.color.white_1, alpha = 72),
                    fontSize = watermarkTypography.subtitleFontSize(large),
                    lineHeight = watermarkTypography.subtitleLineHeight(large),
                    fontFamily = watermarkTypography.fontFamily,
                ),
            ),
        )
    }

    private fun chartColor(colorRes: Int, alpha: Int? = null) = requireContext().chartColor(colorRes, alpha)

    private fun showStatus(message: String) {
        binding.tvDebugValues.text = message
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
    }

    companion object {
        private val MARKER_Z_ORDERS = listOf(
            SeriesMarkerZOrder.NORMAL,
            SeriesMarkerZOrder.ABOVE_SERIES,
            SeriesMarkerZOrder.TOP,
        )
        private const val IMAGE_WATERMARK = "file:///android_asset/watermarks/chart-watermark.svg"
    }
}
