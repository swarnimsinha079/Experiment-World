package com.example.ui.screens.lab

import android.widget.Toast
import androidx.compose.animation.*
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.Reagent
import com.example.data.model.ReagentCategory
import com.example.ui.theme.*
import com.example.viewmodel.LabUiEffect
import com.example.viewmodel.LabViewModel
import com.example.viewmodel.ProgressViewModel
import kotlinx.coroutines.flow.collectLatest

@Composable
fun LabScreen(
    viewModel: LabViewModel,
    progressViewModel: ProgressViewModel? = null,
    onNavigateToAiMentor: ((String) -> Unit)? = null,
    onOpenSettings: (() -> Unit)? = null,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val theme = VirtuLabTheme.colors

    val reagentsInBeaker by viewModel.reagentsInBeaker.collectAsState()
    val currentReaction by viewModel.currentReaction.collectAsState()
    val temperature by viewModel.temperature.collectAsState()
    val ph by viewModel.ph.collectAsState()
    val isBurnerOn by viewModel.isBunsenBurnerOn.collectAsState()
    val isStirrerActive by viewModel.isStirrerActive.collectAsState()
    val isExploding by viewModel.isExploding.collectAsState()
    val isExplanationOpen by viewModel.isExplanationOpen.collectAsState()

    val userProgress = progressViewModel?.progress?.collectAsState()?.value

    var selectedShelfCategory by remember { mutableStateOf<ReagentCategory?>(null) }

    // Award experiment points when reaction triggers
    LaunchedEffect(currentReaction) {
        if (currentReaction != null) {
            progressViewModel?.awardExperimentPoints()
        }
    }

    LaunchedEffect(Unit) {
        viewModel.effects.collectLatest { effect ->
            if (effect is LabUiEffect.ShowToast) {
                Toast.makeText(context, effect.message, Toast.LENGTH_SHORT).show()
            }
        }
    }

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(theme.background)
            .testTag("lab_screen")
    ) {
        // 1. 3D WebGL / Three.js Interactive Laboratory Space
        ThreeJsLabWebView(
            viewModel = viewModel,
            modifier = Modifier.fillMaxSize()
        )

        // 2. Explosion Flash Overlay when Sodium + Water reacts
        AnimatedVisibility(
            visible = isExploding,
            enter = fadeIn(),
            exit = fadeOut(),
            modifier = Modifier.fillMaxSize()
        ) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(
                        Brush.radialGradient(
                            listOf(
                                Color.White.copy(alpha = 0.85f),
                                AmberExplosion.copy(alpha = 0.5f),
                                Color.Transparent
                            )
                        )
                    )
            )
        }

        // 3. Foreground HUD Layout
        Column(
            modifier = Modifier
                .fillMaxSize()
                .statusBarsPadding()
                .padding(horizontal = 14.dp, vertical = 8.dp)
        ) {
            // Top Bar: HUD Status Gauges & Apparatus Toggles
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Left: Real-time Thermometer, pH, and RP Progress Gauges
                Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                    if (userProgress != null) {
                        HudPill(
                            icon = Icons.Default.Bolt,
                            label = "${userProgress.researchPoints} RP",
                            glowColor = AmberExplosion,
                            testTag = "hud_research_points",
                            onClick = { progressViewModel?.setProgressModalOpen(true) }
                        )
                    }
                    HudPill(
                        icon = Icons.Default.Thermostat,
                        label = "${temperature}°C",
                        glowColor = if (temperature > 70) RedReaction else if (temperature > 30) AmberExplosion else EmeraldNeon,
                        testTag = "hud_temperature"
                    )
                    HudPill(
                        icon = Icons.Default.Science,
                        label = "pH $ph",
                        glowColor = if (ph > 11) BasicBlue else if (ph < 4) AcidicRed else NeutralGreen,
                        testTag = "hud_ph"
                    )
                }

                // Right: Apparatus Controls (Periodic Table, Bunsen Burner, Stirrer, Reset, Settings)
                Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                    IconButton(
                        onClick = { viewModel.setPeriodicTableOpen(true) },
                        modifier = Modifier
                            .size(38.dp)
                            .clip(CircleShape)
                            .background(theme.primaryAccent.copy(alpha = 0.2f))
                            .border(1.dp, theme.primaryAccent, CircleShape)
                            .testTag("open_periodic_table_button")
                    ) {
                        Icon(
                            imageVector = Icons.Default.GridOn,
                            contentDescription = "Periodic Table",
                            tint = theme.primaryAccent,
                            modifier = Modifier.size(20.dp)
                        )
                    }

                    IconButton(
                        onClick = { viewModel.toggleBunsenBurner() },
                        modifier = Modifier
                            .size(38.dp)
                            .clip(CircleShape)
                            .background(if (isBurnerOn) AmberExplosion.copy(alpha = 0.25f) else theme.cardSurface)
                            .border(1.dp, if (isBurnerOn) AmberExplosion else theme.border, CircleShape)
                            .testTag("bunsen_burner_toggle")
                    ) {
                        Icon(
                            imageVector = Icons.Default.LocalFireDepartment,
                            contentDescription = "Bunsen Burner",
                            tint = if (isBurnerOn) AmberExplosion else theme.textSecondary,
                            modifier = Modifier.size(20.dp)
                        )
                    }

                    IconButton(
                        onClick = { viewModel.toggleStirrer() },
                        modifier = Modifier
                            .size(38.dp)
                            .clip(CircleShape)
                            .background(if (isStirrerActive) theme.primaryAccent.copy(alpha = 0.25f) else theme.cardSurface)
                            .border(1.dp, if (isStirrerActive) theme.primaryAccent else theme.border, CircleShape)
                            .testTag("stirrer_toggle")
                    ) {
                        Icon(
                            imageVector = Icons.Default.Autorenew,
                            contentDescription = "Magnetic Stirrer",
                            tint = if (isStirrerActive) theme.primaryAccent else theme.textSecondary,
                            modifier = Modifier.size(20.dp)
                        )
                    }

                    IconButton(
                        onClick = { viewModel.resetLab() },
                        modifier = Modifier
                            .size(38.dp)
                            .clip(CircleShape)
                            .background(theme.cardSurface)
                            .border(1.dp, theme.border, CircleShape)
                            .testTag("reset_lab_button")
                    ) {
                        Icon(
                            imageVector = Icons.Default.RestartAlt,
                            contentDescription = "Reset Lab",
                            tint = theme.textSecondary,
                            modifier = Modifier.size(20.dp)
                        )
                    }

                    // Settings Button
                    IconButton(
                        onClick = { onOpenSettings?.invoke() },
                        modifier = Modifier
                            .size(38.dp)
                            .clip(CircleShape)
                            .background(theme.cardSurface)
                            .border(1.dp, theme.border, CircleShape)
                            .testTag("open_settings_button")
                    ) {
                        Icon(
                            imageVector = Icons.Default.Settings,
                            contentDescription = "Settings",
                            tint = theme.primaryAccent,
                            modifier = Modifier.size(20.dp)
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            // Beaker Contents Pill Row
            if (reagentsInBeaker.isNotEmpty()) {
                LazyRow(
                    horizontalArrangement = Arrangement.spacedBy(6.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    items(reagentsInBeaker) { reagent ->
                        InputChip(
                            selected = true,
                            onClick = { viewModel.removeReagent(reagent) },
                            label = {
                                Text(
                                    text = "${reagent.formula} (${reagent.name})",
                                    fontSize = 11.sp,
                                    color = theme.textPrimary
                                )
                            },
                            trailingIcon = {
                                Icon(
                                    imageVector = Icons.Default.Close,
                                    contentDescription = "Remove",
                                    tint = theme.textSecondary,
                                    modifier = Modifier.size(14.dp)
                                )
                            },
                            colors = InputChipDefaults.inputChipColors(
                                selectedContainerColor = theme.surfaceElevated
                            ),
                            border = InputChipDefaults.inputChipBorder(
                                enabled = true,
                                selected = true,
                                selectedBorderColor = theme.primaryAccent.copy(alpha = 0.5f)
                            )
                        )
                    }
                }
            }

            // Reaction Trigger Notification Banner
            AnimatedVisibility(
                visible = currentReaction != null,
                enter = slideInVertically() + fadeIn(),
                exit = slideOutVertically() + fadeOut()
            ) {
                currentReaction?.let { rxn ->
                    Card(
                        colors = CardDefaults.cardColors(containerColor = theme.surface.copy(alpha = 0.95f)),
                        shape = RoundedCornerShape(14.dp),
                        border = CardDefaults.outlinedCardBorder().copy(
                            brush = Brush.horizontalGradient(listOf(theme.primaryAccent, theme.secondaryAccent))
                        ),
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 6.dp)
                            .clickable { viewModel.setExplanationOpen(true) }
                            .testTag("reaction_alert_banner")
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(12.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Column(modifier = Modifier.weight(1f)) {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Icon(
                                        imageVector = Icons.Default.ElectricBolt,
                                        contentDescription = null,
                                        tint = AmberExplosion,
                                        modifier = Modifier.size(16.dp)
                                    )
                                    Spacer(modifier = Modifier.width(6.dp))
                                    Text(
                                        text = "REACTION TRIGGERED",
                                        color = theme.secondaryAccent,
                                        fontSize = 10.sp,
                                        fontWeight = FontWeight.Bold,
                                        letterSpacing = 1.sp
                                    )
                                }
                                Text(
                                    text = rxn.balancedEquation,
                                    color = theme.textPrimary,
                                    fontFamily = FontFamily.Monospace,
                                    fontSize = 13.sp,
                                    fontWeight = FontWeight.SemiBold
                                )
                            }
                            Button(
                                onClick = { viewModel.setExplanationOpen(true) },
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = theme.primaryAccent,
                                    contentColor = if (theme.isDark) MidnightBg else Color.White
                                ),
                                contentPadding = PaddingValues(horizontal = 12.dp, vertical = 6.dp),
                                modifier = Modifier.testTag("open_breakdown_button")
                            ) {
                                Text("Inspect", fontSize = 11.sp, fontWeight = FontWeight.Bold)
                            }
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.weight(1f))

            // Bottom Section: Chemical Reagents Shelf Drawer
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(topStart = 20.dp, topEnd = 20.dp))
                    .background(theme.surface.copy(alpha = 0.95f))
                    .border(1.dp, theme.border, RoundedCornerShape(topStart = 20.dp, topEnd = 20.dp))
                    .padding(top = 10.dp, bottom = 12.dp, start = 12.dp, end = 12.dp)
            ) {
                // Shelf Header & Category Filter
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text(
                            text = "CHEMICAL REAGENT SHELF",
                            color = theme.primaryAccent,
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            letterSpacing = 1.2.sp
                        )
                        Text(
                            text = "Tap to mix into beaker",
                            color = theme.textMuted,
                            fontSize = 10.sp
                        )
                    }

                    FilledTonalButton(
                        onClick = { viewModel.setPeriodicTableOpen(true) },
                        colors = ButtonDefaults.filledTonalButtonColors(
                            containerColor = theme.primaryAccent.copy(alpha = 0.15f),
                            contentColor = theme.primaryAccent
                        ),
                        contentPadding = PaddingValues(horizontal = 10.dp, vertical = 4.dp),
                        modifier = Modifier.testTag("shelf_open_periodic_table_button")
                    ) {
                        Icon(imageVector = Icons.Default.GridOn, contentDescription = null, modifier = Modifier.size(14.dp))
                        Spacer(modifier = Modifier.width(4.dp))
                        Text("Periodic Table", fontSize = 11.sp, fontWeight = FontWeight.Bold)
                    }
                }

                Spacer(modifier = Modifier.height(8.dp))

                // Category Chips
                LazyRow(
                    horizontalArrangement = Arrangement.spacedBy(6.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    item {
                        FilterChip(
                            selected = selectedShelfCategory == null,
                            onClick = { selectedShelfCategory = null },
                            label = { Text("All Reagents", fontSize = 10.sp) },
                            colors = FilterChipDefaults.filterChipColors(
                                selectedContainerColor = theme.primaryAccent.copy(alpha = 0.2f),
                                selectedLabelColor = theme.primaryAccent,
                                labelColor = theme.textSecondary
                            )
                        )
                    }
                    items(ReagentCategory.values()) { cat ->
                        FilterChip(
                            selected = selectedShelfCategory == cat,
                            onClick = {
                                selectedShelfCategory = if (selectedShelfCategory == cat) null else cat
                            },
                            label = { Text(cat.name.replace("_", " "), fontSize = 10.sp) },
                            colors = FilterChipDefaults.filterChipColors(
                                selectedContainerColor = theme.primaryAccent.copy(alpha = 0.2f),
                                selectedLabelColor = theme.primaryAccent,
                                labelColor = theme.textSecondary
                            )
                        )
                    }
                }

                Spacer(modifier = Modifier.height(10.dp))

                // Reagent Cards Horizontal Carousel
                val filteredReagents = remember(selectedShelfCategory) {
                    if (selectedShelfCategory == null) viewModel.availableReagents
                    else viewModel.availableReagents.filter { it.category == selectedShelfCategory }
                }

                LazyRow(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("chemical_reagent_shelf")
                ) {
                    items(filteredReagents) { reagent ->
                        ReagentShelfCard(
                            reagent = reagent,
                            isInBeaker = reagentsInBeaker.any { it.id == reagent.id },
                            onAdd = { viewModel.addReagent(reagent) }
                        )
                    }
                }
            }
        }

        // Slide-up Step-by-Step Explanation Sheet
        if (isExplanationOpen) {
            ExplanationBottomSheet(
                viewModel = viewModel,
                onNavigateToAiMentor = onNavigateToAiMentor
            )
        }

        // Periodic Table Selection Drawer
        val isPeriodicTableOpen by viewModel.isPeriodicTableOpen.collectAsState()
        if (isPeriodicTableOpen) {
            PeriodicTableDrawer(
                viewModel = viewModel,
                onNavigateToAiMentor = onNavigateToAiMentor
            )
        }
    }
}

@Composable
fun ReagentShelfCard(
    reagent: Reagent,
    isInBeaker: Boolean,
    onAdd: () -> Unit
) {
    val theme = VirtuLabTheme.colors

    Card(
        colors = CardDefaults.cardColors(
            containerColor = if (isInBeaker) theme.surfaceElevated else theme.cardSurface
        ),
        shape = RoundedCornerShape(12.dp),
        border = CardDefaults.outlinedCardBorder().copy(
            brush = Brush.verticalGradient(
                if (isInBeaker) listOf(theme.primaryAccent.copy(alpha = 0.6f), theme.border)
                else listOf(theme.border, Color.Transparent)
            )
        ),
        modifier = Modifier
            .width(118.dp)
            .clickable { onAdd() }
            .testTag("reagent_card_${reagent.id}")
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(10.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Formula Icon Avatar
            Box(
                modifier = Modifier
                    .size(40.dp)
                    .clip(CircleShape)
                    .background(theme.surfaceElevated)
                    .border(1.dp, theme.primaryAccent.copy(alpha = 0.4f), CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = reagent.formula,
                    color = theme.primaryAccent,
                    fontFamily = FontFamily.Monospace,
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Bold
                )
            }

            Spacer(modifier = Modifier.height(6.dp))

            Text(
                text = reagent.name,
                color = theme.textPrimary,
                fontSize = 11.sp,
                fontWeight = FontWeight.SemiBold,
                maxLines = 1
            )

            Text(
                text = reagent.state,
                color = theme.textSecondary,
                fontSize = 9.sp
            )

            Spacer(modifier = Modifier.height(6.dp))

            // Add action pill
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center,
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(6.dp))
                    .background(if (isInBeaker) EmeraldNeon.copy(alpha = 0.2f) else theme.primaryAccent.copy(alpha = 0.15f))
                    .padding(vertical = 3.dp)
            ) {
                Icon(
                    imageVector = if (isInBeaker) Icons.Default.Check else Icons.Default.Add,
                    contentDescription = null,
                    tint = if (isInBeaker) theme.secondaryAccent else theme.primaryAccent,
                    modifier = Modifier.size(12.dp)
                )
                Spacer(modifier = Modifier.width(3.dp))
                Text(
                    text = if (isInBeaker) "In Beaker" else "Add",
                    color = if (isInBeaker) theme.secondaryAccent else theme.primaryAccent,
                    fontSize = 10.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}

@Composable
fun HudPill(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    label: String,
    glowColor: Color,
    testTag: String,
    onClick: (() -> Unit)? = null
) {
    val theme = VirtuLabTheme.colors

    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .clip(RoundedCornerShape(20.dp))
            .background(theme.hudBackground)
            .border(1.dp, glowColor.copy(alpha = 0.5f), RoundedCornerShape(20.dp))
            .then(if (onClick != null) Modifier.clickable { onClick() } else Modifier)
            .padding(horizontal = 10.dp, vertical = 6.dp)
            .testTag(testTag)
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            tint = glowColor,
            modifier = Modifier.size(15.dp)
        )
        Spacer(modifier = Modifier.width(6.dp))
        Text(
            text = label,
            color = theme.textPrimary,
            fontSize = 12.sp,
            fontWeight = FontWeight.Bold,
            fontFamily = FontFamily.Monospace
        )
    }
}
