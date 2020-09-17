package com.tradingview.lightweightcharts.api.serializer

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.tradingview.lightweightcharts.api.serializer.adapter.GsonProvider

abstract class Serializer<T> {

    protected open val gson: Gson by lazy { GsonProvider.newInstance() }

    abstract fun serialize(any: Any?): T?
}
