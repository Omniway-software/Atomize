package com.omniway.atomize.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "habits")
data class HabitEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val text: String,
    val isChecked: Boolean = false,
    val streak: Int = 0,
    val date: String,
    val days: String = "",
    val notifyTime: String? = null,
    val notificationsEnabled: Boolean = false
)