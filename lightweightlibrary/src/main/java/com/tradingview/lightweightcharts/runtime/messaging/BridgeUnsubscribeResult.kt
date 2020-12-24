package com.tradingview.lightweightcharts.runtime.messaging

import com.google.gson.JsonElement

class BridgeUnsubscribeResult(
    bridgeMessage: BridgeMessage
) : BridgeMessage(
    bridgeMessage.messageType,
    bridgeMessage.data
) {
    val uuid: String get() = data.uuid
    val result: JsonElement get() = data.result!!
}
