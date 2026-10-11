package com.example.ui.screens.math

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.*
import com.example.viewmodel.MathViewModel

@Composable
fun AlgebraicIdentityVisualizer(
    viewModel: MathViewModel,
    modifier: Modifier = Modifier
) {
    val a by viewModel.variableA.collectAsState()
    val b by viewModel.variableB.collectAsState()
    val isExploded by viewModel.isExploded.collectAsState()
    val explodeFraction by viewModel.explodeGapFraction.collectAsState()

    val totalSide = a + b
    val areaTotal = totalSide * totalSide
    val areaA2 = a * a
    val areaB2 = b * b
    val areaAB = a * b
    val expandedSum = areaA2 + (2 * areaAB) + areaB2

    val animatedGap by animateFloatAsState(
        targetValue = if (isExploded) 28f else explodeFraction * 35f,
        animationSpec = tween(durationMillis = 400),
        label = "gap_animation"
    )

    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(14.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Live Geometric Identity Card
        Card(
            colors = CardDefaults.cardColors(containerColor = CardSurface),
            shape = RoundedCornerShape(16.dp),
            border = CardDefaults.outlinedCardBorder().copy(
                brush = Brush.horizontalGradient(listOf(CyanNeon, VioletMath))
            ),
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(
                modifier = Modifier.padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "GEOMETRIC PROOF OF (a + b)²",
                    color = CyanNeon,
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold,
                    letterSpacing = 1.2.sp
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "(a + b)² = a² + 2ab + b²",
                    color = TextPrimary,
                    fontFamily = FontFamily.Monospace,
                    fontSize = 17.sp,
                    fontWeight = FontWeight.Bold
                )

                Spacer(modifier = Modifier.height(10.dp))

                // Numerical Equivalence Verification Box
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(10.dp))
                        .background(DeepSurface)
                        .padding(horizontal = 12.dp, vertical = 8.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text(text = "LHS: (a + b)²", color = TextMuted, fontSize = 10.sp)
                        Text(
                            text = "($a + $b)² = ${totalSide}² = $areaTotal",
                            color = CyanGlow,
                            fontFamily = FontFamily.Monospace,
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }

                    Icon(
                        imageVector = Icons.Default.CheckCircle,
                        contentDescription = "Verified",
                        tint = EmeraldNeon,
                        modifier = Modifier.size(20.dp)
                    )

                    Column(horizontalAlignment = Alignment.End) {
                        Text(text = "RHS: a² + 2ab + b²", color = TextMuted, fontSize = 10.sp)
                        Text(
                            text = "$areaA2 + ${2 * areaAB} + $areaB2 = $expandedSum",
                            color = EmeraldNeon,
                            fontFamily = FontFamily.Monospace,
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(14.dp))

        // Interactive 2D/3D Vectorized Canvas
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(260.dp)
                .clip(RoundedCornerShape(16.dp))
                .background(DeepSurface)
                .border(1.dp, BorderSubtle, RoundedCornerShape(16.dp))
                .padding(16.dp),
            contentAlignment = Alignment.Center
        ) {
            Canvas(
                modifier = Modifier
                    .fillMaxSize()
                    .testTag("algebra_canvas")
            ) {
                val canvasWidth = size.width
                val canvasHeight = size.height
                val sideLimit = Math.min(canvasWidth, canvasHeight) - 40f
                val scale = sideLimit / (a + b)

                val aPx = a * scale
                val bPx = b * scale
                val gap = animatedGap

                val startX = (canvasWidth - (aPx + bPx + gap)) / 2f
                val startY = (canvasHeight - (aPx + bPx + gap)) / 2f

                // Color Themes for 4 partitions
                val colorA2 = Color(0xFF3B82F6) // Blue a²
                val colorAB1 = Color(0xFF10B981) // Emerald a*b
                val colorAB2 = Color(0xFF06B6D4) // Cyan b*a
                val colorB2 = Color(0xFFF59E0B) // Amber b²

                // 1. Top-Left: a² Square
                drawRect(
                    color = colorA2,
                    topLeft = Offset(startX, startY),
                    size = Size(aPx, aPx)
                )
                drawRect(
                    color = Color.White.copy(alpha = 0.5f),
                    topLeft = Offset(startX, startY),
                    size = Size(aPx, aPx),
                    style = Stroke(width = 2f)
                )

                // 2. Top-Right: a × b Rectangle
                drawRect(
                    color = colorAB1,
                    topLeft = Offset(startX + aPx + gap, startY),
                    size = Size(bPx, aPx)
                )
                drawRect(
                    color = Color.White.copy(alpha = 0.5f),
                    topLeft = Offset(startX + aPx + gap, startY),
                    size = Size(bPx, aPx),
                    style = Stroke(width = 2f)
                )

                // 3. Bottom-Left: b × a Rectangle
                drawRect(
                    color = colorAB2,
                    topLeft = Offset(startX, startY + aPx + gap),
                    size = Size(aPx, bPx)
                )
                drawRect(
                    color = Color.White.copy(alpha = 0.5f),
                    topLeft = Offset(startX, startY + aPx + gap),
                    size = Size(aPx, bPx),
                    style = Stroke(width = 2f)
                )

                // 4. Bottom-Right: b² Square
                drawRect(
                    color = colorB2,
                    topLeft = Offset(startX + aPx + gap, startY + aPx + gap),
                    size = Size(bPx, bPx)
                )
                drawRect(
                    color = Color.White.copy(alpha = 0.5f),
                    topLeft = Offset(startX + aPx + gap, startY + aPx + gap),
                    size = Size(bPx, bPx),
                    style = Stroke(width = 2f)
                )

                // Text Labels on Canvas
                drawContext.canvas.nativeCanvas.apply {
                    val paint = android.graphics.Paint().apply {
                        color = android.graphics.Color.WHITE
                        textSize = 28f
                        isFakeBoldText = true
                        textAlign = android.graphics.Paint.Align.CENTER
                    }

                    // a² Label
                    if (aPx > 35) {
                        drawText("a²", startX + aPx / 2, startY + aPx / 2 + 10, paint)
                    }

                    // ab Top-Right Label
                    if (bPx > 30 && aPx > 25) {
                        drawText("ab", startX + aPx + gap + bPx / 2, startY + aPx / 2 + 10, paint)
                    }

                    // ba Bottom-Left Label
                    if (aPx > 30 && bPx > 25) {
                        drawText("ab", startX + aPx / 2, startY + aPx + gap + bPx / 2 + 10, paint)
                    }

                    // b² Label
                    if (bPx > 30) {
                        drawText("b²", startX + aPx + gap + bPx / 2, startY + aPx + gap + bPx / 2 + 10, paint)
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(14.dp))

        // Sliders & Controls Section
        Card(
            colors = CardDefaults.cardColors(containerColor = CardSurface),
            shape = RoundedCornerShape(14.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(modifier = Modifier.padding(14.dp)) {
                // Slider a
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(text = "Variable a: $a", color = CyanNeon, fontWeight = FontWeight.Bold, fontSize = 13.sp)
                    Text(text = "Area a² = $areaA2", color = TextSecondary, fontSize = 12.sp)
                }
                Slider(
                    value = a,
                    onValueChange = { viewModel.setVariableA(it) },
                    valueRange = 1.0f..8.0f,
                    colors = SliderDefaults.colors(
                        thumbColor = CyanNeon,
                        activeTrackColor = CyanNeon,
                        inactiveTrackColor = BorderSubtle
                    ),
                    modifier = Modifier.testTag("slider_variable_a")
                )

                Spacer(modifier = Modifier.height(6.dp))

                // Slider b
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(text = "Variable b: $b", color = AmberExplosion, fontWeight = FontWeight.Bold, fontSize = 13.sp)
                    Text(text = "Area b² = $areaB2", color = TextSecondary, fontSize = 12.sp)
                }
                Slider(
                    value = b,
                    onValueChange = { viewModel.setVariableB(it) },
                    valueRange = 1.0f..8.0f,
                    colors = SliderDefaults.colors(
                        thumbColor = AmberExplosion,
                        activeTrackColor = AmberExplosion,
                        inactiveTrackColor = BorderSubtle
                    ),
                    modifier = Modifier.testTag("slider_variable_b")
                )

                Spacer(modifier = Modifier.height(10.dp))

                // Explode Partition Button
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Button(
                        onClick = { viewModel.toggleExplode() },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = if (isExploded) EmeraldNeon else CardSurfaceElevated,
                            contentColor = if (isExploded) MidnightBg else TextPrimary
                        ),
                        modifier = Modifier.testTag("toggle_explode_button")
                    ) {
                        Icon(
                            imageVector = if (isExploded) Icons.Default.Compress else Icons.Default.CallSplit,
                            contentDescription = null,
                            modifier = Modifier.size(16.dp)
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = if (isExploded) "Collapse Square" else "Explode 4 Sub-sections",
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }

                    FilledTonalButton(
                        onClick = { viewModel.saveMathNote() },
                        colors = ButtonDefaults.filledTonalButtonColors(
                            containerColor = VioletMath.copy(alpha = 0.25f),
                            contentColor = VioletMath
                        ),
                        modifier = Modifier.testTag("save_math_note_button")
                    ) {
                        Icon(
                            imageVector = Icons.Default.BookmarkAdd,
                            contentDescription = null,
                            modifier = Modifier.size(16.dp)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text("Record", fontSize = 12.sp)
                    }
                }
            }
        }
    }
}
