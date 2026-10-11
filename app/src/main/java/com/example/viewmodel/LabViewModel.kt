package com.example.viewmodel

import android.app.Application
import android.content.Context
import android.os.Build
import android.os.VibrationEffect
import android.os.Vibrator
import android.os.VibratorManager
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.local.LabDatabase
import com.example.data.local.LabRecordEntity
import com.example.data.model.*
import com.example.data.repository.LabRepository
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

sealed class LabUiEffect {
    data class TriggerWeb3DReaction(val animationType: String, val equation: String) : LabUiEffect()
    data class AddWeb3DChemical(val name: String, val symbol: String, val colorHex: String) : LabUiEffect()
    data class SetWeb3DBurner(val enabled: Boolean) : LabUiEffect()
    object ResetWeb3D : LabUiEffect()
    data class ShowToast(val message: String) : LabUiEffect()
}

class LabViewModel(application: Application) : AndroidViewModel(application) {

    private val db = LabDatabase.getDatabase(application)
    private val dao = db.labRecordDao()

    private val _reagentsInBeaker = MutableStateFlow<List<Reagent>>(emptyList())
    val reagentsInBeaker = _reagentsInBeaker.asStateFlow()

    private val _currentReaction = MutableStateFlow<ChemicalReaction?>(null)
    val currentReaction = _currentReaction.asStateFlow()

    private val _temperature = MutableStateFlow(24.5)
    val temperature = _temperature.asStateFlow()

    private val _ph = MutableStateFlow(7.0)
    val ph = _ph.asStateFlow()

    private val _isBunsenBurnerOn = MutableStateFlow(false)
    val isBunsenBurnerOn = _isBunsenBurnerOn.asStateFlow()

    private val _isStirrerActive = MutableStateFlow(false)
    val isStirrerActive = _isStirrerActive.asStateFlow()

    private val _isExploding = MutableStateFlow(false)
    val isExploding = _isExploding.asStateFlow()

    private val _isExplanationOpen = MutableStateFlow(false)
    val isExplanationOpen = _isExplanationOpen.asStateFlow()

    private val _isPeriodicTableOpen = MutableStateFlow(false)
    val isPeriodicTableOpen = _isPeriodicTableOpen.asStateFlow()

    private val _activeStepIndex = MutableStateFlow(0)
    val activeStepIndex = _activeStepIndex.asStateFlow()

    private val _effects = MutableSharedFlow<LabUiEffect>()
    val effects = _effects.asSharedFlow()

    private var temperatureJob: Job? = null

    val availableReagents = LabRepository.reagents
    val periodicElements = LabRepository.periodicElements

    fun setPeriodicTableOpen(open: Boolean) {
        _isPeriodicTableOpen.value = open
    }

    fun addReagent(reagent: Reagent) {
        val current = _reagentsInBeaker.value.toMutableList()
        if (current.none { it.id == reagent.id }) {
            current.add(reagent)
            _reagentsInBeaker.value = current

            viewModelScope.launch {
                _effects.emit(
                    LabUiEffect.AddWeb3DChemical(
                        name = reagent.name,
                        symbol = reagent.formula,
                        colorHex = reagent.colorHex
                    )
                )
            }

            evaluateMix(current)
        }
    }

    fun removeReagent(reagent: Reagent) {
        val current = _reagentsInBeaker.value.toMutableList()
        current.removeAll { it.id == reagent.id }
        _reagentsInBeaker.value = current
        if (current.isEmpty()) {
            resetLab()
        } else {
            evaluateMix(current)
        }
    }

    private fun evaluateMix(reagents: List<Reagent>) {
        val ids = reagents.map { it.id }.toSet()
        val match = LabRepository.findReaction(ids)

        if (match != null && match != _currentReaction.value) {
            _currentReaction.value = match
            _activeStepIndex.value = 0
            _isExplanationOpen.value = true

            // Trigger Animation & Haptics
            triggerReactionEffects(match)
        } else if (match == null) {
            _currentReaction.value = null
        }
    }

    private fun triggerReactionEffects(reaction: ChemicalReaction) {
        viewModelScope.launch {
            val isViolent = reaction.animationType == ReactionAnimationType.SODIUM_WATER ||
                    reaction.animationType == ReactionAnimationType.SODIUM_CHLORINE ||
                    reaction.animationType == ReactionAnimationType.MAGNESIUM_OXYGEN

            if (isViolent) {
                _isExploding.value = true
                vibrate(durationMs = 500, isExplosion = true)
            } else {
                vibrate(durationMs = 150, isExplosion = false)
            }

            _effects.emit(
                LabUiEffect.TriggerWeb3DReaction(
                    animationType = reaction.animationType.name,
                    equation = reaction.balancedEquation
                )
            )

            // Dynamic temperature and pH transition
            animateTempAndPh(
                startTemp = _temperature.value,
                endTemp = reaction.tempTrajectoryC.second,
                startPh = _ph.value,
                endPh = reaction.phTrajectory.second
            )

            if (isViolent) {
                delay(1200)
                _isExploding.value = false
            }
        }
    }

    private fun animateTempAndPh(startTemp: Double, endTemp: Double, startPh: Double, endPh: Double) {
        temperatureJob?.cancel()
        temperatureJob = viewModelScope.launch {
            val steps = 30
            val tempStep = (endTemp - startTemp) / steps
            val phStep = (endPh - startPh) / steps

            var currentT = startTemp
            var currentP = startPh

            repeat(steps) {
                delay(50)
                currentT += tempStep
                currentP += phStep
                _temperature.value = Math.round(currentT * 10.0) / 10.0
                _ph.value = Math.round(currentP * 10.0) / 10.0
            }
            _temperature.value = endTemp
            _ph.value = endPh
        }
    }

    fun toggleBunsenBurner() {
        val newState = !_isBunsenBurnerOn.value
        _isBunsenBurnerOn.value = newState
        viewModelScope.launch {
            _effects.emit(LabUiEffect.SetWeb3DBurner(newState))
        }

        temperatureJob?.cancel()
        temperatureJob = viewModelScope.launch {
            if (newState) {
                while (_isBunsenBurnerOn.value && _temperature.value < 280.0) {
                    delay(300)
                    _temperature.value = Math.round((_temperature.value + 4.2) * 10.0) / 10.0
                }
            } else {
                val targetTemp = _currentReaction.value?.tempTrajectoryC?.second ?: 24.5
                while (!_isBunsenBurnerOn.value && _temperature.value > targetTemp) {
                    delay(300)
                    _temperature.value = Math.max(targetTemp, Math.round((_temperature.value - 2.5) * 10.0) / 10.0)
                }
            }
        }
    }

    fun toggleStirrer() {
        _isStirrerActive.value = !_isStirrerActive.value
    }

    fun resetLab() {
        _reagentsInBeaker.value = emptyList()
        _currentReaction.value = null
        _temperature.value = 24.5
        _ph.value = 7.0
        _isBunsenBurnerOn.value = false
        _isStirrerActive.value = false
        _isExploding.value = false
        _isExplanationOpen.value = false
        _activeStepIndex.value = 0
        temperatureJob?.cancel()

        viewModelScope.launch {
            _effects.emit(LabUiEffect.ResetWeb3D)
        }
    }

    fun setExplanationOpen(open: Boolean) {
        _isExplanationOpen.value = open
    }

    fun nextStep() {
        val rxn = _currentReaction.value ?: return
        if (_activeStepIndex.value < rxn.steps.size - 1) {
            _activeStepIndex.value += 1
        }
    }

    fun prevStep() {
        if (_activeStepIndex.value > 0) {
            _activeStepIndex.value -= 1
        }
    }

    fun saveExperimentToNotebook() {
        val rxn = _currentReaction.value
        val title = rxn?.title ?: "Lab Mixture Experiment"
        val equation = rxn?.balancedEquation ?: _reagentsInBeaker.value.joinToString(" + ") { it.formula }
        val observation = rxn?.observationNotes ?: "Custom mixture created in VirtuLab 3D."

        viewModelScope.launch {
            dao.insertRecord(
                LabRecordEntity(
                    title = title,
                    experimentType = "CHEMISTRY",
                    reactantsOrTopic = _reagentsInBeaker.value.joinToString(", ") { "${it.name} (${it.formula})" },
                    equationOrFormula = equation,
                    observations = observation,
                    finalTemperatureC = _temperature.value,
                    finalPh = _ph.value
                )
            )
            _effects.emit(LabUiEffect.ShowToast("Experiment saved to Lab Notebook!"))
        }
    }

    private fun vibrate(durationMs: Long, isExplosion: Boolean) {
        try {
            val context = getApplication<Application>()
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                val vibratorManager = context.getSystemService(Context.VIBRATOR_MANAGER_SERVICE) as? VibratorManager
                val vibrator = vibratorManager?.defaultVibrator
                if (isExplosion) {
                    val effect = VibrationEffect.createWaveform(longArrayOf(0, 150, 80, 200), intArrayOf(0, 255, 0, 255), -1)
                    vibrator?.vibrate(effect)
                } else {
                    vibrator?.vibrate(VibrationEffect.createOneShot(durationMs, VibrationEffect.DEFAULT_AMPLITUDE))
                }
            } else {
                @Suppress("DEPRECATION")
                val vibrator = context.getSystemService(Context.VIBRATOR_SERVICE) as? Vibrator
                @Suppress("DEPRECATION")
                vibrator?.vibrate(durationMs)
            }
        } catch (_: Exception) {}
    }
}
