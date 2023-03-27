package com.tradingview.lightweightcharts.example.app.view

import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import android.webkit.WebView
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.tradingview.lightweightcharts.example.app.R
import com.tradingview.lightweightcharts.example.app.databinding.ActivityMainBinding
import com.tradingview.lightweightcharts.example.app.router.FragmentFactory
import com.tradingview.lightweightcharts.example.app.view.charts.ChartActionsFragment
import com.tradingview.lightweightcharts.example.app.view.charts.ChartTypeFragment
import com.tradingview.lightweightcharts.example.app.view.charts.CustomPriceFormatterFragment
import com.tradingview.lightweightcharts.example.app.view.charts.CustomThemesFragment
import com.tradingview.lightweightcharts.example.app.view.charts.CustomTooltipFragment
import com.tradingview.lightweightcharts.example.app.view.charts.IndicatorsAndMarkersFragment
import com.tradingview.lightweightcharts.example.app.view.charts.PriceScalesFragment
import com.tradingview.lightweightcharts.example.app.view.charts.RangeSwitcherFragment
import com.tradingview.lightweightcharts.example.app.view.charts.RealTimeEmulationFragment
import com.tradingview.lightweightcharts.example.app.view.charts.SeriesDataFragment
import com.tradingview.lightweightcharts.example.app.view.charts.TimeScaleActionsFragment
import com.tradingview.lightweightcharts.example.app.view.pager.ViewPagerActivity
import com.tradingview.lightweightcharts.example.app.view.util.ITitleFragment

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var actionBar: ActionBarDrawerToggle
    private val context: MainActivity = this

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        WebView.setWebContentsDebuggingEnabled(true)
        setContentView(ActivityMainBinding.inflate(layoutInflater).also { binding = it }.root)
        initializeNavigationDrawer()
        startFragment(ChartTypeFragment::class.java, false)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when {
            actionBar.onOptionsItemSelected(item) -> true
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun initializeNavigationDrawer() {
        actionBar = ActionBarDrawerToggle(context, binding.drawerLayout, R.string.open, R.string.close)
        binding.drawerLayout.addDrawerListener(actionBar)
        actionBar.syncState()
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        binding.navigationView.setNavigationItemSelectedListener { menuItem ->
            when (menuItem.itemId) {
                R.id.menu_chart_type -> startFragment(ChartTypeFragment::class.java)
                R.id.menu_custom_themes -> startFragment(CustomThemesFragment::class.java)
                R.id.menu_range_switcher -> startFragment(RangeSwitcherFragment::class.java)
                R.id.menu_actions -> startFragment(ChartActionsFragment::class.java)
                R.id.menu_time_scale_actions -> startFragment(TimeScaleActionsFragment::class.java)
                R.id.menu_custom_tooltips -> startFragment(CustomTooltipFragment::class.java)
                R.id.menu_custom_price_formatter -> startFragment(CustomPriceFormatterFragment::class.java)
                R.id.menu_price_scales -> startFragment(PriceScalesFragment::class.java)
                R.id.menu_data -> startFragment(SeriesDataFragment::class.java)
                R.id.menu_realtime_emulator -> startFragment(RealTimeEmulationFragment::class.java)
                R.id.menu_indicators_and_markers -> startFragment(IndicatorsAndMarkersFragment::class.java)
                R.id.menu_view_pager -> {
                    startActivity(Intent(this@MainActivity, ViewPagerActivity::class.java))
                }
            }
            binding.drawerLayout.closeDrawers()
            true
        }

        supportFragmentManager.addOnBackStackChangedListener {
            updateTitle()
        }
    }

    override fun onResume() {
        super.onResume()
        updateTitle()
    }

    private fun <T : Fragment> startFragment(
        fragmentClass: Class<T>,
        shouldAddToBackStack: Boolean = true,
    ) {
        supportFragmentManager
            .beginTransaction().apply {
                replace(
                    R.id.fragment_container_fl,
                    FragmentFactory.getInstance(fragmentClass)
                )
                if (shouldAddToBackStack) {
                    addToBackStack(null)
                }
                commit()
            }
    }

    private fun updateTitle() {
        (supportFragmentManager.findFragmentById(R.id.fragment_container_fl) as? ITitleFragment)?.fragmentTitleRes
            ?.let { title -> setTitle(title) }
    }
}