package com.tradingview.lightweightcharts.example.app.view

import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.tabs.TabLayoutMediator
import com.tradingview.lightweightcharts.api.interfaces.SeriesApi
import com.tradingview.lightweightcharts.example.app.R
import com.tradingview.lightweightcharts.example.app.viewmodel.BaseViewModel
import com.tradingview.lightweightcharts.view.ChartsView
import kotlinx.android.synthetic.main.activity_view_pager.*
import kotlinx.android.synthetic.main.fragment_charts.view.*

class ViewPagerActivity : AppCompatActivity() {

    class ChartsViewHolder(
        private val view: View,
        private val activity: AppCompatActivity
    ): RecyclerView.ViewHolder(view) {
        private val viewModel: BaseViewModel =
            ViewModelProvider(activity).get(BaseViewModel::class.java)

        private lateinit var series: SeriesApi

        fun bind() {
            view.charts_view.subscribeOnChartStateChange { state ->
                //Do not add new series when ViewHolder is rebinding
                if (state is ChartsView.State.Ready && ::series.isInitialized.not()) {
                    viewModel.seriesData.observe(activity) { data ->
                        view.charts_view.api.addAreaSeries { areaSeries ->
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
        setContentView(R.layout.activity_view_pager)

        view_pager.adapter = object : RecyclerView.Adapter<ChartsViewHolder>() {
            override fun onCreateViewHolder(
                parent: ViewGroup,
                viewType: Int
            ): ChartsViewHolder {
                val layout = layoutInflater.inflate(R.layout.fragment_charts, parent, false)
                return ChartsViewHolder(layout, this@ViewPagerActivity)
            }

            override fun getItemViewType(position: Int): Int {
                //We should hold the instance of ChartsView as long as possible
                //Every page will create its own ChartsView
                return position
            }

            override fun onBindViewHolder(holder: ChartsViewHolder, position: Int) {
                holder.bind()
            }

            override fun getItemCount(): Int = 5
        }

        TabLayoutMediator(tab_layout, view_pager) { tab, position ->
            tab.text = "Chart ${position + 1}"
            view_pager.setCurrentItem(tab.position, true)
        }.attach()
    }
}