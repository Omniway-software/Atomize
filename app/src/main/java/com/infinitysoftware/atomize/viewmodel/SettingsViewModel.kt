package com.infinitysoftware.atomize.ui.settings

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.infinitysoftware.atomize.model.settings.Settings
import com.infinitysoftware.atomize.model.settings.SettingsDao
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class SettingsViewModel(private val settingsDao: SettingsDao) : ViewModel() {

    private val _settings = MutableStateFlow(Settings())
    val settings: StateFlow<Settings> = _settings.asStateFlow()

    init {
        initializeSettings()
        observeSettings()
    }

    private fun initializeSettings() {
        viewModelScope.launch {
            if (settingsDao.getSettingsOnce() == null) {
                settingsDao.updateSettings(Settings())
            }
        }
    }

    private fun observeSettings() {
        viewModelScope.launch {
            settingsDao.getSettings().collect { settings ->
                _settings.value = settings ?: Settings()
            }
        }
    }

    fun updateShowStreak(showStreak: Boolean) {
        viewModelScope.launch {
            val currentSettings = _settings.value
            settingsDao.updateSettings(currentSettings.copy(showStreak = showStreak))
        }
    }

    fun updateAnimatedIcon(animatedIcon: Boolean) {
        viewModelScope.launch {
            val currentSettings = _settings.value
            settingsDao.updateSettings(currentSettings.copy(animatedIcon = animatedIcon))
        }
    }

    fun updateSettings(showStreak: Boolean, animatedIcon: Boolean) {
        viewModelScope.launch {
            settingsDao.updateSettings(
                Settings(
                    id = 1,
                    showStreak = showStreak,
                    animatedIcon = animatedIcon
                )
            )
        }
    }
}

class SettingsViewModelFactory(
    private val settingsDao: SettingsDao
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(SettingsViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return SettingsViewModel(settingsDao) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}