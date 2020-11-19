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
import com.tradingview.lightweightcharts.api.interfaces.TimeScaleApi.Func.SUBSCRIBE_VISIBLE_TIME_RANGE_CHANGE
import com.tradingview.lightweightcharts.api.interfaces.TimeScaleApi.Params.ANIMATED
import com.tradingview.lightweightcharts.api.interfaces.TimeScaleApi.Params.OPTIONS_PARAM
import com.tradingview.lightweightcharts.api.interfaces.TimeScaleApi.Params.POSITION
import com.tradingview.lightweightcharts.api.interfaces.TimeScaleApi.Params.RANGE
import com.tradingview.lightweightcharts.api.options.models.TimeScaleOptions
import com.tradingview.lightweightcharts.api.serializer.TimeRangeSerializer
import com.tradingview.lightweightcharts.api.serializer.TimeScaleOptionsSerializer
import com.tradingview.lightweightcharts.api.series.models.TimeRange
import com.tradingview.lightweightcharts.runtime.controller.WebMessageController

class TimeScaleApiDelegate(
        private val controller: WebMessageController
) : TimeScaleApi {

    override fun scrollPosition(completion: (Float?) -> Unit) {
        controller.callFunction<Double>(
                SCROLL_POSITION,
                callback = { it!!.toFloat().apply { completion.invoke(this) } }
        )
    }

    override fun scrollToPosition(position: Float, animated: Boolean) {
        controller.callFunction(
                SCROLL_TO_POSITION,
                mapOf(
                        POSITION to position,
                        ANIMATED to animated
                )
        )
    }

    override fun scrollToRealTime() {
        controller.callFunction(
                SCROLL_TO_REAL_TIME
        )
    }

    override fun getVisibleRange(completion: (TimeRange?) -> Unit) {
        controller.callFunction(
                GET_VISIBLE_RANGE,
                callback = completion,
                serializer = TimeRangeSerializer()
        )
    }

    override fun setVisibleRange(range: TimeRange) {
        controller.callFunction(
                SET_VISIBLE_RANGE,
                mapOf(
                        RANGE to range
                )
        )
    }

    override fun resetTimeScale() {
        controller.callFunction(
                RESET_TIME_SCALE
        )
    }

    override fun fitContent() {
        controller.callFunction(
                FIT_CONTENT
        )
    }

    override fun applyOptions(options: TimeScaleOptions, onApply: (Unit?) -> Unit) {
        controller.callFunction(
                APPLY_OPTIONS,
                mapOf(
                        OPTIONS_PARAM to options
                ),
                onApply
        )
    }

    override fun options(completion: (TimeScaleOptions?) -> Unit) {
        controller.callFunction(
                OPTIONS,
                callback = completion,
                serializer = TimeScaleOptionsSerializer()
        )
    }

    override fun subscribeVisibleTimeRangeChange(block: (params: TimeRange?) -> Unit) {
        controller.callSubscribe(
                SUBSCRIBE_VISIBLE_TIME_RANGE_CHANGE,
                callback = block,
                serializer = TimeRangeSerializer()
        )
    }

    override fun unsubscribeVisibleTimeRangeChange(block: (params: TimeRange?) -> Unit) {
        controller.callUnsubscribe(
                SUBSCRIBE_VISIBLE_TIME_RANGE_CHANGE,
                callback = block
        )
    }
}