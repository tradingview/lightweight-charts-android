package com.tradingview.lightweightcharts.api.options.models

import com.tradingview.lightweightcharts.runtime.plugins.Plugin
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
    val priceFormatter: Plugin? = null,

    /**
     * User-defined function for time formatting.
     */
    val timeFormatter: Plugin? = null,

    /**
     * Date formatting string.
     * Might contains `yyyy`, `yy`, `MMMM`, `MMM`, `MM` and `dd`
     * literals which will be replaced with corresponding date's value.
     * Ignored if timeFormatter has been specified.
     */
    val dateFormat: String? = null
)
