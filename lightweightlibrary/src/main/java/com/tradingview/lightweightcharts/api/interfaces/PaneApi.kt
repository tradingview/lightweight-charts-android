package com.tradingview.lightweightcharts.api.interfaces

import com.tradingview.lightweightcharts.api.options.models.*
import com.tradingview.lightweightcharts.api.series.enums.SeriesType
import com.tradingview.lightweightcharts.api.series.models.PriceScaleId

interface PaneApi {

    object Func {
        const val GET_PANES = "getPanes"
        const val ADD_PANE = "addPane"
        const val REMOVE_PANE = "removePane"
        const val SWAP_PANES = "swapPanes"
        const val PANE_ADD_SERIES = "paneAddSeries"
        const val PANE_PRICE_SCALE = "panePriceScale"
        const val PANE_GET_HEIGHT = "paneGetHeight"
        const val PANE_SET_HEIGHT = "paneSetHeight"
        const val PANE_MOVE_TO = "paneMoveTo"
        const val PANE_INDEX = "paneIndex"
        const val PANE_GET_SERIES = "paneGetSeries"
        const val PANE_SET_PRESERVE_EMPTY = "paneSetPreserveEmpty"
        const val PANE_PRESERVE_EMPTY = "panePreserveEmpty"
        const val PANE_GET_STRETCH_FACTOR = "paneGetStretchFactor"
        const val PANE_SET_STRETCH_FACTOR = "paneSetStretchFactor"
    }

    object Params {
        const val PANE_ID = "paneId"
        const val PANE_INDEX = "paneIndex"
        const val PRESERVE = "preserve"
        const val PRESERVE_EMPTY_PANE = "preserveEmptyPane"
        const val FIRST = "first"
        const val SECOND = "second"
        const val STRETCH_FACTOR = "stretchFactor"
        const val PRICE_SCALE_ID = "priceScaleId"
        const val SERIES_TYPE = "seriesType"
        const val OPTIONS = "options"
        const val HEIGHT = "height"
    }

    val uuid: String

    fun addSeries(
        type: SeriesType,
        options: SeriesOptionsCommon? = null,
        onSeriesCreated: (api: SeriesApi) -> Unit
    )

    fun priceScale(id: PriceScaleId): PriceScaleApi

    fun getHeight(onHeightReceived: (Int) -> Unit)

    fun setHeight(height: Int)

    fun moveTo(paneIndex: Int)

    fun paneIndex(onPaneIndexReceived: (Int) -> Unit)

    fun getSeries(onSeriesReceived: (List<SeriesApi>) -> Unit)

    fun setPreserveEmptyPane(preserve: Boolean)

    fun preserveEmptyPane(onPreserveReceived: (Boolean) -> Unit)

    fun getStretchFactor(onStretchFactorReceived: (Float) -> Unit)

    fun setStretchFactor(stretchFactor: Float)
}
