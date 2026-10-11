package com.example.ui.screens.math

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.MathCategory
import com.example.data.model.MathTopic
import com.example.ui.theme.*
import com.example.viewmodel.MathViewModel
import kotlinx.coroutines.flow.collectLatest

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MathZoneScreen(
    viewModel: MathViewModel,
    onOpenSettings: (() -> Unit)? = null,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val theme = VirtuLabTheme.colors
    val selectedTopic by viewModel.selectedTopic.collectAsState()
    val searchQuery by viewModel.searchQuery.collectAsState()
    val selectedCategory by viewModel.selectedCategory.collectAsState()

    var showProofDetails by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        viewModel.toastFlow.collectLatest { msg ->
            Toast.makeText(context, msg, Toast.LENGTH_SHORT).show()
        }
    }

    val filteredTopics = remember(searchQuery, selectedCategory) {
        viewModel.allTopics.filter { topic ->
            val matchesCategory = selectedCategory == null || topic.category == selectedCategory
            val matchesSearch = searchQuery.isBlank() ||
                    topic.title.contains(searchQuery, ignoreCase = true) ||
                    topic.formula.contains(searchQuery, ignoreCase = true) ||
                    topic.summary.contains(searchQuery, ignoreCase = true)
            matchesCategory && matchesSearch
        }
    }

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .background(theme.background)
            .statusBarsPadding()
            .padding(horizontal = 14.dp)
            .testTag("math_zone_screen")
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
                        text = "MATHS INTERACTIVE ZONE",
                        color = theme.tertiaryAccent,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        letterSpacing = 1.2.sp
                    )
                    Text(
                        text = "Concept Vectorization",
                        color = theme.textPrimary,
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold
                    )
                }

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    if (onOpenSettings != null) {
                        IconButton(
                            onClick = onOpenSettings,
                            modifier = Modifier
                                .size(38.dp)
                                .clip(CircleShape)
                                .background(theme.cardSurface)
                                .border(1.dp, theme.border, CircleShape)
                                .testTag("math_settings_button")
                        ) {
                            Icon(
                                imageVector = Icons.Default.Settings,
                                contentDescription = "Settings",
                                tint = theme.primaryAccent,
                                modifier = Modifier.size(20.dp)
                            )
                        }
                    }

                    Icon(
                        imageVector = Icons.Default.SquareFoot,
                        contentDescription = null,
                        tint = theme.primaryAccent,
                        modifier = Modifier.size(28.dp)
                    )
                }
            }
        }

        item {
            // Smart Search Interface
            OutlinedTextField(
                value = searchQuery,
                onValueChange = { viewModel.setSearchQuery(it) },
                placeholder = { Text("Search theories (e.g. Algebraic, Pythagoras, Parabola)...", fontSize = 12.sp, color = theme.textMuted) },
                leadingIcon = {
                    Icon(imageVector = Icons.Default.Search, contentDescription = "Search", tint = theme.primaryAccent)
                },
                trailingIcon = {
                    if (searchQuery.isNotEmpty()) {
                        IconButton(onClick = { viewModel.setSearchQuery("") }) {
                            Icon(imageVector = Icons.Default.Clear, contentDescription = "Clear", tint = theme.textSecondary)
                        }
                    }
                },
                singleLine = true,
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = theme.primaryAccent,
                    unfocusedBorderColor = theme.border,
                    focusedContainerColor = theme.surface,
                    unfocusedContainerColor = theme.surface,
                    focusedTextColor = theme.textPrimary,
                    unfocusedTextColor = theme.textPrimary
                ),
                shape = RoundedCornerShape(14.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("math_search_input")
            )

            Spacer(modifier = Modifier.height(10.dp))

            // Category Chips
            LazyRow(
                horizontalArrangement = Arrangement.spacedBy(6.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                item {
                    FilterChip(
                        selected = selectedCategory == null,
                        onClick = { viewModel.selectCategory(null) },
                        label = { Text("All", fontSize = 11.sp) },
                        colors = FilterChipDefaults.filterChipColors(
                            selectedContainerColor = theme.tertiaryAccent.copy(alpha = 0.2f),
                            selectedLabelColor = theme.tertiaryAccent,
                            labelColor = theme.textSecondary
                        )
                    )
                }
                items(MathCategory.values()) { cat ->
                    FilterChip(
                        selected = selectedCategory == cat,
                        onClick = {
                            viewModel.selectCategory(if (selectedCategory == cat) null else cat)
                        },
                        label = { Text(cat.name, fontSize = 11.sp) },
                        colors = FilterChipDefaults.filterChipColors(
                            selectedContainerColor = theme.tertiaryAccent.copy(alpha = 0.2f),
                            selectedLabelColor = theme.tertiaryAccent,
                            labelColor = theme.textSecondary
                        )
                    )
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Topic Selector Horizontal Cards
            LazyRow(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("math_topics_carousel")
            ) {
                items(filteredTopics) { topic ->
                    val isSelected = topic.id == selectedTopic.id
                    Card(
                        colors = CardDefaults.cardColors(
                            containerColor = if (isSelected) theme.surfaceElevated else theme.cardSurface
                        ),
                        shape = RoundedCornerShape(12.dp),
                        border = CardDefaults.outlinedCardBorder().copy(
                            brush = Brush.horizontalGradient(
                                if (isSelected) listOf(theme.primaryAccent, theme.tertiaryAccent)
                                else listOf(theme.border, theme.border)
                            )
                        ),
                        modifier = Modifier
                            .width(170.dp)
                            .clickable { viewModel.selectTopic(topic) }
                            .testTag("math_topic_card_${topic.id}")
                    ) {
                        Column(modifier = Modifier.padding(10.dp)) {
                            Text(
                                text = topic.category.name,
                                color = if (isSelected) theme.primaryAccent else theme.textMuted,
                                fontSize = 9.sp,
                                fontWeight = FontWeight.Bold
                            )
                            Text(
                                text = topic.title,
                                color = theme.textPrimary,
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Bold,
                                maxLines = 1
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = topic.formula,
                                color = theme.secondaryAccent,
                                fontFamily = FontFamily.Monospace,
                                fontSize = 10.sp,
                                maxLines = 1
                            )
                        }
                    }
                }
            }
        }

        item {
            Spacer(modifier = Modifier.height(12.dp))

            // Dynamic Visualizer Selection
            when (selectedTopic.id) {
                "algebraic_identity" -> AlgebraicIdentityVisualizer(viewModel = viewModel)
                "pythagorean_theorem" -> PythagorasVisualizer(viewModel = viewModel)
                "quadratic_equation" -> QuadraticVisualizer(viewModel = viewModel)
                "unit_circle_trig" -> UnitCircleVisualizer(viewModel = viewModel)
                else -> AlgebraicIdentityVisualizer(viewModel = viewModel)
            }
        }

        item {
            Spacer(modifier = Modifier.height(14.dp))

            // Expandable Detailed Mathematical Proof Card
            Card(
                colors = CardDefaults.cardColors(containerColor = theme.surface),
                shape = RoundedCornerShape(14.dp),
                border = CardDefaults.outlinedCardBorder().copy(brush = Brush.horizontalGradient(listOf(theme.border, theme.border))),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 24.dp)
            ) {
                Column(modifier = Modifier.padding(14.dp)) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { showProofDetails = !showProofDetails },
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = Icons.Default.MenuBook,
                                contentDescription = null,
                                tint = theme.primaryAccent,
                                modifier = Modifier.size(18.dp)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = "Theoretical Derivation & Proof",
                                color = theme.textPrimary,
                                fontSize = 13.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                        Icon(
                            imageVector = if (showProofDetails) Icons.Default.ExpandLess else Icons.Default.ExpandMore,
                            contentDescription = null,
                            tint = theme.textSecondary
                        )
                    }

                    if (showProofDetails) {
                        Spacer(modifier = Modifier.height(10.dp))
                        Text(
                            text = selectedTopic.summary,
                            color = theme.primaryAccent,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Medium
                        )
                        Spacer(modifier = Modifier.height(6.dp))
                        Text(
                            text = selectedTopic.detailedProof,
                            color = theme.textSecondary,
                            fontSize = 12.sp,
                            lineHeight = 18.sp
                        )
                    }
                }
            }
        }
    }
}
