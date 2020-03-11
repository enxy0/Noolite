package com.enxy.noolite.features

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.enxy.noolite.core.exception.Failure
import com.enxy.noolite.core.network.Repository
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
    val settingsManager: SettingsManager,
    private val repository: Repository
) : ViewModel() {
    val scriptList = MutableLiveData(ArrayList<Script>())
    val scriptListFailure = MutableLiveData<Failure>()
    val groupListFailure = MutableLiveData<Failure>()
    val groupList = MutableLiveData(ArrayList<Group>())
    val favouriteGroup = MutableLiveData<Group>()
    val favouriteGroupFailure = MutableLiveData<Failure>()

    init {
        fetchGroupList(ipAddress = settingsManager.ipAddress)
        fetchFavouriteGroup()
        fetchScripts()
    }

    fun fetchGroupList(ipAddress: String, isForceUpdating: Boolean = false) {
        viewModelScope.launch {
            repository.getGroupList(ipAddress, isForceUpdating)
                .either(::handleGroupListFailure, ::handleGroupList)
        }
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
            scriptList.value!!.add(script)
            repository.saveScripts(scriptList.value!!)
        }
    }

    fun setFavouriteGroup(group: Group) {
        viewModelScope.launch {
            favouriteGroup.value = group
            repository.saveFavouriteGroupElement(group)
        }
    }

    private fun handleGroupList(groupList: ArrayList<Group>) {
        viewModelScope.launch {
            this@MainViewModel.groupList.value = groupList
            repository.saveGroupList(groupList)
            favouriteGroupFailure.value = null
            Log.d("MainViewModel", "handleGroupList: groupList=$groupList")
        }
    }

    private fun handleScriptList(scriptList: ArrayList<Script>?) {
        this.scriptList.value = scriptList
    }

    private fun handleGroupListFailure(failure: Failure) {
        this.groupListFailure.value = failure
        Log.d("MainViewModel", "handleGroupListFailure: failure=${failure.javaClass.name}")
    }

    private fun handleFavouriteGroup(group: Group) {
        this.favouriteGroup.value = group
    }

    private fun handleFavouriteGroupFailure(failure: Failure) {
        this.favouriteGroupFailure.value = failure
    }

    fun loadTestData() {
        favouriteGroupFailure.value = null
        groupListFailure.value = null
        groupList.value = TestData.groupElementList
        favouriteGroup.value = TestData.favouriteGroupElement
    }
}