package com.tradingview.lightweightcharts.example.app.view.charts

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.tradingview.lightweightcharts.api.chart.models.color.surface.SolidColor
import com.tradingview.lightweightcharts.api.chart.models.color.surface.VerticalGradientColor
import com.tradingview.lightweightcharts.api.interfaces.SeriesApi
import com.tradingview.lightweightcharts.api.options.models.AreaSeriesOptions
import com.tradingview.lightweightcharts.api.options.models.ChartOptions
import com.tradingview.lightweightcharts.api.options.models.crosshairLineOptions
import com.tradingview.lightweightcharts.api.options.models.crosshairOptions
import com.tradingview.lightweightcharts.api.options.models.gridLineOptions
import com.tradingview.lightweightcharts.api.options.models.gridOptions
import com.tradingview.lightweightcharts.api.options.models.layoutOptions
import com.tradingview.lightweightcharts.api.options.models.watermarkOptions
import com.tradingview.lightweightcharts.api.series.enums.LineStyle
import com.tradingview.lightweightcharts.api.series.enums.LineWidth
import com.tradingview.lightweightcharts.example.app.R
import com.tradingview.lightweightcharts.example.app.databinding.FragmentCustomThemeChartBinding
import com.tradingview.lightweightcharts.example.app.view.util.ITitleFragment
import com.tradingview.lightweightcharts.example.app.view.util.random
import com.tradingview.lightweightcharts.example.app.view.util.randomColor
import com.tradingview.lightweightcharts.example.app.view.util.randomColorTransparent
import com.tradingview.lightweightcharts.example.app.viewmodel.CustomThemesViewModel
import com.tradingview.lightweightcharts.view.ChartsView

class CustomThemesFragment : Fragment(), ITitleFragment {
    override val fragmentTitleRes = R.string.custom_themes

    private val vm by lazy { ViewModelProvider(this).get(CustomThemesViewModel::class.java) }
    private lateinit var binding: FragmentCustomThemeChartBinding

    private val chartApi get() = binding.chartsView.api
    private var seriesApi: SeriesApi? = null

    private val themeChips get() = binding.run { listOf(chipDark, chipLight, chipColorful) }
    private var themeIndex: Int? = null
        set(value) {
            field = value
            refreshUI()
        }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        return FragmentCustomThemeChartBinding.inflate(inflater, container, false)
            .also { binding = it }.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        observeViewModelData()
        subscribeOnChartReady(binding.chartsView)


        themeChips.forEachIndexed { index, chip ->
            chip.setOnClickListener {
                themeIndex = index
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

    private fun observeViewModelData() {
        vm.seriesData.observe(viewLifecycleOwner) { data ->
            chartApi.addAreaSeries(
                options = blueAreaSeriesOptions(),
                onSeriesCreated = { api ->
                    seriesApi = api
                    api.setData(data.list)
                    themeIndex = 0
                }
            )
        }
    }


    private fun refreshUI() {
        if (themeIndex == null)
            return

        themeChips.forEachIndexed { index, chip ->
            chip.isChecked = index == themeIndex
        }

        chartApi.applyOptions {
            when (themeIndex) {
                0 -> darkThemeOptions()
                1 -> lightThemeOptions()
                2 -> colorfulThemeOptions()
            }
        }
        seriesApi?.applyOptions(
            when (themeIndex) {
                2 -> colorfulAreaSeriesOptions()
                else -> blueAreaSeriesOptions()
            }
        )
    }

    private fun ChartOptions.darkThemeOptions() {
        layout = layoutOptions {
            background = SolidColor(resources.randomColor(R.array.dark_gray_array))
            textColor = resources.randomColor(R.array.white_array)
        }
        watermark = watermarkOptions {
            color = resources.randomColorTransparent(R.array.white_array)
        }
        crosshair = crosshairOptions {
            vertLine = crosshairLineOptions {
                color = resources.randomColor(R.array.cyan_array)
                labelBackgroundColor = resources.randomColor(R.array.cyan_array)
            }
            horzLine = crosshairLineOptions {
                color = resources.randomColor(R.array.cyan_array)
                labelBackgroundColor = resources.randomColor(R.array.cyan_array)
            }
        }
        grid = gridOptions {
            vertLines = gridLineOptions {
                color = resources.randomColor(R.array.cyan_array)
            }
            horzLines = gridLineOptions {
                color = resources.randomColor(R.array.cyan_array)
            }
        }
    }

    private fun ChartOptions.lightThemeOptions() {
        layout = layoutOptions {
            background = SolidColor(resources.randomColor(R.array.white_array))
            textColor = resources.randomColor(R.array.dark_gray_array)
        }
        watermark = watermarkOptions {
            color = resources.randomColorTransparent(R.array.dark_gray_array)
        }
        grid = gridOptions {
            vertLines = gridLineOptions {
                color = resources.randomColor(R.array.cyan_array)
            }
            horzLines = gridLineOptions {
                color = resources.randomColor(R.array.cyan_array)
            }
        }
    }


    private fun ChartOptions.colorfulThemeOptions() {
        layout = layoutOptions {
            background = VerticalGradientColor(
                resources.randomColorTransparent(R.array.cyan_array),
                resources.randomColorTransparent(R.array.blue_array),
            )
            textColor = resources.randomColor(R.array.dark_gray_array)
        }
        grid = gridOptions {
            vertLines = gridLineOptions {
                color = resources.randomColor(R.array.all_colorful)
                style = LineStyle.random()
            }
            horzLines = gridLineOptions {
                color = resources.randomColor(R.array.all_colorful)
                style = LineStyle.random()
            }
        }
    }

    private fun blueAreaSeriesOptions(): AreaSeriesOptions =
        AreaSeriesOptions(
            topColor = resources.randomColor(R.array.blue_array),
            bottomColor = resources.randomColorTransparent(R.array.blue_array),
            lineColor = resources.randomColor(R.array.blue_array),
            lineWidth = LineWidth.TWO,
        )


    private fun colorfulAreaSeriesOptions(): AreaSeriesOptions =
        AreaSeriesOptions(
            topColor = resources.randomColor(R.array.all_colorful),
            bottomColor = resources.randomColorTransparent(R.array.all_colorful),
            lineColor = resources.randomColor(R.array.all_colorful),
            lineWidth = LineWidth.TWO,
        )


}