package com.tradingview.lightweightcharts.api.serializer.gson

import android.util.Log
import com.google.gson.Gson
import com.google.gson.TypeAdapter
import com.google.gson.TypeAdapterFactory
import com.google.gson.reflect.TypeToken
import com.google.gson.stream.JsonReader
import com.google.gson.stream.JsonWriter
import com.tradingview.lightweightcharts.api.series.models.ColorWrapper.IntColor
import java.io.IOException


class ForceNullAdapterFactory: TypeAdapterFactory {

    override fun <T : Any?> create(gson: Gson?, type: TypeToken<T>?): TypeAdapter<T>? {
        val name = type!!.rawType.name
        val canonicalName = type!!.rawType.canonicalName
//        if (!Integer::class.java.isAssignableFrom(type!!.rawType)) { return null }

        val delegate = gson!!.getDelegateAdapter(this, type)
        return createCustomTypeAdapter(delegate)
    }

    private fun <T> createCustomTypeAdapter(delegate: TypeAdapter<T>): TypeAdapter<T> {
        return object : TypeAdapter<T>() {
            @Throws(IOException::class)
            override fun write(out: JsonWriter, value: T?) {
                if (value == null) {
                    val serializeNulls: Boolean = out.serializeNulls
                    try {
                        out.serializeNulls = true
                        out.value("")
                    } finally {
                        out.serializeNulls = serializeNulls
                    }
                } else {
                    delegate.write(out, value)
                }


            }

            @Throws(IOException::class)
            override fun read(reader: JsonReader): T {
                return delegate.read(reader)
            }
        }
    }
}