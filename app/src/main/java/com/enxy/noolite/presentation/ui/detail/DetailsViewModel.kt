package com.enxy.noolite.presentation.ui.detail

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.enxy.domain.features.actions.ChannelActionUseCase
import com.enxy.domain.features.actions.model.ChannelAction
import com.enxy.domain.features.home.GetFavoriteGroupUseCase
import com.enxy.domain.features.home.SetFavoriteGroupUseCase
import com.enxy.domain.features.home.model.SetFavoriteGroupPayload
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.map

class DetailsViewModel(
    stateHandle: SavedStateHandle,
    getFavoriteGroupUseCase: GetFavoriteGroupUseCase,
    private val setFavoriteGroupUseCase: SetFavoriteGroupUseCase,
    private val channelActionUseCase: ChannelActionUseCase
) : ViewModel() {

    val group = DetailsFragmentArgs.fromSavedStateHandle(stateHandle).group

    val isFavoriteFlow: Flow<Boolean> = getFavoriteGroupUseCase(Unit)
        .map { result -> this.group.id == result.getOrNull()?.id }

    fun onFavoriteClick(isFavorite: Boolean) {
        setFavoriteGroupUseCase
            .invoke(SetFavoriteGroupPayload(group, isFavorite))
            .launchIn(viewModelScope)
    }

    fun onChannelAction(action: ChannelAction) {
        channelActionUseCase
            .invoke(action)
            .launchIn(viewModelScope)
    }
}
