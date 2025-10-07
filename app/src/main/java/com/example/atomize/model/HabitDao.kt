package com.example.atomize.model

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface HabitDao {
    @Query(value = "SELECT * FROM habits WHERE date = :date ORDER BY id ASC")
    fun observeHabitsForDate(date: String): Flow<List<HabitEntity>>

    @Query(value = "SELECT * FROM habits WHERE date = :date ORDER BY id ASC")
    suspend fun getHabitsForDate(date: String): List<HabitEntity>

    @Query(value = "SELECT COUNT(*) FROM habits WHERE date = :date")
    suspend fun countHabitsForDate(date: String): Int

    @Query(value = "SELECT * FROM habits WHERE days != ''")
    suspend fun getRecurringTemplates(): List<HabitEntity>

    @Query(value = "SELECT COUNT(*) FROM habits WHERE date = :date AND text = :text")
    suspend fun countHabitsByDateAndText(date: String, text: String): Int


    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertHabit(habit: HabitEntity): Long

    @Query(value = "UPDATE habits SET isChecked = :isChecked WHERE id = :id")
    suspend fun updateChecked(id: Int, isChecked: Boolean)

    @Query(value = "UPDATE habits SET text = :text, days = :days, notifyTime = :notifyTime, notificationsEnabled = :notificationsEnabled WHERE id = :id")
    suspend fun updateHabit(
        id: Int,
        text: String,
        days: String,
        notifyTime: String?,
        notificationsEnabled: Boolean
    )

    @Query(value = "UPDATE habits SET streak = streak + 1 WHERE id = :id")
    suspend fun increaseStreak(id: Int)

    @Query(value = "UPDATE habits SET streak = 0 WHERE id = :id")
    suspend fun resetStreak(id: Int)

    @Query(value = "DELETE FROM habits WHERE id = :id")
    suspend fun deleteHabit(id: Int)
}