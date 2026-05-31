package com.example.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "players")
data class Player(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val name: String,
    val position: String,
    val age: Int,
    val height: Double,
    val weight: Double,
    val jerseyNumber: Int,
    val notes: String = ""
)

@Entity(tableName = "exercises")
data class Exercise(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val name: String,
    val description: String,
    val category: String, // "بدني" أو "فني" أو "تكتيكي"
    val targetDurationSeconds: Int, // الزمن المستهدف لكل تمرين
    val isCustom: Boolean = false
)

@Entity(tableName = "performance_records")
data class PerformanceRecord(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val playerId: Int,
    val playerName: String,
    val exerciseId: Int,
    val exerciseName: String,
    val timeAchievedSeconds: Double, // الزمن الفعلي المحقق بالثواني
    val timestamp: Long = System.currentTimeMillis(),
    val notes: String = ""
)
