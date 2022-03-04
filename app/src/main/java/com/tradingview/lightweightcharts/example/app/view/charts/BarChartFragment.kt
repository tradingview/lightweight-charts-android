package com.tradingview.lightweightcharts.example.app.view.charts

import android.Manifest
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Color
import android.os.Bundle
import android.os.Environment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import androidx.core.content.FileProvider
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.tradingview.lightweightcharts.api.chart.models.ImageMimeType
import com.tradingview.lightweightcharts.api.chart.models.color.surface.SolidColor
import com.tradingview.lightweightcharts.api.chart.models.color.toIntColor
import com.tradingview.lightweightcharts.api.options.enums.TrackingModeExitMode
import com.tradingview.lightweightcharts.api.options.models.*
import com.tradingview.lightweightcharts.api.series.enums.CrosshairMode
import com.tradingview.lightweightcharts.api.series.models.PriceScaleId
import com.tradingview.lightweightcharts.example.app.R
import com.tradingview.lightweightcharts.example.app.viewmodel.BarChartViewModel
import com.tradingview.lightweightcharts.view.ChartsView
import permissions.dispatcher.NeedsPermission
import permissions.dispatcher.RuntimePermissions
import java.io.File
import java.io.FileOutputStream

@RuntimePermissions
class BarChartFragment: Fragment() {
    private val chartsView get() = requireView().findViewById<ChartsView>(R.id.charts_view)
    private val screenshotButton get() = requireView().findViewById<Button>(R.id.screenshot_btn)

    private val viewModelProvider get() = ViewModelProvider(this)
    private lateinit var viewModel: BarChartViewModel

    private val chartApi get() = chartsView.api

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = viewModelProvider[BarChartViewModel::class.java]
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.layout_bar_chart_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        chartsView.subscribeOnChartStateChange { state ->
            when (state) {
                is ChartsView.State.Preparing -> Unit
                is ChartsView.State.Ready -> {
                    viewModel.seriesData.observe(viewLifecycleOwner) { data ->
                        chartApi.addBarSeries(
                            options = BarSeriesOptions(
                                priceScaleId = PriceScaleId.RIGHT,
                                thinBars = true,
                                downColor = Color.BLACK.toIntColor(),
                                upColor = Color.BLACK.toIntColor(),
                            ),
                            onSeriesCreated = { series -> series.setData(data.list) }
                        )
                    }
                    applyChartOptions()
                    screenshotButton.setOnClickListener { shareScreenshot() }
                }
                is ChartsView.State.Error -> {
                    Toast.makeText(context, state.exception.localizedMessage, Toast.LENGTH_LONG).show()
                }
            }
        }
    }

    @NeedsPermission(
        Manifest.permission.WRITE_EXTERNAL_STORAGE,
        Manifest.permission.READ_EXTERNAL_STORAGE
    )
    fun shareScreenshot() {
        chartApi.takeScreenshot(ImageMimeType.WEBP) { bitmap ->
            val context = requireContext()

            val picturesDir = context.getExternalFilesDir(Environment.DIRECTORY_PICTURES)
            val file = File(picturesDir, "share.webp")
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, FileOutputStream(file))

            val shareIntent = Intent(Intent.ACTION_SEND)
            shareIntent.type = "image/webp"
            val uri = FileProvider.getUriForFile(context, "com.tradingview.fileprovider", file)
            shareIntent.putExtra(Intent.EXTRA_STREAM, uri)
            context.startActivity(Intent.createChooser(shareIntent, "Share image using"))
        }
    }

    private fun applyChartOptions() {
        chartApi.applyOptions {
            handleScale = handleScaleOptions {
                kineticScroll = kineticScrollOptions {
                    touch = false
                    mouse = false
                }
            }
            layout = layoutOptions {
                background = SolidColor(Color.WHITE)
                textColor = Color.argb(255, 33, 56, 77).toIntColor()
            }
            crosshair = crosshairOptions {
                mode = CrosshairMode.NORMAL
            }
            rightPriceScale = priceScaleOptions {
                borderColor = Color.argb(255, 197, 203, 206).toIntColor()
            }
            timeScale = timeScaleOptions {
                borderColor = Color.argb(255, 197, 203, 206).toIntColor()
                fixRightEdge = true
                minBarSpacing = 0.7f
            }
            trackingMode = TrackingModeOptions(exitMode = TrackingModeExitMode.ON_TOUCH_END)
        }
    }
}