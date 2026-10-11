package com.example.ui.screens.progress

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.example.data.local.ApparatusBadgeEntity
import com.example.ui.theme.*

@Composable
fun BadgeUnlockedDialog(
    badge: ApparatusBadgeEntity,
    onDismiss: () -> Unit
) {
    Dialog(onDismissRequest = onDismiss) {
        Card(
            colors = CardDefaults.cardColors(containerColor = DeepSurface),
            shape = RoundedCornerShape(20.dp),
            border = CardDefaults.outlinedCardBorder().copy(
                brush = Brush.linearGradient(listOf(AmberExplosion, CyanNeon))
            ),
            modifier = Modifier
                .fillMaxWidth(0.92f)
                .testTag("badge_unlocked_dialog")
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // Glowing Celebration Avatar
                Box(
                    modifier = Modifier
                        .size(68.dp)
                        .clip(CircleShape)
                        .background(AmberExplosion.copy(alpha = 0.25f))
                        .border(2.dp, AmberExplosion, CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.WorkspacePremium,
                        contentDescription = "Badge",
                        tint = AmberExplosion,
                        modifier = Modifier.size(38.dp)
                    )
                }

                Spacer(modifier = Modifier.height(14.dp))

                Text(
                    text = "NEW APPARATUS UNLOCKED!",
                    color = AmberExplosion,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold,
                    letterSpacing = 1.2.sp
                )

                Spacer(modifier = Modifier.height(4.dp))

                Text(
                    text = badge.apparatusName,
                    color = TextPrimary,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold
                )

                Text(
                    text = badge.title,
                    color = CyanNeon,
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Medium
                )

                Spacer(modifier = Modifier.height(10.dp))

                Text(
                    text = badge.description,
                    color = TextSecondary,
                    fontSize = 12.sp,
                    lineHeight = 17.sp,
                    modifier = Modifier.padding(horizontal = 8.dp)
                )

                Spacer(modifier = Modifier.height(18.dp))

                Button(
                    onClick = onDismiss,
                    colors = ButtonDefaults.buttonColors(
                        containerColor = CyanNeon,
                        contentColor = MidnightBg
                    ),
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("dismiss_unlock_button")
                ) {
                    Text("Equip to 3D Laboratory", fontWeight = FontWeight.Bold, fontSize = 13.sp)
                }
            }
        }
    }
}
