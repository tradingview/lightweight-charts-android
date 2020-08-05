package com.tradingview.lightweightcharts.api.options.models

/**
 * Structure describing options of the chart. Series options are to be set separately
 */
data class ChartOptions(
    /**
        Width of the chart
     */
    val width: Int? = null,

    /**
        Height of the chart
     */
    val height: Int? = null,

    /**
        Structure with watermark options
     */
    val watermark: WatermarkOptions? = null,

    /**
        Structure with layout options
     */
    val layout: LayoutOptions? = null,

    /**
        Structure with price scale options
     */
    val priceScale: PriceScaleOptions? = null,

    /**
        Structure with time scale options
     */
    val timeScale: TimeScaleOptions? = null,

    /**
        Structure with crosshair options
     */
    val crosshair: CrosshairOptions? = null,

    /**
        Structure with grid options
     */
    val grid: GridOptions? = null,

    /**
        Structure with localization options
     */
    val localization: LocalizationOptions? = null,

    /**
        Structure that describes scrolling behavior
     */
    val handleScroll: HandleScrollOptions? = null,

    /**
        Structure that describes scaling behavior
     */
    val handleScale: HandleScaleOptions? = null
)
