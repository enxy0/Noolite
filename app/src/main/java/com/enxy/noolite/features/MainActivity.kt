package com.enxy.noolite.features

import android.os.Bundle
import androidx.fragment.app.commit
import androidx.fragment.app.commitNow
import androidx.lifecycle.ViewModelProvider
import com.enxy.noolite.R
import com.enxy.noolite.core.network.ConnectionManager
import com.enxy.noolite.core.platform.BaseActivity
import com.enxy.noolite.core.platform.FileManager
import com.enxy.noolite.features.main.MainFragment
import com.enxy.noolite.features.settings.SettingsFragment
import javax.inject.Inject


class MainActivity : BaseActivity() {
    @Inject
    lateinit var connectionManager: ConnectionManager

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory
    private lateinit var viewModel: MainViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        appComponent.inject(this)
        viewModel = getViewModel(viewModelFactory, MainViewModel::class.java)
        getIntentExtras()
        setTheme(viewModel.currentTheme)
        setContentView(R.layout.activity_base)
        showDefaultFragment()
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
            supportFragmentManager.commitNow {
                replace(R.id.fragmentHolder, MainFragment.newInstance())
            }
            if (viewModel.themeChanged) {
                supportFragmentManager.commit {
                    replace(R.id.fragmentHolder, SettingsFragment.newInstance())
                    addToBackStack(null)
                }
            }
    }


    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }
}


