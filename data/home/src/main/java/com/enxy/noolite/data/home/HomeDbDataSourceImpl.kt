package com.enxy.noolite.data.home

import com.enxy.noolite.core.database.home.HomeDao
import com.enxy.noolite.core.database.home.toDomain
import com.enxy.noolite.core.database.home.toEntity
import com.enxy.noolite.core.model.Group
import com.enxy.noolite.domain.common.HomeDbDataSource
import com.enxy.noolite.domain.common.model.SetFavoriteGroupPayload
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

    override suspend fun setFavoriteGroup(
        payload: SetFavoriteGroupPayload
    ) {
        if (payload.isFavorite) {
            homeDao.setFavoriteGroup(payload.group.id)
        } else {
            homeDao.clearFavoriteGroup()
        }
    }
}