package com.tradingview.lightweightcharts.api.options.models

import com.google.gson.annotations.SerializedName
import com.tradingview.lightweightcharts.api.options.enums.PriceScaleSide

/**
 * Structure describing options of the chart. Series options are to be set separately
 */
data class ChartOptions(
    /**
     * ## Deprecated
     * Watermark is no longer a chart option in Lightweight Charts v5.
     * Use the watermark plugin API [com.tradingview.lightweightcharts.api.interfaces.ChartApi.createTextWatermark] instead.
     *
     * Structure with watermark options
     */
    @Deprecated("Watermark is no longer a chart option in v5. Use ChartApi.createTextWatermark/createImageWatermark instead.")
    var watermark: WatermarkOptions? = null,

    /**
     * Structure with layout options
     */
    var layout: LayoutOptions? = null,

    /**
     * ## Deprecated
     * Use [leftPriceScale]/[rightPriceScale]/[overlayPriceScale] instead.
     *
     * Structure with price scale options
     */
    @Deprecated("Use leftPriceScale/rightPriceScale/overlayPriceScale instead")
    var priceScale: PriceScaleOptions? = null,

    var leftPriceScale: PriceScaleOptions? = null,

    var rightPriceScale: PriceScaleOptions? = null,

    @SerializedName("overlayPriceScales")
    var overlayPriceScale: PriceScaleOptions? = null,

    /**
     * Whether to create the default pane at chart creation time.
     */
    var addDefaultPane: Boolean? = null,

    /**
     * Preferred visible price scale side when both left and right can be used.
     */
    var defaultVisiblePriceScaleId: PriceScaleSide? = null,

    /**
     * Draw the currently hovered series above other series in the same pane.
     */
    var hoveredSeriesOnTop: Boolean? = null,

    var width: Int? = null,

    var height: Int? = null,

    var autoSize: Boolean? = null,

    /**
     * Structure with time scale options
     */
    var timeScale: TimeScaleOptions? = null,

    /**
     * Structure with crosshair options
     */
    var crosshair: CrosshairOptions? = null,

    /**
     * Structure with grid options
     */
    var grid: GridOptions? = null,

    /**
     * Structure with localization options
     */
    var localization: LocalizationOptions? = null,

    /**
     * Structure that describes scrolling behavior
     */
    var handleScroll: HandleScrollOptions? = null,

    /**
     * Structure that describes scaling behavior
     */
    var handleScale: HandleScaleOptions? = null,

    /**
     * Structure that describes kinetic scroll behavior
     */
    var kineticScroll: KineticScrollOptions? = null,

    /**
     * Structure that describes tracking mode's behavior
     */
    var trackingMode: TrackingModeOptions? = null,
)

inline fun chartOptions(init: ChartOptions.() -> Unit): ChartOptions {
    return ChartOptions().apply(init)
}
