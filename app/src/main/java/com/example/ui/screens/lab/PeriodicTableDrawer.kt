package com.example.ui.screens.lab

import androidx.compose.animation.*
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
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
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.Reagent
import com.example.data.model.ReagentCategory
import com.example.data.repository.LabRepository
import com.example.ui.theme.*
import com.example.viewmodel.LabViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PeriodicTableDrawer(
    viewModel: LabViewModel,
    onNavigateToAiMentor: ((String) -> Unit)? = null,
    modifier: Modifier = Modifier
) {
    val reagentsInBeaker by viewModel.reagentsInBeaker.collectAsState()
    var searchQuery by remember { mutableStateOf("") }
    var selectedCategoryFilter by remember { mutableStateOf<ReagentCategory?>(null) }
    var inspectedElement by remember {
        mutableStateOf<Reagent?>(
            LabRepository.periodicElements.find { it.id == "Na" }
        )
    }
    var showDeepResearchDialog by remember { mutableStateOf(false) }

    val allElements = LabRepository.periodicElements

    val filteredElements = remember(searchQuery, selectedCategoryFilter) {
        allElements.filter { elem ->
            val matchesCat = selectedCategoryFilter == null || elem.category == selectedCategoryFilter
            val matchesSearch = searchQuery.isBlank() ||
                    elem.name.contains(searchQuery, ignoreCase = true) ||
                    elem.formula.contains(searchQuery, ignoreCase = true) ||
                    elem.atomicNumber.toString() == searchQuery.trim()
            matchesCat && matchesSearch
        }
    }

    val theme = VirtuLabTheme.colors

    ModalBottomSheet(
        onDismissRequest = { viewModel.setPeriodicTableOpen(false) },
        containerColor = theme.surface,
        contentColor = theme.textPrimary,
        scrimColor = Color.Black.copy(alpha = 0.7f),
        dragHandle = {
            BottomSheetDefaults.DragHandle(
                color = theme.border
            )
        },
        modifier = modifier.testTag("periodic_table_drawer")
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight(0.88f)
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
                            imageVector = Icons.Default.GridOn,
                            contentDescription = null,
                            tint = CyanNeon,
                            modifier = Modifier.size(20.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "PERIODIC TABLE OF ELEMENTS",
                            color = CyanNeon,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold,
                            letterSpacing = 1.2.sp
                        )
                    }
                    Text(
                        text = "Pick elements to trigger reactions in the 3D lab",
                        color = TextSecondary,
                        fontSize = 12.sp
                    )
                }

                IconButton(
                    onClick = { viewModel.setPeriodicTableOpen(false) },
                    modifier = Modifier.testTag("close_periodic_table_button")
                ) {
                    Icon(
                        imageVector = Icons.Default.Close,
                        contentDescription = "Close",
                        tint = TextSecondary
                    )
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            // Quick Reaction Presets (e.g. Sodium + Chlorine)
            Text(
                text = "SUGGESTED COMBINATIONS",
                color = TextMuted,
                fontSize = 10.sp,
                fontWeight = FontWeight.Bold,
                letterSpacing = 1.sp
            )
            Spacer(modifier = Modifier.height(4.dp))
            LazyRow(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                item {
                    PresetReactionChip(
                        title = "Na + Cl → Table Salt (NaCl)",
                        accentColor = AmberExplosion,
                        onClick = {
                            val na = allElements.firstOrNull { it.id == "Na" }
                            val cl = allElements.firstOrNull { it.id == "Cl" }
                            if (na != null) viewModel.addReagent(na)
                            if (cl != null) viewModel.addReagent(cl)
                            viewModel.setPeriodicTableOpen(false)
                        }
                    )
                }
                item {
                    PresetReactionChip(
                        title = "2Na + 2H₂O → 2NaOH + H₂ ↑",
                        accentColor = CyanNeon,
                        onClick = {
                            val na = allElements.firstOrNull { it.id == "Na" }
                            val water = LabRepository.laboratorySolutions.firstOrNull { it.id == "H2O" }
                            if (na != null) viewModel.addReagent(na)
                            if (water != null) viewModel.addReagent(water)
                            viewModel.setPeriodicTableOpen(false)
                        }
                    )
                }
                item {
                    PresetReactionChip(
                        title = "2Mg + O₂ → 2MgO (UV Flash)",
                        accentColor = EmeraldNeon,
                        onClick = {
                            val mg = allElements.firstOrNull { it.id == "Mg" }
                            val o = allElements.firstOrNull { it.id == "O" }
                            if (mg != null) viewModel.addReagent(mg)
                            if (o != null) viewModel.addReagent(o)
                            viewModel.setPeriodicTableOpen(false)
                        }
                    )
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            // Search Bar & Filter Row
            OutlinedTextField(
                value = searchQuery,
                onValueChange = { searchQuery = it },
                placeholder = { Text("Search by element (e.g., Sodium, Cl, 11)...", fontSize = 12.sp) },
                leadingIcon = {
                    Icon(imageVector = Icons.Default.Search, contentDescription = null, tint = CyanNeon, modifier = Modifier.size(18.dp))
                },
                trailingIcon = {
                    if (searchQuery.isNotEmpty()) {
                        IconButton(onClick = { searchQuery = "" }) {
                            Icon(imageVector = Icons.Default.Clear, contentDescription = "Clear", tint = TextSecondary, modifier = Modifier.size(16.dp))
                        }
                    }
                },
                singleLine = true,
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = CyanNeon,
                    unfocusedBorderColor = BorderSubtle,
                    focusedContainerColor = CardSurface,
                    unfocusedContainerColor = CardSurface,
                    focusedTextColor = TextPrimary,
                    unfocusedTextColor = TextPrimary
                ),
                shape = RoundedCornerShape(12.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(48.dp)
                    .testTag("periodic_search_input")
            )

            Spacer(modifier = Modifier.height(8.dp))

            // Category Filter Chips
            LazyRow(
                horizontalArrangement = Arrangement.spacedBy(6.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                item {
                    FilterChip(
                        selected = selectedCategoryFilter == null,
                        onClick = { selectedCategoryFilter = null },
                        label = { Text("All", fontSize = 10.sp) },
                        colors = FilterChipDefaults.filterChipColors(
                            selectedContainerColor = CyanNeon.copy(alpha = 0.25f),
                            selectedLabelColor = CyanNeon
                        )
                    )
                }
                val categories = listOf(
                    ReagentCategory.ALKALI_METAL to "Alkali Metals",
                    ReagentCategory.ALKALINE_EARTH to "Alkaline Earth",
                    ReagentCategory.TRANSITION_METAL to "Transition",
                    ReagentCategory.HALOGEN to "Halogens",
                    ReagentCategory.NON_METAL to "Non-metals",
                    ReagentCategory.NOBLE_GAS to "Noble Gases"
                )
                items(categories) { (cat, label) ->
                    FilterChip(
                        selected = selectedCategoryFilter == cat,
                        onClick = {
                            selectedCategoryFilter = if (selectedCategoryFilter == cat) null else cat
                        },
                        label = { Text(label, fontSize = 10.sp) },
                        colors = FilterChipDefaults.filterChipColors(
                            selectedContainerColor = CyanNeon.copy(alpha = 0.25f),
                            selectedLabelColor = CyanNeon
                        )
                    )
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            // Detailed Inspector Card for Currently Inspected Element
            inspectedElement?.let { elem ->
                val inBeaker = reagentsInBeaker.any { it.id == elem.id }
                Card(
                    colors = CardDefaults.cardColors(containerColor = CardSurface),
                    shape = RoundedCornerShape(14.dp),
                    border = CardDefaults.outlinedCardBorder().copy(
                        brush = Brush.horizontalGradient(listOf(getCategoryColor(elem.category), BorderSubtle))
                    ),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 10.dp)
                        .testTag("element_inspector_card")
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(12.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        // Atomic Symbol Tile
                        Box(
                            modifier = Modifier
                                .size(52.dp)
                                .clip(RoundedCornerShape(10.dp))
                                .background(theme.surfaceElevated)
                                .border(1.5.dp, getCategoryColor(elem.category), RoundedCornerShape(10.dp)),
                            contentAlignment = Alignment.Center
                        ) {
                            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                Text(
                                    text = "${elem.atomicNumber}",
                                    color = TextMuted,
                                    fontSize = 9.sp,
                                    fontFamily = FontFamily.Monospace
                                )
                                Text(
                                    text = elem.formula,
                                    color = TextPrimary,
                                    fontFamily = FontFamily.Monospace,
                                    fontSize = 18.sp,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                        }

                        Spacer(modifier = Modifier.width(12.dp))

                        Column(modifier = Modifier.weight(1f)) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Text(
                                    text = elem.name,
                                    color = TextPrimary,
                                    fontSize = 15.sp,
                                    fontWeight = FontWeight.Bold
                                )
                                Spacer(modifier = Modifier.width(6.dp))
                                Text(
                                    text = elem.category.name.replace("_", " "),
                                    color = getCategoryColor(elem.category),
                                    fontSize = 10.sp,
                                    fontWeight = FontWeight.SemiBold
                                )
                            }
                            Text(
                                text = "Mass: ${elem.atomicWeight} u  |  ${elem.electronConfig}",
                                color = TextSecondary,
                                fontFamily = FontFamily.Monospace,
                                fontSize = 11.sp
                            )
                            Text(
                                text = elem.description,
                                color = TextMuted,
                                fontSize = 10.sp,
                                maxLines = 2,
                                lineHeight = 13.sp
                            )
                        }

                        Spacer(modifier = Modifier.width(8.dp))

                        Column(
                            horizontalAlignment = Alignment.End,
                            verticalArrangement = Arrangement.spacedBy(6.dp)
                        ) {
                            Button(
                                onClick = {
                                    if (inBeaker) viewModel.removeReagent(elem)
                                    else viewModel.addReagent(elem)
                                },
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = if (inBeaker) RedReaction.copy(alpha = 0.25f) else CyanNeon,
                                    contentColor = if (inBeaker) RedReaction else (if (theme.isDark) MidnightBg else Color.White)
                                ),
                                contentPadding = PaddingValues(horizontal = 12.dp, vertical = 6.dp),
                                modifier = Modifier.testTag("inspector_add_button_${elem.id}")
                            ) {
                                Text(
                                    text = if (inBeaker) "Pour Out" else "Add",
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.Bold
                                )
                            }

                            FilledTonalButton(
                                onClick = { showDeepResearchDialog = true },
                                colors = ButtonDefaults.filledTonalButtonColors(
                                    containerColor = VioletMath.copy(alpha = 0.25f),
                                    contentColor = VioletMath
                                ),
                                contentPadding = PaddingValues(horizontal = 8.dp, vertical = 4.dp),
                                modifier = Modifier.testTag("level_up_deep_research_button")
                            ) {
                                Icon(imageVector = Icons.Default.Bolt, contentDescription = null, modifier = Modifier.size(12.dp))
                                Spacer(modifier = Modifier.width(3.dp))
                                Text("Level-Up", fontSize = 10.sp, fontWeight = FontWeight.Bold)
                            }
                        }
                    }
                }
            }

            // Grid of Element Tiles
            LazyVerticalGrid(
                columns = GridCells.Adaptive(minSize = 74.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth()
                    .testTag("periodic_elements_grid")
            ) {
                items(filteredElements) { element ->
                    val inBeaker = reagentsInBeaker.any { it.id == element.id }
                    val isSelected = inspectedElement?.id == element.id

                    PeriodicElementTile(
                        element = element,
                        isInBeaker = inBeaker,
                        isSelected = isSelected,
                        onClick = {
                            inspectedElement = element
                        },
                        onQuickAdd = {
                            if (inBeaker) viewModel.removeReagent(element)
                            else viewModel.addReagent(element)
                        }
                    )
                }
            }
        }

        if (showDeepResearchDialog && inspectedElement != null) {
            ElementDeepResearchDialog(
                element = inspectedElement!!,
                onDismiss = { showDeepResearchDialog = false },
                onAskAi = { prompt ->
                    showDeepResearchDialog = false
                    viewModel.setPeriodicTableOpen(false)
                    onNavigateToAiMentor?.invoke(prompt)
                }
            )
        }
    }
}

@Composable
fun PeriodicElementTile(
    element: Reagent,
    isInBeaker: Boolean,
    isSelected: Boolean,
    onClick: () -> Unit,
    onQuickAdd: () -> Unit
) {
    val accentColor = getCategoryColor(element.category)

    Card(
        colors = CardDefaults.cardColors(
            containerColor = if (isSelected) CardSurfaceElevated else CardSurface
        ),
        shape = RoundedCornerShape(10.dp),
        border = CardDefaults.outlinedCardBorder().copy(
            brush = Brush.verticalGradient(
                if (isInBeaker) listOf(EmeraldNeon, CyanNeon)
                else if (isSelected) listOf(accentColor, BorderSubtle)
                else listOf(BorderSubtle, BorderSubtle.copy(alpha = 0.3f))
            )
        ),
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
            .testTag("element_tile_${element.id}")
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(6.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Top row: Atomic number & In-beaker dot
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "${element.atomicNumber}",
                    color = TextMuted,
                    fontSize = 9.sp,
                    fontFamily = FontFamily.Monospace
                )
                if (isInBeaker) {
                    Box(
                        modifier = Modifier
                            .size(6.dp)
                            .clip(CircleShape)
                            .background(EmeraldNeon)
                    )
                }
            }

            // Big Element Symbol
            Text(
                text = element.formula,
                color = if (isInBeaker) EmeraldNeon else TextPrimary,
                fontFamily = FontFamily.Monospace,
                fontSize = 17.sp,
                fontWeight = FontWeight.Bold
            )

            // Element Name
            Text(
                text = element.name,
                color = TextSecondary,
                fontSize = 9.sp,
                fontWeight = FontWeight.Medium,
                maxLines = 1
            )

            // Atomic Mass
            Text(
                text = "${element.atomicWeight}",
                color = TextMuted,
                fontFamily = FontFamily.Monospace,
                fontSize = 8.sp,
                maxLines = 1
            )
        }
    }
}

@Composable
fun PresetReactionChip(
    title: String,
    accentColor: Color,
    onClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .clip(RoundedCornerShape(8.dp))
            .background(CardSurface)
            .border(1.dp, accentColor.copy(alpha = 0.4f), RoundedCornerShape(8.dp))
            .clickable { onClick() }
            .padding(horizontal = 10.dp, vertical = 6.dp)
            .testTag("preset_reaction_chip")
    ) {
        Text(
            text = title,
            color = accentColor,
            fontFamily = FontFamily.Monospace,
            fontSize = 11.sp,
            fontWeight = FontWeight.SemiBold
        )
    }
}

fun getCategoryColor(category: ReagentCategory): Color {
    return when (category) {
        ReagentCategory.ALKALI_METAL -> Color(0xFFF87171) // Red/Coral
        ReagentCategory.ALKALINE_EARTH -> Color(0xFFFB923C) // Orange
        ReagentCategory.TRANSITION_METAL -> Color(0xFF38BDF8) // Sky Blue
        ReagentCategory.HALOGEN -> Color(0xFFA3E635) // Lime Green
        ReagentCategory.NON_METAL -> Color(0xFF00E5FF) // Neon Cyan
        ReagentCategory.NOBLE_GAS -> Color(0xFFC084FC) // Purple
        ReagentCategory.ACID -> Color(0xFFFF5252)
        ReagentCategory.BASE -> Color(0xFF818CF8)
        ReagentCategory.SALT -> Color(0xFF2563EB)
        ReagentCategory.OXIDIZER -> Color(0xFF9333EA)
        ReagentCategory.INDICATOR -> Color(0xFFF43F5E)
        ReagentCategory.SOLVENT -> Color(0xFF00E676)
    }
}
