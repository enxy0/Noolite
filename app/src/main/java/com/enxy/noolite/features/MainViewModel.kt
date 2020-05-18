package com.enxy.noolite.features

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.enxy.noolite.core.data.Action.*
import com.enxy.noolite.core.data.ChannelAction
import com.enxy.noolite.core.data.Dummy
import com.enxy.noolite.core.data.Group
import com.enxy.noolite.core.data.Script
import com.enxy.noolite.core.exception.Failure
import com.enxy.noolite.core.network.ConnectionManager
import com.enxy.noolite.core.network.Repository
import com.enxy.noolite.features.settings.SettingsManager
import kotlinx.coroutines.launch

/*
* MainViewModel contains data which is shared and used across the app (fragments).
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

    /**
     * App has two options for turning on and off the light.
     * if hasToggleButton == true, then there will be only one button (toggle).
     * Otherwise, there will be two buttons - turn on and turn off.
     */
    val hasToggleButton: Boolean
        get() = settingsManager.hasToggleButton

    val themeName: String
        get() = settingsManager.currentTheme

    init {
        fetchGroupList(settingsManager.ipAddress)
        fetchFavouriteGroup()
        fetchScripts()
    }

    /**
     * Performs a single action on the channel.
     */
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

    /**
     * Runs the given script (performs actions on all channels in the script)
     */
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
        this.scriptList.value!!.add(script)
        viewModelScope.launch {
            repository.saveScripts(scriptList.value!!)
        }
    }

    fun removeScript(script: Script) {
        this.scriptList.value!!.remove(script)
        viewModelScope.launch {
            repository.saveScripts(scriptList.value!!)
        }
    }

    fun setFavouriteGroup(group: Group) {
        viewModelScope.launch {
            this@MainViewModel.favouriteGroup.value = group
            repository.saveFavouriteGroupElement(group)
        }
    }

    /**
     * Clears favourite group and removes it from database.
     */
    fun clearFavouriteGroup() {
        this.favouriteGroup.value = null
        this.favouriteGroupFailure.value = Failure.DataNotFound
        viewModelScope.launch {
            repository.removeFavouriteGroup()
        }
    }

    /**
     * Updates theme related values in [SettingsManager].
     * Should be used in Activity to retrieve bundled data
     * after "theme change" (activity restart).
     */
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

    /**
     * Sets test data to showcase app functionality/design.
     */
    fun setTestData() {
        groupListFailure.value = null
        favouriteGroupFailure.value = null
        scriptListFailure.value = null
        groupList.value = Dummy.groupList
        favouriteGroup.value = Dummy.favouriteGroup
        scriptList.value = Dummy.scriptList
    }
}