package com.enxy.data.db.home

import com.enxy.domain.features.common.Group
import com.enxy.domain.features.home.HomeDbDataSource
import com.enxy.domain.features.home.model.SetFavoriteGroupPayload
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

internal class HomeDbDataSourceImpl(
    private val homeDao: HomeDao
) : HomeDbDataSource {
    override fun getGroupsFlow(): Flow<List<Group>> {
        return homeDao.getGroupsFlow().map { list -> list.map { entity -> entity.toDomain() } }
    }

    override suspend fun setGroups(groups: List<Group>) {
        homeDao.clearGroups()
        for (group in groups) {
            homeDao.insertGroup(group.toEntity())
        }
    }

    override fun getFavoriteGroupFlow(): Flow<Group?> {
        return homeDao.getFavoriteGroupFlow().map { group -> group?.toDomain() }
    }

    override suspend fun setFavoriteGroup(payload: SetFavoriteGroupPayload) {
        if (payload.isFavorite) {
            homeDao.setFavoriteGroup(payload.group.id)
        } else {
            homeDao.clearFavoriteGroup()
        }
    }
}
