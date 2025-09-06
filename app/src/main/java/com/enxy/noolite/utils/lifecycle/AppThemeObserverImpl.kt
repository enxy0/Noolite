package com.enxy.noolite.utils.lifecycle

import android.app.UiModeManager
import android.content.Context
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.content.getSystemService
import androidx.lifecycle.LifecycleOwner
import com.enxy.noolite.core.model.AppSettings.Theme
import com.enxy.noolite.domain.common.GetAppSettingsUseCase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.distinctUntilChangedBy
import kotlinx.coroutines.flow.mapNotNull
import kotlinx.coroutines.launch

class AppThemeObserverImpl(
    private val getAppSettingsUseCase: GetAppSettingsUseCase,
    context: Context,
) : AppThemeObserver {
    private val job = SupervisorJob()
    private val scope = CoroutineScope(job + Dispatchers.Main.immediate)
    private val api = ThemeApi(context)

    override fun onCreate(owner: LifecycleOwner) {
        super.onCreate(owner)
        scope.launch {
            getAppSettingsUseCase(Unit)
                .mapNotNull { it.getOrNull() }
                .distinctUntilChangedBy { settings -> settings.theme }
                .collectLatest { settings ->
                    api.setTheme(settings.theme)
                }
        }
    }

    override fun onDestroy(owner: LifecycleOwner) {
        job.cancel()
        super.onDestroy(owner)
    }
}

private class ThemeApi(private val context: Context) {

    fun setTheme(theme: Theme) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            implS(theme)
        } else {
            impl(theme)
        }
    }

    @RequiresApi(Build.VERSION_CODES.S)
    private fun implS(theme: Theme) {
        val uiModeManager = context.getSystemService<UiModeManager>()
        if (uiModeManager != null) {
            val mode = when (theme) {
                Theme.SYSTEM -> {
                    UiModeManager.MODE_NIGHT_AUTO
                }
                Theme.LIGHT -> {
                    UiModeManager.MODE_NIGHT_NO
                }
                Theme.DARK -> {
                    UiModeManager.MODE_NIGHT_YES
                }
            }
            uiModeManager.setApplicationNightMode(mode)
        } else {
            impl(theme)
        }
    }

    private fun impl(theme: Theme) {
        val mode = when (theme) {
            Theme.SYSTEM -> {
                AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM
            }
            Theme.LIGHT -> {
                AppCompatDelegate.MODE_NIGHT_NO
            }
            Theme.DARK -> {
                AppCompatDelegate.MODE_NIGHT_YES
            }
        }
        AppCompatDelegate.setDefaultNightMode(mode)
    }
}
