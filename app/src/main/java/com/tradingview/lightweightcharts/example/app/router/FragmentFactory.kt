package com.tradingview.lightweightcharts.example.app.router

import android.os.Bundle
import androidx.fragment.app.Fragment

class FragmentFactory {
    companion object {
        fun <T : Fragment> getInstance(clazz: Class<T>, bundle: Bundle? = null): T {
            val fragment = clazz.newInstance()
            fragment.arguments = bundle
            return fragment
        }
    }
}