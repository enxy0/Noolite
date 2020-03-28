package com.enxy.noolite.features

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.enxy.noolite.R
import com.enxy.noolite.core.exception.Failure
import com.enxy.noolite.core.network.Repository
import com.enxy.noolite.core.platform.FileManager
import com.enxy.noolite.features.model.Action.*
import com.enxy.noolite.features.model.ChannelAction
import com.enxy.noolite.features.model.Group
import com.enxy.noolite.features.model.Script
import com.enxy.noolite.features.model.TestData
import com.enxy.noolite.features.settings.SettingsManager
import kotlinx.coroutines.launch
import javax.inject.Inject

/*
* MainViewModel contains data which is shared and used across the app (fragments)
*/
class MainViewModel @Inject constructor(
    private val settingsManager: SettingsManager,
    private val repository: Repository
) : ViewModel() {
    val scriptList = MutableLiveData(ArrayList<Script>())
    val groupList = MutableLiveData(ArrayList<Group>())
    val favouriteGroup = MutableLiveData<Group>()
    val failure = MutableLiveData<Failure>()
    val hasToggleButton: Boolean
        get() = settingsManager.hasToggleButton
    val themeChanged: Boolean
        get() = settingsManager.themeChanged
    val currentTheme: Int
        get() = when (settingsManager.currentTheme) {
            FileManager.BLACK_BLUE_THEME_VALUE -> R.style.AppTheme_Black_Amber
            FileManager.WHITE_BLUE_THEME_VALUE -> R.style.AppTheme_White_Blue
            else -> R.style.AppTheme_Dark_Green
        }

    init {
        fetchGroupList(ipAddress = settingsManager.ipAddress)
        fetchFavouriteGroup()
        fetchScripts()
    }

    fun doAction(channelAction: ChannelAction) {
        viewModelScope.launch {
            when (channelAction.action) {
                TURN_OFF -> repository
                    .turnOffLight(channelAction.channelId)
                    .either({}) { }
                TURN_ON -> repository
                    .turnOnLight(channelAction.channelId)
                    .either({}) { }
                TOGGLE_STATE -> repository
                    .changeLightState(channelAction.channelId)
                    .either({}) { }
                CHANGE_BRIGHTNESS -> repository
                    .changeBacklightBrightness(channelAction.channelId, channelAction.brightness!!)
                    .either({}) { }
                CHANGE_COLOR -> repository
                    .changeBacklightColor(channelAction.channelId)
                    .either({}) { }
                START_OVERFLOW -> repository
                    .startBacklightOverflow(channelAction.channelId)
                    .either({}) { }
                STOP_OVERFLOW -> repository
                    .stopBacklightOverflow(channelAction.channelId)
                    .either({}) { }
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
            repository.getScripts().either({}, ::handleScriptList)
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

    fun setThemeChangeValues(_themeChanged: Boolean, settingsScrollX: Int, settingsScrollY: Int) =
        with(settingsManager) {
            themeChanged = _themeChanged
            scrollX = settingsScrollX
            scrollY = settingsScrollY
        }

    private fun handleGroupList(groupList: ArrayList<Group>) {
        viewModelScope.launch {
            this@MainViewModel.groupList.value = groupList
            repository.saveGroupList(groupList)
//            groupListFailure.value = null
            Log.d("MainViewModel", "handleGroupList: groupList=$groupList")
        }
    }

    private fun handleScriptList(scriptList: ArrayList<Script>?) {
        this.scriptList.value = scriptList
    }

    private fun handleGroupListFailure(failure: Failure) {
        this.failure.value = failure
        Log.d("MainViewModel", "handleGroupListFailure: failure=${failure.javaClass.name}")
    }

    private fun handleFavouriteGroup(group: Group) {
        this.favouriteGroup.value = group
    }

    private fun handleFavouriteGroupFailure(failure: Failure) {
        this.failure.value = failure
    }

    fun loadTestData() {
        failure.value = null
        groupList.value = TestData.groupElementList
        favouriteGroup.value = TestData.favouriteGroupElement
    }
}