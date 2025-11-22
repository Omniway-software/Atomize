package com.infinitysoftware.atomize.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.infinitysoftware.atomize.data.repository.FirestoreRepository
import com.infinitysoftware.atomize.model.habit.CalendarState
import com.infinitysoftware.atomize.model.habit.Habit
import com.infinitysoftware.atomize.model.habit.HabitDao
import com.infinitysoftware.atomize.model.habit.HabitDatabase
import com.infinitysoftware.atomize.model.habit.toEntity
import com.infinitysoftware.atomize.model.habit.toHabit
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import java.util.Calendar
import java.util.concurrent.atomic.AtomicInteger

class HabitViewModel(application: Application) : AndroidViewModel(application) {
    private val _habitIdCounter = AtomicInteger(0)
    private val _calendarState = MutableStateFlow(value = CalendarState())
    val calendarState: StateFlow<CalendarState> = _calendarState

    private val _syncStatus = MutableStateFlow<SyncStatus>(SyncStatus.Idle)
    val syncStatus: StateFlow<SyncStatus> = _syncStatus

    private val dao: HabitDao
        get() = HabitDatabase.getDatabase(getApplication()).habitDao()

    private val firestoreRepository = FirestoreRepository.getInstance()
    private val observeJobs = mutableMapOf<String, Job>()

    private val habitCreationMutex = Mutex()
    private val processedDates = mutableSetOf<String>()

    init {
        syncWithFirestore()
    }

    fun refreshDatabase() {
        viewModelScope.launch {
            observeJobs.values.forEach { it.cancel() }
            observeJobs.clear()

            _calendarState.value = CalendarState()

            habitCreationMutex.withLock {
                processedDates.clear()
            }

            syncWithFirestore()
        }
    }

    fun syncWithFirestore() {
        viewModelScope.launch {
            _syncStatus.value = SyncStatus.Syncing

            firestoreRepository.hasExistingData()
                .onSuccess { hasData ->
                    if (hasData) {
                        firestoreRepository.fetchAllHabitsFromFirestore()
                            .onSuccess { remoteHabits ->
                                remoteHabits.forEach { habit ->
                                    dao.insertHabit(habit)
                                }
                                _syncStatus.value = SyncStatus.Success
                            }
                            .onFailure {
                                _syncStatus.value = SyncStatus.Error(it.message ?: "Sync failed")
                            }
                    } else {
                        val localHabits = dao.getAllHabits()
                        firestoreRepository.syncHabitsToFirestore(localHabits)
                            .onSuccess { _syncStatus.value = SyncStatus.Success }
                            .onFailure { _syncStatus.value = SyncStatus.Error(it.message ?: "Sync failed") }
                    }
                }
                .onFailure {
                    _syncStatus.value = SyncStatus.Error(it.message ?: "Sync failed")
                }
        }
    }

    fun setHabitsForDate(date: String, habits: List<Habit>) {
        _calendarState.update { state ->
            state.copy(habitsByDate = state.habitsByDate + (date to habits))
        }
    }

    fun observeHabitsForDate(date: String) {
        if (observeJobs.containsKey(date)) return

        val job = viewModelScope.launch {
            dao.observeHabitsForDate(date).collectLatest { entities ->
                setHabitsForDate(date, entities.map { it.toHabit() })
            }
        }
        observeJobs[date] = job
    }

    fun stopObservingDate(date: String) {
        observeJobs[date]?.cancel()
        observeJobs.remove(date)
    }

    fun clearOldObservations() {
        val today = Calendar.getInstance()
        val cutoffDate = Calendar.getInstance().apply {
            add(Calendar.DAY_OF_YEAR, -30)
        }

        observeJobs.keys.toList().forEach { dateString ->
            try {
                val parts = dateString.split("-")
                val date = Calendar.getInstance().apply {
                    set(Calendar.YEAR, parts[0].toInt())
                    set(Calendar.MONTH, parts[1].toInt() - 1)
                    set(Calendar.DAY_OF_MONTH, parts[2].toInt())
                }

                if (date.before(cutoffDate)) {
                    stopObservingDate(dateString)
                }
            } catch (e: Exception) {
            }
        }
    }

    private fun toDayCode(calendar: Calendar): String = when (calendar.get(Calendar.DAY_OF_WEEK)) {
        Calendar.MONDAY -> "mon"
        Calendar.TUESDAY -> "tue"
        Calendar.WEDNESDAY -> "wed"
        Calendar.THURSDAY -> "thu"
        Calendar.FRIDAY -> "fri"
        Calendar.SATURDAY -> "sat"
        Calendar.SUNDAY -> "sun"
        else -> "sun"
    }

    fun ensureRecurringHabitsForDate(date: String) {
        viewModelScope.launch {
            habitCreationMutex.withLock {
                if (processedDates.contains(date)) {
                    return@launch
                }
                processedDates.add(date)

                val todayString = getTodayString()
                if (date < todayString) return@launch

                val parts = date.split("-")
                if (parts.size != 3) return@launch
                val calendar = Calendar.getInstance().apply {
                    set(Calendar.YEAR, parts[0].toIntOrNull() ?: return@launch)
                    set(Calendar.MONTH, (parts[1].toIntOrNull() ?: return@launch) - 1)
                    set(Calendar.DAY_OF_MONTH, parts[2].toIntOrNull() ?: return@launch)
                }
                val dayCode = toDayCode(calendar)
                val templates = dao.getRecurringTemplates()
                if (templates.isEmpty()) return@launch

                for (template in templates) {
                    val habit = template.toHabit()
                    if (!habit.days.contains(dayCode)) continue

                    val exists = dao.countHabitsByDateAndText(date, habit.text) > 0

                    if (!exists) {
                        val newHabit = habit.copy(
                            id = 0,
                            isChecked = false,
                            streak = 0
                        ).toEntity(date)

                        dao.insertHabit(newHabit)

                        launch {
                            firestoreRepository.syncHabitToFirestore(newHabit)
                        }
                    }
                }
            }
        }
    }

    fun increaseStrike(date: String, habitId: Int) {
        viewModelScope.launch {
            android.util.Log.d("HabitViewModel", "Increasing streak for habit $habitId")

            val habit = dao.getHabitById(habitId)?.toHabit() ?: return@launch
            val newStreak = calculateStreak(habit.text, date, habit.days)

            dao.updateStreak(habitId, newStreak)

            launch {
                dao.getHabitById(habitId)?.let { updatedHabit ->
                    firestoreRepository.syncHabitToFirestore(updatedHabit)
                }
            }
        }
    }

    fun decreaseStrike(date: String, habitId: Int) {
        viewModelScope.launch {
            android.util.Log.d("HabitViewModel", "Decreasing streak for habit $habitId")

            val habit = dao.getHabitById(habitId)?.toHabit() ?: return@launch
            val newStreak = calculateStreak(habit.text, date, habit.days, skipCurrentDay = true)

            dao.updateStreak(habitId, newStreak)

            launch {
                dao.getHabitById(habitId)?.let { updatedHabit ->
                    firestoreRepository.syncHabitToFirestore(updatedHabit)
                }
            }
        }
    }

    private suspend fun calculateStreak(
        habitText: String,
        currentDate: String,
        scheduledDays: List<String>,
        skipCurrentDay: Boolean = false
    ): Int {
        var streak = 0
        val calendar = Calendar.getInstance()

        val parts = currentDate.split("-")
        if (parts.size != 3) return 0

        calendar.apply {
            set(Calendar.YEAR, parts[0].toInt())
            set(Calendar.MONTH, parts[1].toInt() - 1)
            set(Calendar.DAY_OF_MONTH, parts[2].toInt())
        }

        if (skipCurrentDay) {
            calendar.add(Calendar.DAY_OF_YEAR, -1)
        }

        val isRecurring = scheduledDays.isNotEmpty()
        var consecutiveSkips = 0
        val maxConsecutiveSkips = if (isRecurring) 0 else 0

        for (i in 0 until 365) {
            val dateString = "%04d-%02d-%02d".format(
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH) + 1,
                calendar.get(Calendar.DAY_OF_MONTH)
            )

            val dayCode = toDayCode(calendar)

            if (isRecurring) {
                if (!scheduledDays.contains(dayCode)) {
                    calendar.add(Calendar.DAY_OF_YEAR, -1)
                    continue
                }
            }

            val habitForDate = dao.getHabitsByDateAndText(dateString, habitText).firstOrNull()

            if (habitForDate != null && habitForDate.isChecked) {
                streak++
            } else if (habitForDate != null && !habitForDate.isChecked) {
                break
            } else {
                if (isRecurring) {
                    break
                } else {
                    consecutiveSkips++
                    if (consecutiveSkips > maxConsecutiveSkips) {
                        break
                    }
                }
            }

            calendar.add(Calendar.DAY_OF_YEAR, -1)
        }

        return streak
    }

    fun resetStrike(date: String, habitId: Int) {
        viewModelScope.launch {
            dao.resetStreak(habitId)

            launch {
                dao.getHabitById(habitId)?.let { habit ->
                    firestoreRepository.syncHabitToFirestore(habit)
                }
            }
        }
    }

    fun addHabitForDate(date: String, habitText: String) {
        _calendarState.update { currentState ->
            val existingHabits = currentState.habitsByDate[date] ?: emptyList()
            val newHabit = Habit(
                id = _habitIdCounter.getAndIncrement(),
                isChecked = false,
                text = habitText,
                streak = 0
            )
            currentState.copy(
                habitsByDate = currentState.habitsByDate + (date to existingHabits + newHabit)
            )
        }
    }

    fun createHabitPersisted(
        date: String,
        habitText: String,
        days: List<String> = emptyList(),
        notifyTime: String? = null,
        notificationsEnabled: Boolean = false,
        maxLimit: Int = 5
    ) {
        viewModelScope.launch {
            if (habitText.isBlank()) return@launch

            habitCreationMutex.withLock {
                val count = dao.countHabitsForDate(date)
                if (count >= maxLimit) return@launch

                val exists = dao.countHabitsByDateAndText(date, habitText.trim()) > 0
                if (exists) return@launch

                val newHabit = Habit(
                    id = 0,
                    text = habitText.trim(),
                    isChecked = false,
                    streak = 0,
                    days = days,
                    notifyTime = notifyTime,
                    notificationsEnabled = notificationsEnabled
                ).toEntity(date)

                dao.insertHabit(newHabit)

                launch {
                    val insertedHabit = dao.getHabitsByDateAndText(date, habitText.trim()).firstOrNull()
                    insertedHabit?.let {
                        firestoreRepository.syncHabitToFirestore(it)
                    }
                }

                if (days.isNotEmpty()) {
                    processedDates.clear()
                }
            }
        }
    }

    fun updateHabitPersisted(
        id: Int,
        text: String,
        days: List<String>,
        notifyTime: String?,
        notificationsEnabled: Boolean
    ) {
        viewModelScope.launch {
            dao.updateHabit(
                id = id,
                text = text.trim(),
                days = days.joinToString(","),
                notifyTime = notifyTime,
                notificationsEnabled = notificationsEnabled
            )

            launch {
                dao.getHabitById(id)?.let { habit ->
                    firestoreRepository.syncHabitToFirestore(habit)
                }
            }
        }
    }

    fun toggleHabit(date: String, habitId: Int, isChecked: Boolean) {
        viewModelScope.launch {
            dao.updateChecked(habitId, isChecked)

            launch {
                dao.getHabitById(habitId)?.let { habit ->
                    firestoreRepository.syncHabitToFirestore(habit)
                }
            }
        }
    }

    fun deleteHabit(habitId: Int) {
        viewModelScope.launch {
            val habit = dao.getHabitById(habitId)?.toHabit()

            if (habit != null && habit.days.isNotEmpty()) {
                dao.deleteAllHabitsByText(habit.text)

                launch {
                    firestoreRepository.deleteHabitsByTextFromFirestore(habit.text)
                }

                habitCreationMutex.withLock {
                    processedDates.clear()
                }
            } else {
                dao.deleteHabit(habitId)

                launch {
                    firestoreRepository.deleteHabitFromFirestore(habitId)
                }
            }
        }
    }

    fun deleteAllHabitsByText(text: String) {
        viewModelScope.launch {
            dao.deleteAllHabitsByText(text)

            launch {
                firestoreRepository.deleteHabitsByTextFromFirestore(text)
            }
        }
    }

    private fun getTodayString(): String {
        val cal = Calendar.getInstance()
        val y = cal.get(Calendar.YEAR)
        val m = cal.get(Calendar.MONTH) + 1
        val d = cal.get(Calendar.DAY_OF_MONTH)
        return "%04d-%02d-%02d".format(y, m, d)
    }

    suspend fun getCompletedHabitsCountForDate(date: String): Int {
        return dao.countCompletedHabitsForDate(date)
    }

    override fun onCleared() {
        super.onCleared()
        observeJobs.values.forEach { it.cancel() }
        observeJobs.clear()
    }
}

sealed class SyncStatus {
    object Idle : SyncStatus()
    object Syncing : SyncStatus()
    object Success : SyncStatus()
    data class Error(val message: String) : SyncStatus()
}