package com.example.atomize.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.atomize.model.CalendarState
import com.example.atomize.model.Habit
import com.example.atomize.model.HabitDatabase
import com.example.atomize.model.toHabit
import com.example.atomize.model.toEntity
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.util.concurrent.atomic.AtomicInteger
import java.time.LocalDate
import java.time.DayOfWeek

class HabitViewModel(application: Application) : AndroidViewModel(application) {
    private val _habitIdCounter = AtomicInteger(0)

    private val _calendarState = MutableStateFlow(value = CalendarState())
    val calendarState: StateFlow<CalendarState> = _calendarState

    private val dao = HabitDatabase.getDatabase(application).habitDao()
    private var observeJob: Job? = null

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

    private fun toDayCode(date: LocalDate): String = when (date.dayOfWeek) {
        DayOfWeek.MONDAY -> "mon"
        DayOfWeek.TUESDAY -> "tue"
        DayOfWeek.WEDNESDAY -> "wed"
        DayOfWeek.THURSDAY -> "thu"
        DayOfWeek.FRIDAY -> "fri"
        DayOfWeek.SATURDAY -> "sat"
        DayOfWeek.SUNDAY -> "sun"
    }

    fun ensureRecurringHabitsForDate(date: String) {
        viewModelScope.launch {
            val targetDate = runCatching { LocalDate.parse(date) }.getOrNull() ?: return@launch
            val dayCode = toDayCode(targetDate)
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
        // Legacy in-memory add retained for previews; prefer createHabitPersisted
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

    fun createHabitPersisted(date: String, habitText: String, days: List<String> = emptyList(), notifyTime: String? = null, notificationsEnabled: Boolean = false) {
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

    fun updateHabitPersisted(id: Int, text: String, days: List<String>, notifyTime: String?, notificationsEnabled: Boolean) {
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
        viewModelScope.launch { dao.deleteHabit(habitId) }
    }
}