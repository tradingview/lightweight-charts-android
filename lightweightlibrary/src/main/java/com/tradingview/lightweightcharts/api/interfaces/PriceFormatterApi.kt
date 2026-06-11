package com.tradingview.lightweightcharts.api.interfaces

/**
 * Interface to be implemented by the object in order to be used as a price formatter
 */
interface PriceFormatterApi {

    object Func {
        const val PRICE_FORMATTER_FORMAT = "priceFormatterFormat"
    }

    /**
     * Formats the price using the series price formatter
     * @param price the price to be formatted
     * @param onFormatted the formatted price
     */
    fun format(price: Float, onFormatted: (String) -> Unit)
}
