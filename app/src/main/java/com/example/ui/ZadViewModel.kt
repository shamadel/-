package com.example.ui

import android.app.Application
import android.content.Context
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.*
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import org.json.JSONArray
import org.json.JSONObject
import java.util.*

class ZadViewModel(application: Application) : AndroidViewModel(application) {

    private val db = AppDatabase.getDatabase(application)
    private val noteDao = db.quranNoteDao()
    private val logDao = db.readingLogDao()
    private val counterDao = db.tasbihCounterDao()
    private val goalDao = db.khatmGoalDao()

    // Database flows
    val allNotes: StateFlow<List<QuranNote>> = noteDao.getAllNotes()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val allLogs: StateFlow<List<ReadingLog>> = logDao.getAllLogs()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val totalPagesRead: StateFlow<Int> = logDao.getTotalPagesRead()
        .map { it ?: 0 }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), 0)

    val allCounters: StateFlow<List<TasbihCounter>> = counterDao.getAllCounters()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val activeGoal: StateFlow<KhatmGoal?> = goalDao.getActiveGoal()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), null)

    val allGoals: StateFlow<List<KhatmGoal>> = goalDao.getAllGoals()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    // App Preferences (StateFlow)
    private val prefs = application.getSharedPreferences("zad_muslim_prefs", Context.MODE_PRIVATE)

    private val _selectedLanguage = MutableStateFlow(prefs.getString("lang", "ar") ?: "ar")
    val selectedLanguage: StateFlow<String> = _selectedLanguage.asStateFlow()

    private val _selectedCityIndex = MutableStateFlow(prefs.getInt("city_index", 0))
    val selectedCityIndex: StateFlow<Int> = _selectedCityIndex.asStateFlow()

    private val _customLatitude = MutableStateFlow(prefs.getFloat("custom_lat", 21.4225f).toDouble())
    val customLatitude: StateFlow<Double> = _customLatitude.asStateFlow()

    private val _customLongitude = MutableStateFlow(prefs.getFloat("custom_lng", 39.8262f).toDouble())
    val customLongitude: StateFlow<Double> = _customLongitude.asStateFlow()

    private val _isCustomLocation = MutableStateFlow(prefs.getBoolean("is_custom_location", false))
    val isCustomLocation: StateFlow<Boolean> = _isCustomLocation.asStateFlow()

    private val _calcMethod = MutableStateFlow(
        CalculationMethod.valueOf(prefs.getString("calc_method", CalculationMethod.EGYPT.name) ?: CalculationMethod.EGYPT.name)
    )
    val calcMethod: StateFlow<CalculationMethod> = _calcMethod.asStateFlow()

    private val _juristicMethod = MutableStateFlow(
        JuristicMethod.valueOf(prefs.getString("juristic_method", JuristicMethod.STANDARD.name) ?: JuristicMethod.STANDARD.name)
    )
    val juristicMethod: StateFlow<JuristicMethod> = _juristicMethod.asStateFlow()

    private val _selectedAzanVoice = MutableStateFlow(prefs.getString("azan_voice", "عبد الباسط عبد الصمد") ?: "عبد الباسط عبد الصمد")
    val selectedAzanVoice: StateFlow<String> = _selectedAzanVoice.asStateFlow()

    private val _azanEnabled = MutableStateFlow(prefs.getBoolean("azan_enabled", true))
    val azanEnabled: StateFlow<Boolean> = _azanEnabled.asStateFlow()

    private val _darkModeOverride = MutableStateFlow<Boolean?>(
        if (prefs.contains("dark_mode")) prefs.getBoolean("dark_mode", false) else null
    )
    val darkModeOverride: StateFlow<Boolean?> = _darkModeOverride.asStateFlow()

    // Simple reactive local states
    val currentCity: City
        get() = City.PREDEFINED_CITIES[selectedCityIndex.value]

    // Initialize Default Counters if empty
    init {
        viewModelScope.launch {
            counterDao.getAllCounters().first().let { list ->
                if (list.isEmpty()) {
                    counterDao.insertCounter(TasbihCounter(phrase = "سبحان الله", count = 0, target = 33))
                    counterDao.insertCounter(TasbihCounter(phrase = "الحمد لله", count = 0, target = 33))
                    counterDao.insertCounter(TasbihCounter(phrase = "الله أكبر", count = 0, target = 33))
                    counterDao.insertCounter(TasbihCounter(phrase = "أستغفر الله", count = 0, target = 100))
                    counterDao.insertCounter(TasbihCounter(phrase = "لا إله إلا الله", count = 0, target = 100))
                }
            }
        }
    }

    // Setters
    fun setLanguage(lang: String) {
        _selectedLanguage.value = lang
        prefs.edit().putString("lang", lang).apply()
    }

    fun setCity(index: Int) {
        _selectedCityIndex.value = index
        _isCustomLocation.value = false
        val city = City.PREDEFINED_CITIES[index]
        _customLatitude.value = city.latitude
        _customLongitude.value = city.longitude
        prefs.edit().putInt("city_index", index).putBoolean("is_custom_location", false).apply()
    }

    fun setCustomLocation(lat: Double, lng: Double) {
        _customLatitude.value = lat
        _customLongitude.value = lng
        _isCustomLocation.value = true
        prefs.edit()
            .putFloat("custom_lat", lat.toFloat())
            .putFloat("custom_lng", lng.toFloat())
            .putBoolean("is_custom_location", true)
            .apply()
    }

    fun setCalculationMethod(method: CalculationMethod) {
        _calcMethod.value = method
        prefs.edit().putString("calc_method", method.name).apply()
    }

    fun setJuristicMethod(method: JuristicMethod) {
        _juristicMethod.value = method
        prefs.edit().putString("juristic_method", method.name).apply()
    }

    fun setAzanVoice(voice: String) {
        _selectedAzanVoice.value = voice
        prefs.edit().putString("azan_voice", voice).apply()
    }

    fun setAzanEnabled(enabled: Boolean) {
        _azanEnabled.value = enabled
        prefs.edit().putBoolean("azan_enabled", enabled).apply()
    }

    fun setDarkModeOverride(override: Boolean?) {
        _darkModeOverride.value = override
        if (override != null) {
            prefs.edit().putBoolean("dark_mode", override).apply()
        } else {
            prefs.edit().remove("dark_mode").apply()
        }
    }

    // Prayer calculations
    fun getPrayerTimesForToday(): PrayerTimes {
        val date = Calendar.getInstance()
        val lat = if (isCustomLocation.value) customLatitude.value else currentCity.latitude
        val lng = if (isCustomLocation.value) customLongitude.value else currentCity.longitude
        val tz = if (isCustomLocation.value) 3.0 else currentCity.timezone // Default custom to Riyadh/Mecca GMT+3
        return PrayerTimeUtils.calculatePrayerTimes(lat, lng, tz, date, calcMethod.value, juristicMethod.value)
    }

    fun getQiblaAngle(): Double {
        val lat = if (isCustomLocation.value) customLatitude.value else currentCity.latitude
        val lng = if (isCustomLocation.value) customLongitude.value else currentCity.longitude
        return PrayerTimeUtils.calculateQiblaDirection(lat, lng)
    }

    // Room DB Actions
    fun addQuranNote(surahNum: Int, surahName: String, verseNum: Int, rawNote: String) {
        viewModelScope.launch {
            noteDao.insertNote(
                QuranNote(
                    surahNumber = surahNum,
                    surahName = surahName,
                    verseNumber = verseNum,
                    note = rawNote
                )
            )
        }
    }

    fun deleteQuranNote(note: QuranNote) {
        viewModelScope.launch {
            noteDao.deleteNote(note)
        }
    }

    fun logReading(surahNum: Int, surahName: String, pagesCount: Int) {
        viewModelScope.launch {
            // Save log
            logDao.insertLog(
                ReadingLog(
                    surahNumber = surahNum,
                    surahName = surahName,
                    pagesRead = pagesCount
                )
            )

            // Update active khatem goal
            activeGoal.value?.let { goal ->
                val newPage = (goal.currentPage + pagesCount).coerceAtMost(goal.totalPages)
                if (newPage >= goal.totalPages) {
                    goalDao.updateGoal(goal.copy(currentPage = newPage, status = "COMPLETED"))
                } else {
                    goalDao.updateCurrentPage(goal.id, newPage)
                }
            }
        }
    }

    fun startNewKhatmGoal(targetDays: Int) {
        viewModelScope.launch {
            // First mark any existing goals as completed/cancelled
            allGoals.value.forEach {
                if (it.status == "ACTIVE") {
                    goalDao.updateGoal(it.copy(status = "CANCELLED"))
                }
            }
            goalDao.insertGoal(
                KhatmGoal(
                    targetDays = targetDays,
                    currentPage = 1
                )
            )
        }
    }

    fun incrementTasbih(id: Int, curCount: Int, target: Int) {
        viewModelScope.launch {
            val newCount = curCount + 1
            if (newCount >= target) {
                // Wrap around or keep counting
                counterDao.updateCount(id, 0) // Over complete resets
            } else {
                counterDao.updateCount(id, newCount)
            }
        }
    }

    fun resetTasbih(id: Int) {
        viewModelScope.launch {
            counterDao.updateCount(id, 0)
        }
    }

    fun addCustomTasbih(phrase: String, target: Int) {
        viewModelScope.launch {
            counterDao.insertCounter(
                TasbihCounter(
                    phrase = phrase,
                    count = 0,
                    target = target
                )
            )
        }
    }

    fun deleteTasbih(counter: TasbihCounter) {
        viewModelScope.launch {
            counterDao.deleteCounter(counter)
        }
    }

    // JSON Local Import/Export for Multi-Device synchronization and Backups
    fun generateBackupString(): String {
        return try {
            val root = JSONObject()

            // Export notes
            val notesArray = JSONArray()
            allNotes.value.forEach { note ->
                val obj = JSONObject()
                obj.put("surahNumber", note.surahNumber)
                obj.put("surahName", note.surahName)
                obj.put("verseNumber", note.verseNumber)
                obj.put("note", note.note)
                obj.put("timestamp", note.timestamp)
                notesArray.put(obj)
            }
            root.put("notes", notesArray)

            // Export logs
            val logsArray = JSONArray()
            allLogs.value.forEach { log ->
                val obj = JSONObject()
                obj.put("surahNumber", log.surahNumber)
                obj.put("surahName", log.surahName)
                obj.put("pagesRead", log.pagesRead)
                obj.put("timestamp", log.timestamp)
                logsArray.put(obj)
            }
            root.put("logs", logsArray)

            // Export tasbih
            val countersArray = JSONArray()
            allCounters.value.forEach { tc ->
                val obj = JSONObject()
                obj.put("phrase", tc.phrase)
                obj.put("count", tc.count)
                obj.put("target", tc.target)
                obj.put("lastUpdated", tc.lastUpdated)
                countersArray.put(obj)
            }
            root.put("counters", countersArray)

            // Export goals
            val goalsArray = JSONArray()
            allGoals.value.forEach { goal ->
                val obj = JSONObject()
                obj.put("targetDays", goal.targetDays)
                obj.put("currentPage", goal.currentPage)
                obj.put("totalPages", goal.totalPages)
                obj.put("startDate", goal.startDate)
                obj.put("status", goal.status)
                goalsArray.put(obj)
            }
            root.put("goals", goalsArray)

            root.toString(4)
        } catch (e: Exception) {
            ""
        }
    }

    fun restoreBackup(jsonString: String): Boolean {
        return try {
            val root = JSONObject(jsonString)

            viewModelScope.launch {
                // Restore notes
                if (root.has("notes")) {
                    val arr = root.getJSONArray("notes")
                    for (i in 0 until arr.length()) {
                        val obj = arr.getJSONObject(i)
                        noteDao.insertNote(
                            QuranNote(
                                surahNumber = obj.getInt("surahNumber"),
                                surahName = obj.getString("surahName"),
                                verseNumber = obj.getInt("verseNumber"),
                                note = obj.getString("note"),
                                timestamp = obj.getLong("timestamp")
                            )
                        )
                    }
                }

                // Restore logs
                if (root.has("logs")) {
                    val arr = root.getJSONArray("logs")
                    for (i in 0 until arr.length()) {
                        val obj = arr.getJSONObject(i)
                        logDao.insertLog(
                            ReadingLog(
                                surahNumber = obj.getInt("surahNumber"),
                                surahName = obj.getString("surahName"),
                                pagesRead = obj.getInt("pagesRead"),
                                timestamp = obj.getLong("timestamp")
                            )
                        )
                    }
                }

                // Restore counters
                if (root.has("counters")) {
                    val arr = root.getJSONArray("counters")
                    for (i in 0 until arr.length()) {
                        val obj = arr.getJSONObject(i)
                        counterDao.insertCounter(
                            TasbihCounter(
                                phrase = obj.getString("phrase"),
                                count = obj.getInt("count"),
                                target = obj.getInt("target"),
                                lastUpdated = obj.getLong("lastUpdated")
                            )
                        )
                    }
                }

                // Restore goals
                if (root.has("goals")) {
                    val arr = root.getJSONArray("goals")
                    for (i in 0 until arr.length()) {
                        val obj = arr.getJSONObject(i)
                        goalDao.insertGoal(
                            KhatmGoal(
                                targetDays = obj.getInt("targetDays"),
                                currentPage = obj.getInt("currentPage"),
                                totalPages = obj.getInt("totalPages"),
                                startDate = obj.getLong("startDate"),
                                status = obj.getString("status")
                            )
                        )
                    }
                }
            }
            true
        } catch (e: Exception) {
            false
        }
    }
}
