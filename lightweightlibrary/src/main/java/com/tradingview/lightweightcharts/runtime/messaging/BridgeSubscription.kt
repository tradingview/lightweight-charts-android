package com.tradingview.lightweightcharts.runtime.messaging

class BridgeSubscription(
    functionName: String,
    functionParams: Map<String, Any> = emptyMap()
) : BridgeMessage(
    MessageType.SUBSCRIBE,
    functionParams
        .plus(FN to functionName)
        .plus(UUID to java.util.UUID.randomUUID())
) {
    val uuid: String get() = data[UUID].toString()
}
