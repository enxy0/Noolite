package com.enxy.noolite.presentation.ui.home

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.enxy.domain.features.actions.ChannelActionUseCase
import com.enxy.domain.features.actions.GroupActionUseCase
import com.enxy.domain.features.actions.model.ChannelAction
import com.enxy.domain.features.actions.model.GroupAction
import com.enxy.domain.features.common.Script
import com.enxy.domain.features.home.GetHomeDataUseCase
import com.enxy.domain.features.script.ExecuteScriptUseCase
import com.enxy.domain.features.script.RemoveScriptUseCase
import com.enxy.domain.features.settings.GetAppSettingsUseCase
import com.enxy.domain.features.settings.GetNooliteGroupsUseCase
import com.enxy.domain.features.settings.UpdateAppSettingsUseCase
import com.enxy.domain.features.settings.model.AppSettings
import com.enxy.domain.features.settings.model.NooliteSettingsPayload
import com.enxy.noolite.R
import com.enxy.noolite.presentation.ui.home.model.HomeAction
import com.enxy.noolite.presentation.ui.home.model.HomeUiState
import com.enxy.noolite.presentation.utils.extensions.context
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.mapNotNull
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.onStart
import timber.log.Timber

class HomeViewModel(
    application: Application,
    private val getHomeDataUseCase: GetHomeDataUseCase,
    private val getAppSettingsUseCase: GetAppSettingsUseCase,
    private val groupActionUseCase: GroupActionUseCase,
    private val channelActionUseCase: ChannelActionUseCase,
    private val updateAppSettingsUseCase: UpdateAppSettingsUseCase,
    private val getNooliteGroupsUseCase: GetNooliteGroupsUseCase,
    private val removeScriptUseCase: RemoveScriptUseCase,
    private val executeScriptUseCase: ExecuteScriptUseCase
) : AndroidViewModel(application) {

    private val _uiState = MutableStateFlow<HomeUiState>(HomeUiState.Initial)
    val uiState = _uiState.asStateFlow()

    private val _actionsFlow = MutableSharedFlow<HomeAction>(0, 1, BufferOverflow.DROP_OLDEST)
    val actionsFlow: Flow<HomeAction> = _actionsFlow.asSharedFlow()

    private val _appSettingsFlow = MutableStateFlow(AppSettings.default())

    private val messageSuccess: String by lazy {
        context.getString(R.string.settings_update_groups_success)
    }
    private val messageFailure: String by lazy {
        context.getString(R.string.settings_update_groups_failure)
    }

    init {
        loadHomeData()
    }

    fun onScriptClick(script: Script) {
        executeScriptUseCase(script)
            .onEach { result ->
                result
                    .onFailure {
                        Timber.e(it)
                    }
            }
            .launchIn(viewModelScope)
    }

    fun onScriptRemove(script: Script) {
        removeScriptUseCase(script)
            .onEach { result ->
                result
                    .onFailure {
                        Timber.e(it)
                    }
            }
            .launchIn(viewModelScope)
    }

    fun onGroupAction(action: GroupAction) {
        groupActionUseCase(action).launchIn(viewModelScope)
    }

    fun onChannelAction(action: ChannelAction) {
        channelActionUseCase(action).launchIn(viewModelScope)
    }

    fun onApiUrlChange(apiUrl: String) {
        getNooliteGroupsUseCase(NooliteSettingsPayload(apiUrl))
            .onStart { _uiState.value = HomeUiState.Empty(apiUrl, true) }
            .onEach { result ->
                result
                    .onSuccess {
                        updateAppSettingsUseCase
                            .invoke(_appSettingsFlow.value.copy(apiUrl = apiUrl))
                            .collect()
                        _actionsFlow.emit(HomeAction.ShowSnackbar(messageSuccess, false))
                    }
                    .onFailure { throwable ->
                        _uiState.value = HomeUiState.Empty(apiUrl, false)
                        _actionsFlow.emit(HomeAction.ShowSnackbar(messageFailure, true))
                        Timber.e(throwable)
                    }
            }
            .launchIn(viewModelScope)
    }

    private fun loadHomeData() {
        combine(
            getAppSettingsUseCase(Unit).mapNotNull { it.getOrNull() },
            getHomeDataUseCase(Unit).mapNotNull { it.getOrNull() }
        ) { settings, data ->
            _uiState.value = if (data.isEmpty) {
                HomeUiState.Empty(settings.apiUrl, false)
            } else {
                HomeUiState.Content(data)
            }
        }
            .catch { e -> Timber.e(e) }
            .launchIn(viewModelScope)
    }
}
