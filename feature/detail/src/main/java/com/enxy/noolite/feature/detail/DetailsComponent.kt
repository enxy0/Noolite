package com.enxy.noolite.feature.detail

import com.arkivanov.decompose.ComponentContext
import com.enxy.noolite.core.model.ChannelAction
import com.enxy.noolite.core.model.Group
import com.enxy.noolite.core.ui.extensions.componentScope
import com.enxy.noolite.domain.common.ChannelActionUseCase
import com.enxy.noolite.domain.common.model.SetFavoriteGroupPayload
import com.enxy.noolite.domain.home.GetFavoriteGroupUseCase
import com.enxy.noolite.domain.home.SetFavoriteGroupUseCase
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.map
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import org.orbitmvi.orbit.Container
import org.orbitmvi.orbit.ContainerHost
import org.orbitmvi.orbit.container

interface DetailsComponent : ContainerHost<DetailsState, Nothing> {
    fun onBackClick()
    fun onFavoriteClick(favorite: Boolean)
    fun onChannelActionClick(action: ChannelAction)
}

class DetailsComponentImpl(
    componentContext: ComponentContext,
    group: Group,
    private val onBackClicked: () -> Unit,
) : ComponentContext by componentContext,
    DetailsComponent,
    KoinComponent {

    private val getFavoriteGroupUseCase: GetFavoriteGroupUseCase by inject()
    private val setFavoriteGroupUseCase: SetFavoriteGroupUseCase by inject()
    private val channelActionUseCase: ChannelActionUseCase by inject()

    private val scope = componentScope()
    override val container: Container<DetailsState, Nothing> = scope.container(DetailsState(group))

    init {
        loadData()
    }

    override fun onBackClick() {
        onBackClicked()
    }

    override fun onFavoriteClick(favorite: Boolean) {
        intent {
            setFavoriteGroupUseCase
                .invoke(SetFavoriteGroupPayload(container.stateFlow.value.group, favorite))
                .collect()
        }
    }

    override fun onChannelActionClick(action: ChannelAction) {
        intent {
            channelActionUseCase
                .invoke(action)
                .collect()
        }
    }

    private fun loadData() {
        intent {
            getFavoriteGroupUseCase(Unit)
                .map { it.getOrNull() }
                .collectLatest { group ->
                    reduce { state.copy(isFavorite = group?.id == state.group.id) }
                }
        }
    }
}
