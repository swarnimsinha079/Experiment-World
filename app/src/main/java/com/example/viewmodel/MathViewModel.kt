package com.example.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.local.LabDatabase
import com.example.data.local.LabRecordEntity
import com.example.data.model.MathCategory
import com.example.data.model.MathTopic
import com.example.data.repository.LabRepository
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class MathViewModel(application: Application) : AndroidViewModel(application) {

    private val db = LabDatabase.getDatabase(application)
    private val dao = db.labRecordDao()

    val allTopics = LabRepository.mathTopics

    private val _selectedTopic = MutableStateFlow(allTopics.first())
    val selectedTopic = _selectedTopic.asStateFlow()

    private val _searchQuery = MutableStateFlow("")
    val searchQuery = _searchQuery.asStateFlow()

    private val _selectedCategory = MutableStateFlow<MathCategory?>(null)
    val selectedCategory = _selectedCategory.asStateFlow()

    // Identity (a + b)² State
    private val _variableA = MutableStateFlow(4.0f)
    val variableA = _variableA.asStateFlow()

    private val _variableB = MutableStateFlow(3.0f)
    val variableB = _variableB.asStateFlow()

    private val _explodeGapFraction = MutableStateFlow(0.0f)
    val explodeGapFraction = _explodeGapFraction.asStateFlow()

    private val _isExploded = MutableStateFlow(false)
    val isExploded = _isExploded.asStateFlow()

    // Pythagoras State
    private val _pythagorasA = MutableStateFlow(3.0f)
    val pythagorasA = _pythagorasA.asStateFlow()

    private val _pythagorasB = MutableStateFlow(4.0f)
    val pythagorasB = _pythagorasB.asStateFlow()

    // Quadratic State
    private val _quadA = MutableStateFlow(1.0f)
    val quadA = _quadA.asStateFlow()

    private val _quadB = MutableStateFlow(-2.0f)
    val quadB = _quadB.asStateFlow()

    private val _quadC = MutableStateFlow(-3.0f)
    val quadC = _quadC.asStateFlow()

    // Unit Circle State
    private val _angleDeg = MutableStateFlow(45.0f)
    val angleDeg = _angleDeg.asStateFlow()

    private val _toastFlow = MutableSharedFlow<String>()
    val toastFlow = _toastFlow.asSharedFlow()

    fun selectTopic(topic: MathTopic) {
        _selectedTopic.value = topic
    }

    fun setSearchQuery(query: String) {
        _searchQuery.value = query
    }

    fun selectCategory(category: MathCategory?) {
        _selectedCategory.value = category
    }

    fun setVariableA(value: Float) {
        _variableA.value = Math.round(value * 10f) / 10f
    }

    fun setVariableB(value: Float) {
        _variableB.value = Math.round(value * 10f) / 10f
    }

    fun setExplodeGap(fraction: Float) {
        _explodeGapFraction.value = fraction
        _isExploded.value = fraction > 0.05f
    }

    fun toggleExplode() {
        val next = !_isExploded.value
        _isExploded.value = next
        _explodeGapFraction.value = if (next) 0.65f else 0.0f
    }

    fun setPythagorasA(value: Float) {
        _pythagorasA.value = Math.round(value * 10f) / 10f
    }

    fun setPythagorasB(value: Float) {
        _pythagorasB.value = Math.round(value * 10f) / 10f
    }

    fun setQuadA(v: Float) { _quadA.value = Math.round(v * 10f) / 10f }
    fun setQuadB(v: Float) { _quadB.value = Math.round(v * 10f) / 10f }
    fun setQuadC(v: Float) { _quadC.value = Math.round(v * 10f) / 10f }

    fun setAngleDeg(v: Float) {
        _angleDeg.value = Math.round(v * 10f) / 10f
    }

    fun saveMathNote() {
        val topic = _selectedTopic.value
        val summaryNote = when (topic.id) {
            "algebraic_identity" -> {
                val a = _variableA.value
                val b = _variableB.value
                val sum = a + b
                val sumSq = sum * sum
                val expanded = (a * a) + (2 * a * b) + (b * b)
                "Computed: ($a + $b)² = $sum² = $sumSq. Partition: a²=${a*a}, 2ab=${2*a*b}, b²=${b*b} (Sum=$expanded)"
            }
            "pythagorean_theorem" -> {
                val a = _pythagorasA.value
                val b = _pythagorasB.value
                val c = Math.sqrt((a * a + b * b).toDouble())
                "Computed: a=$a, b=$b -> Hypotenuse c=${Math.round(c * 100.0) / 100.0}. Area Check: ${a*a} + ${b*b} = ${Math.round(c*c*10.0)/10.0}"
            }
            else -> topic.summary
        }

        viewModelScope.launch {
            dao.insertRecord(
                LabRecordEntity(
                    title = topic.title,
                    experimentType = "MATH",
                    reactantsOrTopic = topic.category.name,
                    equationOrFormula = topic.formula,
                    observations = summaryNote,
                    finalTemperatureC = 0.0,
                    finalPh = 0.0
                )
            )
            _toastFlow.emit("Math visualization recorded in Lab Notebook!")
        }
    }
}
