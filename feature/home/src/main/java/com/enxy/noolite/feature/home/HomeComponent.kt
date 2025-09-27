package com.enxy.noolite.feature.home

import android.content.Context
import com.arkivanov.decompose.ComponentContext
import com.enxy.noolite.core.model.ChannelAction
import com.enxy.noolite.core.model.Group
import com.enxy.noolite.core.model.GroupAction
import com.enxy.noolite.core.model.Script
import com.enxy.noolite.core.ui.extensions.componentScope
import com.enxy.noolite.domain.common.ChannelActionUseCase
import com.enxy.noolite.domain.common.GetAppSettingsUseCase
import com.enxy.noolite.domain.home.GetHomeDataUseCase
import com.enxy.noolite.domain.home.GroupActionUseCase
import com.enxy.noolite.domain.script.ExecuteScriptUseCase
import com.enxy.noolite.domain.script.RemoveScriptUseCase
import com.enxy.noolite.domain.settings.GetNooliteGroupsUseCase
import com.enxy.noolite.domain.settings.model.NooliteSettingsPayload
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.mapNotNull
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import org.orbitmvi.orbit.Container
import org.orbitmvi.orbit.ContainerHost
import org.orbitmvi.orbit.container
import timber.log.Timber
import com.enxy.noolite.core.ui.R as CoreUiR

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
    private val getNooliteGroupsUseCase: GetNooliteGroupsUseCase by inject()
    private val removeScriptUseCase: RemoveScriptUseCase by inject()
    private val executeScriptUseCase: ExecuteScriptUseCase by inject()
    private val context: Context by inject()

    private val scope = componentScope()

    override val container: Container<HomeState, HomeSideEffect> = scope.container(
        initialState = HomeState.Initial,
    )

    private val messageSuccess: String by lazy {
        context.getString(CoreUiR.string.update_groups_success)
    }
    private val messageFailure: String by lazy {
        context.getString(CoreUiR.string.update_groups_failure)
    }

    init {
        loadHomeData()
    }

    override fun onAddScriptClick() {
        onAddScriptClicked()
    }

    override fun onScriptClick(script: Script) {
        intent {
            executeScriptUseCase(script).collect { result ->
                result.onFailure {
                    Timber.e(it)
                }
            }
        }
    }

    override fun onScriptRemove(script: Script) {
        intent {
            removeScriptUseCase(script)
                .collect { result ->
                    result.onFailure {
                        Timber.e(it)
                    }
                }
        }
    }

    override fun onGroupAction(action: GroupAction) {
        intent {
            groupActionUseCase(action).collect { result ->
                result.onFailure {
                    Timber.e(it)
                }
            }
        }
    }

    override fun onGroupClick(group: Group) {
        onGroupClicked(group)
    }

    override fun onChannelActionClick(action: ChannelAction) {
        intent {
            channelActionUseCase(action).collect { result ->
                result.onFailure {
                    Timber.e(it)
                }
            }
        }
    }

    override fun onConnectClick(apiUrl: String) {
        intent {
            reduce { HomeState.Empty(apiUrl = apiUrl, isLoading = true) }
            getNooliteGroupsUseCase(NooliteSettingsPayload(apiUrl))
                .collect { result ->
                    result
                        .onSuccess {
                            postSideEffect(HomeSideEffect.Message(messageSuccess))
                        }
                        .onFailure { throwable ->
                            reduce { HomeState.Empty(apiUrl = apiUrl, isLoading = false) }
                            postSideEffect(HomeSideEffect.Message(messageFailure))
                            Timber.e(throwable)
                        }
                }
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
                    HomeState.Empty(
                        apiUrl = settings.apiUrl,
                        isLoading = (state as? HomeState.Empty)?.isLoading == true,
                    )
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
