package com.enxy.noolite.presentation.ui.script

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.enxy.domain.features.actions.model.ChannelAction
import com.enxy.domain.features.actions.model.GroupAction
import com.enxy.domain.features.script.CreateScriptUseCase
import com.enxy.domain.features.script.GetGroupsUseCase
import com.enxy.domain.features.script.model.CreateScriptPayload
import com.enxy.noolite.presentation.ui.script.model.ScriptGroup
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.take
import timber.log.Timber

class ScriptViewModel(
    private val getGroupsUseCase: GetGroupsUseCase,
    private val createScriptUseCase: CreateScriptUseCase
) : ViewModel() {

    private val _groups = mutableStateListOf<ScriptGroup>()
    val groups = _groups

    private val _name = mutableStateOf("")
    val name: State<String> = _name

    private val _isError = mutableStateOf(false)
    val isError: State<Boolean> = _isError

    private val _isScriptCreated = MutableSharedFlow<Boolean>(0, 1, BufferOverflow.DROP_OLDEST)
    val isScriptCreated: Flow<Boolean> = _isScriptCreated.asSharedFlow()

    init {
        loadGroups()
    }

    fun onNameChange(name: String) {
        _name.value = name
    }

    fun onCreateScriptClick() {
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
            .launchIn(viewModelScope)
    }

    fun onAddGroupAction(action: GroupAction) {
        modifyGroupAction(action = action, isNew = true)
    }

    fun onRemoveGroupAction(action: GroupAction) {
        modifyGroupAction(action = action, isNew = false)
    }

    fun onAddChannelAction(action: ChannelAction) {
        modifyChannelAction(action = action, isNew = true)
    }

    fun onRemoveChannelAction(action: ChannelAction) {
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
            .launchIn(viewModelScope)
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
