package com.tradingview.lightweightcharts.api.interfaces

import com.tradingview.lightweightcharts.api.options.models.PriceScaleOptions

interface PriceScaleApi {

    object Func {
        const val OPTIONS = "priceScaleOptions"
        const val APPLY_OPTIONS = "priceScaleApplyOptions"
        const val WIDTH = "priceScaleWidth"
    }

    object Params {
        const val UUID = "uuid"
        const val OPTIONS_PARAM = "options"
    }

    val uuid: String

    /**
     * Applies new options to the price scale
     * @param options any subset of PriceScaleOptions
     */
    fun applyOptions(options: PriceScaleOptions)

    /**
     * Returns currently applied options of the price scale
     * @param onOptionsReceived full set of currently applied options, including defaults
     */
    fun options(onOptionsReceived: (PriceScaleOptions) -> Unit)

    /**
     * Returns a width of the price scale if it's visible or 0 if invisible.
     */
    fun width(onWidthReceived: (Float) -> Unit)
}
