package com.tradingview.lightweightcharts.api.series.models

/**
 * Represents a type of priced base value of baseline series type.
 */
data class BaseValuePrice(
    val type: BaseValuePriceType = BaseValuePriceType.PRICE,
    val price: Int
) : BaseValueType

enum class BaseValuePriceType {
    PRICE
}