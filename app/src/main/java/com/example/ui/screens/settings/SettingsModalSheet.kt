package com.example.ui.screens.settings

import android.widget.Toast
import androidx.compose.animation.*
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.repository.AppThemeMode
import com.example.ui.theme.*
import com.example.viewmodel.SettingsViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsModalSheet(
    viewModel: SettingsViewModel,
    onDismiss: () -> Unit,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val settings by viewModel.settings.collectAsState()
    val theme = VirtuLabTheme.colors

    var showResetConfirmDialog by remember { mutableStateOf(false) }

    ModalBottomSheet(
        onDismissRequest = onDismiss,
        containerColor = theme.surface,
        scrimColor = Color.Black.copy(alpha = 0.6f),
        dragHandle = {
            BottomSheetDefaults.DragHandle(
                color = theme.border
            )
        },
        modifier = modifier.testTag("settings_modal_sheet")
    ) {
        LazyColumn(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp)
                .padding(bottom = 32.dp),
            verticalArrangement = Arrangement.spacedBy(20.dp)
        ) {
            // Header
            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text(
                            text = "APP SETTINGS",
                            color = theme.primaryAccent,
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            letterSpacing = 1.2.sp,
                            fontFamily = FontFamily.Monospace
                        )
                        Text(
                            text = "Preferences & Appearance",
                            color = theme.textPrimary,
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }

                    IconButton(
                        onClick = onDismiss,
                        modifier = Modifier
                            .size(36.dp)
                            .clip(CircleShape)
                            .background(theme.cardSurface)
                            .border(1.dp, theme.border, CircleShape)
                            .testTag("close_settings_button")
                    ) {
                        Icon(
                            imageVector = Icons.Default.Close,
                            contentDescription = "Close Settings",
                            tint = theme.textSecondary,
                            modifier = Modifier.size(18.dp)
                        )
                    }
                }
            }

            // Section 1: Appearance & Theme
            item {
                SettingsSectionCard(
                    title = "THEME & DISPLAY MODE",
                    icon = Icons.Default.Palette
                ) {
                    Column(verticalArrangement = Arrangement.spacedBy(14.dp)) {
                        Text(
                            text = "Choose your preferred interface theme. Some students and educators prefer a clean light laboratory aesthetic, while others prefer the immersive deep midnight dark theme.",
                            color = theme.textSecondary,
                            fontSize = 13.sp,
                            lineHeight = 18.sp
                        )

                        // 3 Theme Choices Cards
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(10.dp)
                        ) {
                            ThemeChoiceCard(
                                title = "Dark",
                                subtitle = "Midnight Lab",
                                icon = Icons.Filled.DarkMode,
                                isSelected = settings.themeMode == AppThemeMode.DARK,
                                accentColor = CyanNeon,
                                previewBg = MidnightBg,
                                previewSurface = DeepSurface,
                                testTag = "theme_option_dark",
                                modifier = Modifier.weight(1f),
                                onClick = {
                                    viewModel.setThemeMode(AppThemeMode.DARK)
                                    Toast.makeText(context, "Dark Theme activated", Toast.LENGTH_SHORT).show()
                                }
                            )

                            ThemeChoiceCard(
                                title = "Light",
                                subtitle = "Clean Slate",
                                icon = Icons.Filled.LightMode,
                                isSelected = settings.themeMode == AppThemeMode.LIGHT,
                                accentColor = CyanDeep,
                                previewBg = LightLabBg,
                                previewSurface = LightLabSurface,
                                testTag = "theme_option_light",
                                modifier = Modifier.weight(1f),
                                onClick = {
                                    viewModel.setThemeMode(AppThemeMode.LIGHT)
                                    Toast.makeText(context, "Light Theme activated", Toast.LENGTH_SHORT).show()
                                }
                            )

                            ThemeChoiceCard(
                                title = "System",
                                subtitle = "Auto Sync",
                                icon = Icons.Filled.BrightnessAuto,
                                isSelected = settings.themeMode == AppThemeMode.SYSTEM,
                                accentColor = EmeraldNeon,
                                previewBg = if (theme.isDark) MidnightBg else LightLabBg,
                                previewSurface = if (theme.isDark) DeepSurface else LightLabSurface,
                                testTag = "theme_option_system",
                                modifier = Modifier.weight(1f),
                                onClick = {
                                    viewModel.setThemeMode(AppThemeMode.SYSTEM)
                                    Toast.makeText(context, "System Theme sync activated", Toast.LENGTH_SHORT).show()
                                }
                            )
                        }

                        // Quick Toggle Switch Row
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(12.dp))
                                .background(theme.surfaceElevated)
                                .border(1.dp, theme.border, RoundedCornerShape(12.dp))
                                .padding(horizontal = 14.dp, vertical = 10.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(10.dp)
                            ) {
                                Icon(
                                    imageVector = if (theme.isDark) Icons.Default.DarkMode else Icons.Default.LightMode,
                                    contentDescription = null,
                                    tint = theme.primaryAccent,
                                    modifier = Modifier.size(22.dp)
                                )
                                Column {
                                    Text(
                                        text = if (theme.isDark) "Dark Theme Active" else "Light Theme Active",
                                        color = theme.textPrimary,
                                        fontSize = 14.sp,
                                        fontWeight = FontWeight.SemiBold
                                    )
                                    Text(
                                        text = if (theme.isDark) "Tap to switch to Light Theme" else "Tap to switch to Dark Theme",
                                        color = theme.textSecondary,
                                        fontSize = 12.sp
                                    )
                                }
                            }

                            Switch(
                                checked = theme.isDark,
                                onCheckedChange = { isDark ->
                                    val newMode = if (isDark) AppThemeMode.DARK else AppThemeMode.LIGHT
                                    viewModel.setThemeMode(newMode)
                                },
                                colors = SwitchDefaults.colors(
                                    checkedThumbColor = CyanNeon,
                                    checkedTrackColor = CardSurfaceElevated,
                                    uncheckedThumbColor = CyanDeep,
                                    uncheckedTrackColor = LightLabBorder
                                ),
                                modifier = Modifier.testTag("quick_theme_toggle_switch")
                            )
                        }
                    }
                }
            }

            // Section 2: Interactive Simulation & Hardware
            item {
                SettingsSectionCard(
                    title = "LABORATORY APPARATUS & FEEDBACK",
                    icon = Icons.Default.Science
                ) {
                    Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                        SettingToggleRow(
                            title = "Haptic Vibration Feedback",
                            subtitle = "Vibrate on exothermic reactions, explosions & slider adjustments",
                            icon = Icons.Default.Vibration,
                            isChecked = settings.hapticFeedbackEnabled,
                            testTag = "setting_toggle_haptic",
                            onCheckedChange = { viewModel.setHapticFeedback(it) }
                        )

                        SettingToggleRow(
                            title = "Reaction Audio Effects",
                            subtitle = "Play boiling sound, sizzle and pop on chemical synthesis",
                            icon = Icons.Default.VolumeUp,
                            isChecked = settings.soundEffectsEnabled,
                            testTag = "setting_toggle_sound",
                            onCheckedChange = { viewModel.setSoundEffects(it) }
                        )
                    }
                }
            }

            // Section 3: Academic Curriculum Level
            item {
                SettingsSectionCard(
                    title = "ACADEMIC CURRICULUM LEVEL",
                    icon = Icons.Default.School
                ) {
                    Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                        Text(
                            text = "Calibrates AI Mentor explanations and laboratory mathematical rigor.",
                            color = theme.textSecondary,
                            fontSize = 12.sp
                        )

                        val levels = listOf("Middle School (K-12)", "High School / AP", "University / Research")
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            levels.forEach { level ->
                                val isSelected = settings.academicLevel == level
                                Box(
                                    modifier = Modifier
                                        .weight(1f)
                                        .clip(RoundedCornerShape(10.dp))
                                        .background(if (isSelected) theme.primaryAccent.copy(alpha = 0.15f) else theme.surfaceElevated)
                                        .border(
                                            1.dp,
                                            if (isSelected) theme.primaryAccent else theme.border,
                                            RoundedCornerShape(10.dp)
                                        )
                                        .clickable {
                                            viewModel.setAcademicLevel(level)
                                            Toast.makeText(context, "Level set to: $level", Toast.LENGTH_SHORT).show()
                                        }
                                        .padding(vertical = 10.dp, horizontal = 6.dp),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Text(
                                        text = level,
                                        color = if (isSelected) theme.primaryAccent else theme.textSecondary,
                                        fontSize = 11.sp,
                                        fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                                        maxLines = 1
                                    )
                                }
                            }
                        }
                    }
                }
            }

            // Section 4: Data & Engine Info
            item {
                SettingsSectionCard(
                    title = "ABOUT VIRTU-LAB",
                    icon = Icons.Default.Info
                ) {
                    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "Engine Version",
                                color = theme.textSecondary,
                                fontSize = 13.sp
                            )
                            Text(
                                text = "VirtuLab v1.0 (WebGL Three.js)",
                                color = theme.textPrimary,
                                fontSize = 13.sp,
                                fontWeight = FontWeight.SemiBold
                            )
                        }

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "AI Reasoning Engine",
                                color = theme.textSecondary,
                                fontSize = 13.sp
                            )
                            Text(
                                text = "Gemini 3.1 Pro (High Thinking)",
                                color = theme.primaryAccent,
                                fontSize = 13.sp,
                                fontWeight = FontWeight.SemiBold
                            )
                        }

                        Spacer(modifier = Modifier.height(6.dp))

                        OutlinedButton(
                            onClick = { showResetConfirmDialog = true },
                            colors = ButtonDefaults.outlinedButtonColors(
                                contentColor = RedReaction
                            ),
                            border = androidx.compose.foundation.BorderStroke(1.dp, RedReaction.copy(alpha = 0.5f)),
                            modifier = Modifier
                                .fillMaxWidth()
                                .testTag("reset_settings_button")
                        ) {
                            Icon(
                                imageVector = Icons.Default.RestartAlt,
                                contentDescription = null,
                                modifier = Modifier.size(16.dp)
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Text("Reset Preferences to Defaults", fontSize = 12.sp)
                        }
                    }
                }
            }
        }
    }

    if (showResetConfirmDialog) {
        AlertDialog(
            onDismissRequest = { showResetConfirmDialog = false },
            title = {
                Text("Reset Preferences?")
            },
            text = {
                Text("This will restore default Dark theme, enable all haptic and audio effects, and reset curriculum level to High School / AP.")
            },
            confirmButton = {
                Button(
                    onClick = {
                        viewModel.resetToDefaults()
                        showResetConfirmDialog = false
                        Toast.makeText(context, "Preferences reset to defaults", Toast.LENGTH_SHORT).show()
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = RedReaction)
                ) {
                    Text("Reset")
                }
            },
            dismissButton = {
                TextButton(onClick = { showResetConfirmDialog = false }) {
                    Text("Cancel")
                }
            },
            containerColor = theme.surface,
            titleContentColor = theme.textPrimary,
            textContentColor = theme.textSecondary
        )
    }
}

@Composable
private fun SettingsSectionCard(
    title: String,
    icon: ImageVector,
    content: @Composable () -> Unit
) {
    val theme = VirtuLabTheme.colors

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .background(theme.cardSurface)
            .border(1.dp, theme.border, RoundedCornerShape(16.dp))
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = theme.primaryAccent,
                modifier = Modifier.size(18.dp)
            )
            Text(
                text = title,
                color = theme.primaryAccent,
                fontSize = 11.sp,
                fontWeight = FontWeight.Bold,
                letterSpacing = 1.sp,
                fontFamily = FontFamily.Monospace
            )
        }
        content()
    }
}

@Composable
private fun ThemeChoiceCard(
    title: String,
    subtitle: String,
    icon: ImageVector,
    isSelected: Boolean,
    accentColor: Color,
    previewBg: Color,
    previewSurface: Color,
    testTag: String,
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    val theme = VirtuLabTheme.colors

    Column(
        modifier = modifier
            .clip(RoundedCornerShape(12.dp))
            .background(if (isSelected) accentColor.copy(alpha = 0.12f) else theme.surfaceElevated)
            .border(
                2.dp,
                if (isSelected) accentColor else theme.border,
                RoundedCornerShape(12.dp)
            )
            .clickable(onClick = onClick)
            .padding(10.dp)
            .testTag(testTag),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(6.dp)
    ) {
        // Preview Palette Dot Circle
        Box(
            modifier = Modifier
                .size(34.dp)
                .clip(CircleShape)
                .background(previewBg)
                .border(1.5.dp, if (isSelected) accentColor else theme.border, CircleShape),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = icon,
                contentDescription = title,
                tint = if (isSelected) accentColor else theme.textSecondary,
                modifier = Modifier.size(18.dp)
            )
        }

        Text(
            text = title,
            color = if (isSelected) theme.textPrimary else theme.textSecondary,
            fontSize = 13.sp,
            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium
        )

        Text(
            text = subtitle,
            color = theme.textMuted,
            fontSize = 10.sp,
            maxLines = 1
        )
    }
}

@Composable
private fun SettingToggleRow(
    title: String,
    subtitle: String,
    icon: ImageVector,
    isChecked: Boolean,
    testTag: String,
    onCheckedChange: (Boolean) -> Unit
) {
    val theme = VirtuLabTheme.colors

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(12.dp))
            .background(theme.surfaceElevated)
            .border(1.dp, theme.border, RoundedCornerShape(12.dp))
            .padding(horizontal = 14.dp, vertical = 10.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Row(
            modifier = Modifier.weight(1f),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = if (isChecked) theme.primaryAccent else theme.textMuted,
                modifier = Modifier.size(20.dp)
            )
            Column {
                Text(
                    text = title,
                    color = theme.textPrimary,
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Medium
                )
                Text(
                    text = subtitle,
                    color = theme.textSecondary,
                    fontSize = 11.sp,
                    lineHeight = 15.sp
                )
            }
        }

        Spacer(modifier = Modifier.width(8.dp))

        Switch(
            checked = isChecked,
            onCheckedChange = onCheckedChange,
            colors = SwitchDefaults.colors(
                checkedThumbColor = theme.primaryAccent,
                checkedTrackColor = theme.cardSurface,
                uncheckedThumbColor = theme.textMuted,
                uncheckedTrackColor = theme.border
            ),
            modifier = Modifier.testTag(testTag)
        )
    }
}
