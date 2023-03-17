package com.tradingview.lightweightcharts.example.app.view.pager

import android.os.Bundle
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.tabs.TabLayoutMediator
import com.tradingview.lightweightcharts.api.interfaces.SeriesApi
import com.tradingview.lightweightcharts.example.app.databinding.ActivityViewPagerBinding
import com.tradingview.lightweightcharts.example.app.databinding.FragmentChartsBinding
import com.tradingview.lightweightcharts.example.app.viewmodel.ViewPagerViewModel
import com.tradingview.lightweightcharts.view.ChartsView

class ViewPagerActivity : AppCompatActivity() {

    class ChartsViewHolder(

        private val binding: FragmentChartsBinding,
        private val activity: AppCompatActivity,
    ) : RecyclerView.ViewHolder(binding.root) {
        private val viewModel: ViewPagerViewModel =
            ViewModelProvider(activity).get(ViewPagerViewModel::class.java)

        private lateinit var series: SeriesApi

        fun bind() {
            binding.chartsView.addTouchDelegate(NestedScrollDelegate(activity))
            binding.chartsView.subscribeOnChartStateChange { state ->
                //Do not add new series when ViewHolder is rebinding
                if (state is ChartsView.State.Ready && ::series.isInitialized.not()) {
                    viewModel.seriesData.observe(activity) { data ->
                        binding.chartsView.api.addAreaSeries { areaSeries ->
                            series = areaSeries
                            series.setData(data.list)
                        }
                    }
                }
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityViewPagerBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.viewPager.adapter = object : RecyclerView.Adapter<ChartsViewHolder>() {
            override fun onCreateViewHolder(
                parent: ViewGroup,
                viewType: Int,
            ): ChartsViewHolder =
                ChartsViewHolder(
                    FragmentChartsBinding.inflate(layoutInflater, parent, false),
                    this@ViewPagerActivity
                )


            override fun getItemViewType(position: Int): Int {
                //We should hold the instance of ChartsView as long as possible
                //Every page will create its own ChartsView
                return position
            }

            override fun onBindViewHolder(holder: ChartsViewHolder, position: Int) = holder.bind()

            override fun getItemCount(): Int = 5
        }

        TabLayoutMediator(binding.tabLayout, binding.viewPager) { tab, position ->
            tab.text = "Chart ${position + 1}"
            binding.viewPager.setCurrentItem(tab.position, true)
        }.attach()
    }
}