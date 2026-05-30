package com.example.data

import android.content.Context
import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface QuranNoteDao {
    @Query("SELECT * FROM quran_notes ORDER BY timestamp DESC")
    fun getAllNotes(): Flow<List<QuranNote>>

    @Query("SELECT * FROM quran_notes WHERE surahNumber = :surahNum AND verseNumber = :verseNum")
    fun getNotesForVerse(surahNum: Int, verseNum: Int): Flow<List<QuranNote>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertNote(note: QuranNote)

    @Delete
    suspend fun deleteNote(note: QuranNote)
}

@Dao
interface ReadingLogDao {
    @Query("SELECT * FROM reading_logs ORDER BY timestamp DESC")
    fun getAllLogs(): Flow<List<ReadingLog>>

    @Query("SELECT SUM(pagesRead) FROM reading_logs")
    fun getTotalPagesRead(): Flow<Int?>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertLog(log: ReadingLog)

    @Query("DELETE FROM reading_logs")
    suspend fun clearLogs()
}

@Dao
interface TasbihCounterDao {
    @Query("SELECT * FROM tasbih_counters ORDER BY lastUpdated DESC")
    fun getAllCounters(): Flow<List<TasbihCounter>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCounter(counter: TasbihCounter)

    @Update
    suspend fun updateCounter(counter: TasbihCounter)

    @Delete
    suspend fun deleteCounter(counter: TasbihCounter)

    @Query("UPDATE tasbih_counters SET count = :count, lastUpdated = :timestamp WHERE id = :id")
    suspend fun updateCount(id: Int, count: Int, timestamp: Long = System.currentTimeMillis())
}

@Dao
interface KhatmGoalDao {
    @Query("SELECT * FROM khatm_goals WHERE status = 'ACTIVE' LIMIT 1")
    fun getActiveGoal(): Flow<KhatmGoal?>

    @Query("SELECT * FROM khatm_goals ORDER BY startDate DESC")
    fun getAllGoals(): Flow<List<KhatmGoal>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertGoal(goal: KhatmGoal)

    @Update
    suspend fun updateGoal(goal: KhatmGoal)

    @Query("UPDATE khatm_goals SET currentPage = :page WHERE id = :id")
    suspend fun updateCurrentPage(id: Int, page: Int)
}

@Database(
    entities = [QuranNote::class, ReadingLog::class, TasbihCounter::class, KhatmGoal::class],
    version = 1,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun quranNoteDao(): QuranNoteDao
    abstract fun readingLogDao(): ReadingLogDao
    abstract fun tasbihCounterDao(): TasbihCounterDao
    abstract fun khatmGoalDao(): KhatmGoalDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "zad_al_muslim_db"
                ).fallbackToDestructiveMigration().build()
                INSTANCE = instance
                instance
            }
        }
    }
}
