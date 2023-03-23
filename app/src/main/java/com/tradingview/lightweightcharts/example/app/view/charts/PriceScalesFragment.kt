package com.tradingview.lightweightcharts.example.app.view.charts

import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.tradingview.lightweightcharts.api.chart.models.color.surface.SolidColor
import com.tradingview.lightweightcharts.api.chart.models.color.toIntColor
import com.tradingview.lightweightcharts.api.interfaces.ChartApi
import com.tradingview.lightweightcharts.api.interfaces.SeriesApi
import com.tradingview.lightweightcharts.api.options.models.LineSeriesOptions
import com.tradingview.lightweightcharts.api.options.models.PriceLineOptions
import com.tradingview.lightweightcharts.api.options.models.crosshairLineOptions
import com.tradingview.lightweightcharts.api.options.models.crosshairOptions
import com.tradingview.lightweightcharts.api.options.models.gridLineOptions
import com.tradingview.lightweightcharts.api.options.models.gridOptions
import com.tradingview.lightweightcharts.api.options.models.handleScrollOptions
import com.tradingview.lightweightcharts.api.options.models.layoutOptions
import com.tradingview.lightweightcharts.api.options.models.priceScaleMargins
import com.tradingview.lightweightcharts.api.options.models.priceScaleOptions
import com.tradingview.lightweightcharts.api.series.enums.LastPriceAnimationMode
import com.tradingview.lightweightcharts.api.series.enums.LineStyle
import com.tradingview.lightweightcharts.api.series.enums.LineWidth
import com.tradingview.lightweightcharts.api.series.models.PriceScaleId
import com.tradingview.lightweightcharts.example.app.R
import com.tradingview.lightweightcharts.example.app.databinding.LayoutChartFragmentBinding
import com.tradingview.lightweightcharts.example.app.model.Data
import com.tradingview.lightweightcharts.example.app.view.util.ITitleFragment
import com.tradingview.lightweightcharts.example.app.viewmodel.PriceLinesWithTitlesViewModel
import com.tradingview.lightweightcharts.view.ChartsView

class PriceScalesFragment : Fragment(), ITitleFragment {
    override val fragmentTitleRes = R.string.price_scales

    private lateinit var viewModel: PriceLinesWithTitlesViewModel

    private lateinit var binding: LayoutChartFragmentBinding

    private var series: MutableList<SeriesApi> = mutableListOf()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        return LayoutChartFragmentBinding.inflate(inflater, container, false)
            .also { binding = it }.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        provideViewModel()
        observeViewModelData()
        subscribeOnChartReady(binding.chartsView)
        applyChartOptions()
    }

    private fun provideViewModel() {
        viewModel = ViewModelProvider(this).get(PriceLinesWithTitlesViewModel::class.java)
    }

    private fun observeViewModelData() {
        viewModel.seriesData.observe(viewLifecycleOwner) { data ->
            createSeriesWithData(data, PriceScaleId.RIGHT, binding.chartsView.api) { series ->
                this.series.clear()
                this.series.add(series)

                viewModel.fetchPrices()

                series.createPriceLine(
                    PriceLineOptions(
                        price = viewModel.minimumPrice,
                        color = Color.parseColor("#be1238").toIntColor(),
                        lineWidth = LineWidth.TWO,
                        lineStyle = LineStyle.SOLID,
                        axisLabelVisible = true,
                        title = "minimum price",
                    )
                )

                series.createPriceLine(
                    PriceLineOptions(
                        price = viewModel.avgPrice,
                        color = Color.parseColor("#be1238").toIntColor(),
                        lineWidth = LineWidth.TWO,
                        lineStyle = LineStyle.SOLID,
                        axisLabelVisible = true,
                        title = "average price",
                    )
                )

                val priceLine = series.createPriceLine(
                    PriceLineOptions(
                        price = viewModel.maximumPrice,
                        color = Color.parseColor("#be1238").toIntColor(),
                        lineWidth = LineWidth.TWO,
                        lineStyle = LineStyle.SOLID,
                        axisLabelVisible = true,
                        title = "maximum price",
                    )
                )

                binding.chartsView.api.timeScale.fitContent()
            }
        }
    }

    private fun subscribeOnChartReady(view: ChartsView) {
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

    private fun applyChartOptions() {
        binding.chartsView.api.applyOptions {
            layout = layoutOptions {
                textColor = Color.parseColor("#d1d4dc").toIntColor()
                background = SolidColor(Color.BLACK)
            }
            rightPriceScale = priceScaleOptions {
                scaleMargins = priceScaleMargins {
                    top = 0.3f
                    bottom = 0.25f
                }
            }
            crosshair = crosshairOptions {
                vertLine = crosshairLineOptions {
                    width = LineWidth.THREE
                    color = Color.YELLOW.toIntColor()
                    style = LineStyle.SOLID
                }
                horzLine = crosshairLineOptions {
                    visible = false
                    labelVisible = false
                }
            }
            grid = gridOptions {
                vertLines = gridLineOptions {
                    color = Color.argb(0, 42, 46, 57).toIntColor()
                }
                horzLines = gridLineOptions {
                    color = Color.argb(0, 42, 46, 57).toIntColor()
                }
            }
            handleScroll = handleScrollOptions {
                vertTouchDrag = false
            }
        }
    }

    private fun createSeriesWithData(
        data: Data,
        priceScale: PriceScaleId,
        chartApi: ChartApi,
        onSeriesCreated: (SeriesApi) -> Unit,
    ) {
        chartApi.addLineSeries(
            options = LineSeriesOptions(
                color = Color.rgb(0, 120, 255).toIntColor(),
                lineWidth = LineWidth.TWO,
                crosshairMarkerVisible = false,
                lastValueVisible = false,
                priceLineVisible = false,
                lastPriceAnimation = LastPriceAnimationMode.CONTINUOUS
            ),
            onSeriesCreated = { api ->
                api.setData(data.list)
                onSeriesCreated(api)
            }
        )
    }
}