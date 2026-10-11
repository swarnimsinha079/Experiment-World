package com.example.ui.screens.notebook

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
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.local.LabRecordEntity
import com.example.ui.theme.*
import com.example.viewmodel.NotebookViewModel
import java.text.SimpleDateFormat
import java.util.*

@Composable
fun NotebookScreen(
    viewModel: NotebookViewModel,
    onOpenSettings: (() -> Unit)? = null,
    modifier: Modifier = Modifier
) {
    val theme = VirtuLabTheme.colors
    val records by viewModel.records.collectAsState()
    val dateFormat = SimpleDateFormat("MMM dd, yyyy HH:mm", Locale.getDefault())

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .background(theme.background)
            .statusBarsPadding()
            .padding(horizontal = 14.dp)
            .testTag("notebook_screen")
    ) {
        item {
            // Header
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 12.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Column {
                    Text(
                        text = "LAB NOTEBOOK",
                        color = theme.secondaryAccent,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        letterSpacing = 1.2.sp
                    )
                    Text(
                        text = "Experiment Journal & Proofs",
                        color = theme.textPrimary,
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold
                    )
                }

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    if (records.isNotEmpty()) {
                        IconButton(
                            onClick = { viewModel.clearAll() },
                            modifier = Modifier.testTag("clear_notebook_button")
                        ) {
                            Icon(
                                imageVector = Icons.Default.DeleteSweep,
                                contentDescription = "Clear All Records",
                                tint = RedReaction
                            )
                        }
                    }

                    if (onOpenSettings != null) {
                        IconButton(
                            onClick = onOpenSettings,
                            modifier = Modifier
                                .size(38.dp)
                                .clip(CircleShape)
                                .background(theme.cardSurface)
                                .border(1.dp, theme.border, CircleShape)
                                .testTag("notebook_settings_button")
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
            }
        }

        // Standard Lab Safety Protocol Banner
        item {
            Card(
                colors = CardDefaults.cardColors(containerColor = theme.cardSurface),
                shape = RoundedCornerShape(14.dp),
                border = CardDefaults.outlinedCardBorder().copy(
                    brush = Brush.horizontalGradient(listOf(AmberExplosion.copy(alpha = 0.5f), theme.border))
                ),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 12.dp)
            ) {
                Row(
                    modifier = Modifier.padding(12.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Default.Security,
                        contentDescription = null,
                        tint = AmberExplosion,
                        modifier = Modifier.size(24.dp)
                    )
                    Spacer(modifier = Modifier.width(10.dp))
                    Column {
                        Text(
                            text = "Standard Operating Safety (GLP)",
                            color = theme.textPrimary,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = "Wear splash-resistant eye protection. Neutralize caustic effluents. Never pour water into concentrated acid.",
                            color = theme.textSecondary,
                            fontSize = 11.sp
                        )
                    }
                }
            }
        }

        if (records.isEmpty()) {
            item {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 40.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Icon(
                            imageVector = Icons.Default.MenuBook,
                            contentDescription = null,
                            tint = theme.textMuted,
                            modifier = Modifier.size(48.dp)
                        )
                        Spacer(modifier = Modifier.height(10.dp))
                        Text(
                            text = "No saved lab records yet",
                            color = theme.textSecondary,
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Medium
                        )
                        Text(
                            text = "Perform a chemical reaction or explore a mathematical proof to save entries here.",
                            color = theme.textMuted,
                            fontSize = 11.sp
                        )
                    }
                }
            }
        } else {
            items(records) { record ->
                RecordItemCard(
                    record = record,
                    dateFormatted = dateFormat.format(Date(record.timestamp)),
                    onDelete = { viewModel.deleteRecord(record) }
                )
                Spacer(modifier = Modifier.height(10.dp))
            }
        }
    }
}

@Composable
fun RecordItemCard(
    record: LabRecordEntity,
    dateFormatted: String,
    onDelete: () -> Unit
) {
    val theme = VirtuLabTheme.colors
    val isChemistry = record.experimentType == "CHEMISTRY"

    Card(
        colors = CardDefaults.cardColors(containerColor = theme.cardSurface),
        shape = RoundedCornerShape(14.dp),
        border = CardDefaults.outlinedCardBorder().copy(
            brush = Brush.horizontalGradient(
                if (isChemistry) listOf(theme.primaryAccent.copy(alpha = 0.4f), theme.border)
                else listOf(theme.tertiaryAccent.copy(alpha = 0.4f), theme.border)
            )
        ),
        modifier = Modifier
            .fillMaxWidth()
            .testTag("record_card_${record.id}")
    ) {
        Column(modifier = Modifier.padding(14.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = if (isChemistry) Icons.Default.Science else Icons.Default.SquareFoot,
                        contentDescription = null,
                        tint = if (isChemistry) theme.primaryAccent else theme.tertiaryAccent,
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = record.title,
                        color = theme.textPrimary,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold
                    )
                }

                IconButton(
                    onClick = onDelete,
                    modifier = Modifier.size(24.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.DeleteOutline,
                        contentDescription = "Delete",
                        tint = theme.textMuted,
                        modifier = Modifier.size(16.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(4.dp))

            Text(
                text = dateFormatted,
                color = theme.textMuted,
                fontSize = 10.sp
            )

            Spacer(modifier = Modifier.height(8.dp))

            // Equation / Formula pill
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(8.dp))
                    .background(theme.surfaceElevated)
                    .padding(horizontal = 10.dp, vertical = 6.dp)
            ) {
                Text(
                    text = record.equationOrFormula,
                    color = if (isChemistry) theme.primaryAccent else theme.secondaryAccent,
                    fontFamily = FontFamily.Monospace,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.SemiBold
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = record.observations,
                color = theme.textSecondary,
                fontSize = 11.sp,
                lineHeight = 16.sp
            )

            if (isChemistry && record.finalTemperatureC > 0) {
                Spacer(modifier = Modifier.height(8.dp))
                Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                    Text(
                        text = "Peak Temp: ${record.finalTemperatureC}°C",
                        color = AmberExplosion,
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = "Final pH: ${record.finalPh}",
                        color = if (record.finalPh > 10) BasicBlue else NeutralGreen,
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }
    }
}
