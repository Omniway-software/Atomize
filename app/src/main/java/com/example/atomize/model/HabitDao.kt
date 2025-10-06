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


    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertHabit(habit: HabitEntity): Long

    @Query(value = "UPDATE habits SET isChecked = :isChecked WHERE id = :id")
    suspend fun updateChecked(id: Int, isChecked: Boolean)

    @Query(value = "UPDATE habits SET streak = streak + 1 WHERE id = :id")
    suspend fun increaseStreak(id: Int)

    @Query(value = "UPDATE habits SET streak = 0 WHERE id = :id")
    suspend fun resetStreak(id: Int)

    @Query(value = "DELETE FROM habits WHERE id = :id")
    suspend fun deleteHabit(id: Int)
}