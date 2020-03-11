package com.enxy.noolite.features.channel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.enxy.noolite.core.exception.Failure
import com.enxy.noolite.core.network.Repository
import com.enxy.noolite.features.model.Action
import com.enxy.noolite.features.model.ChannelAction
import com.enxy.noolite.features.settings.SettingsManager
import kotlinx.coroutines.launch
import javax.inject.Inject

class ChannelViewModel @Inject constructor(
    private val repository: Repository,
    private val settingsManager: SettingsManager
) : ViewModel() {
    val hasToggleButton: Boolean
        get() = settingsManager.hasToggleButton

    val failure: MutableLiveData<Failure> = MutableLiveData()

    fun doAction(channelAction: ChannelAction) {
        viewModelScope.launch {
            when (channelAction.action) {
                Action.TURN_OFF -> repository
                    .turnOffLight(channelAction.channelId)
                    .either(::handleFailure) { }
                Action.TURN_ON -> repository
                    .turnOnLight(channelAction.channelId)
                    .either(::handleFailure) { }
                Action.TOGGLE_STATE -> repository
                    .changeLightState(channelAction.channelId)
                    .either(::handleFailure) { }
                Action.CHANGE_BRIGHTNESS -> repository
                    .changeBacklightBrightness(channelAction.channelId, channelAction.brightness!!)
                    .either(::handleFailure) { }
                Action.CHANGE_COLOR -> repository
                    .changeBacklightColor(channelAction.channelId)
                    .either(::handleFailure) { }
                Action.START_OVERFLOW -> repository
                    .startBacklightOverflow(channelAction.channelId)
                    .either(::handleFailure) { }
                Action.STOP_OVERFLOW -> repository
                    .stopBacklightOverflow(channelAction.channelId)
                    .either(::handleFailure) { }
            }
        }
    }

    private fun handleFailure(failure: Failure) {
        this.failure.value = failure
    }
}