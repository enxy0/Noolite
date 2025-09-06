package com.enxy.noolite.domain.common

import com.enxy.noolite.core.model.Group
import com.enxy.noolite.domain.common.model.SetFavoriteGroupPayload
import kotlinx.coroutines.flow.Flow

interface HomeDbDataSource {
    fun getGroupsFlow(): Flow<List<Group>>
    suspend fun setGroups(groups: List<Group>)

    fun getFavoriteGroupFlow(): Flow<Group?>
    suspend fun setFavoriteGroup(payload: SetFavoriteGroupPayload)
}