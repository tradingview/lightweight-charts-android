package com.tradingview.lightweightcharts.example.app.view.charts

import android.annotation.SuppressLint
import android.os.Bundle
import android.os.CountDownTimer
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.tradingview.lightweightcharts.api.interfaces.SeriesApi
import com.tradingview.lightweightcharts.api.options.models.CandlestickSeriesOptions
import com.tradingview.lightweightcharts.api.options.models.PriceScaleOptions
import com.tradingview.lightweightcharts.api.options.models.applyBarSeriesOptions
import com.tradingview.lightweightcharts.api.options.models.crosshairOptions
import com.tradingview.lightweightcharts.api.series.enums.CrosshairMode
import com.tradingview.lightweightcharts.api.series.enums.PriceScaleMode
import com.tradingview.lightweightcharts.api.series.models.PriceScaleId
import com.tradingview.lightweightcharts.example.app.R
import com.tradingview.lightweightcharts.example.app.databinding.FragmentChartPricescaleActionsBinding
import com.tradingview.lightweightcharts.example.app.view.util.ITitleFragment
import com.tradingview.lightweightcharts.example.app.viewmodel.RealTimeEmulationViewModel
import kotlinx.coroutines.Job

class PriceScaleActionsFragment : Fragment(), ITitleFragment {
    override val fragmentTitleRes = R.string.pricescale_actions

    private val chartsView get() = binding.chartsView
    private val chartApi get() = chartsView.api
    private val priceScaleApi get() = chartApi.priceScale(sideVariants[curSideIndex].first)
    private var seriesApi: SeriesApi? = null

    private lateinit var binding: FragmentChartPricescaleActionsBinding

    private var realtimeDataJob: Job? = null

    private var curSideIndex = 0
    private val sideVariants = listOf(
        PriceScaleId.RIGHT to R.string.right,
        PriceScaleId.LEFT to R.string.left,
    )


    private var curPriceScaleModeIndex = 0
    private val scaleModeVariants = listOf(
        PriceScaleMode.NORMAL to R.string.linearly_scale,
        PriceScaleMode.LOGARITHMIC to R.string.logarithmic_scale,
        PriceScaleMode.PERCENTAGE to R.string.percentage_scale,
        PriceScaleMode.INDEXED_TO_100 to R.string.indexedTo100_scale,
    )

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return FragmentChartPricescaleActionsBinding.inflate(inflater, container, false)
            .also { binding = it }
            .root
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val viewModelProvider = ViewModelProvider(this)
        val viewModel = viewModelProvider[RealTimeEmulationViewModel::class.java]
        viewModel.seriesData.observe(viewLifecycleOwner) { data ->
            chartApi.addCandlestickSeries(
                options = CandlestickSeriesOptions(),
                onSeriesCreated = { series ->
                    this.seriesApi = series
                    series.setData(data.list)

                    realtimeDataJob = lifecycleScope.launchWhenResumed {
                        viewModel.seriesFlow.collect(series::update)
                    }
                }
            )
        }





        chartApi.applyOptions {
            crosshair = crosshairOptions {
                mode = CrosshairMode.MAGNET
            }
        }

        binding.apply {
            chipSwitchSide.setOnClickListener {
                curSideIndex = (curSideIndex + 1) % sideVariants.size
                chipSwitchSide.text = getString(sideVariants[curSideIndex].second)

                when (sideVariants[curSideIndex].first) {
                    PriceScaleId.RIGHT -> {

                        chartApi.priceScale(PriceScaleId.RIGHT).applyOptions {
                            visible = true
                        }
                        chartApi.priceScale(PriceScaleId.LEFT).applyOptions {
                            visible = false
                        }
                    }
                    PriceScaleId.LEFT -> {
                        chartApi.priceScale(PriceScaleId.RIGHT).applyOptions {
                            visible = false
                        }
                        chartApi.priceScale(PriceScaleId.LEFT).applyOptions {
                            visible = true
                        }

                    }
                }

                seriesApi?.applyBarSeriesOptions {
                    priceScaleId = sideVariants[curSideIndex].first
                }
            }

            chipPricescaleMode.setOnClickListener {
                curPriceScaleModeIndex = (curPriceScaleModeIndex + 1) % scaleModeVariants.size
                chipPricescaleMode.text = getString(scaleModeVariants[curPriceScaleModeIndex].second)
                priceScaleApi.applyOptions {
                    mode = scaleModeVariants[curPriceScaleModeIndex].first
                }
            }

            chipAutoscale.setOnCheckedChangeListener { buttonView, isChecked ->
                priceScaleApi.applyOptions(PriceScaleOptions(autoScale = isChecked))
            }

            chipInvert.setOnCheckedChangeListener { buttonView, isChecked ->
                priceScaleApi.applyOptions(PriceScaleOptions(invertScale = isChecked))
            }


        }
    }

    override fun onDestroy() {
        realtimeDataJob?.cancel()
        super.onDestroy()
    }


}