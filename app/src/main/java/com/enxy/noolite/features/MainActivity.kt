package com.enxy.noolite.features

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.lifecycle.ViewModelProvider
import com.enxy.noolite.R
import com.enxy.noolite.core.base.BaseActivity
import com.enxy.noolite.core.network.ConnectionManager
import com.enxy.noolite.core.utils.Constants
import com.enxy.noolite.features.main.MainFragment
import com.enxy.noolite.features.settings.SettingsFragment
import javax.inject.Inject


class MainActivity : BaseActivity() {
    @Inject
    lateinit var connectionManager: ConnectionManager

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory
    private lateinit var viewModel: MainViewModel
    private var themeChanged = false

    companion object {
        const val THEME_NAME_KEY = "theme_name"
        const val SCROLL_X_KEY = "scroll_x"
        const val SCROLL_Y_KEY = "scroll_y"

        fun newThemedActivity(
            context: Context,
            themeName: String,
            settingsScrollX: Int,
            settingsScrollY: Int
        ): Intent {
            return Intent(context, MainActivity::class.java).apply {
                putExtra(THEME_NAME_KEY, themeName)
                putExtra(SCROLL_X_KEY, settingsScrollX)
                putExtra(SCROLL_Y_KEY, settingsScrollY)
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        appComponent.inject(this)
        viewModel = getViewModel(viewModelFactory, MainViewModel::class.java)
        getPassedData()
        setTheme(getCurrentTheme())
        setContentView(R.layout.activity_base)
        showDefaultFragment()
    }

    private fun getCurrentTheme(): Int = when (viewModel.currentTheme) {
        Constants.WHITE_THEME_VALUE -> R.style.AppTheme
        Constants.DARK_THEME_VALUE -> R.style.AppTheme_Dark_Green
        Constants.BLACK_THEME_VALUE -> R.style.AppTheme_Black_Amber
        else -> Constants.DEFAULT_THEME
    }

    private fun getPassedData() {
        intent?.extras?.let { bundle ->
            // TODO: Rewrite Default themeName value
            val themeName = bundle.getString(THEME_NAME_KEY) ?: Constants.DARK_THEME_VALUE
            val settingsScrollX = bundle.getInt(SCROLL_X_KEY, 0)
            val settingsScrollY = bundle.getInt(SCROLL_Y_KEY, 0)
            this.themeChanged = true
            viewModel.setThemeChangeValues(
                themeChanged,
                themeName,
                settingsScrollX,
                settingsScrollY
            )
        }
    }

    override fun onResume() {
        super.onResume()
        if (!connectionManager.isWifiConnected())
            notifyError(R.string.error_no_wifi)
    }

    private fun showDefaultFragment() {
        if (supportFragmentManager.findFragmentById(R.id.fragmentHolder) == null) {
            supportFragmentManager.beginTransaction()
                .replace(R.id.fragmentHolder, MainFragment.newInstance())
                .commitNow()
            // If theme was changed we also add SettingsFragment above the MainFragment
            // to imitate seamless theme change
            if (themeChanged)
                supportFragmentManager.beginTransaction()
                    .replace(R.id.fragmentHolder, SettingsFragment.newInstance())
                    .addToBackStack(null)
                    .commit()
        }
    }
}


