package com.tradingview.lightweightcharts.api.interfaces

import com.tradingview.lightweightcharts.api.series.models.TimeRange
import com.tradingview.lightweightcharts.api.options.models.TimeScaleOptions
import com.tradingview.lightweightcharts.api.series.models.Time

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
        const val SUBSCRIBE_VISIBLE_TIME_RANGE_CHANGE = "subscribeVisibleTimeRangeChange"
        const val TIME_TO_COORDINATE = "timeToCoordinate"
        const val COORDINATE_TO_TIME = "coordinateToTime"
    }

    object Params {
        const val OPTIONS_PARAM = "options"
        const val POSITION = "position"
        const val ANIMATED = "animated"
        const val RANGE = "range"
    }

    /**
     * Returns current scroll position of the chart
     * @param completion a distance from the right edge to the latest bar, measured in bars
     */
    fun scrollPosition(completion: (Float) -> Unit)

    /**
     * Scrolls the chart to the specified position
     * @param position target data position
     * @param animated setting this to true makes the chart scrolling smooth and adds animation
     */
    fun scrollToPosition(position: Float, animated: Boolean = false)

    /**
     * Restores default scroll position of the chart. This process is always animated.
     */
    fun scrollToRealTime()

    /**
     * Returns current visible time range of the chart
     * @param completion visible range or null if the chart has no data at all
     */
    fun getVisibleRange(completion: (TimeRange?) -> Unit)

    /**
     * Sets visible range of data
     * @param range target visible range of data
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
     * Converts a time to local x coordinate.
     * @param time time needs to be converted
     * @param onCoordinateReceive returns x coordinate of that time
     *                            or null if no time found on time scale
     */
    fun timeToCoordinate(time: Time, onCoordinateReceive: (x: Double?) -> Unit)

    /**
     * Converts a coordinate to time.
     * @param x coordinate needs to be converted
     * @param onTimeReceive the time of a bar that placed on that coordinate
     *                      or null if no bar found at this coordinate
     */
    fun coordinateToTime(x: Double, onTimeReceive: (time: Time?) -> Unit)

    /**
     * Applies new options to the time scale.
     * @param options any subset of options
     */
    fun applyOptions(options: TimeScaleOptions, onApply: () -> Unit = {})

    /**
     * Returns current options
     * @param completion currently applied options
     */
    fun options(completion: (TimeScaleOptions) -> Unit)

    /**
     * Adds a subscription to visible range changes
     * to receive notification about visible range of data changes
     */
    fun subscribeVisibleTimeRangeChange(block: (params: TimeRange?) -> Unit)

    /**
     * Removes a subscription to visible range changes
     */
    fun unsubscribeVisibleTimeRangeChange(block: (params: TimeRange?) -> Unit)
}
