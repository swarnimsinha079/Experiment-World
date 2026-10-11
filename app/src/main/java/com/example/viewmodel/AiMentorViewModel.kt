package com.example.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.model.ChatMessage
import com.example.data.model.MessageSender
import com.example.data.remote.GeminiApiClient
import com.example.data.remote.GeminiContent
import com.example.data.remote.GeminiPart
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class AiMentorViewModel : ViewModel() {

    private val _messages = MutableStateFlow<List<ChatMessage>>(
        listOf(
            ChatMessage(
                sender = MessageSender.AI_PROFESSOR,
                text = "Hello! I am Dr. Sophie Vance, your VirtuLab Science & Mathematics Mentor. " +
                        "Ask me anything about reaction kinetics, valence electrons, thermodynamics, or geometric math proofs!"
            )
        )
    )
    val messages = _messages.asStateFlow()

    private val _isThinking = MutableStateFlow(false)
    val isThinking = _isThinking.asStateFlow()

    private val _useHighThinking = MutableStateFlow(false)
    val useHighThinking = _useHighThinking.asStateFlow()

    private val _inputText = MutableStateFlow("")
    val inputText = _inputText.asStateFlow()

    fun setInputText(text: String) {
        _inputText.value = text
    }

    fun setUseHighThinking(enabled: Boolean) {
        _useHighThinking.value = enabled
    }

    fun sendMessage(userText: String? = null) {
        val query = (userText ?: _inputText.value).trim()
        if (query.isBlank()) return

        val userMessage = ChatMessage(
            sender = MessageSender.USER,
            text = query
        )

        val updated = _messages.value + userMessage
        _messages.value = updated
        _inputText.value = ""
        _isThinking.value = true

        viewModelScope.launch {
            // Build conversation history (excluding initial greeting)
            val history = updated.drop(1).dropLast(1).map { msg ->
                GeminiContent(
                    parts = listOf(GeminiPart(text = msg.text)),
                    role = if (msg.sender == MessageSender.USER) "user" else "model"
                )
            }

            val reply = GeminiApiClient.askSciMathProfessor(
                userPrompt = query,
                history = history,
                useHighThinking = _useHighThinking.value
            )

            _isThinking.value = false
            _messages.value = _messages.value + ChatMessage(
                sender = MessageSender.AI_PROFESSOR,
                text = reply,
                isThinking = _useHighThinking.value
            )
        }
    }

    fun clearChat() {
        _messages.value = listOf(
            ChatMessage(
                sender = MessageSender.AI_PROFESSOR,
                text = "Chat cleared. What scientific concept or mathematical theorem shall we explore next?"
            )
        )
    }
}
