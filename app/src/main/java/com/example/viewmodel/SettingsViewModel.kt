package com.example.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.example.data.repository.AppThemeMode
import com.example.data.repository.SettingsRepository
import com.example.data.repository.UserSettings
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class SettingsViewModel(application: Application) : AndroidViewModel(application) {

    private val repository = SettingsRepository.getInstance(application)

    val settings: StateFlow<UserSettings> = repository.settingsFlow

    private val _isSettingsModalOpen = MutableStateFlow(false)
    val isSettingsModalOpen: StateFlow<Boolean> = _isSettingsModalOpen.asStateFlow()

    fun setSettingsModalOpen(open: Boolean) {
        _isSettingsModalOpen.value = open
    }

    fun setThemeMode(mode: AppThemeMode) {
        repository.setThemeMode(mode)
    }

    fun toggleTheme() {
        repository.toggleLightDarkTheme()
    }

    fun setHapticFeedback(enabled: Boolean) {
        repository.setHapticFeedback(enabled)
    }

    fun setSoundEffects(enabled: Boolean) {
        repository.setSoundEffects(enabled)
    }

    fun setAcademicLevel(level: String) {
        repository.setAcademicLevel(level)
    }

    fun resetToDefaults() {
        repository.resetToDefaults()
    }
}
