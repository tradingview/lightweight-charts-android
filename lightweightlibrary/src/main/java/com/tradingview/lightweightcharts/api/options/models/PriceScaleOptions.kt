package com.tradingview.lightweightcharts.api.options.models

import com.tradingview.lightweightcharts.api.series.enums.PriceScaleMode
import com.tradingview.lightweightcharts.api.options.enums.PriceAxisPosition

open class PriceScaleOptions(

        /**
         * True makes chart calculate the price range automatically based on the visible data range
         */
        val autoScale: Boolean? = null,

        /**
         * Mode of the price scale
         */
        val mode: PriceScaleMode? = null,

        /**
         * True inverts the scale. Makes larger values drawn lower.
         * Affects both the price scale and the data on the chart
         */
        val invertScale: Boolean? = null,

        /**
         * True value prevents labels on the price scale from overlapping
         * one another by aligning them one below others
         */
        val alignLabels: Boolean? = null,

        /**
         * Defines position of the price scale on the chart
         */
        @Deprecated("")
        open val position: PriceAxisPosition? = null,

        /**
         * Defines price margins for the price scale
         */
        val scaleMargins: PriceScaleMargins? = null,

        /**
         * Set true to draw a border between the price scale and the chart area
         */
        val borderVisible: Boolean? = null,

        /**
         * Defines a color of the border between the price scale and the chart area.
         * It is ignored if borderVisible is false
         */
        val borderColor: String? = null,

        /**
         * Indicates whether the price scale displays only full lines of text or partial lines.
         */
        val entireTextOnly: Boolean? = null,

        /**
         * Indicates if this price scale visible. Could not be applied to overlay price scale
         */
        val visible: Boolean? = null,

        /**
         * True value add a small horizontal ticks on price axis labels
         */
        val drawTicks: Boolean? = null
)
