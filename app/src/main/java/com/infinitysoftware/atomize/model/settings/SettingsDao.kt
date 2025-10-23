package com.infinitysoftware.atomize.model.settings

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface SettingsDao {
    @Query("SELECT * FROM settings WHERE id = 1")
    fun getSettings(): Flow<Settings?>

    @Insert(onConflict = OnConflictStrategy.Companion.REPLACE)
    suspend fun updateSettings(settings: Settings)

    @Query("SELECT * FROM settings WHERE id = 1")
    suspend fun getSettingsOnce(): Settings?
}