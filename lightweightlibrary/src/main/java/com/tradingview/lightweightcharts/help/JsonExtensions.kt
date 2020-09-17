package com.tradingview.lightweightcharts.help

import com.google.gson.JsonElement
import com.google.gson.JsonPrimitive
import kotlin.contracts.ExperimentalContracts
import kotlin.contracts.contract

@ExperimentalContracts
fun JsonElement?.isNumber(): Boolean {
    contract {
        returns(true) implies(this@isNumber is JsonPrimitive)
    }
    return this is JsonPrimitive && this.isNumber
}