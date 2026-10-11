package com.example.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.local.ApparatusBadgeEntity
import com.example.data.local.UserProgressEntity
import com.example.data.repository.ProgressEventType
import com.example.data.repository.UserProgressRepository
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class ProgressViewModel(application: Application) : AndroidViewModel(application) {

    private val repository = UserProgressRepository(application)

    val progress: StateFlow<UserProgressEntity> = repository.progressFlow
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = UserProgressEntity()
        )

    val badges: StateFlow<List<ApparatusBadgeEntity>> = repository.badgesFlow
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    private val _unlockedBadgeAlert = MutableStateFlow<ApparatusBadgeEntity?>(null)
    val unlockedBadgeAlert = _unlockedBadgeAlert.asStateFlow()

    private val _isProgressModalOpen = MutableStateFlow(false)
    val isProgressModalOpen = _isProgressModalOpen.asStateFlow()

    init {
        viewModelScope.launch {
            repository.unlockEventFlow.collectLatest { notification ->
                _unlockedBadgeAlert.value = notification.badge
            }
        }
    }

    fun setProgressModalOpen(open: Boolean) {
        _isProgressModalOpen.value = open
    }

    fun dismissUnlockedAlert() {
        _unlockedBadgeAlert.value = null
    }

    fun awardExperimentPoints() {
        viewModelScope.launch {
            repository.awardPoints(50, ProgressEventType.EXPERIMENT_COMPLETED)
        }
    }

    fun awardDeepResearchPoints() {
        viewModelScope.launch {
            repository.awardPoints(25, ProgressEventType.DEEP_RESEARCH_READ)
        }
    }

    fun awardMathProofPoints() {
        viewModelScope.launch {
            repository.awardPoints(30, ProgressEventType.MATH_PROOF_SOLVED)
        }
    }

    fun awardAiConsultPoints() {
        viewModelScope.launch {
            repository.awardPoints(15, ProgressEventType.AI_QUERY_ASKED)
        }
    }
}
