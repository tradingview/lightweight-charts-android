package com.tradingview.lightweightcharts.runtime.messaging

import com.google.gson.JsonElement

open class BridgeMessage(
    val messageType: MessageType,
    val data: Data
) {
    override fun toString(): String {
        return "${this::class.java.simpleName}(messageType=$messageType, data=$data)"
    }
}

open class Data(
    open val uuid: String,
    open val fn: String? = null,
    open val params: Map<String, Any>? = null,
    open val result: JsonElement? = null,
    open val message: String? = null,
    open val debug: Boolean? = null
)

