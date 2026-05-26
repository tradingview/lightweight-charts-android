package com.tradingview.lightweightcharts.runtime.messaging

import java.util.*

class BridgeFunction(
    functionName: String,
    functionParams: Map<String, Any> = emptyMap(),
    expectsResult: Boolean = true
) : BridgeMessage(
    MessageType.FUNCTION,
    Data(
        fn = functionName,
        uuid = UUID.randomUUID().toString(),
        params = functionParams,
        expectsResult = expectsResult
    )
) {
    val uuid: String get() = data.uuid
}
