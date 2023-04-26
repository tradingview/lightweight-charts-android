package com.tradingview.lightweightcharts.example.app.view.charts

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.tradingview.lightweightcharts.api.interfaces.SeriesApi
import com.tradingview.lightweightcharts.api.options.models.applyAreaSeriesOptions
import com.tradingview.lightweightcharts.api.series.models.PriceScaleId
import com.tradingview.lightweightcharts.example.app.R
import com.tradingview.lightweightcharts.example.app.databinding.FragmentRangeSwitcherBinding
import com.tradingview.lightweightcharts.example.app.view.util.ITitleFragment
import com.tradingview.lightweightcharts.example.app.view.util.randomColor
import com.tradingview.lightweightcharts.example.app.view.util.randomColorTransparent
import com.tradingview.lightweightcharts.example.app.viewmodel.RangesViewModel
import com.tradingview.lightweightcharts.view.ChartsView

class RangeSwitcherFragment : Fragment(), ITitleFragment {
    override val fragmentTitleRes = R.string.range_switcher

    private val vm by lazy { ViewModelProvider(this)[RangesViewModel::class.java] }

    private lateinit var binding: FragmentRangeSwitcherBinding
    private val chartApi get() = binding.chartsView.api
    private var curSeries: SeriesApi? = null

    private val rangeChips get() = binding.run { listOf(chipDay, chipWeek, chipMonth, chipYear) }
    private var rangeIndex: Int? = null
        set(value) {
            field = value
            refreshUI()
        }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        return FragmentRangeSwitcherBinding.inflate(inflater, container, false).also { binding = it }.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        vm.init()
        binding.chartsView.subscribeOnChartStateChange { state ->
            when (state) {
                is ChartsView.State.Preparing -> Unit
                is ChartsView.State.Ready -> {
                    curSeries = null
                    vm.seriesDailyAreaData.observe(viewLifecycleOwner) { data ->
                        rangeIndex = 0
                    }
                }

                is ChartsView.State.Error -> {
                    Toast.makeText(context, state.exception.localizedMessage, Toast.LENGTH_LONG).show()
                }
            }
        }

        rangeChips.forEachIndexed { index, chip ->
            chip.setOnClickListener {
                rangeIndex = index
            }
        }

    }


    private fun refreshUI() {
        if (rangeIndex == null || vm.seriesDailyAreaData.value == null)
            return

        rangeChips.forEachIndexed { index, chip ->
            chip.isChecked = index == rangeIndex
        }

        val data = when (rangeIndex) {
            0 -> vm.seriesDailyAreaData.value!!.list
            1 -> vm.seriesWeeklyAreaData.value!!.list
            2 -> vm.seriesMonthlyAreaData.value!!.list
            3 -> vm.seriesYearlyAreaData.value!!.list
            else -> vm.seriesDailyAreaData.value!!.list
        }

        if (curSeries == null) {
            chartApi.addAreaSeries { series ->
                curSeries = series

                series.setData(data)

                series.applyAreaSeriesOptions {
                    priceScaleId = PriceScaleId.RIGHT
                    lineColor = resources.randomColor(R.array.blue_array)
                    topColor = resources.randomColor(R.array.blue_array)
                    bottomColor = resources.randomColorTransparent(R.array.blue_array)
                }
            }
        } else {
            curSeries?.apply {
                setData(data)
            }
        }

    }

}