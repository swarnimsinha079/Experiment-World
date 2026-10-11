package com.example.ui.screens.progress

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
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
import com.example.data.local.ApparatusBadgeEntity
import com.example.ui.theme.*
import com.example.viewmodel.ProgressViewModel
import java.text.SimpleDateFormat
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ResearchProgressSheet(
    viewModel: ProgressViewModel,
    modifier: Modifier = Modifier
) {
    val theme = VirtuLabTheme.colors
    val progress by viewModel.progress.collectAsState()
    val badges by viewModel.badges.collectAsState()

    val nextThreshold = when (progress.levelNumber) {
        1 -> 100
        2 -> 250
        3 -> 500
        4 -> 1000
        else -> 1000
    }

    val prevThreshold = when (progress.levelNumber) {
        1 -> 0
        2 -> 100
        3 -> 250
        4 -> 500
        else -> 1000
    }

    val progressFraction = if (progress.levelNumber >= 5) 1.0f else {
        ((progress.researchPoints - prevThreshold).toFloat() / (nextThreshold - prevThreshold).toFloat())
            .coerceIn(0f, 1f)
    }

    ModalBottomSheet(
        onDismissRequest = { viewModel.setProgressModalOpen(false) },
        containerColor = theme.surface,
        contentColor = theme.textPrimary,
        scrimColor = Color.Black.copy(alpha = 0.7f),
        dragHandle = {
            BottomSheetDefaults.DragHandle(
                color = theme.border
            )
        },
        modifier = modifier.testTag("research_progress_sheet")
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight(0.85f)
                .padding(horizontal = 16.dp)
                .padding(bottom = 16.dp)
        ) {
            // Header
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Default.Bolt,
                            contentDescription = null,
                            tint = AmberExplosion,
                            modifier = Modifier.size(20.dp)
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = "RESEARCH PROGRESS & APPARATUS UNLOCKS",
                            color = AmberExplosion,
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            letterSpacing = 1.1.sp
                        )
                    }
                    Text(
                        text = "Earn RP by synthesizing compounds & reading articles",
                        color = theme.textSecondary,
                        fontSize = 12.sp
                    )
                }

                IconButton(onClick = { viewModel.setProgressModalOpen(false) }) {
                    Icon(imageVector = Icons.Default.Close, contentDescription = "Close", tint = theme.textSecondary)
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Main Level & RP Progress Card
            Card(
                colors = CardDefaults.cardColors(containerColor = theme.cardSurface),
                shape = RoundedCornerShape(16.dp),
                border = CardDefaults.outlinedCardBorder().copy(
                    brush = Brush.horizontalGradient(listOf(theme.primaryAccent, theme.tertiaryAccent))
                ),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column {
                            Text(
                                text = "LEVEL ${progress.levelNumber}",
                                color = theme.primaryAccent,
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold,
                                letterSpacing = 1.sp
                            )
                            Text(
                                text = progress.rankTitle,
                                color = theme.textPrimary,
                                fontSize = 18.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }

                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(20.dp))
                                .background(theme.surfaceElevated)
                                .border(1.dp, AmberExplosion.copy(alpha = 0.5f), RoundedCornerShape(20.dp))
                                .padding(horizontal = 12.dp, vertical = 6.dp)
                        ) {
                            Text(
                                text = "${progress.researchPoints} RP",
                                color = AmberExplosion,
                                fontFamily = FontFamily.Monospace,
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    // Progress Bar towards Next Rank
                    LinearProgressIndicator(
                        progress = { progressFraction },
                        color = theme.primaryAccent,
                        trackColor = theme.border,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(8.dp)
                            .clip(RoundedCornerShape(4.dp))
                    )

                    Spacer(modifier = Modifier.height(6.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(
                            text = if (progress.levelNumber >= 5) "Max Rank Achieved" else "Progress to Level ${progress.levelNumber + 1}",
                            color = theme.textMuted,
                            fontSize = 10.sp
                        )
                        Text(
                            text = if (progress.levelNumber >= 5) "Master Tier" else "${progress.researchPoints} / $nextThreshold RP",
                            color = theme.textSecondary,
                            fontFamily = FontFamily.Monospace,
                            fontSize = 10.sp
                        )
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    // Statistics Row
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(10.dp))
                            .background(theme.surfaceElevated)
                            .padding(horizontal = 12.dp, vertical = 8.dp),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        StatItem(label = "Experiments", count = progress.experimentsCompleted)
                        StatItem(label = "Research Reads", count = progress.articlesRead)
                        StatItem(label = "Math Proofs", count = progress.mathProofsCompleted)
                    }
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            Text(
                text = "APPARATUS BADGE UNLOCKS",
                color = theme.textSecondary,
                fontSize = 11.sp,
                fontWeight = FontWeight.Bold,
                letterSpacing = 1.sp
            )

            Spacer(modifier = Modifier.height(8.dp))

            // Badges List
            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth()
                    .testTag("apparatus_badges_list")
            ) {
                items(badges) { badge ->
                    ApparatusBadgeCard(
                        badge = badge,
                        userRP = progress.researchPoints
                    )
                }
            }
        }
    }
}

@Composable
fun ApparatusBadgeCard(
    badge: ApparatusBadgeEntity,
    userRP: Int
) {
    val theme = VirtuLabTheme.colors
    val isUnlocked = badge.isUnlocked
    val dateFormat = SimpleDateFormat("MMM dd, yyyy", Locale.getDefault())

    Card(
        colors = CardDefaults.cardColors(
            containerColor = if (isUnlocked) theme.surfaceElevated else theme.cardSurface
        ),
        shape = RoundedCornerShape(12.dp),
        border = CardDefaults.outlinedCardBorder().copy(
            brush = Brush.horizontalGradient(
                if (isUnlocked) listOf(EmeraldNeon.copy(alpha = 0.6f), theme.primaryAccent.copy(alpha = 0.4f))
                else listOf(theme.border, theme.border)
            )
        ),
        modifier = Modifier
            .fillMaxWidth()
            .testTag("apparatus_badge_${badge.badgeId}")
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Badge Avatar
            Box(
                modifier = Modifier
                    .size(46.dp)
                    .clip(CircleShape)
                    .background(
                        if (isUnlocked) EmeraldNeon.copy(alpha = 0.2f) else theme.surfaceElevated
                    )
                    .border(
                        1.5.dp,
                        if (isUnlocked) EmeraldNeon else theme.border,
                        CircleShape
                    ),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = if (isUnlocked) Icons.Default.CheckCircle else Icons.Default.Lock,
                    contentDescription = null,
                    tint = if (isUnlocked) EmeraldNeon else theme.textMuted,
                    modifier = Modifier.size(22.dp)
                )
            }

            Spacer(modifier = Modifier.width(12.dp))

            Column(modifier = Modifier.weight(1f)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = badge.apparatusName,
                        color = theme.textPrimary,
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Bold
                    )

                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(6.dp))
                            .background(
                                if (isUnlocked) EmeraldNeon.copy(alpha = 0.15f)
                                else theme.border.copy(alpha = 0.3f)
                            )
                            .padding(horizontal = 6.dp, vertical = 2.dp)
                    ) {
                        Text(
                            text = if (isUnlocked) "UNLOCKED" else "${badge.requiredRP} RP",
                            color = if (isUnlocked) EmeraldNeon else theme.textSecondary,
                            fontFamily = FontFamily.Monospace,
                            fontSize = 9.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }

                Text(
                    text = badge.title,
                    color = if (isUnlocked) theme.primaryAccent else theme.textSecondary,
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Medium
                )

                Text(
                    text = badge.description,
                    color = theme.textMuted,
                    fontSize = 10.sp,
                    lineHeight = 14.sp
                )

                if (isUnlocked && badge.unlockedAt > 0) {
                    Text(
                        text = "Unlocked: ${dateFormat.format(Date(badge.unlockedAt))}",
                        color = EmeraldNeon.copy(alpha = 0.8f),
                        fontSize = 9.sp
                    )
                } else if (!isUnlocked) {
                    val remaining = badge.requiredRP - userRP
                    Text(
                        text = "Need $remaining more RP to unlock",
                        color = AmberExplosion,
                        fontSize = 9.sp
                    )
                }
            }
        }
    }
}

@Composable
fun StatItem(label: String, count: Int) {
    val theme = VirtuLabTheme.colors
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(text = "$count", color = theme.primaryAccent, fontSize = 14.sp, fontWeight = FontWeight.Bold, fontFamily = FontFamily.Monospace)
        Text(text = label, color = theme.textMuted, fontSize = 9.sp)
    }
}
