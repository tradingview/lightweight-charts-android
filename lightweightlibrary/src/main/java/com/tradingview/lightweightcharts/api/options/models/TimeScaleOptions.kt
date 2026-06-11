package com.tradingview.lightweightcharts.api.options.models

import com.tradingview.lightweightcharts.api.chart.models.color.IntColor
import com.tradingview.lightweightcharts.api.options.enums.ConflationPriority
import com.tradingview.lightweightcharts.runtime.plugins.Plugin

data class TimeScaleOptions(
    /**
     * Sets the margin space in bars from the right side of the chart
     */
    var rightOffset: Float? = null,

    /**
     * Sets the space between bars in pixels
     */
    var barSpacing: Float? = null,

    /**
     * Sets the min space between bars in pixels
     */
    var minBarSpacing: Float? = null,

    /**
     * If true, prevents scrolling to the left of the first historical bar
     */
    var fixLeftEdge: Boolean? = null,

    /**
     * If true, prevents scrolling to the right of the most recent bar
     */
    var fixRightEdge: Boolean? = null,

    /**
     * If true, prevents changing visible time area during chart resizing
     */
    var lockVisibleTimeRangeOnResize: Boolean? = null,

    /**
     * If false, the hovered bar remains in the same place when scrolling
     */
    var rightBarStaysOnScroll: Boolean? = null,

    /**
     * If true, the time scale border is visible
     */
    var borderVisible: Boolean? = null,

    /**
     * The time scale border color
     */
    var borderColor: IntColor? = null,

    /**
     * If true, the time scale is shown on a chart
     */
    var visible: Boolean? = null,

    /**
     * If true, the time is shown on the time scale and in the vertical crosshair label
     */
    var timeVisible: Boolean? = null,

    /**
     * If true, seconds are shown on the label of the crosshair vertical line
     * in hh:mm:ss format on intraday intervals
     */
    var secondsVisible: Boolean? = null,

    /**
     * If true, the visible range is shifted by the number of new bars
     * when new bars are added (note that this only applies when the last bar is visible)
     */
    var shiftVisibleRangeOnNewBar: Boolean? = null,

    /**
     * Allow the visible range to be shifted to the right when a new bar is added
     * which is replacing an existing whitespace time point on the chart
     */
    var allowShiftVisibleRangeOnWhitespaceReplacement: Boolean? = null,

    /**
     * Allows to override the tick marks formatter
     */
    var tickMarkFormatter: Plugin? = null,

    /**
     * Draw small vertical line on time axis labels.
     */
    var ticksVisible: Boolean? = null,

    /**
     * Sets the max space between bars in pixels.
     */
    var maxBarSpacing: Float? = null,

    /**
     * Sets right offset in pixels. Takes precedence over [rightOffset].
     */
    var rightOffsetPixels: Float? = null,

    /**
     * Maximum tick mark label length.
     */
    var tickMarkMaxCharacterLength: Int? = null,

    /**
     * Minimum time scale height.
     */
    var minimumHeight: Int? = null,

    /**
     * Changes horizontal alignment of the time scale tick marks to a uniform distribution.
     */
    var uniformDistribution: Boolean? = null,

    /**
     * Allow major time scale labels to be rendered in a bolder font weight.
     */
    var allowBoldLabels: Boolean? = null,

    /**
     * Ignore time scale points containing only whitespace
     * when drawing grid lines, tick marks, and snapping the crosshair.
     */
    var ignoreWhitespaceIndices: Boolean? = null,

    var enableConflation: Boolean? = null,
    var conflationThresholdFactor: Float? = null,
    var precomputeConflationOnInit: Boolean? = null,
    var precomputeConflationPriority: ConflationPriority? = null,
)

inline fun timeScaleOptions(init: TimeScaleOptions.() -> Unit): TimeScaleOptions {
    return TimeScaleOptions().apply(init)
}
