package com.enxy.data.db.home

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.enxy.data.db.home.model.GroupEntity
import kotlinx.coroutines.flow.Flow

@Dao
internal interface HomeDao {
    @Query("SELECT * FROM GroupEntity")
    fun getGroupsFlow(): Flow<List<GroupEntity>>

    @Query("SELECT * FROM GroupEntity WHERE isFavorite = 1 LIMIT 1")
    fun getFavoriteGroupFlow(): Flow<GroupEntity?>

    @Query("UPDATE GroupEntity SET isFavorite = CASE WHEN id = :groupId THEN 1 ELSE 0 END")
    fun setFavoriteGroup(groupId: Int)

    @Query("UPDATE GroupEntity SET isFavorite = 0")
    fun clearFavoriteGroup()

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertGroup(group: GroupEntity)

    @Query("DELETE FROM GroupEntity")
    fun clearGroups()
}
