package com.enxy.noolite.presentation.ui.settings

import android.content.Context
import com.arkivanov.decompose.ComponentContext
import com.enxy.noolite.R
import com.enxy.noolite.domain.features.common.Event
import com.enxy.noolite.domain.features.demo.SetDemoInfoUseCase
import com.enxy.noolite.domain.features.settings.GetAppSettingsUseCase
import com.enxy.noolite.domain.features.settings.GetNooliteGroupsUseCase
import com.enxy.noolite.domain.features.settings.UpdateAppSettingsUseCase
import com.enxy.noolite.domain.features.settings.model.AppSettings
import com.enxy.noolite.domain.features.settings.model.NooliteSettingsPayload
import com.enxy.noolite.presentation.ui.settings.model.SettingsAction
import com.enxy.noolite.utils.componentScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import timber.log.Timber

interface SettingsComponent {
    val settingsFlow: StateFlow<AppSettings>
    val eventsFlow: StateFlow<Event<SettingsAction>?>
    fun setDarkTheme(darkTheme: Boolean)
    fun setNotifyWifiChange(notifyWifiChange: Boolean)
    fun setApiUrl(apiUrl: String)
    fun setTestData()
}

class SettingsComponentImpl(
    componentContext: ComponentContext,
) : ComponentContext by componentContext,
    SettingsComponent,
    KoinComponent { // TODO: Remove

    private val scope = componentScope()

    private val context: Context by inject()
    private val getAppSettingsUseCase: GetAppSettingsUseCase by inject()
    private val updateAppSettingsUseCase: UpdateAppSettingsUseCase by inject()
    private val getNooliteGroupsUseCase: GetNooliteGroupsUseCase by inject()
    private val setDemoInfoUseCase: SetDemoInfoUseCase by inject()

    private val _settingsFlow = MutableStateFlow(AppSettings.default())
    override val settingsFlow = _settingsFlow.asStateFlow()

    private val _eventsFlow = MutableStateFlow<Event<SettingsAction>?>(null)
    override val eventsFlow: StateFlow<Event<SettingsAction>?> = _eventsFlow.asStateFlow()

    init {
        loadSettings()
    }

    override fun setDarkTheme(darkTheme: Boolean) {
        updateAppSettings(_settingsFlow.value.copy(darkTheme = AppSettings.Theme.from(darkTheme)))
    }

    override fun setNotifyWifiChange(notifyWifiChange: Boolean) {
        updateAppSettings(_settingsFlow.value.copy(notifyWifiChange = notifyWifiChange))
    }

    override fun setApiUrl(apiUrl: String) {
        updateNooliteSettings(apiUrl)
        updateAppSettings(_settingsFlow.value.copy(apiUrl = apiUrl))
    }

    override fun setTestData() {
        setDemoInfoUseCase(Unit)
            .onEach { result ->
                val message = if (result.isSuccess) {
                    context.getString(R.string.settings_demo_info_success)
                } else {
                    context.getString(R.string.settings_demo_info_failure)
                }
                _eventsFlow.emit(Event(SettingsAction.ShowSnackbar(message, result.isFailure)))
            }
            .launchIn(scope)
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
            .launchIn(scope)
    }

    private fun updateAppSettings(settings: AppSettings) {
        updateAppSettingsUseCase(settings).launchIn(scope)
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
            .launchIn(scope)
    }
}
