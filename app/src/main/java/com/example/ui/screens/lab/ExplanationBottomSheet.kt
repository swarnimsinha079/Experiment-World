package com.example.ui.screens.lab

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.*
import com.example.viewmodel.LabViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ExplanationBottomSheet(
    viewModel: LabViewModel,
    onNavigateToAiMentor: ((String) -> Unit)? = null,
    modifier: Modifier = Modifier
) {
    val reaction by viewModel.currentReaction.collectAsState()
    val activeStepIndex by viewModel.activeStepIndex.collectAsState()
    val temperature by viewModel.temperature.collectAsState()
    val ph by viewModel.ph.collectAsState()

    val theme = VirtuLabTheme.colors
    val rxn = reaction ?: return
    val currentStep = rxn.steps.getOrNull(activeStepIndex) ?: return

    ModalBottomSheet(
        onDismissRequest = { viewModel.setExplanationOpen(false) },
        containerColor = theme.surface,
        contentColor = theme.textPrimary,
        scrimColor = Color.Black.copy(alpha = 0.65f),
        dragHandle = {
            BottomSheetDefaults.DragHandle(
                color = theme.border
            )
        },
        modifier = modifier.testTag("explanation_bottom_sheet")
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp)
                .padding(bottom = 28.dp)
        ) {
            // Header: Reaction Title & Status Badge
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = "REACTION ANALYSIS",
                        color = CyanNeon,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        letterSpacing = 1.2.sp
                    )
                    Text(
                        text = rxn.title,
                        color = TextPrimary,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
                IconButton(
                    onClick = { viewModel.saveExperimentToNotebook() },
                    modifier = Modifier.testTag("save_experiment_button")
                ) {
                    Icon(
                        imageVector = Icons.Default.BookmarkAdd,
                        contentDescription = "Save Experiment",
                        tint = EmeraldNeon
                    )
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            // Chemical Equation Banner (LaTeX / Code Style)
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(12.dp))
                    .background(theme.surfaceElevated)
                    .border(1.dp, theme.border, RoundedCornerShape(12.dp))
                    .padding(horizontal = 14.dp, vertical = 10.dp)
            ) {
                Text(
                    text = rxn.balancedEquation,
                    color = CyanGlow,
                    fontFamily = FontFamily.Monospace,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.SemiBold
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Thermodynamic & Metric Chips Row
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                MetricChip(
                    icon = Icons.Default.LocalFireDepartment,
                    label = "Enthalpy ΔH",
                    value = rxn.deltaH,
                    accentColor = AmberExplosion,
                    modifier = Modifier.weight(1.4f)
                )
                MetricChip(
                    icon = Icons.Default.Thermostat,
                    label = "Peak Temp",
                    value = "${temperature}°C",
                    accentColor = if (temperature > 60) RedReaction else EmeraldNeon,
                    modifier = Modifier.weight(1f)
                )
                MetricChip(
                    icon = Icons.Default.Science,
                    label = "pH Level",
                    value = "$ph",
                    accentColor = if (ph > 11) BasicBlue else if (ph < 4) AcidicRed else NeutralGreen,
                    modifier = Modifier.weight(0.9f)
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Step Indicator Row (Step 1 of 4)
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = "STEP ${activeStepIndex + 1} OF ${rxn.steps.size}",
                    color = TextSecondary,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.SemiBold
                )

                // Segmented Progress Dots
                Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                    rxn.steps.indices.forEach { index ->
                        Box(
                            modifier = Modifier
                                .size(if (index == activeStepIndex) 20.dp else 8.dp, 8.dp)
                                .clip(CircleShape)
                                .background(
                                    if (index == activeStepIndex) CyanNeon
                                    else if (index < activeStepIndex) EmeraldNeon
                                    else BorderSubtle
                                )
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            // Animated Step Content Card
            AnimatedContent(
                targetState = currentStep,
                transitionSpec = { fadeIn() togetherWith fadeOut() },
                label = "step_content"
            ) { step ->
                Card(
                    colors = CardDefaults.cardColors(containerColor = CardSurface),
                    shape = RoundedCornerShape(16.dp),
                    border = CardDefaults.outlinedCardBorder().copy(
                        brush = Brush.horizontalGradient(listOf(CyanNeon.copy(alpha = 0.3f), BorderSubtle))
                    ),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text(
                            text = step.title,
                            color = TextPrimary,
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = step.subtitle,
                            color = CyanNeon,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Medium
                        )

                        Spacer(modifier = Modifier.height(8.dp))

                        Text(
                            text = step.explanation,
                            color = TextSecondary,
                            fontSize = 13.sp,
                            lineHeight = 19.sp
                        )

                        Spacer(modifier = Modifier.height(12.dp))

                        // Thermodynamic Detail Pill
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(8.dp))
                                .background(DeepSurface)
                                .padding(horizontal = 10.dp, vertical = 8.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.Bolt,
                                contentDescription = null,
                                tint = AmberExplosion,
                                modifier = Modifier.size(16.dp)
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = step.thermodynamicDetail,
                                color = TextPrimary,
                                fontSize = 11.sp,
                                fontFamily = FontFamily.Monospace
                            )
                        }

                        Spacer(modifier = Modifier.height(8.dp))

                        // Safety / Industrial Note Pill
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(8.dp))
                                .background(RedReaction.copy(alpha = 0.1f))
                                .border(1.dp, RedReaction.copy(alpha = 0.3f), RoundedCornerShape(8.dp))
                                .padding(horizontal = 10.dp, vertical = 8.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.WarningAmber,
                                contentDescription = null,
                                tint = RedReaction,
                                modifier = Modifier.size(16.dp)
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = step.safetyNote,
                                color = RedReaction.copy(alpha = 0.9f),
                                fontSize = 11.sp
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Navigation Controls & Ask AI
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                OutlinedButton(
                    onClick = { viewModel.prevStep() },
                    enabled = activeStepIndex > 0,
                    colors = ButtonDefaults.outlinedButtonColors(contentColor = TextPrimary),
                    modifier = Modifier.weight(1f)
                ) {
                    Icon(imageVector = Icons.Default.ArrowBack, contentDescription = null, modifier = Modifier.size(16.dp))
                    Spacer(modifier = Modifier.width(4.dp))
                    Text("Prev", fontSize = 12.sp)
                }

                Button(
                    onClick = { viewModel.nextStep() },
                    enabled = activeStepIndex < rxn.steps.size - 1,
                    colors = ButtonDefaults.buttonColors(
                        containerColor = CyanNeon,
                        contentColor = if (theme.isDark) MidnightBg else Color.White
                    ),
                    modifier = Modifier.weight(1f)
                ) {
                    Text("Next Step", fontSize = 12.sp, fontWeight = FontWeight.Bold)
                    Spacer(modifier = Modifier.width(4.dp))
                    Icon(imageVector = Icons.Default.ArrowForward, contentDescription = null, modifier = Modifier.size(16.dp))
                }

                FilledTonalButton(
                    onClick = {
                        onNavigateToAiMentor?.invoke("Explain the thermodynamics and molecular orbital transition of ${rxn.balancedEquation}")
                    },
                    colors = ButtonDefaults.filledTonalButtonColors(
                        containerColor = VioletMath.copy(alpha = 0.25f),
                        contentColor = VioletMath
                    ),
                    modifier = Modifier.testTag("ask_ai_mentor_reaction_button")
                ) {
                    Icon(imageVector = Icons.Default.AutoAwesome, contentDescription = null, modifier = Modifier.size(16.dp))
                    Spacer(modifier = Modifier.width(4.dp))
                    Text("Ask AI", fontSize = 12.sp, fontWeight = FontWeight.SemiBold)
                }
            }
        }
    }
}

@Composable
fun MetricChip(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    label: String,
    value: String,
    accentColor: Color,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .clip(RoundedCornerShape(10.dp))
            .background(CardSurface)
            .border(1.dp, BorderSubtle, RoundedCornerShape(10.dp))
            .padding(horizontal = 8.dp, vertical = 6.dp)
    ) {
        Column {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    imageVector = icon,
                    contentDescription = null,
                    tint = accentColor,
                    modifier = Modifier.size(12.dp)
                )
                Spacer(modifier = Modifier.width(4.dp))
                Text(text = label, color = TextMuted, fontSize = 9.sp, fontWeight = FontWeight.Medium)
            }
            Text(
                text = value,
                color = TextPrimary,
                fontSize = 11.sp,
                fontWeight = FontWeight.Bold,
                maxLines = 1
            )
        }
    }
}
