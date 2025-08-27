package com.enxy.noolite.features.home

import android.content.Context
import com.arkivanov.decompose.ComponentContext
import com.enxy.noolite.R
import com.enxy.noolite.domain.features.actions.ChannelActionUseCase
import com.enxy.noolite.domain.features.actions.GroupActionUseCase
import com.enxy.noolite.domain.features.actions.model.ChannelAction
import com.enxy.noolite.domain.features.actions.model.GroupAction
import com.enxy.noolite.domain.features.common.Group
import com.enxy.noolite.domain.features.common.Script
import com.enxy.noolite.domain.features.home.GetHomeDataUseCase
import com.enxy.noolite.domain.features.script.ExecuteScriptUseCase
import com.enxy.noolite.domain.features.script.RemoveScriptUseCase
import com.enxy.noolite.domain.features.settings.GetAppSettingsUseCase
import com.enxy.noolite.domain.features.settings.GetNooliteGroupsUseCase
import com.enxy.noolite.domain.features.settings.UpdateAppSettingsUseCase
import com.enxy.noolite.domain.features.settings.model.AppSettings
import com.enxy.noolite.domain.features.settings.model.NooliteSettingsPayload
import com.enxy.noolite.utils.extensions.componentScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.mapNotNull
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.onStart
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import org.orbitmvi.orbit.Container
import org.orbitmvi.orbit.ContainerHost
import org.orbitmvi.orbit.container
import timber.log.Timber

interface HomeComponent : ContainerHost<HomeState, HomeSideEffect> {
    fun onAddScriptClick()
    fun onScriptClick(script: Script)
    fun onScriptRemove(script: Script)
    fun onGroupAction(action: GroupAction)
    fun onGroupClick(group: Group)
    fun onChannelActionClick(action: ChannelAction)
    fun onConnectClick(apiUrl: String)
    fun onSettingsClick()
}

class HomeComponentImpl(
    componentContext: ComponentContext,
    private val onSettingsClicked: () -> Unit,
    private val onGroupClicked: (group: Group) -> Unit,
    private val onAddScriptClicked: () -> Unit,
) : ComponentContext by componentContext,
    KoinComponent,
    HomeComponent {

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

    override val container: Container<HomeState, HomeSideEffect> = scope.container(
        initialState = HomeState.Initial,
    )

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

    override fun onAddScriptClick() {
        onAddScriptClicked()
    }

    override fun onScriptClick(script: Script) {
        executeScriptUseCase(script)
            .onEach { result ->
                result
                    .onFailure {
                        Timber.e(it)
                    }
            }
            .launchIn(scope)
    }

    override fun onScriptRemove(script: Script) {
        removeScriptUseCase(script)
            .onEach { result ->
                result
                    .onFailure {
                        Timber.e(it)
                    }
            }
            .launchIn(scope)
    }

    override fun onGroupAction(action: GroupAction) {
        groupActionUseCase(action).launchIn(scope)
    }

    override fun onGroupClick(group: Group) {
        onGroupClicked(group)
    }

    override fun onChannelActionClick(action: ChannelAction) {
        channelActionUseCase(action).launchIn(scope)
    }

    override fun onConnectClick(apiUrl: String) {
        intent {
            getNooliteGroupsUseCase(NooliteSettingsPayload(apiUrl))
                .onStart { reduce { HomeState.Empty(apiUrl = apiUrl, isLoading = true) } }
                .onEach { result ->
                    result
                        .onSuccess {
                            updateAppSettingsUseCase
                                .invoke(_appSettingsFlow.value.copy(apiUrl = apiUrl))
                                .collect()
                            postSideEffect(HomeSideEffect.Message(messageSuccess))
                        }
                        .onFailure { throwable ->
                            reduce { HomeState.Empty(apiUrl = apiUrl, isLoading = false) }
                            postSideEffect(HomeSideEffect.Message(messageFailure))
                            Timber.e(throwable)
                        }
                }
                .launchIn(scope)
        }
    }

    override fun onSettingsClick() {
        onSettingsClicked()
    }

    private fun loadHomeData() = intent {
        combine(
            getAppSettingsUseCase(Unit).mapNotNull { it.getOrNull() },
            getHomeDataUseCase(Unit).mapNotNull { it.getOrNull() }
        ) { settings, data ->
            reduce {
                if (data.isEmpty) {
                    HomeState.Empty(apiUrl = settings.apiUrl, isLoading = false)
                } else {
                    HomeState.Content(data)
                }
            }
        }
            .catch { e ->
                Timber.e(e)
                postSideEffect(HomeSideEffect.Message(e.message.orEmpty()))
            }
            .collect()
    }
}
