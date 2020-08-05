package com.tradingview.lightweightcharts.api.serializer

import com.google.gson.Gson
import com.google.gson.GsonBuilder

abstract class Serializer<T> {

    protected open val gson: Gson by lazy {
        GsonBuilder().create()
    }

    abstract fun serialize(any: Any?): T?
}
