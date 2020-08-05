package com.tradingview.lightweightcharts.api.options.models

import com.tradingview.lightweightcharts.api.series.enums.LineStyle
import com.tradingview.lightweightcharts.api.series.enums.LineWidth
import com.tradingview.lightweightcharts.api.series.models.PriceFormat
import com.tradingview.lightweightcharts.api.options.common.SeriesSpecificOptions

/**
 * Structure describing options common for all types of series
 */
interface SeriesOptionsCommon: SeriesSpecificOptions {
    /** Visibility of the label with the latest visible price on the price scale */
    val lastValueVisible: Boolean?
    /** Title of the series. This label is placed with price axis label */
    val title: String?
    /** Visibility of the price line. Price line is a horizontal line indicating the last price of the series */
    val priceLineVisible: Boolean?
    /** Width of the price line. Ignored if priceLineVisible is false */
    val priceLineWidth: LineWidth?
    /** Color of the price line. Ignored if priceLineVisible is false */
    val priceLineColor: String?
    /** Price line style. Suitable for percentage and indexedTo100 scales */
    val priceLineStyle: LineStyle?
    /** Formatting settings associated with the series */
    val priceFormat: PriceFormat?
    /** Visibility of base line. Suitable for percentage and indexedTo100 scales */
    val baseLineVisible: Boolean?
    /** Color of the base line in IndexedTo100 mode */
    val baseLineColor: String?
    /** Base line width. Suitable for percentage and indexedTo100 scales. Ignored if baseLineVisible is not set */
    val baseLineWidth: LineWidth?
    /** Base line style. Suitable for percentage and indexedTo100 scales. Ignored if baseLineVisible is not set */
    val baseLineStyle: LineStyle?
}
