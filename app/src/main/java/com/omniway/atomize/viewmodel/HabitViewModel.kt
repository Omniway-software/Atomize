package com.omniway.atomize.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.omniway.atomize.model.CalendarState
import com.omniway.atomize.model.Habit
import com.omniway.atomize.model.HabitDatabase
import com.omniway.atomize.model.toHabit
import com.omniway.atomize.model.toEntity
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.util.concurrent.atomic.AtomicInteger
import java.util.Calendar
import kotlinx.coroutines.delay
import java.util.Locale

class HabitViewModel(application: Application) : AndroidViewModel(application) {
    private val _habitIdCounter = AtomicInteger(0)

    private val _calendarState = MutableStateFlow(value = CalendarState())
    val calendarState: StateFlow<CalendarState> = _calendarState

    private val dao = HabitDatabase.getDatabase(application).habitDao()
    private var observeJob: Job? = null

    init {
        scheduleMidnightUncheck()
    }

    fun setHabitsForDate(date: String, habits: List<Habit>) {
        _calendarState.update { state ->
            state.copy(habitsByDate = state.habitsByDate + (date to habits))
        }
    }

    fun observeHabitsForDate(date: String) {
        observeJob?.cancel()
        observeJob = viewModelScope.launch {
            dao.observeHabitsForDate(date).collectLatest { entities ->
                setHabitsForDate(date, entities.map { it.toHabit() })
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
                if (exists) continue
                dao.insertHabit(
                    habit.copy(id = 0, isChecked = false, streak = 0).toEntity(date)
                )
            }
        }
    }

    fun increaseStrike(date: String, habitId: Int) {
        viewModelScope.launch { dao.increaseStreak(habitId) }
    }

    fun resetStrike(date: String, habitId: Int) {
        viewModelScope.launch { dao.resetStreak(habitId) }
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
        notificationsEnabled: Boolean = false
    ) {
        viewModelScope.launch {
            if (habitText.isBlank()) return@launch
            val count = dao.countHabitsForDate(date)
            if (count >= 5) return@launch
            dao.insertHabit(
                Habit(
                    id = 0,
                    text = habitText.trim(),
                    isChecked = false,
                    streak = 0,
                    days = days,
                    notifyTime = notifyTime,
                    notificationsEnabled = notificationsEnabled
                ).toEntity(date)
            )
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
        }
    }

    fun toggleHabit(date: String, habitId: Int, isChecked: Boolean) {
        viewModelScope.launch { dao.updateChecked(habitId, isChecked) }
    }

    fun deleteHabit(habitId: Int) {
        viewModelScope.launch {
            val habit = dao.getHabitById(habitId)?.toHabit()

            if (habit != null && habit.days.isNotEmpty()) {
                dao.deleteAllHabitsByText(habit.text)
            } else {
                dao.deleteHabit(habitId)
            }
        }
    }

    fun deleteAllHabitsByText(text: String) {
        viewModelScope.launch {
            dao.deleteAllHabitsByText(text)
        }
    }

    suspend fun getCompletedHabitsCountForDate(date: String): Int {
        return dao.countCompletedHabitsForDate(date)
    }

    private fun scheduleMidnightUncheck() {
        viewModelScope.launch {
            while (true) {
                val now = Calendar.getInstance()
                val midnight = Calendar.getInstance().apply {
                    add(Calendar.DAY_OF_YEAR, 1)
                    set(Calendar.HOUR_OF_DAY, 0)
                    set(Calendar.MINUTE, 0)
                    set(Calendar.SECOND, 0)
                    set(Calendar.MILLISECOND, 0)
                }

                val delayMillis = midnight.timeInMillis - now.timeInMillis
                delay(delayMillis)

                dao.resetAllCheckedStates()

                val today = String.format(
                    Locale.US,
                    "%04d-%02d-%02d",
                    midnight.get(Calendar.YEAR),
                    midnight.get(Calendar.MONTH) + 1,
                    midnight.get(Calendar.DAY_OF_MONTH)
                )
                observeHabitsForDate(today)
            }
        }
    }
}

