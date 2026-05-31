package com.example

import android.app.Application
import androidx.room.Room
import com.example.data.AppDatabase
import com.example.data.SoccerRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MyApplication : Application() {
    lateinit var database: AppDatabase
    lateinit var repository: SoccerRepository

    override fun onCreate() {
        super.onCreate()
        
        // Initialize Database
        database = Room.databaseBuilder(
            applicationContext,
            AppDatabase::class.java,
            "soccer_tracker.db"
        )
        .fallbackToDestructiveMigration()
        .build()

        repository = SoccerRepository(database)

        // Seed data asynchronously inside background coroutine
        CoroutineScope(Dispatchers.IO).launch {
            repository.prepopulateExercisesIfEmpty()
        }
    }
}
