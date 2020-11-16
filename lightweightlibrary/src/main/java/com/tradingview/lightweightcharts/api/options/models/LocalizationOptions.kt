package com.tradingview.lightweightcharts.api.options.models

import com.tradingview.lightweightcharts.runtime.plugins.PriceFormatter
import com.tradingview.lightweightcharts.runtime.plugins.TimeFormatter

data class LocalizationOptions(
    /**
     * Current locale, which will be used for formatting dates.
     */
    val locale: String? = null,

    /**
     * User-defined function for price formatting.
     * Could be used for some specific cases, that could not be covered with PriceFormat
     */
    val priceFormatter: PriceFormatter? = null,

    /**
     * User-defined function for time formatting.
     */
    val timeFormatter: TimeFormatter? = null,

    /**
     * One of predefined options to format time. Ignored if timeFormatter has been specified.
     */
    val dateFormat: String? = null
)
