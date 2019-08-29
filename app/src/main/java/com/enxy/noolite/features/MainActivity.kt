package com.enxy.noolite.features

import android.os.Build
import android.os.Bundle
import androidx.activity.viewModels
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.commit
import com.enxy.noolite.R
import com.enxy.noolite.core.platform.BaseActivity
import com.enxy.noolite.core.platform.FileManager
import kotlinx.android.synthetic.main.activity_base.*

class MainActivity : BaseActivity() {
    private val viewModel by viewModels<MainViewModel>()
    private var currentFragmentPosition = -1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        getIntentExtras()
        setUpTheme()
        setContentView(R.layout.activity_base)
        setUpToolbar()
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
        if (!viewModel.networkHandler.isWifiConnected())
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
                loadFragment(SettingsFragment(), 2, themeChanged = true)
                navView.selectedItemId = R.id.navigation_settings
            }
            else {
                loadFragment(ChannelFragment(), 0, firstTimeCreated = true)
                navView.selectedItemId = R.id.navigation_my_room
            }
    }


    private fun setUpNavView() {
        navView.setOnNavigationItemSelectedListener {
            clearFragmentBackStack()
            val newPosition: Int
            when (it.itemId) {
                R.id.navigation_my_room -> {
                    newPosition = 0
                    if (currentFragmentPosition != newPosition)
                        loadFragment(ChannelFragment(), 0)
                    return@setOnNavigationItemSelectedListener true
                }
                R.id.navigation_rooms -> {
                    newPosition = 1
                    if (currentFragmentPosition != newPosition)
                        loadFragment(GroupFragment(), 1)
                    return@setOnNavigationItemSelectedListener true
                }
                R.id.navigation_settings -> {
                    newPosition = 2
                    if (currentFragmentPosition != newPosition)
                        loadFragment(SettingsFragment(), 2)
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

    private fun loadFragment(fragment: Fragment, newPosition: Int = 0, firstTimeCreated: Boolean = false, themeChanged: Boolean = false) {
        supportFragmentManager.commit {
            when {
                firstTimeCreated -> setCustomAnimations(R.anim.grow_fade_in_from_bottom, R.anim.grow_fade_in_from_bottom)
                themeChanged -> setCustomAnimations(0, 0)
                viewModel.settingsManager.hasFragmentTransitionAnimation -> {
                    if (newPosition > currentFragmentPosition)
                        setCustomAnimations(R.anim.slide_in_right, R.anim.slide_out_left)
                    else if (newPosition < currentFragmentPosition)
                        setCustomAnimations(R.anim.slide_in_left, R.anim.slide_out_right)
                }
            }
            replace(R.id.fragmentHolder, fragment)
        }
        currentFragmentPosition = newPosition
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


