package com.example.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "quran_notes")
data class QuranNote(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val surahNumber: Int,
    val surahName: String,
    val verseNumber: Int,
    val note: String,
    val timestamp: Long = System.currentTimeMillis()
)

@Entity(tableName = "reading_logs")
data class ReadingLog(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val surahNumber: Int,
    val surahName: String,
    val pagesRead: Int,
    val timestamp: Long = System.currentTimeMillis()
)

@Entity(tableName = "tasbih_counters")
data class TasbihCounter(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val phrase: String,
    val count: Int,
    val target: Int,
    val lastUpdated: Long = System.currentTimeMillis()
)

@Entity(tableName = "khatm_goals")
data class KhatmGoal(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val targetDays: Int,
    val currentPage: Int = 1,
    val totalPages: Int = 604,
    val startDate: Long = System.currentTimeMillis(),
    val status: String = "ACTIVE" // "ACTIVE", "COMPLETED"
)
