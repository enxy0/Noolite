package com.enxy.noolite.features

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.enxy.noolite.core.exception.Failure
import com.enxy.noolite.core.network.ConnectionManager
import com.enxy.noolite.core.network.Repository
import com.enxy.noolite.features.model.Action.*
import com.enxy.noolite.features.model.ChannelAction
import com.enxy.noolite.features.model.Group
import com.enxy.noolite.features.model.Script
import com.enxy.noolite.features.model.TestData
import com.enxy.noolite.features.settings.SettingsManager
import kotlinx.coroutines.launch

/*
* MainViewModel contains data which is shared and used across the app (fragments)
*/
class MainViewModel(
    private val settingsManager: SettingsManager,
    private val repository: Repository,
    private val connectionManager: ConnectionManager
) : ViewModel() {
    val scriptList = MutableLiveData(ArrayList<Script>())
    val scriptListFailure = MutableLiveData<Failure>()

    val groupList = MutableLiveData(ArrayList<Group>())
    val groupListFailure = MutableLiveData<Failure>()

    val favouriteGroup = MutableLiveData<Group>()
    val favouriteGroupFailure = MutableLiveData<Failure>()

    val isWifiConnected: Boolean
        get() = connectionManager.isWifiConnected()

    val hasToggleButton: Boolean
        get() = settingsManager.hasToggleButton

    val themeName: String
        get() = settingsManager.currentTheme

    init {
        fetchGroupList(settingsManager.ipAddress)
        fetchFavouriteGroup()
        fetchScripts()
    }

    fun doAction(channelAction: ChannelAction) {
        viewModelScope.launch {
            when (channelAction.action) {
                TURN_OFF -> repository
                    .turnOffLight(channelAction.channelId)
                TURN_ON -> repository
                    .turnOnLight(channelAction.channelId)
                TOGGLE_STATE -> repository
                    .changeLightState(channelAction.channelId)
                CHANGE_BRIGHTNESS -> repository
                    .changeBacklightBrightness(channelAction.channelId, channelAction.brightness!!)
                CHANGE_COLOR -> repository
                    .changeBacklightColor(channelAction.channelId)
                START_OVERFLOW -> repository
                    .startBacklightOverflow(channelAction.channelId)
                STOP_OVERFLOW -> repository
                    .stopBacklightOverflow(channelAction.channelId)
            }
        }
    }

    fun runScript(script: Script) {
        for (channelAction in script.actionsList)
            doAction(channelAction)
    }

    private fun fetchGroupList(ipAddress: String, isForceUpdating: Boolean = false) {
        viewModelScope.launch {
            repository.getGroupList(ipAddress, isForceUpdating)
                .either(::handleGroupListFailure, ::handleGroupList)
        }
    }

    fun updateGroupList(groupList: ArrayList<Group>) {
        handleGroupList(groupList)
    }

    private fun fetchFavouriteGroup() {
        viewModelScope.launch {
            repository.getFavouriteGroup()
                .either(::handleFavouriteGroupFailure, ::handleFavouriteGroup)
        }
    }

    private fun fetchScripts() {
        viewModelScope.launch {
            repository.getScripts().either(::handleScriptListFailure, ::handleScriptList)
        }
    }

    fun addScript(script: Script) {
        viewModelScope.launch {
            this@MainViewModel.scriptList.value!!.add(script)
            repository.saveScripts(scriptList.value!!)
        }
    }

    fun setFavouriteGroup(group: Group) {
        viewModelScope.launch {
            this@MainViewModel.favouriteGroup.value = group
            repository.saveFavouriteGroupElement(group)
        }
    }

    fun clearFavouriteGroup() {
        this.favouriteGroup.value = null
        this.favouriteGroupFailure.value = Failure.DataNotFound
        viewModelScope.launch {
            repository.removeFavouriteGroup()
        }
    }

    fun setThemeChangeValues(
        _themeChanged: Boolean,
        themeName: String,
        settingsScrollX: Int,
        settingsScrollY: Int
    ) {
        settingsManager.apply {
            themeChanged = _themeChanged
            currentTheme = themeName
            scrollX = settingsScrollX
            scrollY = settingsScrollY
        }

    }

    private fun handleGroupList(groupList: ArrayList<Group>) {
        viewModelScope.launch {
            this@MainViewModel.groupList.value = groupList
            repository.saveGroupList(groupList)
        }
    }

    private fun handleScriptList(scriptList: ArrayList<Script>?) {
        this.scriptList.value = scriptList
    }

    private fun handleGroupListFailure(failure: Failure) {
        this.groupListFailure.value = failure
    }

    private fun handleFavouriteGroup(group: Group) {
        this.favouriteGroup.value = group
    }

    private fun handleFavouriteGroupFailure(failure: Failure) {
        this.favouriteGroupFailure.value = failure
    }

    private fun handleScriptListFailure(failure: Failure) {
        this.scriptListFailure.value = failure
    }

    fun loadTestData() {
        groupListFailure.value = null
        favouriteGroupFailure.value = null
        groupList.value = TestData.groupElementList
        favouriteGroup.value = TestData.favouriteGroupElement
    }
}