package com.example.ui.theme

import android.app.Activity
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat
import com.example.data.repository.AppThemeMode

private val VirtuLabDarkColorScheme = darkColorScheme(
    primary = CyanNeon,
    onPrimary = MidnightBg,
    primaryContainer = CardSurfaceElevated,
    onPrimaryContainer = CyanGlow,
    secondary = EmeraldNeon,
    onSecondary = MidnightBg,
    secondaryContainer = CardSurface,
    onSecondaryContainer = EmeraldNeon,
    tertiary = VioletMath,
    onTertiary = TextPrimary,
    background = MidnightBg,
    onBackground = TextPrimary,
    surface = DeepSurface,
    onSurface = TextPrimary,
    surfaceVariant = CardSurface,
    onSurfaceVariant = TextSecondary,
    outline = BorderSubtle,
    error = RedReaction,
    onError = TextPrimary
)

private val VirtuLabLightColorScheme = lightColorScheme(
    primary = CyanDeep,
    onPrimary = LightLabSurface,
    primaryContainer = LightLabCardElevated,
    onPrimaryContainer = CyanDeep,
    secondary = EmeraldDeep,
    onSecondary = LightLabSurface,
    secondaryContainer = LightLabCardElevated,
    onSecondaryContainer = EmeraldDeep,
    tertiary = VioletDeep,
    onTertiary = LightLabSurface,
    background = LightLabBg,
    onBackground = LightTextPrimary,
    surface = LightLabSurface,
    onSurface = LightTextPrimary,
    surfaceVariant = LightLabCardSurface,
    onSurfaceVariant = LightTextSecondary,
    outline = LightLabBorder,
    error = RedReaction,
    onError = LightLabSurface
)

@Composable
fun MyApplicationTheme(
    themeMode: AppThemeMode = AppThemeMode.DARK,
    content: @Composable () -> Unit
) {
    val systemInDark = isSystemInDarkTheme()
    val isDark = when (themeMode) {
        AppThemeMode.DARK -> true
        AppThemeMode.LIGHT -> false
        AppThemeMode.SYSTEM -> systemInDark
    }

    val virtuLabColors = if (isDark) DarkVirtuLabColors else LightVirtuLabColors
    val colorScheme = if (isDark) VirtuLabDarkColorScheme else VirtuLabLightColorScheme

    val view = LocalView.current
    if (!view.isInEditMode) {
        SideEffect {
            val window = (view.context as? Activity)?.window
            if (window != null) {
                window.statusBarColor = virtuLabColors.background.toArgb()
                window.navigationBarColor = virtuLabColors.surface.toArgb()
                val insetsController = WindowCompat.getInsetsController(window, view)
                insetsController.isAppearanceLightStatusBars = !isDark
                insetsController.isAppearanceLightNavigationBars = !isDark
            }
        }
    }

    CompositionLocalProvider(LocalVirtuLabColors provides virtuLabColors) {
        MaterialTheme(
            colorScheme = colorScheme,
            typography = Typography,
            content = content
        )
    }
}
