package com.tradingview.lightweightcharts.example.app.view.charts

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.tradingview.lightweightcharts.api.chart.models.color.surface.SolidColor
import com.tradingview.lightweightcharts.api.options.models.AreaSeriesOptions
import com.tradingview.lightweightcharts.api.options.models.gridLineOptions
import com.tradingview.lightweightcharts.api.options.models.gridOptions
import com.tradingview.lightweightcharts.api.options.models.layoutOptions
import com.tradingview.lightweightcharts.api.series.enums.LineWidth
import com.tradingview.lightweightcharts.api.series.models.AreaData
import com.tradingview.lightweightcharts.api.series.models.Time
import com.tradingview.lightweightcharts.example.app.R
import com.tradingview.lightweightcharts.example.app.databinding.LayoutChartFragmentBinding
import com.tradingview.lightweightcharts.example.app.view.util.chartColor
import com.tradingview.lightweightcharts.example.app.view.util.ITitleFragment
import com.tradingview.lightweightcharts.view.ChartsView
import kotlin.math.sin

class NoAttributionLogoFragment : Fragment(), ITitleFragment {
    override val fragmentTitleRes = R.string.no_attribution_logo

    private lateinit var binding: LayoutChartFragmentBinding
    private var isReady = false

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        return LayoutChartFragmentBinding.inflate(inflater, container, false)
            .also { binding = it }.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.chartsView.subscribeOnChartStateChange { state ->
            when (state) {
                is ChartsView.State.Ready -> if (!isReady) {
                    isReady = true
                    setupChart()
                }
                is ChartsView.State.Error -> {
                    Toast.makeText(context, state.exception.localizedMessage, Toast.LENGTH_LONG).show()
                }
                is ChartsView.State.Preparing -> Unit
            }
        }
    }

    private fun setupChart() {
        binding.chartsView.api.applyOptions {
            layout = layoutOptions {
                background = SolidColor(chartColor(R.color.blue_1))
                textColor = chartColor(R.color.white_2)
                attributionLogo = false
            }
            grid = gridOptions {
                vertLines = gridLineOptions { color = chartColor(R.color.white_1, alpha = 22) }
                horzLines = gridLineOptions { color = chartColor(R.color.white_1, alpha = 28) }
            }
        }

        binding.chartsView.api.addAreaSeries(
            options = AreaSeriesOptions(
                title = "Attribution logo disabled",
                topColor = chartColor(R.color.blue_2, alpha = 116),
                bottomColor = chartColor(R.color.blue_2, alpha = 8),
                lineColor = chartColor(R.color.blue_2),
                lineWidth = LineWidth.TWO,
            ),
        ) { series ->
            series.setData(createData())
        }
    }

    private fun createData(): List<AreaData> {
        val firstTimestamp = currentUnixTimestamp() - (DATA_POINTS - 1) * DAY_SECONDS
        return (0 until DATA_POINTS).map { index ->
            AreaData(
                time = Time.Utc(firstTimestamp + index * DAY_SECONDS),
                value = 80f + sin(index / 8.0).toFloat() * 8f + index * 0.12f,
            )
        }
    }

    private fun chartColor(colorRes: Int, alpha: Int? = null) = requireContext().chartColor(colorRes, alpha)

    companion object {
        private const val DATA_POINTS = 120
        private const val DAY_SECONDS = 86_400L

        private fun currentUnixTimestamp(): Long = System.currentTimeMillis() / 1000L
    }
}
