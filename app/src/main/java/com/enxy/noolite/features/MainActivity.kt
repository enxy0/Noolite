package com.enxy.noolite.features

import android.os.Build
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.core.content.ContextCompat
import androidx.fragment.app.commit
import androidx.lifecycle.ViewModelProvider
import androidx.viewpager.widget.ViewPager
import com.enxy.noolite.R
import com.enxy.noolite.core.network.ConnectionManager
import com.enxy.noolite.core.platform.BaseActivity
import com.enxy.noolite.core.platform.FileManager
import com.enxy.noolite.features.settings.SettingsFragment
import kotlinx.android.synthetic.main.activity_base.*
import kotlinx.android.synthetic.main.toolbar.*
import javax.inject.Inject


class MainActivity : BaseActivity() {
    @Inject
    lateinit var connectionManager: ConnectionManager

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory
    private lateinit var viewModel: MainViewModel

    companion object {
        const val CHANNEL_FRAGMENT_POSITION = 0
        const val GROUP_FRAGMENT_POSITION = 1
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.toolbar_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.settings -> {
                supportFragmentManager.commit {
                    replace(R.id.fragmentHolder, SettingsFragment.newInstance())
                    addToBackStack(null)
                }
            }
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        appComponent.inject(this)
        viewModel = getViewModel(viewModelFactory, MainViewModel::class.java)
        getIntentExtras()
        setTheme(viewModel.currentTheme)
        setContentView(R.layout.activity_base)
        setUpToolbar()
        setUpViewPager()
        showDefaultFragment()
        if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.O) {
            window.navigationBarColor = ContextCompat.getColor(this, android.R.color.black)
        }
    }

    private fun getIntentExtras() {
        intent?.extras?.let { bundle ->
            val themeChanged = bundle.getBoolean(FileManager.THEME_CHANGED)
            val settingsScrollX = bundle.getInt(FileManager.SCROLL_X_KEY, 0)
            val settingsScrollY = bundle.getInt(FileManager.SCROLL_Y_KEY, 0)
            viewModel.setThemeChangeValues(themeChanged, settingsScrollX, settingsScrollY)
        }
    }

    override fun onResume() {
        super.onResume()
        if (!connectionManager.isWifiConnected())
            notifyError(R.string.error_no_wifi)
    }

    private fun showDefaultFragment() {
        if (supportFragmentManager.findFragmentById(R.id.fragmentHolder) == null)
            if (viewModel.themeChanged) {
                setToolbarTitle(R.string.title_settings)
                supportFragmentManager.commit {
                    replace(R.id.fragmentHolder, SettingsFragment.newInstance())
                    addToBackStack(null)
                }
            } else {
                setToolbarTitle(R.string.title_favourite)
                viewPager.currentItem = CHANNEL_FRAGMENT_POSITION
            }
    }

    private fun setUpViewPager() {
        val sectionsPagerAdapter =
            SectionsPagerAdapter(supportFragmentManager)
        viewPager.adapter = sectionsPagerAdapter

        viewPager.addOnPageChangeListener(object : ViewPager.OnPageChangeListener {
            override fun onPageScrolled(
                position: Int,
                positionOffset: Float,
                positionOffsetPixels: Int
            ) = Unit

            override fun onPageSelected(position: Int) {
                when (position) {
                    CHANNEL_FRAGMENT_POSITION -> setToolbarTitle(R.string.title_favourite)
                    GROUP_FRAGMENT_POSITION -> setToolbarTitle(R.string.title_home)
                }
            }

            override fun onPageScrollStateChanged(state: Int) = Unit
        })
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


