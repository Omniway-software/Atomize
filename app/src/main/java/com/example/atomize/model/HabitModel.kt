package com.example.atomize.model

data class Habit(
    val id: Int,
    val text: String,
    val isChecked: Boolean,
    val streak: Int
)

fun HabitEntity.toHabit(): Habit = Habit(
    id = id,
    text = text,
    isChecked = isChecked,
    streak = streak
)

fun Habit.toEntity(date: String): HabitEntity = HabitEntity(
    id = id,
    text = text,
    isChecked = isChecked,
    streak = streak,
    date = date
)

data class CalendarState(
    val habitsByDate: Map<String, List<Habit>> = emptyMap()
)