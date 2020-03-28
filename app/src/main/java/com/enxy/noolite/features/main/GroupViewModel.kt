package com.enxy.noolite.features.main

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.enxy.noolite.core.exception.Failure
import com.enxy.noolite.core.network.Repository
import com.enxy.noolite.features.model.Action.*
import com.enxy.noolite.features.model.ChannelAction
import com.enxy.noolite.features.model.Script
import kotlinx.coroutines.launch
import javax.inject.Inject

class GroupViewModel @Inject constructor(
    private val repository: Repository
) : ViewModel() {
    val failure: MutableLiveData<Failure> = MutableLiveData()

    fun doAction(channelAction: ChannelAction) {
        viewModelScope.launch {
            when (channelAction.action) {
                TURN_OFF -> repository
                    .turnOffLight(channelAction.channelId)
                    .either(::handleFailure) { }
                TURN_ON -> repository
                    .turnOnLight(channelAction.channelId)
                    .either(::handleFailure) { }
                TOGGLE_STATE -> repository
                    .changeLightState(channelAction.channelId)
                    .either(::handleFailure) { }
                CHANGE_BRIGHTNESS -> repository
                    .changeBacklightBrightness(channelAction.channelId, channelAction.brightness!!)
                    .either(::handleFailure) { }
                CHANGE_COLOR -> repository
                    .changeBacklightColor(channelAction.channelId)
                    .either(::handleFailure) { }
                START_OVERFLOW -> repository
                    .startBacklightOverflow(channelAction.channelId)
                    .either(::handleFailure) { }
                STOP_OVERFLOW -> repository
                    .stopBacklightOverflow(channelAction.channelId)
                    .either(::handleFailure) { }
            }
        }
    }

    fun runScript(script: Script) {
        for (channelAction in script.actionsList)
            doAction(channelAction)
    }

    private fun handleFailure(failure: Failure) {
        this.failure.value = failure
    }
}