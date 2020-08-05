package com.tradingview.lightweightcharts.runtime.messaging

class BridgeUnsubscribe(
    functionName: String,
    uuid: String
) : BridgeMessage(
    MessageType.UNSUBSCRIBE,
    mapOf(FN to functionName, UUID to uuid)
)