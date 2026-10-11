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
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.*
import com.example.viewmodel.MathViewModel
import kotlin.math.*

@Composable
fun UnitCircleVisualizer(
    viewModel: MathViewModel,
    modifier: Modifier = Modifier
) {
    val deg by viewModel.angleDeg.collectAsState()
    val rad = deg * (PI / 180.0)

    val cosVal = (cos(rad) * 1000.0).roundToInt() / 1000.0
    val sinVal = (sin(rad) * 1000.0).roundToInt() / 1000.0
    val tanVal = if (abs(cosVal) > 0.01) (tan(rad) * 1000.0).roundToInt() / 1000.0 else Double.POSITIVE_INFINITY

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
                brush = Brush.horizontalGradient(listOf(CyanNeon, VioletMath))
            ),
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(
                modifier = Modifier.padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "UNIT CIRCLE TRIGONOMETRY",
                    color = CyanNeon,
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold,
                    letterSpacing = 1.2.sp
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "θ = ${deg}°  (${((deg / 180.0) * 100).roundToInt() / 100.0}π rad)",
                    color = TextPrimary,
                    fontFamily = FontFamily.Monospace,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold
                )

                Spacer(modifier = Modifier.height(8.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(text = "cos θ (X)", color = EmeraldNeon, fontSize = 11.sp, fontWeight = FontWeight.Bold)
                        Text(text = "$cosVal", color = TextPrimary, fontFamily = FontFamily.Monospace, fontSize = 13.sp)
                    }
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(text = "sin θ (Y)", color = CyanNeon, fontSize = 11.sp, fontWeight = FontWeight.Bold)
                        Text(text = "$sinVal", color = TextPrimary, fontFamily = FontFamily.Monospace, fontSize = 13.sp)
                    }
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(text = "tan θ", color = AmberExplosion, fontSize = 11.sp, fontWeight = FontWeight.Bold)
                        Text(
                            text = if (tanVal.isInfinite()) "∞" else "$tanVal",
                            color = TextPrimary,
                            fontFamily = FontFamily.Monospace,
                            fontSize = 13.sp
                        )
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(14.dp))

        // Unit Circle Canvas
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(250.dp)
                .clip(RoundedCornerShape(16.dp))
                .background(DeepSurface)
                .border(1.dp, BorderSubtle, RoundedCornerShape(16.dp))
                .padding(12.dp)
        ) {
            Canvas(
                modifier = Modifier
                    .fillMaxSize()
                    .testTag("unit_circle_canvas")
            ) {
                val center = Offset(size.width / 2f, size.height / 2f)
                val radius = Math.min(size.width, size.height) * 0.4f

                // Axes
                drawLine(color = BorderSubtle, start = Offset(0f, center.y), end = Offset(size.width, center.y), strokeWidth = 2f)
                drawLine(color = BorderSubtle, start = Offset(center.x, 0f), end = Offset(center.x, size.height), strokeWidth = 2f)

                // Circle boundary
                drawCircle(color = TextSecondary.copy(alpha = 0.3f), radius = radius, center = center, style = Stroke(width = 2.5f))

                // Point on circle
                val pointX = center.x + (cos(rad) * radius).toFloat()
                val pointY = center.y - (sin(rad) * radius).toFloat() // Inverted Y

                // Radius Line
                drawLine(color = Color.White, start = center, end = Offset(pointX, pointY), strokeWidth = 3f)

                // Cosine Horizontal Line (green)
                drawLine(color = EmeraldNeon, start = center, end = Offset(pointX, center.y), strokeWidth = 3.5f)

                // Sine Vertical Line (cyan)
                drawLine(color = CyanNeon, start = Offset(pointX, center.y), end = Offset(pointX, pointY), strokeWidth = 3.5f)

                // Marker point
                drawCircle(color = AmberExplosion, radius = 6f, center = Offset(pointX, pointY))
            }
        }

        Spacer(modifier = Modifier.height(14.dp))

        // Angle Slider
        Card(
            colors = CardDefaults.cardColors(containerColor = CardSurface),
            shape = RoundedCornerShape(14.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(modifier = Modifier.padding(14.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(text = "Angle θ: ${deg}°", color = CyanNeon, fontSize = 13.sp, fontWeight = FontWeight.Bold)
                    Text(text = "cos²θ + sin²θ = 1.0", color = EmeraldNeon, fontSize = 12.sp, fontFamily = FontFamily.Monospace)
                }
                Slider(
                    value = deg,
                    onValueChange = { viewModel.setAngleDeg(it) },
                    valueRange = 0.0f..360.0f,
                    colors = SliderDefaults.colors(thumbColor = CyanNeon, activeTrackColor = CyanNeon),
                    modifier = Modifier.testTag("slider_angle_theta")
                )
            }
        }
    }
}
