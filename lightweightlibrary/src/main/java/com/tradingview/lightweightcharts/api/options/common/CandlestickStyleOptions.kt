package com.tradingview.lightweightcharts.api.options.common

/** Structure describing a drawing style of the candlestick chart  */
interface CandlestickStyleOptions {
    /** Color of rising candlesticks */
    val upColor: String?

    /** Color of falling candlesticks */
    val downColor: String?

    /** Flag to draw/hide candlestick wicks */
    val wickVisible: Boolean?

    /** Flag to draw/hide candlestick borders around bodies */
    val borderVisible: Boolean?

    /**
     * Color of borders around candles' bodies. Ignored if borderVisible == false
     * If specified, it overrides both borderUpColor and borderDownColor options
     */
    val borderColor: String?

    /** Color of the border of rising candlesticks. Ignored if borderVisible == false or borderColor is specified */
    val borderUpColor: String?

    /** Color of the border of rising candlesticks. Ignored if borderVisible == false or borderColor is specified */
    val borderDownColor: String?

    /**
     * Color of candlestick wicks. Ignored if wickVisible == false
     * If specified, it overrides both wickUpColor and wickDownColor options
     */
    val wickColor: String?

    /** Color of rising candlestick wicks. Ignored if wickVisible == false or wickColor is specified */
    val wickUpColor: String?

    /** Color of falling candlestick wicks. Ignored if wickVisible == false or wickColor is specified */
    val wickDownColor: String?
}