package com.example.ui

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.MyApplication
import com.example.data.Exercise
import com.example.data.Player
import com.example.data.PerformanceRecord
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class FootballViewModel(application: Application) : AndroidViewModel(application) {
    private val repository = (application as MyApplication).repository

    val players: StateFlow<List<Player>> = repository.allPlayers
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    val exercises: StateFlow<List<Exercise>> = repository.allExercises
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    val performanceRecords: StateFlow<List<PerformanceRecord>> = repository.allRecords
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    fun addPlayer(
        name: String,
        position: String,
        age: Int,
        height: Double,
        weight: Double,
        jerseyNumber: Int,
        notes: String
    ) {
        viewModelScope.launch {
            val player = Player(
                name = name,
                position = position,
                age = age,
                height = height,
                weight = weight,
                jerseyNumber = jerseyNumber,
                notes = notes
            )
            repository.insertPlayer(player)
        }
    }

    fun updatePlayer(player: Player) {
        viewModelScope.launch {
            repository.updatePlayer(player)
        }
    }

    fun deletePlayer(id: Int) {
        viewModelScope.launch {
            repository.deletePlayer(id)
        }
    }

    fun addExercise(name: String, description: String, category: String, targetDurationSeconds: Int) {
        viewModelScope.launch {
            val exercise = Exercise(
                name = name,
                description = description,
                category = category,
                targetDurationSeconds = targetDurationSeconds,
                isCustom = true
            )
            repository.insertExercise(exercise)
        }
    }

    fun deleteExercise(id: Int) {
        viewModelScope.launch {
            repository.deleteExercise(id)
        }
    }

    fun addPerformanceRecord(
        playerId: Int,
        playerName: String,
        exerciseId: Int,
        exerciseName: String,
        timeAchievedSeconds: Double,
        notes: String
    ) {
        viewModelScope.launch {
            val record = PerformanceRecord(
                playerId = playerId,
                playerName = playerName,
                exerciseId = exerciseId,
                exerciseName = exerciseName,
                timeAchievedSeconds = timeAchievedSeconds,
                notes = notes
            )
            repository.insertRecord(record)
        }
    }

    fun deletePerformanceRecord(id: Int) {
        viewModelScope.launch {
            repository.deleteRecord(id)
        }
    }
}
