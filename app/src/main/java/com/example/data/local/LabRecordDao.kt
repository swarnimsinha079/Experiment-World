package com.example.data.local

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface LabRecordDao {
    @Query("SELECT * FROM lab_records ORDER BY timestamp DESC")
    fun getAllRecords(): Flow<List<LabRecordEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertRecord(record: LabRecordEntity): Long

    @Delete
    suspend fun deleteRecord(record: LabRecordEntity)

    @Query("DELETE FROM lab_records")
    suspend fun clearAllRecords()
}
