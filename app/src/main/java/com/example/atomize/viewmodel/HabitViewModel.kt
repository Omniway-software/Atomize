package com.example.atomize.viewmodel

import androidx.lifecycle.ViewModel
import com.example.atomize.model.CalendarState
import com.example.atomize.model.Habit
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import java.util.concurrent.atomic.AtomicInteger

class HabitViewModel : ViewModel() {
    private val _habitIdCounter = AtomicInteger(0)

    private val _calendarState = MutableStateFlow(value = CalendarState())
    val calendarState: StateFlow<CalendarState> = _calendarState

    fun increaseStrike(date: String, habitId: Int) {
        _calendarState.update { state ->
            val updatedHabits = state.habitsByDate[date]?.map { h ->
                if (h.id == habitId) h.copy(streak = h.streak + 1) else h
            } ?: emptyList()
            state.copy(habitsByDate = state.habitsByDate + (date to updatedHabits))
        }
    }

    fun resetStrike(date: String, habitId: Int) {
        _calendarState.update { state ->
            val updatedHabits = state.habitsByDate[date]?.map { h ->
                if (h.id == habitId) h.copy(streak = 0) else h
            } ?: emptyList()
            state.copy(habitsByDate = state.habitsByDate + (date to updatedHabits))
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

    fun toggleHabit(date: String, habitId: Int, isChecked: Boolean) {
        _calendarState.update { state ->
            val updatedHabits = state.habitsByDate[date]?.map { h ->
                if (h.id == habitId) h.copy(isChecked = isChecked) else h
            } ?: emptyList()
            state.copy(habitsByDate = state.habitsByDate + (date to updatedHabits))
        }
    }
}