package com.infinitysoftware.atomize.model.habit

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

    @Insert(onConflict = OnConflictStrategy.Companion.REPLACE)
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

    //@Query("UPDATE habits SET streak = 0 WHERE id = :id")
    //suspend fun resetStreak(id: Int)

    @Query("UPDATE habits SET streak = 0 WHERE id = :habitId")
    suspend fun resetStreak(habitId: Int)

    @Query("UPDATE habits SET streak = CASE WHEN streak > 0 THEN streak - 1 ELSE 0 END WHERE id = :habitId")
    suspend fun decreaseStreak(habitId: Int)

    @Query("DELETE FROM habits WHERE id = :id")
    suspend fun deleteHabit(id: Int)

    @Query("UPDATE habits SET isChecked = 0")
    suspend fun resetAllCheckedStates()

    @Query("SELECT * FROM habits WHERE id = :id LIMIT 1")
    suspend fun getHabitById(id: Int): HabitEntity?

    @Query("DELETE FROM habits WHERE text = :text")
    suspend fun deleteAllHabitsByText(text: String)

    @Query("SELECT * FROM habits")
    suspend fun getAllHabits(): List<HabitEntity>

    @Query("SELECT * FROM habits WHERE date = :date AND text = :text")
    suspend fun getHabitsByDateAndText(date: String, text: String): List<HabitEntity>

    @Query("DELETE FROM habits")
    suspend fun deleteAllHabits()
}