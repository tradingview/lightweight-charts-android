package com.tradingview.lightweightcharts.example.app.view.charts

import android.annotation.SuppressLint
import android.os.Bundle
import android.os.CountDownTimer
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.tradingview.lightweightcharts.api.options.models.CandlestickSeriesOptions
import com.tradingview.lightweightcharts.api.options.models.crosshairOptions
import com.tradingview.lightweightcharts.api.series.enums.CrosshairMode
import com.tradingview.lightweightcharts.api.series.models.Time
import com.tradingview.lightweightcharts.api.series.models.TimeRange
import com.tradingview.lightweightcharts.example.app.R
import com.tradingview.lightweightcharts.example.app.databinding.FragmentChartTimescaleActionsBinding
import com.tradingview.lightweightcharts.example.app.view.util.ITitleFragment
import com.tradingview.lightweightcharts.example.app.viewmodel.RealTimeEmulationViewModel
import com.tradingview.lightweightcharts.view.gesture.TouchDelegate
import kotlinx.coroutines.Job

class TimeScaleActionsFragment : Fragment(), ITitleFragment {
    override val fragmentTitleRes = R.string.timescale_actions

    private val chartsView get() = binding.chartsView
    private val chartApi get() = chartsView.api
    private val timeScaleApi get() = chartApi.timeScale

    private var savedRange: TimeRange? = null
    private var subscribeVisibleRange: TimeRange? = null
    private var captureTouch = false // capture all touch events for log by coordinates

    private lateinit var binding: FragmentChartTimescaleActionsBinding

    private var realtimeDataJob: Job? = null

    private var touchedTime: Time? = null
    private var touchedCoordinateFromTime: Float? = null

    private var touchedLogical: Int? = null
    private var touchedCoordinateFromLogical: Float? = null

    private var refreshTimer: CountDownTimer? = null

    private val onTimeRangeChanged: (params: TimeRange?) -> Unit = {
        subscribeVisibleRange = it
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return FragmentChartTimescaleActionsBinding.inflate(inflater, container, false)
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

        binding.apply {
            chipToRealtime.setOnClickListener { timeScaleApi.scrollToRealTime() }
            chipFit.setOnClickListener { timeScaleApi.fitContent() }
            chipReset.setOnClickListener { timeScaleApi.resetTimeScale() }

            chipSaveRange.setOnClickListener {
                timeScaleApi.getVisibleRange {
                    savedRange = it
                }

                // timeScaleApi.subscribeVisibleTimeRangeChange(onTimeRangeChanged)
            }
            chipRestoreRange.setOnClickListener {
                savedRange?.let { timeScaleApi.setVisibleRange(it) }

                // timeScaleApi.unsubscribeVisibleTimeRangeChange(onTimeRangeChanged)
            }

            chipInterceptTouch.setOnCheckedChangeListener { buttonView, isChecked ->
                captureTouch = isChecked
            }

            chipCustomOptions.setOnClickListener {
                //emulate change in runtime
                timeScaleApi.applyOptions {
                    ticksVisible = true
                }
            }

            chartsView.addTouchDelegate(object : TouchDelegate {
                override fun beforeTouchEvent(view: ViewGroup) = Unit

                override fun onTouchEvent(view: ViewGroup, event: MotionEvent): Boolean {
                    val x = event.x
                    timeScaleApi.coordinateToTime(x) { time ->
                        touchedTime = time
                        if (time != null) timeScaleApi.timeToCoordinate(time) { touchedCoordinateFromTime = it }
                    }
                    timeScaleApi.coordinateToLogical(x) { logical ->
                        touchedLogical = logical
                        if (logical != null) timeScaleApi.logicalToCoordinate(logical) {
                            touchedCoordinateFromLogical = it
                        }
                    }
                    return captureTouch
                }

            })


        }
    }

    override fun onResume() {
        super.onResume()
        refreshTimer = object : CountDownTimer(Long.MAX_VALUE, 100) {
            override fun onTick(millisUntilFinished: Long) {
                refreshUI()
            }

            override fun onFinish() = Unit
        }
        refreshTimer?.start()
    }

    override fun onPause() {
        super.onPause()
        refreshTimer?.cancel()
        refreshTimer = null
    }

    override fun onDestroy() {
        realtimeDataJob?.cancel()
        super.onDestroy()
    }


    private fun refreshUI() {
        timeScaleApi.width { width ->
            timeScaleApi.height { height ->
                binding.tvDebugValues.text = buildString {
                    appendLine("timescale size ${width} - ${height}")
                    if (subscribeVisibleRange != null) appendLine("timerange ${subscribeVisibleRange?.from} - ${subscribeVisibleRange?.to}")
                    appendLine("touched time ${touchedTime}")
                    appendLine("touched logical ${touchedLogical}")
                    appendLine("touched coordinate ${touchedCoordinateFromTime} - ${touchedCoordinateFromLogical}")
                }
            }
        }


    }


}