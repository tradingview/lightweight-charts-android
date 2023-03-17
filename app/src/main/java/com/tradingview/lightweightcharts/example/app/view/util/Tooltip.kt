package com.tradingview.lightweightcharts.example.app.view.util

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.LinearLayout
import com.tradingview.lightweightcharts.example.app.databinding.TooltipLayoutBinding

class Tooltip @JvmOverloads constructor(
    context: Context?,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0,
    defStyleRes: Int = 0,
) : LinearLayout(context, attrs, defStyleAttr, defStyleRes) {

    private val binding = TooltipLayoutBinding.inflate(LayoutInflater.from(context), this, true)

    fun setSymbolName(value: String) {
        binding.symbolNameTv.text = value
    }

    fun setPrice(value: String) {
        binding.priceTv.text = value
    }

    fun setDate(value: String) {
        binding.timeTv.text = value
    }
}