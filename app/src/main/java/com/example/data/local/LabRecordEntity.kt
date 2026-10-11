package com.example.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "lab_records")
data class LabRecordEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val title: String,
    val experimentType: String, // "CHEMISTRY" or "MATH"
    val reactantsOrTopic: String,
    val equationOrFormula: String,
    val observations: String,
    val finalTemperatureC: Double,
    val finalPh: Double,
    val timestamp: Long = System.currentTimeMillis()
)
