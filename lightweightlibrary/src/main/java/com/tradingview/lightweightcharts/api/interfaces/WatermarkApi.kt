package com.tradingview.lightweightcharts.api.interfaces

import com.tradingview.lightweightcharts.api.options.models.ImageWatermarkOptions
import com.tradingview.lightweightcharts.api.options.models.TextWatermarkOptions

interface WatermarkApi {
    object Func {
        const val CREATE_TEXT_WATERMARK = "createTextWatermark"
        const val CREATE_IMAGE_WATERMARK = "createImageWatermark"
        const val WATERMARK_APPLY_OPTIONS = "watermarkApplyOptions"
        const val WATERMARK_DETACH = "watermarkDetach"
    }

    object Params {
        const val WATERMARK_ID = "watermarkId"
        const val PANE_ID = "paneId"
        const val PANE_INDEX = "paneIndex"
        const val IMAGE_URL = "imageUrl"
        const val OPTIONS = "options"
    }

    val uuid: String

    fun applyTextOptions(options: TextWatermarkOptions)

    fun applyImageOptions(options: ImageWatermarkOptions)

    fun detach()
}
