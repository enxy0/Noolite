package com.enxy.noolite.features

import android.os.Build
import android.os.Bundle
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import androidx.viewpager.widget.ViewPager
import com.enxy.noolite.R
import com.enxy.noolite.core.network.ConnectionManager
import com.enxy.noolite.core.platform.BaseActivity
import com.enxy.noolite.core.platform.FileManager
import com.enxy.noolite.features.adapter.SectionsPagerAdapter
import kotlinx.android.synthetic.main.activity_base.*
import javax.inject.Inject


class MainActivity : BaseActivity() {
    @Inject
    lateinit var connectionManager: ConnectionManager
    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory
    private lateinit var viewModel: MainViewModel
    private var currentFragmentPosition = -1

    companion object {
        const val CHANNEL_FRAGMENT_POSITION = 0
        const val GROUP_FRAGMENT_POSITION = 1
        const val SETTINGS_FRAGMENT_POSITION = 2
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        appComponent.inject(this)
        viewModel = getViewModel(viewModelFactory, MainViewModel::class.java)
        getIntentExtras()
        setUpTheme()
        setContentView(R.layout.activity_base)
        setUpToolbar()
        setUpViewPager()
        showDefaultFragment()
        setUpNavView()
        if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.O) {
            window.navigationBarColor = ContextCompat.getColor(this, android.R.color.black)
        }
    }

    private fun getIntentExtras() {
        intent?.extras?.let { bundle ->
            with(viewModel.settingsManager) {
                themeChanged = bundle.getBoolean(FileManager.THEME_CHANGED)
                scrollX = bundle.getInt(FileManager.SCROLL_X_KEY, 0)
                scrollY = bundle.getInt(FileManager.SCROLL_Y_KEY, 0)
            }
        }
    }

    override fun onResume() {
        super.onResume()
        if (!connectionManager.isWifiConnected())
            notifyError(R.string.error_no_wifi)
    }

    private fun setUpTheme() {
        when (viewModel.settingsManager.currentTheme) {
            FileManager.WHITE_RED_THEME_VALUE -> setTheme(R.style.AppTheme_White_Red)
            FileManager.WHITE_BLUE_THEME_VALUE -> setTheme(R.style.AppTheme_White_Blue)
            FileManager.DARK_GREEN_THEME_VALUE -> setTheme(R.style.AppTheme_Dark_Green)
            FileManager.BLACK_BLUE_THEME_VALUE -> setTheme(R.style.AppTheme_Black_Blue)
        }
    }

    private fun showDefaultFragment() {
        if (supportFragmentManager.findFragmentById(R.id.fragmentHolder) == null)
            if (viewModel.settingsManager.themeChanged) {
                navView.selectedItemId = R.id.navigation_settings
                viewPager.currentItem = SETTINGS_FRAGMENT_POSITION
            } else {
                navView.selectedItemId = R.id.navigation_favourite
                viewPager.currentItem = CHANNEL_FRAGMENT_POSITION
            }
    }

    private fun setUpViewPager() {
        val sectionsPagerAdapter = SectionsPagerAdapter(supportFragmentManager)
        viewPager.adapter = sectionsPagerAdapter

        viewPager.addOnPageChangeListener(object : ViewPager.OnPageChangeListener {
            override fun onPageScrolled(
                position: Int,
                positionOffset: Float,
                positionOffsetPixels: Int
            ) {

            }

            override fun onPageSelected(position: Int) {
                when (position) {
                    CHANNEL_FRAGMENT_POSITION -> navView.selectedItemId = R.id.navigation_favourite
                    GROUP_FRAGMENT_POSITION -> navView.selectedItemId = R.id.navigation_groups
                    SETTINGS_FRAGMENT_POSITION -> navView.selectedItemId = R.id.navigation_settings

                }
            }

            override fun onPageScrollStateChanged(state: Int) {

            }
        })
    }

    private fun setUpNavView() {
        navView.setOnNavigationItemSelectedListener {
            clearFragmentBackStack()
            when (it.itemId) {
                R.id.navigation_favourite -> {
                    viewPager.currentItem = CHANNEL_FRAGMENT_POSITION
                    currentFragmentPosition = CHANNEL_FRAGMENT_POSITION
                    return@setOnNavigationItemSelectedListener true
                }
                R.id.navigation_groups -> {
                    viewPager.currentItem = GROUP_FRAGMENT_POSITION
                    currentFragmentPosition = GROUP_FRAGMENT_POSITION
                    return@setOnNavigationItemSelectedListener true
                }
                R.id.navigation_settings -> {
                    currentFragmentPosition = SETTINGS_FRAGMENT_POSITION
                    viewPager.currentItem = SETTINGS_FRAGMENT_POSITION
                    return@setOnNavigationItemSelectedListener true
                }
            }
            return@setOnNavigationItemSelectedListener false
        }
    }

    private fun clearFragmentBackStack() {
        val size = supportFragmentManager.backStackEntryCount
        for (i in 0..size)
            supportFragmentManager.popBackStack()
    }

    private fun setUpToolbar() {
        setSupportActionBar(toolbar)
        supportActionBar!!.setDisplayShowTitleEnabled(false)
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }
}


