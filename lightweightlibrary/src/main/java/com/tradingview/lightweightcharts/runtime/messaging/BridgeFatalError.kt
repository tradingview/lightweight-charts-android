package com.tradingview.lightweightcharts.runtime.messaging

class BridgeFatalError(
    bridgeMessage: BridgeMessage
) : BridgeMessage(bridgeMessage.messageType, bridgeMessage.data) {
    // JS omits the uuid and function name for errors that are not tied to a call
    val uuid: String? get() = data.uuid
    val functionName: String? get() = data.fn
    val message: String get() = data.message ?: "Unknown JavaScript error"
}
