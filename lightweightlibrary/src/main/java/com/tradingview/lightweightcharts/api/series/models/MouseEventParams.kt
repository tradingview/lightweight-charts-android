package com.tradingview.lightweightcharts.api.series.models

data class MouseEventParams(
    /**
     * The X coordinate of the mouse pointer in local (DOM content) coordinates.
     */
    val time: Time? = null,
    /**
     * Logical index
     */
    val logical: Int? = null,

    /**
     * Fractional logical index returned by Lightweight Charts v5.
     */
    val logicalFloat: Float? = null,

    /**
     * Location of the event in the chart.
     */
    val point: Point? = null,

    /**
     * Pane index for the event location.
     */
    val paneIndex: Int? = null,

    /**
     * Data of all series at the location of the event in the chart.
     */
    val seriesData: List<BarPrices>? = null,

    /**
     * The SeriesApi uuid at the point of the mouse event.
     */
    val hoveredSeries: String? = null,

    /**
     * Deprecated v5 hovered object id, preserved for compatibility with core payloads.
     */
    val hoveredObjectId: String? = null,

    /**
     * Rich hovered-source metadata from v5 hit testing.
     */
    val hoveredInfo: HoveredInfo? = null,

    /**
     * The underlying source mouse or touch event data, if available
     */
    val sourceEvent: TouchMouseEventData? = null,

)
