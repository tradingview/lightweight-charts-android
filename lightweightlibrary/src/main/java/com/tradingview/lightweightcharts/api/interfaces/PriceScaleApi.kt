package com.tradingview.lightweightcharts.api.interfaces

import com.tradingview.lightweightcharts.api.options.models.PriceScaleOptions
import com.tradingview.lightweightcharts.api.series.models.PriceRange

interface PriceScaleApi {

    object Func {
        const val OPTIONS = "priceScaleOptions"
        const val APPLY_OPTIONS = "priceScaleApplyOptions"
        const val WIDTH = "priceScaleWidth"
        const val SET_VISIBLE_RANGE = "priceScaleSetVisibleRange"
        const val GET_VISIBLE_RANGE = "priceScaleGetVisibleRange"
        const val SET_AUTO_SCALE = "priceScaleSetAutoScale"
    }

    object Params {
        const val UUID = "uuid"
        const val OPTIONS_PARAM = "options"
        const val CALLER = "caller"
        const val PRICE_SCALE_ID = "priceScaleId"
        const val PANE_INDEX = "paneIndex"
        const val RANGE = "range"
        const val ON = "on"
    }

    val uuid: String

    /**
     * Applies new options to the price scale
     * @param options any subset of PriceScaleOptions
     */
    fun applyOptions(options: PriceScaleOptions)

    fun applyOptions(options: PriceScaleOptions.() -> Unit) = applyOptions(PriceScaleOptions().apply(options))

    /**
     * Returns currently applied options of the price scale
     * @param onOptionsReceived full set of currently applied options, including defaults
     */
    fun options(onOptionsReceived: (PriceScaleOptions) -> Unit)

    /**
     * Returns a width of the price scale if it's visible or 0 if invisible.
     */
    fun width(onWidthReceived: (Float) -> Unit)

    /**
     * Sets the visible range of the price scale.
     */
    fun setVisibleRange(range: PriceRange)

    /**
     * Returns the visible range of the price scale, or null if range is not set.
     */
    fun getVisibleRange(onRangeReceived: (PriceRange?) -> Unit)

    /**
     * Sets auto scale mode.
     */
    fun setAutoScale(on: Boolean)
}
