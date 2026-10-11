package com.example.data.repository

import android.content.Context
import android.content.SharedPreferences
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

enum class AppThemeMode(val displayName: String) {
    DARK("Dark (Sci-Math Midnight)"),
    LIGHT("Light (Clean Lab Slate)"),
    SYSTEM("Follow System")
}

data class UserSettings(
    val themeMode: AppThemeMode = AppThemeMode.DARK,
    val hapticFeedbackEnabled: Boolean = true,
    val soundEffectsEnabled: Boolean = true,
    val academicLevel: String = "High School / AP",
    val showFormulaSubscripts: Boolean = true
)

class SettingsRepository(context: Context) {
    private val prefs: SharedPreferences = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)

    private val _settingsFlow = MutableStateFlow(loadSettings())
    val settingsFlow: StateFlow<UserSettings> = _settingsFlow.asStateFlow()

    private fun loadSettings(): UserSettings {
        val themeModeName = prefs.getString(KEY_THEME_MODE, AppThemeMode.DARK.name) ?: AppThemeMode.DARK.name
        val themeMode = try {
            AppThemeMode.valueOf(themeModeName)
        } catch (_: Exception) {
            AppThemeMode.DARK
        }

        return UserSettings(
            themeMode = themeMode,
            hapticFeedbackEnabled = prefs.getBoolean(KEY_HAPTIC, true),
            soundEffectsEnabled = prefs.getBoolean(KEY_SOUND, true),
            academicLevel = prefs.getString(KEY_ACADEMIC_LEVEL, "High School / AP") ?: "High School / AP",
            showFormulaSubscripts = prefs.getBoolean(KEY_SUBSCRIPTS, true)
        )
    }

    fun setThemeMode(mode: AppThemeMode) {
        prefs.edit().putString(KEY_THEME_MODE, mode.name).apply()
        _settingsFlow.value = _settingsFlow.value.copy(themeMode = mode)
    }

    fun toggleLightDarkTheme() {
        val current = _settingsFlow.value.themeMode
        val next = if (current == AppThemeMode.LIGHT) AppThemeMode.DARK else AppThemeMode.LIGHT
        setThemeMode(next)
    }

    fun setHapticFeedback(enabled: Boolean) {
        prefs.edit().putBoolean(KEY_HAPTIC, enabled).apply()
        _settingsFlow.value = _settingsFlow.value.copy(hapticFeedbackEnabled = enabled)
    }

    fun setSoundEffects(enabled: Boolean) {
        prefs.edit().putBoolean(KEY_SOUND, enabled).apply()
        _settingsFlow.value = _settingsFlow.value.copy(soundEffectsEnabled = enabled)
    }

    fun setAcademicLevel(level: String) {
        prefs.edit().putString(KEY_ACADEMIC_LEVEL, level).apply()
        _settingsFlow.value = _settingsFlow.value.copy(academicLevel = level)
    }

    fun resetToDefaults() {
        prefs.edit().clear().apply()
        val defaultSettings = UserSettings()
        _settingsFlow.value = defaultSettings
    }

    companion object {
        private const val PREFS_NAME = "virtulab_user_settings"
        private const val KEY_THEME_MODE = "pref_theme_mode"
        private const val KEY_HAPTIC = "pref_haptic_feedback"
        private const val KEY_SOUND = "pref_sound_effects"
        private const val KEY_ACADEMIC_LEVEL = "pref_academic_level"
        private const val KEY_SUBSCRIPTS = "pref_subscripts"

        @Volatile
        private var INSTANCE: SettingsRepository? = null

        fun getInstance(context: Context): SettingsRepository {
            return INSTANCE ?: synchronized(this) {
                INSTANCE ?: SettingsRepository(context.applicationContext).also { INSTANCE = it }
            }
        }
    }
}
