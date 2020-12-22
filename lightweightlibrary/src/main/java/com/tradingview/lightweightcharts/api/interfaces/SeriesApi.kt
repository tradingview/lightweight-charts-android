package com.tradingview.lightweightcharts.api.interfaces

import com.tradingview.lightweightcharts.api.series.common.SeriesData
import com.tradingview.lightweightcharts.api.series.common.PriceFormatter
import com.tradingview.lightweightcharts.api.series.common.PriceLine
import com.tradingview.lightweightcharts.api.options.models.SeriesOptionsCommon
import com.tradingview.lightweightcharts.api.options.models.PriceLineOptions
import com.tradingview.lightweightcharts.api.series.models.SeriesMarker

interface SeriesApi {

    object Func {

        const val ADD_LINE_SERIES = "addLineSeries"
        const val ADD_AREA_SERIES = "addAreaSeries"
        const val ADD_BAR_SERIES = "addBarSeries"
        const val ADD_CANDLESTICK_SERIES = "addCandlestickSeries"
        const val ADD_HISTOGRAM_SERIES = "addHistogramSeries"
        const val SET_SERIES = "setSeries"
        const val PRICE_TO_COORDINATE = "priceToCoordinate"
        const val PRICE_FORMATTER = "priceFormatter"
        const val COORDINATE_TO_PRICE = "coordinateToPrice"
        const val APPLY_OPTIONS = "applyOptions"
        const val SET_MARKERS = "setMarkers"
        const val CREATE_PRICE_LINE = "createPriceLine"
        const val REMOVE_PRICE_LINE = "removePriceLine"
        const val UPDATE = "update"
    }

    object Params {
        const val SERIES_UUID = "seriesId"
        const val PRICE_SCALE_ID = "priceScaleId"
        const val LINE_ID = "lineId"
        const val DATA = "data"
        const val PRICE = "price"
        const val COORDINATE = "coordinate"
        const val OPTIONS = "options"
        const val BAR = "bar"
    }

    val uuid: String

    /**
     * Returns current price formatter
     * @return interface to the price formatter object that can be used
     * to format prices in the same way as the chart does
     */
    fun priceFormatter(): PriceFormatter

    /**
     * Converts specified series price to pixel coordinate according to the chart price scale
     * @param price input price to be converted
     * @param completion pixel coordinate of the price level on the chart
     */
    fun priceToCoordinate(price: Float, completion: (Float?) -> Unit)

    /**
     * Converts specified coordinate to price value according to the series price scale
     * @param coordinate input coordinate to be converted
     * @param completion price value  of the coordinate on the chart
     */
    fun coordinateToPrice(coordinate: Float, completion: (Float?) -> Unit)

    /**
     * Applies new options to the existing series
     * @param options any subset of options
     */
    fun applyOptions(options: SeriesOptionsCommon)

    /**
     * Returns currently applied options
     * @param completion full set of currently applied options, including defaults
     */
    fun options(completion: (SeriesOptionsCommon) -> Unit)

    /**
     * Sets or replaces series data
     * @param data ordered (earlier time point goes first) array of data items.
     * Old data is fully replaced with the new one.
     */
    fun setData(data: List<SeriesData>)

    /**
     * Adds or replaces a new bar
     * @param bar a single data item to be added.
     * Time of the new item must be greater or equal to the latest existing time point.
     * If the new item's time is equal to the last existing item's time,
     * then the existing item is replaced with the new one.
     */
    fun update(bar: SeriesData)

    /**
     * Sets markers for the series
     * @param data array of series markers.
     * This array should be sorted by time.
     * Several markers with same time are allowed.
     */
    fun setMarkers(data: List<SeriesMarker>)

    /**
     * Creates a new price line
     * @param options any subset of options
     */
    fun createPriceLine(options: PriceLineOptions): PriceLine

    /**
     * Removes an existing price line
     * @param line line to remove
     */
    fun removePriceLine(line: PriceLine)

}