package com.tradingview.lightweightcharts.api.options.models

/**
 * Structure describing options of the chart. Series options are to be set separately
 */
class ChartOptions(
    /**
     * Structure with watermark options
     */
    val watermark: WatermarkOptions? = null,

    /**
     * Structure with layout options
     */
    val layout: LayoutOptions? = null,

    /**
     * ## Deprecated
     * Use [leftPriceScale]/[rightPriceScale]/[overlayPriceScale] instead.
     *
     * Structure with price scale options
     */
    @Deprecated("Use leftPriceScale/rightPriceScale/overlayPriceScale instead")
    val priceScale: PriceScaleOptions? = null,

    val leftPriceScale: PriceScaleOptions? = null,

    val rightPriceScale: PriceScaleOptions? = null,

    //TODO: Omit<PriceScaleOptions, 'visible' | 'autoScale'>
    val overlayPriceScale: PriceScaleOptions? = null,

    /**
     * Structure with time scale options
     */
    val timeScale: TimeScaleOptions? = null,

    /**
     * Structure with crosshair options
     */
    val crosshair: CrosshairOptions? = null,

    /**
     * Structure with grid options
     */
    val grid: GridOptions? = null,

    /**
     * Structure with localization options
     */
    val localization: LocalizationOptions? = null,

    /**
     * Structure that describes scrolling behavior
     */
    val handleScroll: HandleScrollOptions? = null,

    /**
     * Structure that describes scaling behavior
     */
    val handleScale: HandleScaleOptions? = null
)
