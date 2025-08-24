package com.enxy.noolite.presentation.utils.lifecycle

import androidx.appcompat.app.AppCompatDelegate
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
    private val getAppSettingsUseCase: GetAppSettingsUseCase
) : AppThemeObserver {
    private val job = SupervisorJob()
    private val scope = CoroutineScope(job + Dispatchers.Main.immediate)

    override fun onCreate(owner: LifecycleOwner) {
        super.onCreate(owner)
        getAppSettingsUseCase(Unit)
            .mapNotNull { it.getOrNull() }
            .distinctUntilChangedBy { settings -> settings.darkTheme }
            .onEach { settings ->
                AppCompatDelegate.setDefaultNightMode(settings.darkTheme.getNightMode())
            }
            .launchIn(scope)
    }

    override fun onDestroy(owner: LifecycleOwner) {
        job.cancel()
        super.onDestroy(owner)
    }
}


private fun Theme.getNightMode() = when (this) {
    Theme.SYSTEM -> AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM
    Theme.LIGHT -> AppCompatDelegate.MODE_NIGHT_NO
    Theme.DARK -> AppCompatDelegate.MODE_NIGHT_YES
}
