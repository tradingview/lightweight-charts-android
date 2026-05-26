package com.tradingview.lightweightcharts.api.delegates

import com.tradingview.lightweightcharts.api.interfaces.WatermarkApi
import com.tradingview.lightweightcharts.api.interfaces.WatermarkApi.Func.WATERMARK_APPLY_OPTIONS
import com.tradingview.lightweightcharts.api.interfaces.WatermarkApi.Func.WATERMARK_DETACH
import com.tradingview.lightweightcharts.api.interfaces.WatermarkApi.Params.OPTIONS
import com.tradingview.lightweightcharts.api.interfaces.WatermarkApi.Params.WATERMARK_ID
import com.tradingview.lightweightcharts.api.options.models.ImageWatermarkOptions
import com.tradingview.lightweightcharts.api.options.models.TextWatermarkOptions
import com.tradingview.lightweightcharts.runtime.controller.WebMessageController
import com.tradingview.lightweightcharts.runtime.version.ChartRuntimeObject

class WatermarkApiDelegate(
    override val uuid: String,
    private val controller: WebMessageController,
    private val kind: Kind,
) : WatermarkApi, ChartRuntimeObject {

    enum class Kind {
        TEXT,
        IMAGE,
    }

    override fun getVersion(): Int = controller.hashCode()

    override fun applyTextOptions(options: TextWatermarkOptions) {
        require(kind == Kind.TEXT) { "Text watermark options can only be applied to text watermarks" }
        controller.callFunction(WATERMARK_APPLY_OPTIONS, mapOf(WATERMARK_ID to uuid, OPTIONS to options))
    }

    override fun applyImageOptions(options: ImageWatermarkOptions) {
        require(kind == Kind.IMAGE) { "Image watermark options can only be applied to image watermarks" }
        controller.callFunction(WATERMARK_APPLY_OPTIONS, mapOf(WATERMARK_ID to uuid, OPTIONS to options))
    }

    override fun detach() {
        controller.callFunction(WATERMARK_DETACH, mapOf(WATERMARK_ID to uuid))
    }
}
