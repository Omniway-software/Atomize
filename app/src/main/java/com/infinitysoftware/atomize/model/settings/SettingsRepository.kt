package com.infinitysoftware.atomize.model.settings

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class SettingsRepository(private val settingsDao: SettingsDao) {
    val settings: Flow<Settings> = settingsDao.getSettings().map { it ?: Settings() }

    suspend fun updateSettings(settings: Settings) {
        settingsDao.updateSettings(settings)
    }
}