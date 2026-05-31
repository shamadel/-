package com.example.data

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface PlayerDao {
    @Query("SELECT * FROM players ORDER BY name ASC")
    fun getAllPlayers(): Flow<List<Player>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPlayer(player: Player)

    @Update
    suspend fun updatePlayer(player: Player)

    @Query("DELETE FROM players WHERE id = :id")
    suspend fun deletePlayerById(id: Int)
}

@Dao
interface ExerciseDao {
    @Query("SELECT * FROM exercises ORDER BY id ASC")
    fun getAllExercises(): Flow<List<Exercise>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertExercise(exercise: Exercise)

    @Query("DELETE FROM exercises WHERE id = :id")
    suspend fun deleteExerciseById(id: Int)
}

@Dao
interface PerformanceRecordDao {
    @Query("SELECT * FROM performance_records ORDER BY timestamp DESC")
    fun getAllRecords(): Flow<List<PerformanceRecord>>

    @Query("SELECT * FROM performance_records WHERE playerId = :playerId ORDER BY timestamp DESC")
    fun getRecordsByPlayer(playerId: Int): Flow<List<PerformanceRecord>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertRecord(record: PerformanceRecord)

    @Query("DELETE FROM performance_records WHERE id = :id")
    suspend fun deleteRecordById(id: Int)
}

@Database(entities = [Player::class, Exercise::class, PerformanceRecord::class], version = 1, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {
    abstract fun playerDao(): PlayerDao
    abstract fun exerciseDao(): ExerciseDao
    abstract fun performanceRecordDao(): PerformanceRecordDao
}
