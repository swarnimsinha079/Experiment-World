package com.example.ui.theme

import androidx.compose.runtime.Composable
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.Color

// Sci-Math Dark Theme Palette
val MidnightBg = Color(0xFF090D16)
val DeepSurface = Color(0xFF111827)
val CardSurface = Color(0xFF1E293B)
val CardSurfaceElevated = Color(0xFF283548)
val BorderSubtle = Color(0xFF334155)

// Sci-Math Light Theme Palette (Clean Lab High-Tech)
val LightLabBg = Color(0xFFF1F5F9)
val LightLabSurface = Color(0xFFFFFFFF)
val LightLabCardSurface = Color(0xFFFFFFFF)
val LightLabCardElevated = Color(0xFFF8FAFC)
val LightLabBorder = Color(0xFFCBD5E1)
val LightTextPrimary = Color(0xFF0F172A)
val LightTextSecondary = Color(0xFF475569)
val LightTextMuted = Color(0xFF94A3B8)

// Accent Colors
val CyanNeon = Color(0xFF00E5FF)
val CyanGlow = Color(0xFF18FFFF)
val CyanDeep = Color(0xFF0284C7)
val EmeraldNeon = Color(0xFF00E676)
val EmeraldDeep = Color(0xFF059669)
val AmberExplosion = Color(0xFFFF9100)
val RedReaction = Color(0xFFFF3D00)
val VioletMath = Color(0xFF7C4DFF)
val VioletDeep = Color(0xFF6D28D9)
val BlueChemistry = Color(0xFF2979FF)
val PinkAcid = Color(0xFFFF4081)

// Neutral Text & Icons
val TextPrimary = Color(0xFFF8FAFC)
val TextSecondary = Color(0xFF94A3B8)
val TextMuted = Color(0xFF64748B)

// Status colors
val AcidicRed = Color(0xFFFF5252)
val NeutralGreen = Color(0xFF69F0AE)
val BasicBlue = Color(0xFF448AFF)

data class VirtuLabColors(
    val background: Color,
    val surface: Color,
    val surfaceElevated: Color,
    val cardSurface: Color,
    val border: Color,
    val textPrimary: Color,
    val textSecondary: Color,
    val textMuted: Color,
    val primaryAccent: Color,
    val secondaryAccent: Color,
    val tertiaryAccent: Color,
    val hudBackground: Color,
    val hudBorder: Color,
    val isDark: Boolean
)

val DarkVirtuLabColors = VirtuLabColors(
    background = MidnightBg,
    surface = DeepSurface,
    surfaceElevated = CardSurfaceElevated,
    cardSurface = CardSurface,
    border = BorderSubtle,
    textPrimary = TextPrimary,
    textSecondary = TextSecondary,
    textMuted = TextMuted,
    primaryAccent = CyanNeon,
    secondaryAccent = EmeraldNeon,
    tertiaryAccent = VioletMath,
    hudBackground = Color(0xDF111827),
    hudBorder = Color(0x4400E5FF),
    isDark = true
)

val LightVirtuLabColors = VirtuLabColors(
    background = LightLabBg,
    surface = LightLabSurface,
    surfaceElevated = LightLabCardElevated,
    cardSurface = LightLabCardSurface,
    border = LightLabBorder,
    textPrimary = LightTextPrimary,
    textSecondary = LightTextSecondary,
    textMuted = LightTextMuted,
    primaryAccent = CyanDeep,
    secondaryAccent = EmeraldDeep,
    tertiaryAccent = VioletDeep,
    hudBackground = Color(0xF2FFFFFF),
    hudBorder = Color(0x330284C7),
    isDark = false
)

val LocalVirtuLabColors = staticCompositionLocalOf { DarkVirtuLabColors }

object VirtuLabTheme {
    val colors: VirtuLabColors
        @Composable
        get() = LocalVirtuLabColors.current
}
