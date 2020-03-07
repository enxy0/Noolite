package com.enxy.noolite.features.group

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.enxy.noolite.core.exception.Failure
import com.enxy.noolite.core.network.Repository
import com.enxy.noolite.features.model.Group
import com.enxy.noolite.features.settings.SettingsManager
import kotlinx.coroutines.launch
import javax.inject.Inject

class GroupViewModel @Inject constructor(
    private val repository: Repository,
    private val settingsManager: SettingsManager
) : ViewModel() {
    val groupList: MutableLiveData<ArrayList<Group>> = MutableLiveData()
    val failure: MutableLiveData<Failure> = MutableLiveData()


    init {
        fetchGroupList()
    }

    private fun fetchGroupList() {
        viewModelScope.launch {
            repository.getGroupList(settingsManager.ipAddress, false)
                .either(::handleFailure, ::handleGroupList)
        }
    }

    fun turnOnLight(channelId: Int) {
        viewModelScope.launch {
            repository.turnOnLight(channelId).either(::handleFailure) { }
        }
    }

    fun turnOffLight(channelId: Int) {
        viewModelScope.launch {
            repository.turnOffLight(channelId).either(::handleFailure) { }
        }
    }

    private fun handleGroupList(groupList: ArrayList<Group>) {
        this.groupList.value = groupList
    }

    private fun handleFailure(failure: Failure) {
        this.failure.value = failure
    }
}