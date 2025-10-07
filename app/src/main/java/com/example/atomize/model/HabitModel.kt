package com.example.atomize.model

data class Habit(
    val id: Int,
    val isChecked: Boolean,
    val text: String,
    val streak: Int
)

data class CalendarState(
    val habitsByDate: Map<String, List<Habit>> = emptyMap()
)