package com.infinitysoftware.atomize.model.settings

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "settings")
data class Settings(
    @PrimaryKey
    val id: Int = 1,
    val showStreak: Boolean = true,
    val animatedIcon: Boolean = true
)