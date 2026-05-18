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
import com.tradingview.lightweightcharts.api.options.models.AreaSeriesOptions
import com.tradingview.lightweightcharts.api.options.models.gridLineOptions
import com.tradingview.lightweightcharts.api.options.models.gridOptions
import com.tradingview.lightweightcharts.api.options.models.layoutOptions
import com.tradingview.lightweightcharts.api.series.enums.LineWidth
import com.tradingview.lightweightcharts.api.series.models.AreaData
import com.tradingview.lightweightcharts.api.series.models.Time
import com.tradingview.lightweightcharts.example.app.R
import com.tradingview.lightweightcharts.example.app.databinding.LayoutChartFragmentBinding
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
                background = SolidColor(Color.parseColor("#0B1220").toIntColor())
                textColor = Color.parseColor("#E5E7EB").toIntColor()
                attributionLogo = false
            }
            grid = gridOptions {
                vertLines = gridLineOptions { color = Color.argb(22, 148, 163, 184).toIntColor() }
                horzLines = gridLineOptions { color = Color.argb(28, 148, 163, 184).toIntColor() }
            }
        }

        binding.chartsView.api.addAreaSeries(
            options = AreaSeriesOptions(
                title = "Attribution logo disabled",
                topColor = Color.argb(116, 96, 165, 250).toIntColor(),
                bottomColor = Color.argb(8, 96, 165, 250).toIntColor(),
                lineColor = Color.parseColor("#60A5FA").toIntColor(),
                lineWidth = LineWidth.TWO,
            ),
        ) { series ->
            series.setData(createData())
        }
    }

    private fun createData(): List<AreaData> {
        return (0 until 120).map { index ->
            AreaData(
                time = Time.Utc(1_704_067_200L + index * 86_400L),
                value = 80f + sin(index / 8.0).toFloat() * 8f + index * 0.12f,
            )
        }
    }
}
