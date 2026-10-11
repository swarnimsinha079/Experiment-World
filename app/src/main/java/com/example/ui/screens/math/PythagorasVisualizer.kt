package com.example.ui.screens.math

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.*
import com.example.viewmodel.MathViewModel
import kotlin.math.roundToInt
import kotlin.math.sqrt

@Composable
fun PythagorasVisualizer(
    viewModel: MathViewModel,
    modifier: Modifier = Modifier
) {
    val a by viewModel.pythagorasA.collectAsState()
    val b by viewModel.pythagorasB.collectAsState()

    val cDouble = sqrt((a * a + b * b).toDouble())
    val c = (cDouble * 10.0).roundToInt() / 10.0
    val areaA = (a * a * 10.0).roundToInt() / 10.0
    val areaB = (b * b * 10.0).roundToInt() / 10.0
    val areaC = (c * c * 10.0).roundToInt() / 10.0

    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(14.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Equation Card
        Card(
            colors = CardDefaults.cardColors(containerColor = CardSurface),
            shape = RoundedCornerShape(16.dp),
            border = CardDefaults.outlinedCardBorder().copy(
                brush = Brush.horizontalGradient(listOf(EmeraldNeon, CyanNeon))
            ),
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(
                modifier = Modifier.padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "PYTHAGOREAN THEOREM: a² + b² = c²",
                    color = EmeraldNeon,
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold,
                    letterSpacing = 1.2.sp
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "a² ($areaA) + b² ($areaB) = c² ($areaC)",
                    color = TextPrimary,
                    fontFamily = FontFamily.Monospace,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "Hypotenuse c = √($areaA + $areaB) = $c",
                    color = CyanGlow,
                    fontFamily = FontFamily.Monospace,
                    fontSize = 13.sp
                )
            }
        }

        Spacer(modifier = Modifier.height(14.dp))

        // Geometric Proof Canvas
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
                    .testTag("pythagoras_canvas")
            ) {
                val canvasW = size.width
                val canvasH = size.height

                val scale = Math.min(canvasW, canvasH) / 16f
                val aPx = a * scale
                val bPx = b * scale

                val originX = canvasW * 0.45f
                val originY = canvasH * 0.65f

                // Vertices of Right Triangle
                val pRight = Offset(originX, originY) // right angle
                val pTop = Offset(originX, originY - bPx) // top vertex
                val pRightLeg = Offset(originX + aPx, originY) // right vertex

                // Attached Square on Leg a (horizontal bottom)
                drawRect(
                    color = Color(0xFF3B82F6).copy(alpha = 0.5f),
                    topLeft = Offset(originX, originY),
                    size = androidx.compose.ui.geometry.Size(aPx, aPx)
                )
                drawRect(
                    color = Color(0xFF3B82F6),
                    topLeft = Offset(originX, originY),
                    size = androidx.compose.ui.geometry.Size(aPx, aPx),
                    style = Stroke(width = 2f)
                )

                // Attached Square on Leg b (vertical left)
                drawRect(
                    color = Color(0xFF10B981).copy(alpha = 0.5f),
                    topLeft = Offset(originX - bPx, originY - bPx),
                    size = androidx.compose.ui.geometry.Size(bPx, bPx)
                )
                drawRect(
                    color = Color(0xFF10B981),
                    topLeft = Offset(originX - bPx, originY - bPx),
                    size = androidx.compose.ui.geometry.Size(bPx, bPx),
                    style = Stroke(width = 2f)
                )

                // Right Triangle Filling
                val triPath = Path().apply {
                    moveTo(pRight.x, pRight.y)
                    lineTo(pTop.x, pTop.y)
                    lineTo(pRightLeg.x, pRightLeg.y)
                    close()
                }
                drawPath(triPath, color = CyanNeon.copy(alpha = 0.35f))
                drawPath(triPath, color = CyanNeon, style = Stroke(width = 3f))

                // Right Angle Marker (small square at pRight)
                val squareMarker = 14f
                drawRect(
                    color = Color.White,
                    topLeft = Offset(originX, originY - squareMarker),
                    size = androidx.compose.ui.geometry.Size(squareMarker, squareMarker),
                    style = Stroke(width = 1.5f)
                )

                // Text Annotations
                drawContext.canvas.nativeCanvas.apply {
                    val paint = android.graphics.Paint().apply {
                        color = android.graphics.Color.WHITE
                        textSize = 24f
                        isFakeBoldText = true
                        textAlign = android.graphics.Paint.Align.CENTER
                    }

                    // a² Area label
                    drawText("a²=$areaA", originX + aPx / 2f, originY + aPx / 2f + 8, paint)
                    // b² Area label
                    drawText("b²=$areaB", originX - bPx / 2f, originY - bPx / 2f + 8, paint)
                    // Hypotenuse label
                    paint.color = android.graphics.Color.parseColor("#18FFFF")
                    drawText("c=$c", (pTop.x + pRightLeg.x) / 2f + 14, (pTop.y + pRightLeg.y) / 2f - 10, paint)
                }
            }
        }

        Spacer(modifier = Modifier.height(14.dp))

        // Sliders
        Card(
            colors = CardDefaults.cardColors(containerColor = CardSurface),
            shape = RoundedCornerShape(14.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(modifier = Modifier.padding(14.dp)) {
                Text(text = "Leg a: $a", color = CyanNeon, fontWeight = FontWeight.Bold, fontSize = 13.sp)
                Slider(
                    value = a,
                    onValueChange = { viewModel.setPythagorasA(it) },
                    valueRange = 1.0f..7.0f,
                    colors = SliderDefaults.colors(thumbColor = CyanNeon, activeTrackColor = CyanNeon),
                    modifier = Modifier.testTag("slider_pythagoras_a")
                )

                Spacer(modifier = Modifier.height(6.dp))

                Text(text = "Leg b: $b", color = EmeraldNeon, fontWeight = FontWeight.Bold, fontSize = 13.sp)
                Slider(
                    value = b,
                    onValueChange = { viewModel.setPythagorasB(it) },
                    valueRange = 1.0f..7.0f,
                    colors = SliderDefaults.colors(thumbColor = EmeraldNeon, activeTrackColor = EmeraldNeon),
                    modifier = Modifier.testTag("slider_pythagoras_b")
                )
            }
        }
    }
}
