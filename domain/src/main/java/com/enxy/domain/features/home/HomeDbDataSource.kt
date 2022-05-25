package com.enxy.domain.features.home

import com.enxy.domain.features.common.Group
import com.enxy.domain.features.home.model.SetFavoriteGroupPayload
import kotlinx.coroutines.flow.Flow

interface HomeDbDataSource {
    fun getGroupsFlow(): Flow<List<Group>>
    suspend fun setGroups(groups: List<Group>)

    fun getFavoriteGroupFlow(): Flow<Group?>
    suspend fun setFavoriteGroup(payload: SetFavoriteGroupPayload)
}
