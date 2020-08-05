package com.tradingview.lightweightcharts.runtime.messaging

open class BridgeMessage(
    val messageType: MessageType,
    val data: Map<String, Any>
) {

    companion object {
        const val UUID = "uuid"
        const val FN = "fn"
        const val RESULT = "result"
        const val MESSAGE = "message"
        const val DEBUG = "debug"
    }

    override fun toString(): String {
        return "${this::class.java.simpleName}(messageType=$messageType, data=$data)"
    }
}