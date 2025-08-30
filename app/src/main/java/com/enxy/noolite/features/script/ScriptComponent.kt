package com.enxy.noolite.features.script

import android.content.Context
import com.arkivanov.decompose.ComponentContext
import com.enxy.noolite.R
import com.enxy.noolite.domain.common.Group
import com.enxy.noolite.domain.features.actions.model.ChannelAction
import com.enxy.noolite.domain.features.actions.model.GroupAction
import com.enxy.noolite.domain.features.script.CreateScriptUseCase
import com.enxy.noolite.domain.features.script.GetGroupsUseCase
import com.enxy.noolite.domain.features.script.model.CreateScriptPayload
import com.enxy.noolite.utils.extensions.componentScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.take
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import org.orbitmvi.orbit.Container
import org.orbitmvi.orbit.ContainerHost
import org.orbitmvi.orbit.container
import timber.log.Timber

interface ScriptComponent : ContainerHost<ScriptState, Nothing> {
    fun onNameChange(name: String)
    fun onCreateScriptClick()
    fun onAddGroupAction(action: GroupAction)
    fun onRemoveGroupAction(action: GroupAction)
    fun onAddChannelAction(action: ChannelAction)
    fun onRemoveChannelAction(action: ChannelAction)
    fun onExpandGroupClick(groupId: Int, expanded: Boolean)
    fun onExpandChannelClick(channelId: Int, expanded: Boolean)
    fun onBackClick()
}

class ScriptComponentImpl(
    componentContext: ComponentContext,
    private val onBackClicked: () -> Unit,
    private val onScripCreated: () -> Unit,
) : ComponentContext by componentContext,
    ScriptComponent,
    KoinComponent {

    private val getGroupsUseCase: GetGroupsUseCase by inject()
    private val createScriptUseCase: CreateScriptUseCase by inject()
    private val context: Context by inject()
    private val scope = componentScope()
    private var modifyChannelJob: Job? = null
    private var modifyGroupJob: Job? = null

    override val container: Container<ScriptState, Nothing> = scope.container(ScriptState.Loading)

    init {
        loadGroups()
    }

    override fun onNameChange(name: String) {
        intent {
            reduce {
                val state = state as? ScriptState.Content ?: return@reduce state
                state.copy(name = name)
            }
        }
    }

    override fun onCreateScriptClick() {
        intent {
            val state = container.stateFlow.value as? ScriptState.Content ?: return@intent
            if (state.name.isBlank()) {
                reduce {
                    state.copy(error = context.getString(R.string.script_name_error))
                }
                return@intent
            }
            val actions = state.groups.flatMap { it.toChannelActions() }
            createScriptUseCase(CreateScriptPayload(state.name, actions))
                .take(1)
                .collect { result ->
                    result
                        .onSuccess { onScripCreated() }
                        .onFailure { Timber.e(it) }
                }
        }
    }

    override fun onAddGroupAction(action: GroupAction) {
        modifyGroupAction(action = action, isNew = true)
    }

    override fun onRemoveGroupAction(action: GroupAction) {
        modifyGroupAction(action = action, isNew = false)
    }

    override fun onAddChannelAction(action: ChannelAction) {
        modifyChannelAction(action = action, isNew = true)
    }

    override fun onRemoveChannelAction(action: ChannelAction) {
        modifyChannelAction(action = action, isNew = false)
    }

    override fun onExpandGroupClick(groupId: Int, expanded: Boolean) {
        intent {
            reduce {
                val state = state as? ScriptState.Content ?: return@reduce state
                val groups = state.groups.map { scriptGroup ->
                    if (scriptGroup.group.id == groupId) {
                        scriptGroup.copy(expanded = expanded)
                    } else {
                        scriptGroup
                    }
                }
                state.copy(groups = groups)
            }
        }
    }

    override fun onExpandChannelClick(channelId: Int, expanded: Boolean) {
        intent {
            reduce {
                val state = state as? ScriptState.Content ?: return@reduce state
                val groups = state.groups.map { scriptGroup ->
                    val channels = scriptGroup.channels.map { channel ->
                        if (channel.id == channelId) {
                            channel.copy(expanded = expanded)
                        } else {
                            channel
                        }
                    }
                    scriptGroup.copy(channels = channels)
                }
                state.copy(groups = groups)
            }
        }
    }

    override fun onBackClick() {
        onBackClicked()
    }

    private fun loadGroups() = intent {
        getGroupsUseCase(Unit)
            .collectLatest { result ->
                result
                    .onSuccess { groups ->
                        val scriptGroups = groups.map(Group::toScriptGroup)
                        reduce {
                            val currentState = state
                            if (currentState !is ScriptState.Content) {
                                ScriptState.Content(name = "", error = "", groups = scriptGroups)
                            } else {
                                currentState.copy(groups = scriptGroups)
                            }
                        }
                    }
            }
    }

    private fun modifyGroupAction(
        action: GroupAction,
        isNew: Boolean
    ) {
        modifyGroupJob?.cancel()
        modifyGroupJob = intent {
            val contentState = state as? ScriptState.Content ?: return@intent
            val groups = contentState.groups.map { scriptGroup ->
                if (scriptGroup.group.id == action.group.id) {
                    val actions = scriptGroup.actions.toMutableSet().apply {
                        if (isNew) add(action) else remove(action)
                    }
                    scriptGroup.copy(actions = actions)
                } else {
                    scriptGroup
                }
            }
            reduce {
                contentState.copy(groups = groups)
            }
        }
    }

    private fun modifyChannelAction(
        action: ChannelAction,
        isNew: Boolean
    ) {
        modifyChannelJob?.cancel()
        modifyChannelJob = intent {
            val contentState = state as? ScriptState.Content ?: return@intent
            val groups = contentState.groups.map { scriptGroup ->
                if (scriptGroup.channels.any { channel -> channel.id == action.channelId }) {
                    val channels = scriptGroup.channels.map { channel ->
                        if (channel.id == action.channelId) {
                            val actions = channel.actions.toMutableSet().apply {
                                if (isNew) add(action) else remove(action)
                            }
                            channel.copy(actions = actions)
                        } else {
                            channel
                        }
                    }
                    scriptGroup.copy(channels = channels)
                } else {
                    scriptGroup
                }
            }
            reduce { contentState.copy(groups = groups) }
        }
    }
}
