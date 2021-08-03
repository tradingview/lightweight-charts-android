package com.tradingview.lightweightcharts.api.options.common

import com.tradingview.lightweightcharts.api.series.models.ColorWrapper

/** Structure describing a drawing style of the candlestick chart  */
interface CandlestickStyleOptions {
    /** Color of rising candlesticks */
    val upColor: ColorWrapper?

    /** Color of falling candlesticks */
    val downColor: ColorWrapper?

    /** Flag to draw/hide candlestick wicks */
    val wickVisible: Boolean?

    /** Flag to draw/hide candlestick borders around bodies */
    val borderVisible: Boolean?

    /**
     * Color of borders around candles' bodies. Ignored if borderVisible == false
     * If specified, it overrides both borderUpColor and borderDownColor options
     */
    val borderColor: ColorWrapper?

    /** Color of the border of rising candlesticks. Ignored if borderVisible == false or borderColor is specified */
    val borderUpColor: ColorWrapper?

    /** Color of the border of rising candlesticks. Ignored if borderVisible == false or borderColor is specified */
    val borderDownColor: ColorWrapper?

    /**
     * Color of candlestick wicks. Ignored if wickVisible == false
     * If specified, it overrides both wickUpColor and wickDownColor options
     */
    val wickColor: ColorWrapper?

    /** Color of rising candlestick wicks. Ignored if wickVisible == false or wickColor is specified */
    val wickUpColor: ColorWrapper?

    /** Color of falling candlestick wicks. Ignored if wickVisible == false or wickColor is specified */
    val wickDownColor: ColorWrapper?
}