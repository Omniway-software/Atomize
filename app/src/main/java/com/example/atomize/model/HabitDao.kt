package com.example.atomize.model

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface HabitDao {
    @Query("SELECT * FROM habits WHERE date = :date ORDER BY id ASC")
    fun observeHabitsForDate(date: String): Flow<List<HabitEntity>>

    @Query("SELECT * FROM habits WHERE date = :date ORDER BY id ASC")
    suspend fun getHabitsForDate(date: String): List<HabitEntity>

    @Query("SELECT COUNT(*) FROM habits WHERE date = :date")
    suspend fun countHabitsForDate(date: String): Int

    @Query("SELECT * FROM habits WHERE days != ''")
    suspend fun getRecurringTemplates(): List<HabitEntity>

    @Query("SELECT COUNT(*) FROM habits WHERE date = :date AND text = :text")
    suspend fun countHabitsByDateAndText(date: String, text: String): Int

    @Query("SELECT COUNT(*) FROM habits WHERE date = :date AND isChecked = 1")
    suspend fun countCompletedHabitsForDate(date: String): Int

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertHabit(habit: HabitEntity): Long

    @Query("UPDATE habits SET isChecked = :isChecked WHERE id = :id")
    suspend fun updateChecked(id: Int, isChecked: Boolean)

    @Query("UPDATE habits SET text = :text, days = :days, notifyTime = :notifyTime, notificationsEnabled = :notificationsEnabled WHERE id = :id")
    suspend fun updateHabit(
        id: Int,
        text: String,
        days: String,
        notifyTime: String?,
        notificationsEnabled: Boolean
    )

    @Query("UPDATE habits SET streak = streak + 1 WHERE id = :id")
    suspend fun increaseStreak(id: Int)

    @Query("UPDATE habits SET streak = 0 WHERE id = :id")
    suspend fun resetStreak(id: Int)

    @Query("DELETE FROM habits WHERE id = :id")
    suspend fun deleteHabit(id: Int)

    // 🔹 NOVO: resetuje sve checkboxe u ponoć bez brisanja streaka
    @Query("UPDATE habits SET isChecked = 0")
    suspend fun resetAllCheckedStates()
}