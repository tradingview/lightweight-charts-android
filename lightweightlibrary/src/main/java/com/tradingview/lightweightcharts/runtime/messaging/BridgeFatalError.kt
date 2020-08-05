package com.tradingview.lightweightcharts.runtime.messaging

class BridgeFatalError(
    bridgeMessage: BridgeMessage
) : BridgeMessage(bridgeMessage.messageType, bridgeMessage.data) {
    val uuid: String get() = data[UUID].toString()
    val message: String get() = data[MESSAGE].toString()
}