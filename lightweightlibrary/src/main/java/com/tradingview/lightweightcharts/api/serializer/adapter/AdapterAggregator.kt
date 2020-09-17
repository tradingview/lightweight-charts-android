package com.tradingview.lightweightcharts.api.serializer.adapter

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.tradingview.lightweightcharts.api.series.enums.*
import com.tradingview.lightweightcharts.api.series.models.BarPrices
import com.tradingview.lightweightcharts.api.series.models.Time


fun GsonBuilder.registerDefaultAdapters(): GsonBuilder {
    registerTypeAdapter(Time::class.java, Time.TimeAdapter())
    registerTypeAdapter(BarPrices::class.java, BarPrices.BarPricesAdapter())

    //series enums
    registerTypeAdapter(CrosshairMode::class.java, CrosshairMode.CrosshairModeAdapter())
    registerTypeAdapter(LineStyle::class.java, LineStyle.LineStyleAdapter())
    registerTypeAdapter(LineType::class.java, LineType.LineTypeAdapter())
    registerTypeAdapter(LineWidth::class.java, LineWidth.LineWidthAdapter())
    registerTypeAdapter(PriceScaleMode::class.java, PriceScaleMode.PriceScaleModeAdapter())

    return this
}

object GsonProvider {
    fun newInstance(): Gson {
        return GsonBuilder()
            .registerDefaultAdapters()
            .create()
    }
}