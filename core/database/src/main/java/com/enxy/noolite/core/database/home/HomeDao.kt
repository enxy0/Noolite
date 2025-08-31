package com.enxy.noolite.core.database.home

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.enxy.noolite.core.database.home.model.GroupEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface HomeDao {
    @Query("SELECT * FROM GroupEntity")
    fun getGroupsFlow(): Flow<List<GroupEntity>>

    @Query("SELECT * FROM GroupEntity WHERE isFavorite = 1 LIMIT 1")
    fun getFavoriteGroupFlow(): Flow<GroupEntity?>

    @Query("UPDATE GroupEntity SET isFavorite = CASE WHEN id = :groupId THEN 1 ELSE 0 END")
    suspend fun setFavoriteGroup(groupId: Int)

    @Query("UPDATE GroupEntity SET isFavorite = 0")
    suspend fun clearFavoriteGroup()

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertGroup(group: GroupEntity)

    @Query("DELETE FROM GroupEntity")
    suspend fun clearGroups()
}
