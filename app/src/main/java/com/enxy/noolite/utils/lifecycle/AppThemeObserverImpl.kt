package com.enxy.noolite.utils.lifecycle

import android.app.UiModeManager
import android.content.Context
import android.os.Build
import androidx.annotation.MainThread
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.content.getSystemService
import androidx.lifecycle.LifecycleOwner
import com.enxy.noolite.domain.features.settings.GetAppSettingsUseCase
import com.enxy.noolite.domain.features.settings.model.AppSettings.Theme
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.distinctUntilChangedBy
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.mapNotNull
import kotlinx.coroutines.flow.onEach

class AppThemeObserverImpl(
    private val getAppSettingsUseCase: GetAppSettingsUseCase,
    private val context: Context,
) : AppThemeObserver {
    private val job = SupervisorJob()
    private val scope = CoroutineScope(job + Dispatchers.Main.immediate)

    override fun onCreate(owner: LifecycleOwner) {
        super.onCreate(owner)
        getAppSettingsUseCase(Unit)
            .mapNotNull { it.getOrNull() }
            .distinctUntilChangedBy { settings -> settings.theme }
            .onEach { settings ->
                setAppTheme(settings.theme)
            }
            .launchIn(scope)
    }

    override fun onDestroy(owner: LifecycleOwner) {
        job.cancel()
        super.onDestroy(owner)
    }

    @MainThread
    fun setAppTheme(theme: Theme) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            setAppThemeInternalApiS(theme)
        } else {
            setAppThemeInternal(theme)
        }
    }

    private fun setAppThemeInternal(theme: Theme) {
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

    @RequiresApi(Build.VERSION_CODES.S)
    private fun setAppThemeInternalApiS(theme: Theme) {
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
            setAppThemeInternal(theme)
        }
    }
}
