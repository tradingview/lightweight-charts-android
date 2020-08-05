package com.tradingview.lightweightcharts.runtime.messaging

class BridgeSubscribeResult(
    bridgeMessage: BridgeMessage
) : BridgeMessage(bridgeMessage.messageType, bridgeMessage.data) {

    val uuid: String get() = data[UUID].toString()

    val result: Any? get() = data[RESULT]

}