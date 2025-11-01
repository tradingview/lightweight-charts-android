package com.tradingview.lightweightcharts.example.app.view.charts

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.tradingview.lightweightcharts.api.options.models.CandlestickSeriesOptions
import com.tradingview.lightweightcharts.api.options.models.crosshairOptions
import com.tradingview.lightweightcharts.api.series.enums.CrosshairMode
import com.tradingview.lightweightcharts.example.app.R
import com.tradingview.lightweightcharts.example.app.view.util.ITitleFragment
import com.tradingview.lightweightcharts.example.app.viewmodel.RealTimeEmulationViewModel
import com.tradingview.lightweightcharts.view.ChartsView
import kotlinx.coroutines.Job

class RealTimeEmulationFragment : Fragment(), ITitleFragment {
    override val fragmentTitleRes = R.string.realtime

    private val chartsView get() = requireView().findViewById<ChartsView>(R.id.charts_view)
    private val chartApi get() = chartsView.api

    private var realtimeDataJob: Job? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.layout_chart_fragment, container, false)
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
    }

    override fun onDestroy() {
        realtimeDataJob?.cancel()
        super.onDestroy()
    }
}