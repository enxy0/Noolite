package com.enxy.noolite.presentation.ui.script

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import com.arkivanov.decompose.ComponentContext
import com.enxy.noolite.domain.features.actions.model.ChannelAction
import com.enxy.noolite.domain.features.actions.model.GroupAction
import com.enxy.noolite.domain.features.script.CreateScriptUseCase
import com.enxy.noolite.domain.features.script.GetGroupsUseCase
import com.enxy.noolite.domain.features.script.model.CreateScriptPayload
import com.enxy.noolite.presentation.ui.script.model.ScriptGroup
import com.enxy.noolite.utils.componentScope
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.take
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import timber.log.Timber

interface ScriptComponent {
    val groups: List<ScriptGroup>
    val name: State<String>
    val isError: State<Boolean>
    val isScriptCreated: Flow<Boolean>
    fun onNameChange(name: String)
    fun onCreateScriptClick()
    fun onAddGroupAction(action: GroupAction)
    fun onRemoveGroupAction(action: GroupAction)
    fun onAddChannelAction(action: ChannelAction)
    fun onRemoveChannelAction(action: ChannelAction)
}

class ScriptComponentImpl(
    componentContext: ComponentContext,
) : ComponentContext by componentContext,
    ScriptComponent,
    KoinComponent { // TODO: Remove

    private val getGroupsUseCase: GetGroupsUseCase by inject()
    private val createScriptUseCase: CreateScriptUseCase by inject()

    private val scope = componentScope()

    private val _groups = mutableStateListOf<ScriptGroup>()
    override val groups = _groups

    private val _name = mutableStateOf("")
    override val name: State<String> = _name

    private val _isError = mutableStateOf(false)
    override val isError: State<Boolean> = _isError

    private val _isScriptCreated = MutableSharedFlow<Boolean>(0, 1, BufferOverflow.DROP_OLDEST)
    override val isScriptCreated: Flow<Boolean> = _isScriptCreated.asSharedFlow()

    init {
        loadGroups()
    }

    override fun onNameChange(name: String) {
        _name.value = name
    }

    override fun onCreateScriptClick() {
        if (!validate()) return
        val name = _name.value
        val actions = _groups.flatMap { it.toChannelActions() }
        createScriptUseCase(CreateScriptPayload(name, actions))
            .onEach { result ->
                result
                    .onSuccess {
                        _isScriptCreated.emit(true)
                    }
                    .onFailure {
                        Timber.e(it)
                    }
            }
            .launchIn(scope)
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

    private fun loadGroups() {
        getGroupsUseCase(Unit)
            .take(1)
            .onEach { result ->
                result
                    .onSuccess { groups ->
                        _groups.addAll(groups.map { it.toScriptGroup() })
                    }
                    .onFailure {
                        Timber.e(it)
                    }
            }
            .launchIn(scope)
    }

    private fun modifyGroupAction(
        action: GroupAction,
        isNew: Boolean
    ) {
        val index = groups.indexOfFirst { scriptGroup -> scriptGroup.group.id == action.group.id }
        if (index in groups.indices) {
            val group = groups[index]
            val actions = group.actions.toMutableList().apply {
                if (isNew) add(action) else remove(action)
            }
            _groups[index] = group.copy(actions = actions)
        }
    }

    private fun modifyChannelAction(
        action: ChannelAction,
        isNew: Boolean
    ) {
        var indexOfChannel = -1
        val indexOfGroup = groups.indexOfFirst { model ->
            indexOfChannel = model.channels.indexOfFirst { scriptChannel ->
                scriptChannel.id == action.channelId
            }
            indexOfChannel in model.channels.indices
        }
        if (indexOfGroup in groups.indices) {
            val group = groups[indexOfGroup]
            val channel = group.channels[indexOfChannel]
            val actions = channel.actions.toMutableList().apply {
                if (isNew) add(action) else remove(action)
            }
            val channels = group.channels.toMutableList()
            channels[indexOfChannel] = channel.copy(actions = actions)
            _groups[indexOfGroup] = group.copy(channels = channels)
        }
    }

    private fun validate(): Boolean {
        val isValid = _name.value.isNotBlank()
        _isError.value = !isValid
        return isValid
    }
}
