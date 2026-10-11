package com.example.data.model

enum class ReagentCategory {
    ALKALI_METAL,
    ALKALINE_EARTH,
    TRANSITION_METAL,
    HALOGEN,
    NON_METAL,
    NOBLE_GAS,
    ACID,
    BASE,
    SALT,
    SOLVENT,
    OXIDIZER,
    INDICATOR
}

data class Reagent(
    val id: String,
    val name: String,
    val formula: String,
    val category: ReagentCategory,
    val state: String, // "Solid", "Gas", "Liquid", "Aqueous"
    val colorHex: String,
    val atomicWeight: Double,
    val description: String,
    val hazardNote: String,
    val atomicNumber: Int = 0,
    val group: Int = 0,
    val period: Int = 0,
    val electronConfig: String = ""
)

data class ReactionStep(
    val stepNumber: Int,
    val title: String,
    val subtitle: String,
    val explanation: String,
    val thermodynamicDetail: String,
    val safetyNote: String
)

enum class ReactionAnimationType {
    SODIUM_WATER,
    SODIUM_CHLORINE,
    MAGNESIUM_OXYGEN,
    NEUTRALIZATION,
    ELEPHANT_TOOTHPASTE,
    PRECIPITATION,
    EFFERVESCENCE,
    GENERIC
}

data class ChemicalReaction(
    val id: String,
    val title: String,
    val reactantIds: Set<String>,
    val balancedEquation: String,
    val animationType: ReactionAnimationType,
    val deltaH: String, // e.g., "-822 kJ/mol (Vigorous Synthesis)"
    val tempTrajectoryC: Pair<Double, Double>, // Start -> Peak
    val phTrajectory: Pair<Double, Double>, // Start -> Final
    val observationNotes: String,
    val steps: List<ReactionStep>
)

enum class MathCategory {
    ALGEBRA,
    GEOMETRY,
    CALCULUS,
    TRIGONOMETRY
}

data class MathTopic(
    val id: String,
    val title: String,
    val category: MathCategory,
    val formula: String,
    val summary: String,
    val detailedProof: String
)

data class ChatMessage(
    val id: String = java.util.UUID.randomUUID().toString(),
    val sender: MessageSender,
    val text: String,
    val timestamp: Long = System.currentTimeMillis(),
    val isThinking: Boolean = false
)

enum class MessageSender {
    USER,
    AI_PROFESSOR,
    SYSTEM
}
