package com.tradingview.lightweightcharts.example.app.view.charts

import android.Manifest
import android.content.Intent
import android.graphics.Bitmap
import android.os.Bundle
import android.os.Environment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.FileProvider
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.tradingview.lightweightcharts.api.chart.models.ImageMimeType
import com.tradingview.lightweightcharts.api.options.models.CandlestickSeriesOptions
import com.tradingview.lightweightcharts.api.options.models.crosshairOptions
import com.tradingview.lightweightcharts.api.series.enums.CrosshairMode
import com.tradingview.lightweightcharts.api.series.models.PriceScaleId
import com.tradingview.lightweightcharts.example.app.R
import com.tradingview.lightweightcharts.example.app.databinding.FragmentChartActionsBinding
import com.tradingview.lightweightcharts.example.app.view.util.ITitleFragment
import com.tradingview.lightweightcharts.example.app.viewmodel.RealTimeEmulationViewModel
import kotlinx.coroutines.Job
import permissions.dispatcher.NeedsPermission
import java.io.File
import java.io.FileOutputStream

class ChartActionsFragment : Fragment(), ITitleFragment {
    override val fragmentTitleRes = R.string.actions

    private val chartsView get() = binding.chartsView
    private val chartApi get() = chartsView.api
    private val timeScaleApi get() = chartApi.timeScale
    private val leftPriceScale get() = chartApi.priceScale(PriceScaleId.LEFT)
    private val rightPriceScale get() = chartApi.priceScale(PriceScaleId.RIGHT)

    private lateinit var binding: FragmentChartActionsBinding

    private var realtimeDataJob: Job? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return FragmentChartActionsBinding.inflate(inflater, container, false)
            .also { binding = it }
            .root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val viewModelProvider = ViewModelProvider(this)
        val viewModel = viewModelProvider[RealTimeEmulationViewModel::class.java]
        viewModel.seriesData.observe(viewLifecycleOwner) { data ->
            chartApi.addCandlestickSeries(
                options = CandlestickSeriesOptions(),
                onSeriesCreated = { series ->
                    series.setData(data.list)
                    realtimeDataJob = lifecycleScope.launchWhenResumed {
                        viewModel.seriesFlow.collect(series::update)
                    }
                }
            )
        }

        chartApi.applyOptions {
            crosshair = crosshairOptions {
                mode = CrosshairMode.NORMAL
            }
        }

        binding.chipTakeScreenShoot.setOnClickListener {
            shareScreenshot()
        }

    }

    override fun onDestroy() {
        realtimeDataJob?.cancel()
        super.onDestroy()
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

}