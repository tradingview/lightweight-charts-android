package com.tradingview.lightweightcharts.example.app.view

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.webkit.WebView
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.lifecycleScope
import com.google.android.material.navigation.NavigationView
import com.tradingview.lightweightcharts.example.app.R
import com.tradingview.lightweightcharts.example.app.model.SeriesDataType
import com.tradingview.lightweightcharts.example.app.router.FragmentFactory
import com.tradingview.lightweightcharts.example.app.view.charts.*
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.coroutines.Job
import kotlinx.coroutines.cancelAndJoin
import kotlin.reflect.KClass

class MainActivity : AppCompatActivity() {
    private lateinit var drawer: DrawerLayout
    private lateinit var actionBar: ActionBarDrawerToggle
    private lateinit var navigationView: NavigationView
    private val context: MainActivity = this


    private var realtimeDataJob: Job? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        WebView.setWebContentsDebuggingEnabled(true)
        setContentView(R.layout.activity_main)
        initializeNavigationDrawer()
    }
    
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.data_menu, menu)
        return true
    }

//    override fun onOptionsItemSelected(item: MenuItem): Boolean {
//        when {
//            item.itemId == R.id.static_data -> {
//                lifecycleScope.launchWhenResumed {
//                    realtimeDataJob?.cancelAndJoin()
//                    viewModel.selectSeries(SeriesDataType.AREA)
//                }
//                return true
//            }
//            item.itemId == R.id.real_time_data -> {
//                clearSeries()
//                viewModel.selectSeries(SeriesDataType.AREA)
//                realtimeDataJob = lifecycleScope.launchWhenResumed {
//                    viewModel.seriesFlow.collect {
//                        leftSeries.lastOrNull()?.update(it)
//                        rightSeries.lastOrNull()?.update(it)
//                    }
//                }
//                return true
//            }
//            item.itemId == R.id.clear_series -> {
//                lifecycleScope.launchWhenResumed {
//                    clearSeries()
//                    realtimeDataJob?.cancelAndJoin()
//                }
//                return true
//            }
//            actionBar.onOptionsItemSelected(item) -> return true
//            else -> return super.onOptionsItemSelected(item)
//        }
//    }

    private fun initializeNavigationDrawer() {
        drawer = findViewById(R.id.drawer_layout)
        actionBar = ActionBarDrawerToggle(context, drawer_layout, R.string.open, R.string.close)
        navigationView = findViewById(R.id.navigation_view)

        drawer.addDrawerListener(actionBar)
        actionBar.syncState()
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        navigationView.setNavigationItemSelectedListener { menuItem ->
            lifecycleScope.launchWhenResumed {

                when (menuItem.itemId) {
                    R.id.menu_bar_chart -> startFragment(BarChartFragment::class.java)
                    R.id.menu_custom_price_formatter -> startFragment(CustomPriceFormatterFragment::class.java)
                    R.id.menu_custom_themes -> startFragment(CustomThemesFragment::class.java)
                    R.id.menu_floating_tooltip -> startFragment(FloatingTooltipFragment::class.java)
                    R.id.menu_volume_study -> startFragment(VolumeStudyFragment::class.java)
                    R.id.menu_real_time_emulation -> startFragment(RealTimeEmulationFragment::class.java)
                    R.id.menu_series_markers -> startFragment(SeriesMarkersFragment::class.java)
                    R.id.menu_price_lines_with_titles -> startFragment(PriceLinesWithTitlesFragment::class.java)
                    R.id.menu_view_pager -> {
                        startActivity(Intent(this@MainActivity, ViewPagerActivity::class.java))
                    }
                }
            }
            drawer.closeDrawers()
            true
        }
    }

    private fun <T : Fragment> startFragment(fragmentClass: Class<T>) {
        supportFragmentManager
                .beginTransaction()
                .replace(
                        R.id.fragment_container_fl,
                        FragmentFactory.getInstance(fragmentClass)
                )
    }
}