package com.example.ui.screens.ai

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
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
import com.example.data.model.ChatMessage
import com.example.data.model.MessageSender
import com.example.ui.theme.*
import com.example.viewmodel.AiMentorViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AiMentorScreen(
    viewModel: AiMentorViewModel,
    initialPrompt: String? = null,
    onOpenSettings: (() -> Unit)? = null,
    modifier: Modifier = Modifier
) {
    val theme = VirtuLabTheme.colors
    val messages by viewModel.messages.collectAsState()
    val isThinking by viewModel.isThinking.collectAsState()
    val useHighThinking by viewModel.useHighThinking.collectAsState()
    val inputText by viewModel.inputText.collectAsState()

    val listState = rememberLazyListState()

    LaunchedEffect(initialPrompt) {
        if (!initialPrompt.isNullOrBlank()) {
            viewModel.sendMessage(initialPrompt)
        }
    }

    LaunchedEffect(messages.size, isThinking) {
        if (messages.isNotEmpty()) {
            listState.animateScrollToItem(messages.size)
        }
    }

    val quickQuestions = listOf(
        "Why does Sodium explode with water at atomic level?",
        "Prove geometrically why (a + b)² has a 2ab term.",
        "Explain Le Chatelier's principle and equilibrium shift.",
        "Derive the quadratic formula step by step."
    )

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(theme.background)
            .statusBarsPadding()
            .testTag("ai_mentor_screen")
    ) {
        // Top Bar
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 14.dp, vertical = 10.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(
                    modifier = Modifier
                        .size(38.dp)
                        .clip(CircleShape)
                        .background(theme.tertiaryAccent.copy(alpha = 0.2f))
                        .border(1.dp, theme.tertiaryAccent, CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.Psychology,
                        contentDescription = "AI Mentor",
                        tint = theme.tertiaryAccent,
                        modifier = Modifier.size(22.dp)
                    )
                }
                Spacer(modifier = Modifier.width(10.dp))
                Column {
                    Text(
                        text = "Dr. Sophie Vance",
                        color = theme.textPrimary,
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = "VirtuLab Sci-Math Senior Mentor",
                        color = theme.primaryAccent,
                        fontSize = 10.sp
                    )
                }
            }

            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                // High Thinking Mode Toggle
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .clip(RoundedCornerShape(20.dp))
                        .background(theme.surfaceElevated)
                        .border(
                            1.dp,
                            if (useHighThinking) theme.tertiaryAccent else theme.border,
                            RoundedCornerShape(20.dp)
                        )
                        .clickable { viewModel.setUseHighThinking(!useHighThinking) }
                        .padding(horizontal = 8.dp, vertical = 5.dp)
                        .testTag("toggle_thinking_mode")
                ) {
                    Icon(
                        imageVector = Icons.Default.Bolt,
                        contentDescription = "Thinking Mode",
                        tint = if (useHighThinking) theme.tertiaryAccent else theme.textMuted,
                        modifier = Modifier.size(14.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = if (useHighThinking) "High Thinking ON" else "Standard AI",
                        color = if (useHighThinking) theme.tertiaryAccent else theme.textMuted,
                        fontSize = 10.sp,
                        fontWeight = FontWeight.SemiBold
                    )
                }

                if (onOpenSettings != null) {
                    IconButton(
                        onClick = onOpenSettings,
                        modifier = Modifier
                            .size(36.dp)
                            .clip(CircleShape)
                            .background(theme.cardSurface)
                            .border(1.dp, theme.border, CircleShape)
                            .testTag("ai_settings_button")
                    ) {
                        Icon(
                            imageVector = Icons.Default.Settings,
                            contentDescription = "Settings",
                            tint = theme.primaryAccent,
                            modifier = Modifier.size(18.dp)
                        )
                    }
                }
            }
        }

        // Quick Suggestion Chips
        LazyRow(
            horizontalArrangement = Arrangement.spacedBy(6.dp),
            contentPadding = PaddingValues(horizontal = 14.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            items(quickQuestions) { q ->
                SuggestionChip(
                    onClick = { viewModel.sendMessage(q) },
                    label = { Text(text = q, fontSize = 10.sp, maxLines = 1, color = theme.textSecondary) },
                    colors = SuggestionChipDefaults.suggestionChipColors(
                        containerColor = theme.cardSurface
                    ),
                    border = SuggestionChipDefaults.suggestionChipBorder(
                        enabled = true,
                        borderColor = theme.border
                    )
                )
            }
        }

        Spacer(modifier = Modifier.height(6.dp))

        // Message Thread
        LazyColumn(
            state = listState,
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
                .padding(horizontal = 14.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            items(messages) { msg ->
                MessageBubble(message = msg)
            }

            if (isThinking) {
                item {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier
                            .clip(RoundedCornerShape(12.dp))
                            .background(theme.cardSurface)
                            .padding(horizontal = 12.dp, vertical = 8.dp)
                    ) {
                        CircularProgressIndicator(
                            color = theme.primaryAccent,
                            modifier = Modifier.size(14.dp),
                            strokeWidth = 2.dp
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = if (useHighThinking) "Dr. Vance is analyzing deep STEM concepts with High Thinking..." else "Dr. Vance is generating explanation...",
                            color = theme.textSecondary,
                            fontSize = 11.sp
                        )
                    }
                }
            }
        }

        // Input Field Bar
        Surface(
            color = theme.surface,
            tonalElevation = 4.dp,
            modifier = Modifier.fillMaxWidth()
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 12.dp, vertical = 8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                OutlinedTextField(
                    value = inputText,
                    onValueChange = { viewModel.setInputText(it) },
                    placeholder = { Text("Ask about equations, reactions, proofs...", fontSize = 12.sp, color = theme.textMuted) },
                    singleLine = false,
                    maxLines = 3,
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = theme.primaryAccent,
                        unfocusedBorderColor = theme.border,
                        focusedContainerColor = theme.cardSurface,
                        unfocusedContainerColor = theme.cardSurface,
                        focusedTextColor = theme.textPrimary,
                        unfocusedTextColor = theme.textPrimary
                    ),
                    shape = RoundedCornerShape(20.dp),
                    modifier = Modifier
                        .weight(1f)
                        .testTag("ai_message_input")
                )

                Spacer(modifier = Modifier.width(8.dp))

                IconButton(
                    onClick = { viewModel.sendMessage() },
                    enabled = inputText.isNotBlank() && !isThinking,
                    modifier = Modifier
                        .size(44.dp)
                        .clip(CircleShape)
                        .background(if (inputText.isNotBlank()) theme.primaryAccent else theme.cardSurface)
                        .testTag("send_ai_message_button")
                ) {
                    Icon(
                        imageVector = Icons.Default.Send,
                        contentDescription = "Send",
                        tint = if (inputText.isNotBlank()) (if (theme.isDark) MidnightBg else Color.White) else theme.textMuted,
                        modifier = Modifier.size(20.dp)
                    )
                }
            }
        }
    }
}

@Composable
fun MessageBubble(message: ChatMessage) {
    val theme = VirtuLabTheme.colors
    val isUser = message.sender == MessageSender.USER
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = if (isUser) Arrangement.End else Arrangement.Start
    ) {
        if (!isUser) {
            Box(
                modifier = Modifier
                    .size(28.dp)
                    .clip(CircleShape)
                    .background(theme.tertiaryAccent.copy(alpha = 0.2f))
                    .border(1.dp, theme.tertiaryAccent, CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.AutoAwesome,
                    contentDescription = null,
                    tint = theme.tertiaryAccent,
                    modifier = Modifier.size(14.dp)
                )
            }
            Spacer(modifier = Modifier.width(6.dp))
        }

        Card(
            colors = CardDefaults.cardColors(
                containerColor = if (isUser) theme.primaryAccent.copy(alpha = 0.18f) else theme.cardSurface
            ),
            shape = RoundedCornerShape(
                topStart = 14.dp,
                topEnd = 14.dp,
                bottomStart = if (isUser) 14.dp else 4.dp,
                bottomEnd = if (isUser) 4.dp else 14.dp
            ),
            border = CardDefaults.outlinedCardBorder().copy(
                brush = Brush.horizontalGradient(
                    if (isUser) listOf(theme.primaryAccent.copy(alpha = 0.5f), theme.primaryAccent.copy(alpha = 0.5f))
                    else listOf(theme.border, theme.border)
                )
            ),
            modifier = Modifier.widthIn(max = 300.dp)
        ) {
            Column(modifier = Modifier.padding(12.dp)) {
                if (message.isThinking) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.padding(bottom = 4.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Bolt,
                            contentDescription = null,
                            tint = theme.tertiaryAccent,
                            modifier = Modifier.size(12.dp)
                        )
                        Spacer(modifier = Modifier.width(3.dp))
                        Text(
                            text = "High Thinking Answer",
                            color = theme.tertiaryAccent,
                            fontSize = 9.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }

                Text(
                    text = message.text,
                    color = theme.textPrimary,
                    fontSize = 13.sp,
                    lineHeight = 19.sp
                )
            }
        }
    }
}
