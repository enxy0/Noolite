package com.enxy.noolite.features.detail

import com.arkivanov.decompose.ComponentContext
import com.enxy.noolite.domain.features.actions.ChannelActionUseCase
import com.enxy.noolite.domain.features.actions.model.ChannelAction
import com.enxy.noolite.domain.features.common.Group
import com.enxy.noolite.domain.features.home.GetFavoriteGroupUseCase
import com.enxy.noolite.domain.features.home.SetFavoriteGroupUseCase
import com.enxy.noolite.domain.features.home.model.SetFavoriteGroupPayload
import com.enxy.noolite.utils.extensions.componentScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.map
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

interface DetailsComponent {
    val group: Group
    val isFavoriteGroup: Flow<Boolean>
    fun onBackClick()
    fun onFavoriteClick(favorite: Boolean)
    fun onChannelActionClick(action: ChannelAction)
}

class DetailsComponentImpl(
    componentContext: ComponentContext,
    override val group: Group,
    private val onBackClicked: () -> Unit,
) : ComponentContext by componentContext,
    DetailsComponent,
    KoinComponent {

    private val getFavoriteGroupUseCase: GetFavoriteGroupUseCase by inject()
    private val setFavoriteGroupUseCase: SetFavoriteGroupUseCase by inject()
    private val channelActionUseCase: ChannelActionUseCase by inject()

    private val scope = componentScope()

    override val isFavoriteGroup: Flow<Boolean> = getFavoriteGroupUseCase(Unit)
        .map { result -> this.group.id == result.getOrNull()?.id }

    override fun onBackClick() {
        onBackClicked()
    }

    override fun onFavoriteClick(favorite: Boolean) {
        setFavoriteGroupUseCase
            .invoke(SetFavoriteGroupPayload(group, favorite))
            .launchIn(scope)
    }

    override fun onChannelActionClick(action: ChannelAction) {
        channelActionUseCase
            .invoke(action)
            .launchIn(scope)
    }
}
