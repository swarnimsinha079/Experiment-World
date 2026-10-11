package com.example.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface UserProgressDao {
    @Query("SELECT * FROM user_progress WHERE id = 1")
    fun getProgressFlow(): Flow<UserProgressEntity?>

    @Query("SELECT * FROM user_progress WHERE id = 1")
    suspend fun getProgressOnce(): UserProgressEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertOrUpdateProgress(progress: UserProgressEntity)

    @Query("SELECT * FROM apparatus_badges ORDER BY requiredRP ASC")
    fun getAllBadgesFlow(): Flow<List<ApparatusBadgeEntity>>

    @Query("SELECT * FROM apparatus_badges ORDER BY requiredRP ASC")
    suspend fun getAllBadgesOnce(): List<ApparatusBadgeEntity>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertBadgesInitial(badges: List<ApparatusBadgeEntity>)

    @Update
    suspend fun updateBadge(badge: ApparatusBadgeEntity)
}
