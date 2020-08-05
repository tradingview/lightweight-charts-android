package com.tradingview.lightweightcharts.runtime.messaging

class BridgeFunction(
    functionName: String,
    functionParams: Map<String, Any> = emptyMap()
) : BridgeMessage(
    MessageType.FUNCTION,
    functionParams
        .plus(FN to functionName)
        .plus(UUID to java.util.UUID.randomUUID())
) {

    val uuid: String get() = data[UUID].toString()

}
