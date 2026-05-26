package com.tradingview.lightweightcharts.example.app.view.charts

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.tradingview.lightweightcharts.api.chart.models.ImageMimeType
import com.tradingview.lightweightcharts.api.options.models.CandlestickSeriesOptions
import com.tradingview.lightweightcharts.api.options.models.crosshairOptions
import com.tradingview.lightweightcharts.api.series.enums.CrosshairMode
import com.tradingview.lightweightcharts.example.app.R
import com.tradingview.lightweightcharts.example.app.databinding.FragmentChartActionsBinding
import com.tradingview.lightweightcharts.example.app.view.util.ITitleFragment
import com.tradingview.lightweightcharts.example.app.view.util.ScreenshotShare
import com.tradingview.lightweightcharts.example.app.viewmodel.RealTimeEmulationViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

class ChartActionsFragment : Fragment(), ITitleFragment {
    override val fragmentTitleRes = R.string.actions

    private val chartsView get() = binding.chartsView
    private val chartApi get() = chartsView.api

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
                    realtimeDataJob = viewLifecycleOwner.lifecycleScope.launch {
                        viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.RESUMED) {
                            viewModel.seriesFlow.collect(series::update)
                        }
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

    fun shareScreenshot() {
        chartApi.takeScreenshot(ImageMimeType.PNG) { bitmap ->
            ScreenshotShare.share(requireContext(), bitmap)
        }
    }
}
