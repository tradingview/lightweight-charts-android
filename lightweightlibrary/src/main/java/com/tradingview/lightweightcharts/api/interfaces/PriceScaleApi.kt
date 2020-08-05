package com.tradingview.lightweightcharts.api.interfaces

import com.tradingview.lightweightcharts.api.options.models.PriceScaleOptions

interface PriceScaleApi {

    object Func {
        const val OPTIONS = "priceScaleOptions"
        const val APPLY_OPTIONS = "priceScaleApplyOptions"
    }

    object Params {
        const val PRICE_SCALE_ID = "priceScaleId"
        const val OPTIONS_PARAM = "options"
    }

    val uuid: String

    /**
     * Applies new options to the price scale
     * - Parameter options: any subset of PriceScaleOptions
     */
    fun applyOptions(options: PriceScaleOptions)

    /**
     * Returns currently applied options of the price scale
     * - Parameter completion: full set of currently applied options, including defaults
     */
    fun options(completion: (PriceScaleOptions?) -> Unit)

}
