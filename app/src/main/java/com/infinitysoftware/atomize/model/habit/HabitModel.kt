package com.infinitysoftware.atomize.model.habit

data class Habit(
    val id: Int,
    val text: String,
    val isChecked: Boolean,
    val streak: Int,
    val days: List<String> = emptyList(),
    val notifyTime: String? = null,
    val notificationsEnabled: Boolean = false
)

fun HabitEntity.toHabit(): Habit = Habit(
    id = id,
    text = text,
    isChecked = isChecked,
    streak = streak,
    days = if (days.isBlank()) emptyList() else days.split(',').map { it.trim() },
    notifyTime = notifyTime,
    notificationsEnabled = notificationsEnabled
)

fun Habit.toEntity(date: String): HabitEntity = HabitEntity(
    id = id,
    text = text,
    isChecked = isChecked,
    streak = streak,
    date = date,
    days = days.joinToString(separator = ","),
    notifyTime = notifyTime,
    notificationsEnabled = notificationsEnabled
)

data class CalendarState(
    val habitsByDate: Map<String, List<Habit>> = emptyMap()
)