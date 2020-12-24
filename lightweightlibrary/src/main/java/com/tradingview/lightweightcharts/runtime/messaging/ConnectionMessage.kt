package com.tradingview.lightweightcharts.runtime.messaging

import java.util.*

class ConnectionMessage(debug: Boolean): BridgeMessage(
    MessageType.CONNECTION,
    Data(
        uuid = UUID.randomUUID().toString(),
        debug = debug
    )
)