package com.tradingview.lightweightcharts.example.app.view.charts

import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.tradingview.lightweightcharts.api.interfaces.ChartApi
import com.tradingview.lightweightcharts.api.interfaces.SeriesApi
import com.tradingview.lightweightcharts.api.options.models.*
import com.tradingview.lightweightcharts.api.series.enums.*
import com.tradingview.lightweightcharts.api.series.models.LineData
import com.tradingview.lightweightcharts.api.series.models.PriceFormat
import com.tradingview.lightweightcharts.api.series.models.PriceScaleId
import com.tradingview.lightweightcharts.api.series.models.SeriesMarker
import com.tradingview.lightweightcharts.example.app.R
import com.tradingview.lightweightcharts.example.app.model.Data
import com.tradingview.lightweightcharts.example.app.model.SeriesDataType
import com.tradingview.lightweightcharts.example.app.plugins.AutoscaleInfoProvider
import com.tradingview.lightweightcharts.example.app.viewmodel.BaseViewModel
import com.tradingview.lightweightcharts.runtime.plugins.PriceFormatter
import com.tradingview.lightweightcharts.view.ChartsView
import kotlinx.android.synthetic.main.layout_chart_fragment.*

abstract class BaseFragment<V: BaseViewModel>: Fragment() {

    protected lateinit var viewModel: BaseViewModel

    protected val firstChartApi: ChartApi by lazy { charts_view.api }
    protected val secondChartApi: ChartApi by lazy { charts_view_second.api }

    protected var leftSeries: MutableList<SeriesApi> = mutableListOf()
    protected var rightSeries: MutableList<SeriesApi> = mutableListOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        provideViewModel()
        observeViewModelData()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.layout_chart_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        subscribeOnChartReady(charts_view)
        subscribeOnChartReady(charts_view_second)
        applyChartOptions()
    }

    abstract fun provideViewModel()

    protected fun observeViewModelData() {
        viewModel.seriesData.observe(this, { data ->
            createSeriesWithData(data, PriceScaleId.LEFT, firstChartApi) { series ->
                leftSeries.forEach(firstChartApi::removeSeries)
                leftSeries.clear()
                leftSeries.add(series)
            }
            createSeriesWithData(data, PriceScaleId.RIGHT, secondChartApi) { series ->
                rightSeries.forEach(secondChartApi::removeSeries)
                rightSeries.clear()
                rightSeries.add(series)
            }
        })
    }

    protected fun createSeriesWithData(
            data: Data,
            priceScale: PriceScaleId,
            chartApi: ChartApi,
            onSeriesCreated: (SeriesApi) -> Unit
    ) {
        when (data.type) {
            SeriesDataType.AREA -> chartApi.addAreaSeries(
                    options = AreaSeriesOptions(
                            priceFormat = PriceFormat.priceFormatCustom(
                                    PriceFormatter("{price}$!"),
                                    0.02f
                            ),
                            priceScaleId = priceScale,
                            autoscaleInfoProvider = AutoscaleInfoProvider(),
                            priceLineSource = PriceLineSource.LAST_BAR
                    ),
                    onSeriesCreated = { api ->
                        api.setData(data.list.map { it as LineData })
                        api.setMarkers(
                                listOf(
                                        SeriesMarker(
                                                time = data.list[0].time,
                                                position = SeriesMarkerPosition.ABOVE_BAR,
                                                color = Color.BLACK,
                                                shape = SeriesMarkerShape.ARROW_DOWN,
                                                text = "Example",
                                                size = 2
                                        )
                                )
                        )
                        val options = PriceLineOptions(
                                price = 44.1f,
                                //css color green
                                color = Color.rgb(0,128,0),
                                lineWidth = LineWidth.TWO,
                                lineStyle = LineStyle.SOLID,
                                axisLabelVisible = true,
                                title = "P/L 500"
                        )
                        val priceLine = api.createPriceLine(options)
                        api.removePriceLine(priceLine)
                        onSeriesCreated(api)
                    }
            )

            SeriesDataType.LINE -> chartApi.addLineSeries(
                    options = LineSeriesOptions(
                            priceScaleId = priceScale
                    ),
                    onSeriesCreated = { api ->
                        api.setData(data.list)
                        onSeriesCreated(api)
                    }
            )

            SeriesDataType.BAR -> chartApi.addBarSeries(
                    options = BarSeriesOptions(
                            priceScaleId = priceScale
                    ),
                    onSeriesCreated = { api ->
                        api.setData(data.list)
                        onSeriesCreated(api)
                    }
            )

            SeriesDataType.CANDLESTICK -> chartApi.addCandlestickSeries(
                    options = CandlestickSeriesOptions(
                            priceScaleId = priceScale
                    ),
                    onSeriesCreated = { api ->
                        api.setData(data.list)
                        onSeriesCreated(api)
                    }
            )

            SeriesDataType.HISTOGRAM -> chartApi.addHistogramSeries(
                    options = HistogramSeriesOptions(
                            priceScaleId = priceScale
                    ),
                    onSeriesCreated = { api ->
                        api.setData(data.list)
                        onSeriesCreated(api)
                    }
            )
        }
    }

    abstract fun applyChartOptions()

    override fun onDestroyView() {
        super.onDestroyView()
//        realtimeDataJob?.cancelAndJoin()
    }

    protected fun subscribeOnChartReady(view: ChartsView) {
        view.subscribeOnChartStateChange { state ->
            when (state) {
                is ChartsView.State.Preparing -> Unit
                is ChartsView.State.Ready -> {
                    Toast.makeText(context, "Chart ${view.id} is ready", Toast.LENGTH_SHORT).show()
                }
                is ChartsView.State.Error -> {
                    Toast.makeText(context, state.exception.localizedMessage, Toast.LENGTH_LONG).show()
                }
            }
        }
    }
}