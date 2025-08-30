package com.enxy.noolite.domain.features.home

import com.enxy.noolite.domain.common.Group
import com.enxy.noolite.domain.features.home.model.SetFavoriteGroupPayload
import kotlinx.coroutines.flow.Flow

interface HomeDbDataSource {
    fun getGroupsFlow(): Flow<List<Group>>
    suspend fun setGroups(groups: List<Group>)

    fun getFavoriteGroupFlow(): Flow<Group?>
    suspend fun setFavoriteGroup(payload: SetFavoriteGroupPayload)
}
