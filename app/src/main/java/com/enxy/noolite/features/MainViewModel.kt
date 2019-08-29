package com.enxy.noolite.features

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.enxy.noolite.core.exception.Failure
import com.enxy.noolite.core.interactor.UseCase.None
import com.enxy.noolite.core.network.NetworkHandler
import com.enxy.noolite.core.network.NetworkRepository.NetworkManager
import com.enxy.noolite.core.platform.FileManager
import com.enxy.noolite.core.platform.Serializer
import com.enxy.noolite.features.interactor.*
import com.enxy.noolite.features.model.GroupListHolderModel
import com.enxy.noolite.features.model.GroupModel
import kotlinx.coroutines.*

class MainViewModel(application: Application) : AndroidViewModel(application), CoroutineScope {
    private val job = SupervisorJob()
    override val coroutineContext = Dispatchers.IO + job

    // Work with objects and files
    private val serializer = Serializer()
    private val fileManager = FileManager(application.applicationContext)
    val settingsManager = SettingsManager(fileManager)

    // Network Info
    val networkHandler = NetworkHandler(application.applicationContext)
    private val networkManager = NetworkManager(networkHandler)

    // Actions with Light
    private val getGroupHolder = GetGroupHolder(serializer, networkManager, fileManager)
    private val getFavouriteGroupElement = GetFavouriteGroupElement(serializer, fileManager)
    private val turnOnLight = TurnOnLight(networkManager)
    private val turnOffLight = TurnOffLight(networkManager)
    private val changeLightState = ChangeLightState(networkManager)
    private val changeBacklightColor = ChangeBacklightColor(networkManager)
    private val changeBacklightBrightness = ChangeBacklightBrightness(networkManager)
    private val startBacklightOverflow = StartBacklightOverflow(networkManager)
    private val stopBacklightOverflow = StopBacklightOverflow(networkManager)
    var groupFailure = MutableLiveData<Failure>()
    var favouriteFailure = MutableLiveData<Failure>()
    var chosenFailure = MutableLiveData<Failure>()
    var lightFailure = MutableLiveData<Failure>()
    val groupElementList = MutableLiveData(ArrayList<GroupModel>())
    var favouriteGroupElement = MutableLiveData<GroupModel>()
    var chosenGroupElement = MutableLiveData<GroupModel>()

    init {
        loadGroupElementList(ipAddress = settingsManager.ipAddress)
        loadFavouriteGroupElement()
    }

    private fun loadFavouriteGroupElement() = getFavouriteGroupElement(None()) {
        it.either(::updateFavouriteFailure, ::updateFavouriteGroupElement)
    }

    fun loadGroupElementList(force: Boolean = false, ipAddress: String) = getGroupHolder(GetGroupHolder.ParamsHolder(force, ipAddress)) {
        it.either(::updateGroupFailure, ::updateGroupHolder)
    }

    fun turnOffLight(channelId: Int) = turnOffLight(channelId) {
        it.either(::updateLightFailure, { })
    }

    fun turnOnLight(channelId: Int) = turnOnLight(channelId) {
        it.either(::updateLightFailure, { })
    }

    fun changeLightState(channelId: Int) = changeLightState(channelId) {
        it.either(::updateLightFailure, { })
    }

    fun changeBacklightColor(channelId: Int) = changeBacklightColor(channelId) {
        it.either(::updateLightFailure, { })
    }

    fun startBacklightOverflow(channelId: Int) = startBacklightOverflow(channelId) {
        it.either(::updateLightFailure, { })
    }

    fun stopBacklightOverflow(channelId: Int) = stopBacklightOverflow(channelId) {
        it.either(::updateLightFailure, { })
    }

    fun changeBacklightBrightness(channelId: Int, brightness: Int): Unit = changeBacklightBrightness(arrayOf(channelId, brightness)) {
        it.either(::updateLightFailure, { })
    }

    private fun updateGroupHolder(groupListHolderModel: GroupListHolderModel) {
        this.groupElementList.value = groupListHolderModel.groupListModel

        launch {
            val string = serializer.serialize(groupListHolderModel)
            fileManager.saveStringToPrefs(FileManager.MAIN_DATA_FILE, FileManager.GROUP_ELEMENT_LIST_KEY, string)
        }

        with(this.groupFailure) {
            if (value != null)
                value = null
        }
        Log.d("MainViewModel", "updateGroupHolder: groupListHolderModel=${groupListHolderModel.groupListModel}")
    }

    fun updateFavouriteGroupElement(groupModel: GroupModel) {
        this.favouriteGroupElement.value = groupModel

        launch {
            val string = serializer.serialize(groupModel)
            fileManager.saveStringToPrefs(FileManager.MAIN_DATA_FILE, FileManager.FAVOURITE_GROUP_KEY, string)
        }

        with(this.favouriteFailure) {
            if (value != null)
                value = null
        }
        Log.d("MainViewModel", "updateFavouriteGroupElement: favouriteGroupElement=$favouriteGroupElement")
    }

    private fun updateGroupFailure(failure: Failure) {
        with(this.groupElementList) {
            if (value != null)
                value = null
        }
        this.groupFailure.value = failure
        Log.d("MainViewModel", "updateGroupFailure: failure=${failure.javaClass.name}")
    }

    private fun updateFavouriteFailure(failure: Failure) {
        this.favouriteFailure.value = failure
    }

    private fun updateLightFailure(failure: Failure) {
        this.lightFailure.value = failure
    }

    override fun onCleared() {
        super.onCleared()
        getGroupHolder.onDestroy()
        getFavouriteGroupElement.onDestroy()
        turnOnLight.onDestroy()
        turnOffLight.onDestroy()
        changeLightState.onDestroy()
        changeBacklightBrightness.onDestroy()
        changeBacklightColor.onDestroy()
        startBacklightOverflow.onDestroy()
        stopBacklightOverflow.onDestroy()
        job.cancelChildren()
    }
}