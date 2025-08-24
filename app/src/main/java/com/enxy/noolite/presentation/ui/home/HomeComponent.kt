package com.enxy.noolite.presentation.ui.home

import android.content.Context
import com.arkivanov.decompose.ComponentContext
import com.enxy.noolite.R
import com.enxy.noolite.domain.features.actions.ChannelActionUseCase
import com.enxy.noolite.domain.features.actions.GroupActionUseCase
import com.enxy.noolite.domain.features.actions.model.ChannelAction
import com.enxy.noolite.domain.features.actions.model.GroupAction
import com.enxy.noolite.domain.features.common.Script
import com.enxy.noolite.domain.features.home.GetHomeDataUseCase
import com.enxy.noolite.domain.features.script.ExecuteScriptUseCase
import com.enxy.noolite.domain.features.script.RemoveScriptUseCase
import com.enxy.noolite.domain.features.settings.GetAppSettingsUseCase
import com.enxy.noolite.domain.features.settings.GetNooliteGroupsUseCase
import com.enxy.noolite.domain.features.settings.UpdateAppSettingsUseCase
import com.enxy.noolite.domain.features.settings.model.AppSettings
import com.enxy.noolite.domain.features.settings.model.NooliteSettingsPayload
import com.enxy.noolite.presentation.ui.home.model.HomeAction
import com.enxy.noolite.presentation.ui.home.model.HomeUiState
import com.enxy.noolite.utils.componentScope
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
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import timber.log.Timber

class HomeComponent(
    componentContext: ComponentContext
) : ComponentContext by componentContext,
    KoinComponent {

    private val getHomeDataUseCase: GetHomeDataUseCase by inject()
    private val getAppSettingsUseCase: GetAppSettingsUseCase by inject()
    private val groupActionUseCase: GroupActionUseCase by inject()
    private val channelActionUseCase: ChannelActionUseCase by inject()
    private val updateAppSettingsUseCase: UpdateAppSettingsUseCase by inject()
    private val getNooliteGroupsUseCase: GetNooliteGroupsUseCase by inject()
    private val removeScriptUseCase: RemoveScriptUseCase by inject()
    private val executeScriptUseCase: ExecuteScriptUseCase by inject()
    private val context: Context by inject()

    private val scope = componentScope()

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
            .launchIn(scope)
    }

    fun onScriptRemove(script: Script) {
        removeScriptUseCase(script)
            .onEach { result ->
                result
                    .onFailure {
                        Timber.e(it)
                    }
            }
            .launchIn(scope)
    }

    fun onGroupAction(action: GroupAction) {
        groupActionUseCase(action).launchIn(scope)
    }

    fun onChannelAction(action: ChannelAction) {
        channelActionUseCase(action).launchIn(scope)
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
            .launchIn(scope)
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
            .launchIn(scope)
    }
}
