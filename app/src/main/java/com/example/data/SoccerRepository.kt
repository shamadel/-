package com.example.data

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first

class SoccerRepository(private val database: AppDatabase) {
    private val playerDao = database.playerDao()
    private val exerciseDao = database.exerciseDao()
    private val recordDao = database.performanceRecordDao()

    val allPlayers: Flow<List<Player>> = playerDao.getAllPlayers()
    val allExercises: Flow<List<Exercise>> = exerciseDao.getAllExercises()
    val allRecords: Flow<List<PerformanceRecord>> = recordDao.getAllRecords()

    fun getRecordsByPlayer(playerId: Int): Flow<List<PerformanceRecord>> {
        return recordDao.getRecordsByPlayer(playerId)
    }

    suspend fun insertPlayer(player: Player) {
        playerDao.insertPlayer(player)
    }

    suspend fun updatePlayer(player: Player) {
        playerDao.updatePlayer(player)
    }

    suspend fun deletePlayer(id: Int) {
        playerDao.deletePlayerById(id)
    }

    suspend fun insertExercise(exercise: Exercise) {
        exerciseDao.insertExercise(exercise)
    }

    suspend fun deleteExercise(id: Int) {
        exerciseDao.deleteExerciseById(id)
    }

    suspend fun insertRecord(record: PerformanceRecord) {
        recordDao.insertRecord(record)
    }

    suspend fun deleteRecord(id: Int) {
        recordDao.deleteRecordById(id)
    }

    // Prepopulate drills if DB is empty
    suspend fun prepopulateExercisesIfEmpty() {
        val current = exerciseDao.getAllExercises().first()
        if (current.isEmpty()) {
            val defaults = listOf(
                Exercise(
                    name = "عدو سريع 50 متر",
                    description = "تمرين سرعة بدني لتطوير الركض الخاطف بالمسافات القصيرة.",
                    category = "بدني",
                    targetDurationSeconds = 10
                ),
                Exercise(
                    name = "تمرين الجري المتعرج بالكرة",
                    description = "التحرك والمراوغة بالكرة بين 8 أقماع لقياس الرشاقة والتحكم.",
                    category = "فني",
                    targetDurationSeconds = 25
                ),
                Exercise(
                    name = "تمرين جري التحمل (كوبير)",
                    description = "الجري المستمر لتطوير اللياقة والقدرة اللاهوائية.",
                    category = "بدني",
                    targetDurationSeconds = 300
                ),
                Exercise(
                    name = "اختبار دقة التمرير المتكرر",
                    description = "تنفيذ تمريرات متتالية نحو زوايا محددة تحت ضغط عامل الزمن.",
                    category = "فني",
                    targetDurationSeconds = 60
                ),
                Exercise(
                    name = "التصويب السريع على المرمى",
                    description = "استقبال الكرة، الالتفاف، والتسديد السريع نحو الأهداف البعيدة.",
                    category = "فني",
                    targetDurationSeconds = 45
                )
            )
            for (ex in defaults) {
                exerciseDao.insertExercise(ex)
            }
        }
    }
}
