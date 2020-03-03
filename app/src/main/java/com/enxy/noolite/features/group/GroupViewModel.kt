package com.enxy.noolite.features.group

import androidx.lifecycle.LiveData
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
    private val _groupList: MutableLiveData<ArrayList<Group>> = MutableLiveData()
    private val _failure: MutableLiveData<Failure> = MutableLiveData()

    val failure: LiveData<Failure> = _failure
    val groupList: LiveData<ArrayList<Group>> = _groupList

    init {
        fetchGroupList()
    }

    private fun fetchGroupList() {
        viewModelScope.launch {
            repository.getGroupList(settingsManager.ipAddress, false)
                .either(::handleFailure, ::handleGroupList)
        }
    }

    private fun handleGroupList(groupList: ArrayList<Group>) {
        this._groupList.value = groupList
    }

    private fun handleFailure(failure: Failure) {
        this._failure.value = failure
    }

    fun turnOnLight(channelId: Int) {
        viewModelScope.launch {
            repository.turnOnLight(channelId).either(::handleFailure, { })
        }
    }

    fun turnOffLight(channelId: Int) {
        viewModelScope.launch {
            repository.turnOffLight(channelId).either(::handleFailure, { })
        }
    }
}