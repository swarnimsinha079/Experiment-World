package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.ui.screens.ai.AiMentorScreen
import com.example.ui.screens.lab.LabScreen
import com.example.ui.screens.math.MathZoneScreen
import com.example.ui.screens.notebook.NotebookScreen
import com.example.ui.screens.progress.BadgeUnlockedDialog
import com.example.ui.screens.progress.ResearchProgressSheet
import com.example.ui.screens.settings.SettingsModalSheet
import com.example.ui.theme.*
import com.example.viewmodel.*

enum class VirtuLabTab(
    val title: String,
    val selectedIcon: ImageVector,
    val unselectedIcon: ImageVector
) {
    LAB("3D Lab", Icons.Filled.Science, Icons.Outlined.Science),
    MATH("Math Zone", Icons.Filled.SquareFoot, Icons.Outlined.SquareFoot),
    AI_MENTOR("AI Mentor", Icons.Filled.Psychology, Icons.Outlined.Psychology),
    NOTEBOOK("Notebook", Icons.Filled.MenuBook, Icons.Outlined.MenuBook)
}

@Composable
fun MainDashboard(
    labViewModel: LabViewModel = viewModel(),
    mathViewModel: MathViewModel = viewModel(),
    aiMentorViewModel: AiMentorViewModel = viewModel(),
    notebookViewModel: NotebookViewModel = viewModel(),
    progressViewModel: ProgressViewModel = viewModel(),
    settingsViewModel: SettingsViewModel = viewModel()
) {
    val theme = VirtuLabTheme.colors
    var currentTab by remember { mutableStateOf(VirtuLabTab.LAB) }
    var pendingAiPrompt by remember { mutableStateOf<String?>(null) }

    val unlockedBadge by progressViewModel.unlockedBadgeAlert.collectAsState()
    val isProgressSheetOpen by progressViewModel.isProgressModalOpen.collectAsState()
    val isSettingsOpen by settingsViewModel.isSettingsModalOpen.collectAsState()

    Scaffold(
        bottomBar = {
            NavigationBar(
                containerColor = theme.surface,
                contentColor = theme.textPrimary,
                tonalElevation = 8.dp,
                modifier = Modifier
                    .navigationBarsPadding()
                    .testTag("main_navigation_bar")
            ) {
                VirtuLabTab.values().forEach { tab ->
                    val isSelected = currentTab == tab
                    NavigationBarItem(
                        selected = isSelected,
                        onClick = { currentTab = tab },
                        icon = {
                            Icon(
                                imageVector = if (isSelected) tab.selectedIcon else tab.unselectedIcon,
                                contentDescription = tab.title,
                                modifier = Modifier.size(22.dp)
                            )
                        },
                        label = {
                            Text(
                                text = tab.title,
                                fontSize = 11.sp
                            )
                        },
                        colors = NavigationBarItemDefaults.colors(
                            selectedIconColor = if (theme.isDark) MidnightBg else Color.White,
                            selectedTextColor = theme.primaryAccent,
                            indicatorColor = theme.primaryAccent,
                            unselectedIconColor = theme.textSecondary,
                            unselectedTextColor = theme.textSecondary
                        ),
                        modifier = Modifier.testTag("nav_item_${tab.name.lowercase()}")
                    )
                }
            }
        },
        containerColor = theme.background,
        modifier = Modifier.fillMaxSize()
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(bottom = innerPadding.calculateBottomPadding())
        ) {
            when (currentTab) {
                VirtuLabTab.LAB -> {
                    LabScreen(
                        viewModel = labViewModel,
                        progressViewModel = progressViewModel,
                        onNavigateToAiMentor = { prompt ->
                            pendingAiPrompt = prompt
                            currentTab = VirtuLabTab.AI_MENTOR
                        },
                        onOpenSettings = {
                            settingsViewModel.setSettingsModalOpen(true)
                        }
                    )
                }
                VirtuLabTab.MATH -> {
                    MathZoneScreen(
                        viewModel = mathViewModel,
                        onOpenSettings = {
                            settingsViewModel.setSettingsModalOpen(true)
                        }
                    )
                }
                VirtuLabTab.AI_MENTOR -> {
                    AiMentorScreen(
                        viewModel = aiMentorViewModel,
                        initialPrompt = pendingAiPrompt,
                        onOpenSettings = {
                            settingsViewModel.setSettingsModalOpen(true)
                        }
                    )
                    // Reset pending prompt once consumed
                    DisposableEffect(Unit) {
                        onDispose { pendingAiPrompt = null }
                    }
                }
                VirtuLabTab.NOTEBOOK -> {
                    NotebookScreen(
                        viewModel = notebookViewModel,
                        onOpenSettings = {
                            settingsViewModel.setSettingsModalOpen(true)
                        }
                    )
                }
            }

            // Research Progress Sheet Modal
            if (isProgressSheetOpen) {
                ResearchProgressSheet(
                    viewModel = progressViewModel
                )
            }

            // Settings Sheet Modal (Switch theme from Light to Dark or Dark to Light!)
            if (isSettingsOpen) {
                SettingsModalSheet(
                    viewModel = settingsViewModel,
                    onDismiss = { settingsViewModel.setSettingsModalOpen(false) }
                )
            }

            // Celebratory Badge Unlocked Dialog
            unlockedBadge?.let { badge ->
                BadgeUnlockedDialog(
                    badge = badge,
                    onDismiss = { progressViewModel.dismissUnlockedAlert() }
                )
            }
        }
    }
}
