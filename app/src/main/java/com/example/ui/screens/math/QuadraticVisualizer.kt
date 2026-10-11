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
fun QuadraticVisualizer(
    viewModel: MathViewModel,
    modifier: Modifier = Modifier
) {
    val a by viewModel.quadA.collectAsState()
    val b by viewModel.quadB.collectAsState()
    val c by viewModel.quadC.collectAsState()

    val discriminant = (b * b - 4 * a * c).toDouble()
    val discRounded = (discriminant * 10.0).roundToInt() / 10.0

    val vertexX = if (a != 0f) -b / (2 * a) else 0f
    val vertexY = a * vertexX * vertexX + b * vertexX + c
    val vxRound = (vertexX * 10.0).roundToInt() / 10.0
    val vyRound = (vertexY * 10.0).roundToInt() / 10.0

    val rootsText = when {
        a == 0f -> "Linear equation: x = ${if (b != 0f) -c / b else "undefined"}"
        discriminant > 0 -> {
            val r1 = (-b + sqrt(discriminant)) / (2 * a)
            val r2 = (-b - sqrt(discriminant)) / (2 * a)
            "Two Real Roots: x₁ = ${(r1 * 10.0).roundToInt() / 10.0}, x₂ = ${(r2 * 10.0).roundToInt() / 10.0}"
        }
        discriminant == 0.0 -> {
            val r = -b / (2 * a)
            "Single Repeated Root: x = ${(r * 10.0).roundToInt() / 10.0}"
        }
        else -> "No Real Roots (Complex Conjugate Pair)"
    }

    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(14.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Card(
            colors = CardDefaults.cardColors(containerColor = CardSurface),
            shape = RoundedCornerShape(16.dp),
            border = CardDefaults.outlinedCardBorder().copy(
                brush = Brush.horizontalGradient(listOf(AmberExplosion, VioletMath))
            ),
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(
                modifier = Modifier.padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "QUADRATIC FUNCTION: y = ax² + bx + c",
                    color = AmberExplosion,
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold,
                    letterSpacing = 1.2.sp
                )
                Spacer(modifier = Modifier.height(4.dp))
                val signB = if (b >= 0) "+ $b" else "- ${-b}"
                val signC = if (c >= 0) "+ $c" else "- ${-c}"
                Text(
                    text = "y = ${a}x² $signB x $signC",
                    color = TextPrimary,
                    fontFamily = FontFamily.Monospace,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "Discriminant Δ = b² - 4ac = $discRounded  |  Vertex: ($vxRound, $vyRound)",
                    color = CyanGlow,
                    fontSize = 11.sp,
                    fontFamily = FontFamily.Monospace
                )
                Spacer(modifier = Modifier.height(2.dp))
                Text(
                    text = rootsText,
                    color = if (discriminant >= 0) EmeraldNeon else RedReaction,
                    fontSize = 11.sp,
                    fontWeight = FontWeight.SemiBold
                )
            }
        }

        Spacer(modifier = Modifier.height(14.dp))

        // Parabola Cartesian Canvas
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(240.dp)
                .clip(RoundedCornerShape(16.dp))
                .background(DeepSurface)
                .border(1.dp, BorderSubtle, RoundedCornerShape(16.dp))
                .padding(12.dp)
        ) {
            Canvas(
                modifier = Modifier
                    .fillMaxSize()
                    .testTag("quadratic_canvas")
            ) {
                val w = size.width
                val h = size.height
                val centerX = w / 2f
                val centerY = h / 2f
                val scale = 22f // pixels per unit

                // Draw Coordinate Axes
                drawLine(
                    color = BorderSubtle,
                    start = Offset(0f, centerY),
                    end = Offset(w, centerY),
                    strokeWidth = 2f
                )
                drawLine(
                    color = BorderSubtle,
                    start = Offset(centerX, 0f),
                    end = Offset(centerX, h),
                    strokeWidth = 2f
                )

                // Parabolic Curve Path
                val curvePath = Path()
                var firstPoint = true

                val xMin = -w / (2 * scale)
                val xMax = w / (2 * scale)
                val step = 0.2f

                var xMath = xMin
                while (xMath <= xMax) {
                    val yMath = a * xMath * xMath + b * xMath + c
                    val canvasX = centerX + xMath * scale
                    val canvasY = centerY - yMath * scale // invert Y for screen space

                    if (firstPoint) {
                        curvePath.moveTo(canvasX, canvasY)
                        firstPoint = false
                    } else {
                        curvePath.lineTo(canvasX, canvasY)
                    }
                    xMath += step
                }

                drawPath(
                    path = curvePath,
                    color = AmberExplosion,
                    style = Stroke(width = 3.5f)
                )

                // Vertex Point
                val vertexCanvasX = centerX + vertexX * scale
                val vertexCanvasY = centerY - vertexY * scale
                drawCircle(
                    color = CyanNeon,
                    radius = 6f,
                    center = Offset(vertexCanvasX, vertexCanvasY)
                )
            }
        }

        Spacer(modifier = Modifier.height(14.dp))

        // Sliders for a, b, c
        Card(
            colors = CardDefaults.cardColors(containerColor = CardSurface),
            shape = RoundedCornerShape(14.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(modifier = Modifier.padding(14.dp)) {
                Text(text = "Coefficient a: $a", color = AmberExplosion, fontSize = 12.sp, fontWeight = FontWeight.Bold)
                Slider(
                    value = a,
                    onValueChange = { viewModel.setQuadA(it) },
                    valueRange = -3.0f..3.0f,
                    colors = SliderDefaults.colors(thumbColor = AmberExplosion, activeTrackColor = AmberExplosion)
                )

                Text(text = "Coefficient b: $b", color = CyanNeon, fontSize = 12.sp, fontWeight = FontWeight.Bold)
                Slider(
                    value = b,
                    onValueChange = { viewModel.setQuadB(it) },
                    valueRange = -5.0f..5.0f,
                    colors = SliderDefaults.colors(thumbColor = CyanNeon, activeTrackColor = CyanNeon)
                )

                Text(text = "Coefficient c: $c", color = EmeraldNeon, fontSize = 12.sp, fontWeight = FontWeight.Bold)
                Slider(
                    value = c,
                    onValueChange = { viewModel.setQuadC(it) },
                    valueRange = -5.0f..5.0f,
                    colors = SliderDefaults.colors(thumbColor = EmeraldNeon, activeTrackColor = EmeraldNeon)
                )
            }
        }
    }
}
