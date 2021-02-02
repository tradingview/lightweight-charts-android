package com.tradingview.lightweightcharts.example.app.view.util

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.LinearLayout
import android.widget.TextView
import com.tradingview.lightweightcharts.example.app.R
import kotlinx.android.synthetic.main.layout_chart_fragment.*
import kotlinx.android.synthetic.main.tooltip_layout.view.*

class Tooltip @JvmOverloads constructor(
        context: Context?,
        attrs: AttributeSet? = null,
        defStyleAttr: Int = 0,
        defStyleRes: Int = 0
) : LinearLayout(context, attrs, defStyleAttr, defStyleRes) {

    private val symbolName: TextView by lazy { symbol_name_tv }
    private val price: TextView by lazy { price_tv }
    private val date: TextView by lazy { time_tv }

    init {
        LayoutInflater.from(context).inflate(R.layout.tooltip_layout, this, true)
    }

    fun setSymbolName(value: String) {
        symbolName.text = value
    }

    fun setPrice(value: String) {
        price.text = value
    }

    fun setDate(value: String) {
        date.text = value
    }
}