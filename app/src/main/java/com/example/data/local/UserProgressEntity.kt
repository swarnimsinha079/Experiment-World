package com.example.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "user_progress")
data class UserProgressEntity(
    @PrimaryKey
    val id: Int = 1,
    val researchPoints: Int = 0,
    val levelNumber: Int = 1,
    val rankTitle: String = "Lab Novice",
    val experimentsCompleted: Int = 0,
    val articlesRead: Int = 0,
    val mathProofsCompleted: Int = 0
)

@Entity(tableName = "apparatus_badges")
data class ApparatusBadgeEntity(
    @PrimaryKey
    val badgeId: String,
    val title: String,
    val apparatusName: String,
    val description: String,
    val requiredRP: Int,
    val isUnlocked: Boolean = false,
    val unlockedAt: Long = 0L,
    val iconResName: String = "ic_badge"
)
