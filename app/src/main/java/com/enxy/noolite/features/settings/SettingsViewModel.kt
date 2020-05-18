package com.enxy.noolite.features.settings

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.enxy.noolite.core.data.Group
import com.enxy.noolite.core.exception.Failure
import com.enxy.noolite.core.network.Repository
import kotlinx.coroutines.launch

/**
 * [SettingsViewModel] contains data that is being used by [SettingsFragment].
 */
class SettingsViewModel(
    private val settingsManager: SettingsManager,
    private val repository: Repository
) : ViewModel() {
    val hasToggleButton: Boolean
        get() = settingsManager.hasToggleButton
    val ipAddress: String
        get() = settingsManager.ipAddress
    val currentTheme: String
        get() = settingsManager.currentTheme
    val wifiNotification: Boolean
        get() = settingsManager.wifiNotification
    val appBuildNumber: String
        get() = settingsManager.appBuildNumber
    val appVersion: String
        get() = settingsManager.appVersion
    val themeChanged: Boolean
        get() = settingsManager.themeChanged
    val scrollX: Int
        get() = settingsManager.scrollX
    val scrollY: Int
        get() = settingsManager.scrollY
    val groupList: MutableLiveData<ArrayList<Group>> = MutableLiveData()
    val failure: MutableLiveData<Failure> = MutableLiveData()

    fun fetchGroupList(ipAddress: String) {
        viewModelScope.launch {
            repository.getGroupList(ipAddress, true).either(::handleFailure, ::handleGroupList)
        }
    }

    fun setHasToggleButton(isChecked: Boolean) {
        settingsManager.hasToggleButton = isChecked
    }

    fun setIpAddress(ipAddress: String) {
        settingsManager.ipAddress = ipAddress
    }

    fun setTheme(themeName: String) {
        settingsManager.currentTheme = themeName
    }

    fun setWifiNotification(isChecked: Boolean) {
        settingsManager.wifiNotification = isChecked
    }

    /**
     * Ends the process of theme change by clearing all the values.
     */
    fun clearThemeChangeValues() = with(settingsManager) {
        themeChanged = false
        scrollX = 0
        scrollY = 0
    }

    fun prepareToFetchGroupList(ipAddress: String) {
        setIpAddress(ipAddress)
        this.groupList.value = null
        this.failure.value = null
    }

    private fun handleFailure(failure: Failure) {
        this.failure.value = failure
    }

    private fun handleGroupList(groupList: ArrayList<Group>) {
        this.groupList.value = groupList
    }
}