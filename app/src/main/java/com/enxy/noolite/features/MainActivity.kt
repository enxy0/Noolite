package com.enxy.noolite.features

import android.content.Context
import android.content.Intent
import android.os.Bundle
import com.enxy.noolite.R
import com.enxy.noolite.core.base.BaseActivity
import com.enxy.noolite.core.utils.Constants
import com.enxy.noolite.core.utils.Constants.Companion.DEFAULT_THEME_NAME
import com.enxy.noolite.features.main.MainFragment
import com.enxy.noolite.features.settings.SettingsFragment
import org.koin.android.ext.android.inject


class MainActivity : BaseActivity() {
    private val viewModel: MainViewModel by inject()
    private var themeChanged = false // indicates if theme was changed (activity restarted).

    companion object {
        const val THEME_NAME_KEY = "theme"
        const val SCROLL_X_KEY = "scroll_x"
        const val SCROLL_Y_KEY = "scroll_y"

        /**
         * Creates new activity with given theme.
         */
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
        getPassedData()
        setTheme(getCurrentTheme())
        setContentView(R.layout.activity_base)
        showDefaultFragment()
    }

    private fun getCurrentTheme(): Int {
        return when (viewModel.themeName) {
            Constants.WHITE_THEME_VALUE -> R.style.AppTheme
            Constants.DARK_THEME_VALUE -> R.style.AppTheme_Dark_Green
            Constants.BLACK_THEME_VALUE -> R.style.AppTheme_Black_Amber
            else -> Constants.DEFAULT_THEME
        }
    }

    private fun getPassedData() {
        intent?.extras?.let { bundle ->
            val themeName = bundle.getString(THEME_NAME_KEY) ?: DEFAULT_THEME_NAME
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

    /**
     * Notifies the user whether the phone is connected to the WiFi or not.
     * Snackbar shows every time when app is shown to the user (for first time or when
     */
    override fun onResume() {
        super.onResume()
        if (!viewModel.isWifiConnected)
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


