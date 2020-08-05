package com.tradingview.lightweightcharts.api.interfaces

import com.tradingview.lightweightcharts.api.series.models.TimeRange
import com.tradingview.lightweightcharts.api.options.models.TimeScaleOptions

interface TimeScaleApi {

    object Func {
        const val SCROLL_POSITION = "scrollPosition"
        const val SCROLL_TO_POSITION = "scrollToPosition"
        const val OPTIONS = "timeScaleOptions"
        const val APPLY_OPTIONS = "timeScaleApplyOptions"
        const val SCROLL_TO_REAL_TIME = "scrollToRealTime"
        const val GET_VISIBLE_RANGE = "getVisibleRange"
        const val SET_VISIBLE_RANGE = "setVisibleRange"
        const val RESET_TIME_SCALE = "resetTimeScale"
        const val FIT_CONTENT = "fitContent"
    }

    object Params {
        const val OPTIONS_PARAM = "options"
        const val TIME_SCALE_ID = "timeScaleId"
        const val POSITION = "position"
        const val ANIMATED = "animated"
        const val RANGE = "range"
    }

    val uuid: String

    /**
     * Returns current scroll position of the chart
     * - Parameter completion: a distance from the right edge to the latest bar, measured in bars
     */
    fun scrollPosition(completion: (Float?) -> Unit)

    /**
     * Scrolls the chart to the specified position
     * - Parameter position: target data position
     * - Parameter animated: setting this to true makes the chart scrolling smooth and adds animation
     */
    fun scrollToPosition(position: Float, animated: Boolean = false)

    /**
     * Restores default scroll position of the chart. This process is always animated.
     */
    fun scrollToRealTime()

    /**
     * Returns current visible time range of the chart
     * - Parameter completion: visible range or null if the chart has no data at all
     */
    fun getVisibleRange(completion: (TimeRange?) -> Unit)

    /**
     * Sets visible range of data
     * - Parameter range: target visible range of data
     */
    fun setVisibleRange(range: TimeRange)

    /**
     * Restores default zooming and scroll position of the time scale
     */
    fun resetTimeScale()

    /**
     * Automatically calculates the visible range to fit all data from all series
     * This is a momentary operation.
     */
    fun fitContent()

    /**
     * Applies new options to the time scale.
     * - Parameter options: any subset of options
     */
    fun applyOptions(options: TimeScaleOptions)

    /**
     * Returns current options
     * - Parameter completion: currently applied options
     */
    fun options(completion: (TimeScaleOptions?) -> Unit)
}
