package com.enxy.noolite.presentation.ui.settings

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.enxy.domain.features.common.Event
import com.enxy.domain.features.demo.SetDemoInfoUseCase
import com.enxy.domain.features.settings.GetAppSettingsUseCase
import com.enxy.domain.features.settings.GetNooliteGroupsUseCase
import com.enxy.domain.features.settings.UpdateAppSettingsUseCase
import com.enxy.domain.features.settings.model.AppSettings
import com.enxy.domain.features.settings.model.NooliteSettingsPayload
import com.enxy.noolite.R
import com.enxy.noolite.presentation.ui.settings.model.SettingsAction
import com.enxy.noolite.presentation.utils.extensions.context
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import timber.log.Timber

class SettingsViewModel(
    application: Application,
    private val getAppSettingsUseCase: GetAppSettingsUseCase,
    private val updateAppSettingsUseCase: UpdateAppSettingsUseCase,
    private val getNooliteGroupsUseCase: GetNooliteGroupsUseCase,
    private val setDemoInfoUseCase: SetDemoInfoUseCase
) : AndroidViewModel(application) {

    private val _settingsFlow = MutableStateFlow(AppSettings.default())
    val settingsFlow = _settingsFlow.asStateFlow()

    private val _eventsFlow = MutableStateFlow<Event<SettingsAction>?>(null)
    val eventsFlow: StateFlow<Event<SettingsAction>?> = _eventsFlow.asStateFlow()

    init {
        loadSettings()
    }

    fun setDarkTheme(darkTheme: Boolean) {
        updateAppSettings(_settingsFlow.value.copy(darkTheme = AppSettings.Theme.from(darkTheme)))
    }

    fun setNotifyWifiChange(notifyWifiChange: Boolean) {
        updateAppSettings(_settingsFlow.value.copy(notifyWifiChange = notifyWifiChange))
    }

    fun setApiUrl(apiUrl: String) {
        updateNooliteSettings(apiUrl)
        updateAppSettings(_settingsFlow.value.copy(apiUrl = apiUrl))
    }

    fun setTestData() {
        setDemoInfoUseCase(Unit)
            .onEach { result ->
                val message = if (result.isSuccess) {
                    context.getString(R.string.settings_demo_info_success)
                } else {
                    context.getString(R.string.settings_demo_info_failure)
                }
                _eventsFlow.emit(Event(SettingsAction.ShowSnackbar(message, result.isFailure)))
            }
            .launchIn(viewModelScope)
    }

    private fun loadSettings() {
        getAppSettingsUseCase(Unit)
            .onEach { result ->
                result
                    .onSuccess { settings ->
                        _settingsFlow.emit(settings)
                    }
                    .onFailure {
                        Timber.e(it)
                    }
            }
            .launchIn(viewModelScope)
    }

    private fun updateAppSettings(settings: AppSettings) {
        updateAppSettingsUseCase(settings).launchIn(viewModelScope)
    }

    private fun updateNooliteSettings(apiUrl: String) {
        getNooliteGroupsUseCase(NooliteSettingsPayload(apiUrl))
            .onEach { result ->
                val message = if (result.isSuccess) {
                    context.getString(R.string.settings_update_groups_success)
                } else {
                    context.getString(R.string.settings_update_groups_failure)
                }
                _eventsFlow.emit(Event(SettingsAction.ShowSnackbar(message, result.isFailure)))
                result.onFailure { throwable -> Timber.e(throwable) }
            }
            .launchIn(viewModelScope)
    }
}
