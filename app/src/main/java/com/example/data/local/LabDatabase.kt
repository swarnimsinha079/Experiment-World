package com.example.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(
    entities = [
        LabRecordEntity::class,
        UserProgressEntity::class,
        ApparatusBadgeEntity::class
    ],
    version = 2,
    exportSchema = false
)
abstract class LabDatabase : RoomDatabase() {
    abstract fun labRecordDao(): LabRecordDao
    abstract fun userProgressDao(): UserProgressDao

    companion object {
        @Volatile
        private var INSTANCE: LabDatabase? = null

        fun getDatabase(context: Context): LabDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    LabDatabase::class.java,
                    "virtulab_database"
                )
                    .fallbackToDestructiveMigration()
                    .build()
                INSTANCE = instance
                instance
            }
        }
    }
}
