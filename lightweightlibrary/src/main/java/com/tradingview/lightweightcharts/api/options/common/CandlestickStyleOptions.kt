package com.tradingview.lightweightcharts.api.options.common

import com.tradingview.lightweightcharts.api.series.models.color.Colorable

/** Structure describing a drawing style of the candlestick chart  */
interface CandlestickStyleOptions {
    /** Color of rising candlesticks */
    val upColor: Colorable?

    /** Color of falling candlesticks */
    val downColor: Colorable?

    /** Flag to draw/hide candlestick wicks */
    val wickVisible: Boolean?

    /** Flag to draw/hide candlestick borders around bodies */
    val borderVisible: Boolean?

    /**
     * Color of borders around candles' bodies. Ignored if borderVisible == false
     * If specified, it overrides both borderUpColor and borderDownColor options
     */
    val borderColor: Colorable?

    /** Color of the border of rising candlesticks. Ignored if borderVisible == false or borderColor is specified */
    val borderUpColor: Colorable?

    /** Color of the border of rising candlesticks. Ignored if borderVisible == false or borderColor is specified */
    val borderDownColor: Colorable?

    /**
     * Color of candlestick wicks. Ignored if wickVisible == false
     * If specified, it overrides both wickUpColor and wickDownColor options
     */
    val wickColor: Colorable?

    /** Color of rising candlestick wicks. Ignored if wickVisible == false or wickColor is specified */
    val wickUpColor: Colorable?

    /** Color of falling candlestick wicks. Ignored if wickVisible == false or wickColor is specified */
    val wickDownColor: Colorable?
}