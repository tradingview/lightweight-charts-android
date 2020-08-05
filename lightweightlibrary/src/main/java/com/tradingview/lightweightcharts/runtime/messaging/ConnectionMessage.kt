package com.tradingview.lightweightcharts.runtime.messaging

class ConnectionMessage(debug: Boolean): BridgeMessage(
    MessageType.CONNECTION,
    mapOf(DEBUG to debug)
)