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
import com.tradingview.lightweightcharts.example.app.view.util.ITitleFragment
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

        chartApi.applyOptions {
            layout = layoutOptions {
                background = SolidColor(Color.parseColor("#131722").toIntColor())
                textColor = Color.parseColor("#D1D4DC").toIntColor()
                attributionLogo = true
            }
            crosshair = crosshairOptions {
                mode = CrosshairMode.MAGNET_OHLC
            }
            grid = gridOptions {
                vertLines = gridLineOptions { color = Color.argb(20, 197, 203, 206).toIntColor() }
                horzLines = gridLineOptions { color = Color.argb(32, 197, 203, 206).toIntColor() }
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
                color = Color.parseColor("#FFFFFF").toIntColor(),
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
                color = Color.parseColor("#F5A623").toIntColor(),
                shape = SeriesMarkerShape.CIRCLE,
                size = 2,
                id = "above-bar",
                text = "aboveBar",
            ),
            SeriesMarker(
                time = belowBar.time,
                position = SeriesMarkerPosition.BELOW_BAR,
                color = Color.parseColor("#1E88E5").toIntColor(),
                shape = SeriesMarkerShape.ARROW_UP,
                id = "below-bar",
                text = "belowBar ${floor(belowBar.low)}",
            ),
            SeriesMarker(
                time = inBar.time,
                position = SeriesMarkerPosition.IN_BAR,
                color = Color.parseColor("#E91E63").toIntColor(),
                shape = SeriesMarkerShape.SQUARE,
                id = "in-bar",
                text = "inBar",
            ),
            SeriesMarker(
                time = priceBar.time,
                position = SeriesMarkerPosition.AT_PRICE_TOP,
                price = priceBar.high + 2f,
                color = Color.parseColor("#9C27B0").toIntColor(),
                shape = SeriesMarkerShape.SQUARE,
                id = "at-price-top",
                text = "atPriceTop",
            ),
            SeriesMarker(
                time = priceBar.time,
                position = SeriesMarkerPosition.AT_PRICE_MIDDLE,
                price = (priceBar.high + priceBar.low) / 2f,
                color = Color.parseColor("#00ACC1").toIntColor(),
                shape = SeriesMarkerShape.CIRCLE,
                id = "at-price-middle",
                text = "atPriceMiddle",
            ),
            SeriesMarker(
                time = priceBar.time,
                position = SeriesMarkerPosition.AT_PRICE_BOTTOM,
                price = priceBar.low - 2f,
                color = Color.parseColor("#43A047").toIntColor(),
                shape = SeriesMarkerShape.SQUARE,
                id = "at-price-bottom",
                text = "atPriceBottom",
            ),
            SeriesMarker(
                time = zOrderBar.time,
                position = SeriesMarkerPosition.AT_PRICE_MIDDLE,
                price = zOrderBar.close,
                color = Color.parseColor("#FFEB3B").toIntColor(),
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
                    color = Color.argb(56, 255, 255, 255).toIntColor(),
                    fontSize = if (large) 52 else 30,
                    lineHeight = if (large) 56 else 34,
                    fontStyle = "bold",
                    fontFamily = "sans-serif",
                ),
                TextWatermarkLineOptions(
                    text = "primitive markers + watermarks",
                    color = Color.argb(72, 255, 255, 255).toIntColor(),
                    fontSize = if (large) 18 else 14,
                    lineHeight = if (large) 24 else 18,
                    fontFamily = "sans-serif",
                ),
            ),
        )
    }

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
        private const val IMAGE_WATERMARK =
            "data:image/svg+xml,%3Csvg xmlns='http://www.w3.org/2000/svg' width='240' height='120' viewBox='0 0 240 120'%3E%3Crect width='240' height='120' rx='18' fill='%23296FF0' fill-opacity='0.45'/%3E%3Cpath d='M44 82 96 34l34 31 28-24 38 41' fill='none' stroke='%23FFFFFF' stroke-width='12' stroke-linecap='round' stroke-linejoin='round'/%3E%3C/svg%3E"
    }
}
