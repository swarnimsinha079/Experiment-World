package com.example.ui.screens.lab

import androidx.compose.animation.core.*
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.example.data.model.Reagent
import com.example.ui.theme.*
import kotlin.math.cos
import kotlin.math.sin

@Composable
fun ElementDeepResearchDialog(
    element: Reagent,
    onDismiss: () -> Unit,
    onAskAi: (String) -> Unit
) {
    var selectedResearchTab by remember { mutableStateOf(0) }
    val tabs = listOf("Atomic Orbitals", "Periodic Trends", "Quantum Mechanics", "Applications")

    // Rotation animation for Bohr model electrons
    val infiniteTransition = rememberInfiniteTransition(label = "bohr_electron_orbit")
    val angleRad by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = (2 * Math.PI).toFloat(),
        animationSpec = infiniteRepeatable(
            animation = tween(4000, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "orbit_angle"
    )

    // Electron shell counts according to 2n² (e.g. Na: 2, 8, 1; Cl: 2, 8, 7)
    val shells = remember(element.atomicNumber) {
        val z = element.atomicNumber
        when {
            z <= 2 -> listOf(z)
            z <= 10 -> listOf(2, z - 2)
            z <= 18 -> listOf(2, 8, z - 10)
            z <= 36 -> listOf(2, 8, 8, z - 18)
            else -> listOf(2, 8, 18, z - 28)
        }
    }

    val theme = VirtuLabTheme.colors

    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(usePlatformDefaultWidth = false)
    ) {
        Surface(
            modifier = Modifier
                .fillMaxWidth(0.95f)
                .fillMaxHeight(0.92f)
                .clip(RoundedCornerShape(24.dp))
                .border(1.5.dp, Brush.linearGradient(listOf(theme.primaryAccent, theme.tertiaryAccent)), RoundedCornerShape(24.dp))
                .testTag("element_deep_research_dialog"),
            color = theme.surface
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(20.dp)
            ) {
                // Header Row
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Box(
                            modifier = Modifier
                                .size(40.dp)
                                .clip(CircleShape)
                                .background(VioletMath.copy(alpha = 0.25f))
                                .border(1.dp, VioletMath, CircleShape),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.Bolt,
                                contentDescription = null,
                                tint = VioletMath,
                                modifier = Modifier.size(22.dp)
                            )
                        }
                        Spacer(modifier = Modifier.width(10.dp))
                        Column {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Text(
                                    text = "LEVEL-UP: DEEP RESEARCH PHASE",
                                    color = VioletMath,
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.Bold,
                                    letterSpacing = 1.2.sp
                                )
                                Spacer(modifier = Modifier.width(6.dp))
                                Box(
                                    modifier = Modifier
                                        .clip(RoundedCornerShape(6.dp))
                                        .background(EmeraldNeon.copy(alpha = 0.2f))
                                        .padding(horizontal = 6.dp, vertical = 2.dp)
                                ) {
                                    Text("PHASE 2", color = EmeraldNeon, fontSize = 9.sp, fontWeight = FontWeight.Bold)
                                }
                            }
                            Text(
                                text = "${element.name} (${element.formula}) • Z=${element.atomicNumber}",
                                color = TextPrimary,
                                fontSize = 18.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }

                    IconButton(onClick = onDismiss) {
                        Icon(imageVector = Icons.Default.Close, contentDescription = "Close", tint = TextSecondary)
                    }
                }

                Spacer(modifier = Modifier.height(14.dp))

                // Tab Selector
                TabRow(
                    selectedTabIndex = selectedResearchTab,
                    containerColor = CardSurface,
                    contentColor = CyanNeon,
                    modifier = Modifier.clip(RoundedCornerShape(12.dp))
                ) {
                    tabs.forEachIndexed { index, title ->
                        Tab(
                            selected = selectedResearchTab == index,
                            onClick = { selectedResearchTab = index },
                            text = {
                                Text(
                                    text = title,
                                    fontSize = 11.sp,
                                    fontWeight = if (selectedResearchTab == index) FontWeight.Bold else FontWeight.Medium
                                )
                            }
                        )
                    }
                }

                Spacer(modifier = Modifier.height(14.dp))

                // Content Area
                Column(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxWidth()
                        .verticalScroll(rememberScrollState())
                ) {
                    when (selectedResearchTab) {
                        0 -> {
                            // TAB 1: Animated Bohr Model & Electron Orbitals
                            BohrModelSection(
                                element = element,
                                shells = shells,
                                angleRad = angleRad
                            )
                        }
                        1 -> {
                            // TAB 2: Periodic Trends & Energetics
                            PeriodicTrendsSection(element = element)
                        }
                        2 -> {
                            // TAB 3: Quantum Numbers & Subshells
                            QuantumMechanicsSection(element = element)
                        }
                        3 -> {
                            // TAB 4: Real-World Applications & Synthesis
                            ApplicationsSection(element = element)
                        }
                    }
                }

                Spacer(modifier = Modifier.height(14.dp))

                // Bottom Action: Ask AI Deep Research Question
                Button(
                    onClick = {
                        val prompt = "Provide a PhD-level research explanation of ${element.name} (${element.formula}), covering its atomic valence shell energetics, relativistic electron orbital effects, and reaction mechanisms with halogens/alkali metals."
                        onAskAi(prompt)
                    },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = CyanNeon,
                        contentColor = MidnightBg
                    ),
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("deep_research_ask_ai_button")
                ) {
                    Icon(imageVector = Icons.Default.AutoAwesome, contentDescription = null, modifier = Modifier.size(18.dp))
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "Consult Dr. Sophie Vance on ${element.name} (High Thinking)",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }
    }
}

@Composable
fun BohrModelSection(
    element: Reagent,
    shells: List<Int>,
    angleRad: Float
) {
    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Bohr Model Canvas
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(230.dp)
                .clip(RoundedCornerShape(16.dp))
                .background(MidnightBg)
                .border(1.dp, BorderSubtle, RoundedCornerShape(16.dp)),
            contentAlignment = Alignment.Center
        ) {
            Canvas(modifier = Modifier.fillMaxSize()) {
                val center = Offset(size.width / 2f, size.height / 2f)
                val maxRadius = Math.min(size.width, size.height) * 0.42f
                val shellSpacing = maxRadius / (shells.size + 1)

                // Draw Glowing Nucleus
                drawCircle(
                    color = RedReaction.copy(alpha = 0.25f),
                    radius = 24f,
                    center = center
                )
                drawCircle(
                    color = RedReaction,
                    radius = 16f,
                    center = center
                )

                // Draw Orbital Shell Rings & Orbiting Electrons
                shells.forEachIndexed { shellIndex, electronCount ->
                    val ringRadius = (shellIndex + 1) * shellSpacing

                    // Orbital Ring Line
                    drawCircle(
                        color = CyanNeon.copy(alpha = 0.35f),
                        radius = ringRadius,
                        center = center,
                        style = Stroke(width = 1.5f)
                    )

                    // Orbiting Electrons on this ring
                    for (e in 0 until electronCount) {
                        // Alternate rotation direction for even/odd shells
                        val speedMult = if (shellIndex % 2 == 0) 1f else -0.8f
                        val phase = (2 * Math.PI * e / electronCount).toFloat()
                        val currentAngle = angleRad * speedMult + phase

                        val ex = center.x + (cos(currentAngle) * ringRadius)
                        val ey = center.y + (sin(currentAngle) * ringRadius)

                        // Outer glow & core
                        drawCircle(
                            color = CyanGlow,
                            radius = 6f,
                            center = Offset(ex, ey)
                        )
                        drawCircle(
                            color = Color.White,
                            radius = 3.5f,
                            center = Offset(ex, ey)
                        )
                    }
                }
            }

            // Overlay Nucleus Label
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text(
                    text = "${element.formula}",
                    color = Color.White,
                    fontWeight = FontWeight.Bold,
                    fontSize = 12.sp,
                    fontFamily = FontFamily.Monospace
                )
                Text(
                    text = "${element.atomicNumber}+",
                    color = TextPrimary,
                    fontSize = 9.sp,
                    fontFamily = FontFamily.Monospace
                )
            }
        }

        Spacer(modifier = Modifier.height(12.dp))

        // Shell Electron Configuration Breakdown Card
        Card(
            colors = CardDefaults.cardColors(containerColor = CardSurface),
            shape = RoundedCornerShape(12.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(modifier = Modifier.padding(12.dp)) {
                Text(
                    text = "BOHR SHELL ARCHITECTURE: [${shells.joinToString(", ")}]",
                    color = CyanNeon,
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold,
                    letterSpacing = 1.sp
                )
                Spacer(modifier = Modifier.height(6.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    shells.forEachIndexed { index, count ->
                        val shellName = listOf("K", "L", "M", "N", "O")[index.coerceAtMost(4)]
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Text(text = "Shell $shellName (n=${index + 1})", color = TextMuted, fontSize = 9.sp)
                            Text(
                                text = "$count e⁻",
                                color = if (index == shells.size - 1) AmberExplosion else EmeraldNeon,
                                fontWeight = FontWeight.Bold,
                                fontSize = 13.sp,
                                fontFamily = FontFamily.Monospace
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun PeriodicTrendsSection(element: Reagent) {
    val z = element.atomicNumber
    val electronegativity = when (element.formula) {
        "F" -> "3.98 (Highest)"
        "Cl" -> "3.16"
        "O" -> "3.44"
        "N" -> "3.04"
        "Na" -> "0.93 (Low - Strong Metal)"
        "K" -> "0.82"
        "Mg" -> "1.31"
        "Ca" -> "1.00"
        "H" -> "2.20"
        else -> "1.85"
    }

    val ionizationEnergy = when (element.formula) {
        "He" -> "2372 kJ/mol (Maximum)"
        "F" -> "1681 kJ/mol"
        "Cl" -> "1251 kJ/mol"
        "Na" -> "496 kJ/mol (Ultra-low)"
        "K" -> "419 kJ/mol"
        "Mg" -> "738 kJ/mol"
        else -> "890 kJ/mol"
    }

    val atomicRadius = when (element.formula) {
        "Na" -> "186 pm"
        "Cl" -> "99 pm"
        "H" -> "53 pm"
        "Mg" -> "160 pm"
        "K" -> "227 pm"
        else -> "140 pm"
    }

    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
        TrendMetricRow(
            label = "Pauling Electronegativity",
            value = electronegativity,
            description = "Measures the tendency of an atom to attract shared bonding electrons.",
            color = if (element.category.name.contains("HALOGEN")) EmeraldNeon else AmberExplosion
        )
        TrendMetricRow(
            label = "1st Ionization Energy",
            value = ionizationEnergy,
            description = "Energy required to remove the outermost valence electron in gas phase.",
            color = CyanNeon
        )
        TrendMetricRow(
            label = "Covalent Atomic Radius",
            value = atomicRadius,
            description = "Radial boundary of electron density cloud.",
            color = VioletMath
        )
        TrendMetricRow(
            label = "Group & Period Placement",
            value = "Group ${element.group}, Period ${element.period}",
            description = "Governs valence orbital topology and principal quantum level n=${element.period}.",
            color = TextPrimary
        )
    }
}

@Composable
fun QuantumMechanicsSection(element: Reagent) {
    Card(
        colors = CardDefaults.cardColors(containerColor = CardSurface),
        shape = RoundedCornerShape(14.dp),
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(modifier = Modifier.padding(14.dp)) {
            Text(
                text = "QUANTUM SUBSHELL CONFIGURATION",
                color = CyanNeon,
                fontSize = 11.sp,
                fontWeight = FontWeight.Bold,
                letterSpacing = 1.sp
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = element.electronConfig.ifBlank { "Full Orbital Specification" },
                color = CyanGlow,
                fontFamily = FontFamily.Monospace,
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold
            )

            Spacer(modifier = Modifier.height(10.dp))

            Text(
                text = "AUFBAU PRINCIPLE & HUND'S RULE",
                color = TextSecondary,
                fontSize = 11.sp,
                fontWeight = FontWeight.Bold
            )
            Text(
                text = "Electrons populate atomic orbitals of increasing energy levels according to the Madelung rule (n + l). Unpaired electrons maintain parallel spin alignments to minimize inter-electronic Coulomb repulsion.",
                color = TextMuted,
                fontSize = 11.sp,
                lineHeight = 16.sp
            )

            Spacer(modifier = Modifier.height(10.dp))

            Text(
                text = "VALENCE BOND REACTION ENTHALPY",
                color = TextSecondary,
                fontSize = 11.sp,
                fontWeight = FontWeight.Bold
            )
            Text(
                text = "When ${element.name} participates in reactions, its valence orbital hybridization directly determines the stereochemistry, molecular bond angles, and transition state Gibbs free energy ΔG°.",
                color = TextMuted,
                fontSize = 11.sp,
                lineHeight = 16.sp
            )
        }
    }
}

@Composable
fun ApplicationsSection(element: Reagent) {
    Card(
        colors = CardDefaults.cardColors(containerColor = CardSurface),
        shape = RoundedCornerShape(14.dp),
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(modifier = Modifier.padding(14.dp)) {
            Text(
                text = "INDUSTRIAL & SCIENTIFIC UTILITY",
                color = EmeraldNeon,
                fontSize = 11.sp,
                fontWeight = FontWeight.Bold,
                letterSpacing = 1.sp
            )
            Spacer(modifier = Modifier.height(6.dp))
            Text(
                text = element.description,
                color = TextPrimary,
                fontSize = 12.sp,
                lineHeight = 17.sp
            )

            Spacer(modifier = Modifier.height(10.dp))

            Text(
                text = "SAFETY CLASSIFICATION (GHS / MSDS)",
                color = RedReaction,
                fontSize = 11.sp,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = element.hazardNote,
                color = RedReaction.copy(alpha = 0.9f),
                fontSize = 11.sp
            )
        }
    }
}

@Composable
fun TrendMetricRow(
    label: String,
    value: String,
    description: String,
    color: Color
) {
    Card(
        colors = CardDefaults.cardColors(containerColor = CardSurface),
        shape = RoundedCornerShape(12.dp),
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(modifier = Modifier.padding(12.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(text = label, color = TextMuted, fontSize = 11.sp)
                Text(
                    text = value,
                    color = color,
                    fontWeight = FontWeight.Bold,
                    fontSize = 12.sp,
                    fontFamily = FontFamily.Monospace
                )
            }
            Spacer(modifier = Modifier.height(2.dp))
            Text(text = description, color = TextSecondary, fontSize = 10.sp, lineHeight = 14.sp)
        }
    }
}
