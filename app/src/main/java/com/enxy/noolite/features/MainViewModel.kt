package com.enxy.noolite.features

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.enxy.noolite.core.exception.Failure
import com.enxy.noolite.core.network.Repository
import com.enxy.noolite.features.model.Action
import com.enxy.noolite.features.model.Group
import com.enxy.noolite.features.model.Script
import com.enxy.noolite.features.model.TestData
import com.enxy.noolite.features.settings.SettingsManager
import kotlinx.coroutines.launch
import javax.inject.Inject

class MainViewModel @Inject constructor(
    val settingsManager: SettingsManager,
    private val repository: Repository
) : ViewModel() {
    val scriptList = MutableLiveData(ArrayList<Script>())
    val groupFailure = MutableLiveData<Failure>()
    val favouriteFailure = MutableLiveData<Failure>()
    val lightFailure = MutableLiveData<Failure>()
    val groupElementList = MutableLiveData(ArrayList<Group>())
    val favouriteGroupElement = MutableLiveData<Group>()

    init {
        loadGroupElementList(ipAddress = settingsManager.ipAddress)
        loadFavouriteGroupElement()
        Log.d("MainViewModel", "init: ViewModel initialized")
    }

    private fun loadFavouriteGroupElement() = viewModelScope.launch {
        repository.getFavouriteGroup()
            .either(::updateFavouriteFailure, ::updateFavouriteGroupElement)
    }

    private fun getScripts() = viewModelScope.launch {
        repository.getScripts().either({}, ::updateScriptList)
    }

    fun addScript(script: Script) = viewModelScope.launch {
        scriptList.value!!.add(script)
        repository.saveScripts(scriptList.value!!)
    }

    fun executeScript(script: Script) {
        for (channelAction in script.actionsList) {
            when (channelAction.action) {
                Action.TURN_OFF -> turnOffLight(channelAction.channelId)
                Action.TURN_ON -> turnOnLight(channelAction.channelId)
                Action.TOGGLE_STATE -> changeLightState(channelAction.channelId)
                Action.CHANGE_BRIGHTNESS -> changeBacklightBrightness(
                    channelAction.channelId, channelAction.brightness!!
                )
                Action.CHANGE_COLOR -> changeBacklightColor(channelAction.channelId)
                Action.START_OVERFLOW -> startBacklightOverflow(channelAction.channelId)
                Action.STOP_OVERFLOW -> stopBacklightOverflow(channelAction.channelId)
            }
        }
    }

    fun loadGroupElementList(ipAddress: String, isForceUpdating: Boolean = false) =
        viewModelScope.launch {
            repository.getGroupList(ipAddress, isForceUpdating)
                .either(::updateGroupFailure, ::updateGroupHolder)
        }

    fun turnOffLight(channelId: Int) = viewModelScope.launch {
        repository.turnOffLight(channelId).either(::updateLightFailure, { })
    }

    fun turnOnLight(channelId: Int) = viewModelScope.launch {
        repository.turnOnLight(channelId).either(::updateLightFailure, { })
    }

    fun changeLightState(channelId: Int) = viewModelScope.launch {
        repository.changeLightState(channelId).either(::updateLightFailure, { })
    }

    fun changeBacklightColor(channelId: Int) = viewModelScope.launch {
        repository.changeBacklightColor(channelId).either(::updateLightFailure, { })
    }

    fun startBacklightOverflow(channelId: Int) = viewModelScope.launch {
        repository.startBacklightOverflow(channelId).either(::updateLightFailure, { })
    }

    fun stopBacklightOverflow(channelId: Int) = viewModelScope.launch {
        repository.stopBacklightOverflow(channelId).either(::updateLightFailure, { })
    }

    fun changeBacklightBrightness(channelId: Int, brightness: Int) = viewModelScope.launch {
        repository.changeBacklightBrightness(channelId, brightness)
            .either(::updateLightFailure, { })
    }

    private fun updateGroupHolder(groupList: ArrayList<Group>) =
        viewModelScope.launch {
            groupElementList.value = groupList
            repository.saveGroupList(groupList)
            groupFailure.value = null
            Log.d("MainViewModel", "updateGroupHolder: groupListHolderModel=${groupList}")
        }

    fun updateFavouriteGroupElement(group: Group) = viewModelScope.launch {
        favouriteGroupElement.value = group
        repository.saveFavouriteGroupElement(group)
        favouriteFailure.value = null
        Log.d(
            "MainViewModel",
            "updateFavouriteGroupElement: favouriteGroupElement=${favouriteGroupElement.value}"
        )
    }

    private fun updateScriptList(scriptList: ArrayList<Script>?) {
        this.scriptList.value = scriptList
    }

    private fun updateGroupFailure(failure: Failure) {
        this.groupElementList.value = null
        this.groupFailure.value = failure
        Log.d("MainViewModel", "updateGroupFailure: failure=${failure.javaClass.name}")
    }

    private fun updateFavouriteFailure(failure: Failure) {
        this.favouriteFailure.value = failure
    }

    private fun updateLightFailure(failure: Failure) {
        this.lightFailure.value = failure
    }

    fun loadTestData() {
        groupFailure.value = null
        favouriteFailure.value = null
        groupElementList.value = TestData.groupElementList
        favouriteGroupElement.value = TestData.favouriteGroupElement
    }
}