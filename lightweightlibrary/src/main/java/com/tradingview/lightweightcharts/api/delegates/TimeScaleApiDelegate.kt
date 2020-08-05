package com.tradingview.lightweightcharts.api.delegates

import com.tradingview.lightweightcharts.api.interfaces.TimeScaleApi
import com.tradingview.lightweightcharts.api.interfaces.TimeScaleApi.Func.APPLY_OPTIONS
import com.tradingview.lightweightcharts.api.interfaces.TimeScaleApi.Func.FIT_CONTENT
import com.tradingview.lightweightcharts.api.interfaces.TimeScaleApi.Func.GET_VISIBLE_RANGE
import com.tradingview.lightweightcharts.api.interfaces.TimeScaleApi.Func.OPTIONS
import com.tradingview.lightweightcharts.api.interfaces.TimeScaleApi.Func.RESET_TIME_SCALE
import com.tradingview.lightweightcharts.api.interfaces.TimeScaleApi.Func.SCROLL_POSITION
import com.tradingview.lightweightcharts.api.interfaces.TimeScaleApi.Func.SCROLL_TO_POSITION
import com.tradingview.lightweightcharts.api.interfaces.TimeScaleApi.Func.SCROLL_TO_REAL_TIME
import com.tradingview.lightweightcharts.api.interfaces.TimeScaleApi.Func.SET_VISIBLE_RANGE
import com.tradingview.lightweightcharts.api.interfaces.TimeScaleApi.Params.ANIMATED
import com.tradingview.lightweightcharts.api.interfaces.TimeScaleApi.Params.OPTIONS_PARAM
import com.tradingview.lightweightcharts.api.interfaces.TimeScaleApi.Params.POSITION
import com.tradingview.lightweightcharts.api.interfaces.TimeScaleApi.Params.RANGE
import com.tradingview.lightweightcharts.api.interfaces.TimeScaleApi.Params.TIME_SCALE_ID
import com.tradingview.lightweightcharts.api.options.models.TimeScaleOptions
import com.tradingview.lightweightcharts.api.serializer.TimeRangeSerializer
import com.tradingview.lightweightcharts.api.serializer.TimeScaleOptionsSerializer
import com.tradingview.lightweightcharts.api.series.models.TimeRange
import com.tradingview.lightweightcharts.runtime.controller.WebMessageController

class TimeScaleApiDelegate(
    override val uuid: String,
    private val controller: WebMessageController
): TimeScaleApi {

    override fun scrollPosition(completion: (Float?) -> Unit) {
        controller.callFunction<Double>(
            SCROLL_POSITION,
            mapOf(TIME_SCALE_ID to uuid),
            callback = { it!!.toFloat().apply { completion.invoke(this)} }
        )
    }

    override fun scrollToPosition(position: Float, animated: Boolean) {
        controller.callFunction(
            SCROLL_TO_POSITION,
            mapOf(
                TIME_SCALE_ID to uuid,
                POSITION to position,
                ANIMATED to animated
            )
        )
    }

    override fun scrollToRealTime() {
        controller.callFunction(
            SCROLL_TO_REAL_TIME,
            mapOf(TIME_SCALE_ID to uuid)
        )
    }

    override fun getVisibleRange(completion: (TimeRange?) -> Unit) {
        controller.callFunction(
            GET_VISIBLE_RANGE,
            mapOf(TIME_SCALE_ID to uuid),
            callback = completion,
            serializer = TimeRangeSerializer()
        )
    }

    override fun setVisibleRange(range: TimeRange) {
        controller.callFunction(
            SET_VISIBLE_RANGE,
            mapOf(
                TIME_SCALE_ID to uuid,
                RANGE to range
            )
        )
    }

    override fun resetTimeScale() {
        controller.callFunction(
            RESET_TIME_SCALE,
            mapOf(TIME_SCALE_ID to uuid)
        )
    }

    override fun fitContent() {
        controller.callFunction(
            FIT_CONTENT,
            mapOf(TIME_SCALE_ID to uuid)
        )
    }

    override fun applyOptions(options: TimeScaleOptions) {
        controller.callFunction(
            APPLY_OPTIONS,
            mapOf(
                TIME_SCALE_ID to uuid,
                OPTIONS_PARAM to options
            )
        )
    }

    override fun options(completion: (TimeScaleOptions?) -> Unit) {
        controller.callFunction(
            OPTIONS,
            mapOf(TIME_SCALE_ID to uuid),
            callback = completion,
            serializer = TimeScaleOptionsSerializer()
        )
    }

}