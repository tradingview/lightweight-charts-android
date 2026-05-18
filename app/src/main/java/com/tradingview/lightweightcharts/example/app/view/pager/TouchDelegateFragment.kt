package com.tradingview.lightweightcharts.example.app.view.pager

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.tabs.TabLayoutMediator
import com.tradingview.lightweightcharts.api.interfaces.SeriesApi
import com.tradingview.lightweightcharts.example.app.R
import com.tradingview.lightweightcharts.example.app.databinding.FragmentChartsBinding
import com.tradingview.lightweightcharts.example.app.databinding.FragmentTouchDelegateBinding
import com.tradingview.lightweightcharts.example.app.view.util.ITitleFragment
import com.tradingview.lightweightcharts.example.app.viewmodel.ViewPagerViewModel
import com.tradingview.lightweightcharts.view.ChartsView

class TouchDelegateFragment : Fragment(), ITitleFragment {
    override val fragmentTitleRes = R.string.touch_delegate

    private lateinit var binding: FragmentTouchDelegateBinding
    private lateinit var viewModel: ViewPagerViewModel

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        return FragmentTouchDelegateBinding.inflate(inflater, container, false)
            .also { binding = it }.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = ViewModelProvider(this)[ViewPagerViewModel::class.java]
        binding.viewPager.adapter = ChartPagerAdapter(viewLifecycleOwner)

        TabLayoutMediator(binding.tabLayout, binding.viewPager) { tab, position ->
            tab.text = "Chart ${position + 1}"
        }.attach()
    }

    private inner class ChartPagerAdapter(
        private val lifecycleOwner: LifecycleOwner,
    ) : RecyclerView.Adapter<ChartsViewHolder>() {
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ChartsViewHolder {
            return ChartsViewHolder(
                FragmentChartsBinding.inflate(layoutInflater, parent, false),
                lifecycleOwner
            )
        }

        override fun getItemViewType(position: Int): Int = position

        override fun onBindViewHolder(holder: ChartsViewHolder, position: Int) = holder.bind()

        override fun getItemCount(): Int = PAGE_COUNT
    }

    private inner class ChartsViewHolder(
        private val binding: FragmentChartsBinding,
        private val lifecycleOwner: LifecycleOwner,
    ) : RecyclerView.ViewHolder(binding.root) {
        private lateinit var series: SeriesApi
        private var touchDelegateAdded = false

        fun bind() {
            if (!touchDelegateAdded) {
                binding.chartsView.addTouchDelegate(NestedScrollDelegate(requireContext()))
                touchDelegateAdded = true
            }
            binding.chartsView.subscribeOnChartStateChange { state ->
                if (state is ChartsView.State.Ready && ::series.isInitialized.not()) {
                    viewModel.seriesData.observe(lifecycleOwner) { data ->
                        binding.chartsView.api.addAreaSeries { areaSeries ->
                            series = areaSeries
                            series.setData(data.list)
                        }
                    }
                }
            }
        }
    }

    companion object {
        private const val PAGE_COUNT = 5
    }
}
