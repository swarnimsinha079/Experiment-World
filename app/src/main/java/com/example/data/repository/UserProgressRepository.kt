package com.example.data.repository

import android.content.Context
import com.example.data.local.ApparatusBadgeEntity
import com.example.data.local.LabDatabase
import com.example.data.local.UserProgressEntity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

enum class ProgressEventType {
    EXPERIMENT_COMPLETED,
    DEEP_RESEARCH_READ,
    MATH_PROOF_SOLVED,
    AI_QUERY_ASKED
}

data class BadgeUnlockNotification(
    val badge: ApparatusBadgeEntity,
    val newTotalRP: Int
)

class UserProgressRepository(context: Context) {
    private val db = LabDatabase.getDatabase(context)
    private val dao = db.userProgressDao()
    private val scope = CoroutineScope(Dispatchers.IO)

    val progressFlow: Flow<UserProgressEntity> = dao.getProgressFlow()
        .map { it ?: UserProgressEntity() }

    val badgesFlow: Flow<List<ApparatusBadgeEntity>> = dao.getAllBadgesFlow()

    private val _unlockEventFlow = MutableSharedFlow<BadgeUnlockNotification>()
    val unlockEventFlow = _unlockEventFlow.asSharedFlow()

    init {
        scope.launch {
            ensureInitialized()
        }
    }

    suspend fun ensureInitialized() {
        val currentProgress = dao.getProgressOnce()
        if (currentProgress == null) {
            dao.insertOrUpdateProgress(UserProgressEntity())
        }

        val currentBadges = dao.getAllBadgesOnce()
        if (currentBadges.isEmpty()) {
            val defaultBadges = listOf(
                ApparatusBadgeEntity(
                    badgeId = "BEAKER_MASTER",
                    title = "Borosilicate Beaker Master",
                    apparatusName = "Borosilicate Beaker (Grade A+)",
                    description = "Unlocks laboratory glassware with thermal shock resilience up to 500°C.",
                    requiredRP = 50,
                    iconResName = "science"
                ),
                ApparatusBadgeEntity(
                    badgeId = "BUNSEN_PYRO",
                    title = "Bunsen Pyrotechnician",
                    apparatusName = "High-Intensity Plasma Burner",
                    description = "Unlocks adjustable oxidant intake and blue cone heating up to 300°C.",
                    requiredRP = 150,
                    iconResName = "local_fire_department"
                ),
                ApparatusBadgeEntity(
                    badgeId = "ANALYTICAL_BALANCE",
                    title = "Precision Analytical Balance",
                    apparatusName = "Mettler Microbalance (0.0001g)",
                    description = "Unlocks analytical mass weighing with electrostatic draft shielding.",
                    requiredRP = 300,
                    iconResName = "scale"
                ),
                ApparatusBadgeEntity(
                    badgeId = "CRYO_STIRRER",
                    title = "Magnetic Cryo-Agitator",
                    apparatusName = "Dual-Speed Vortex Stirrer",
                    description = "Unlocks high-RPM vortex cavitation with Peltier cooling plate.",
                    requiredRP = 500,
                    iconResName = "autorenew"
                ),
                ApparatusBadgeEntity(
                    badgeId = "ROTARY_EVAP",
                    title = "High-Vacuum Rotovap",
                    apparatusName = "Rotary Evaporator Module",
                    description = "Unlocks accelerated solvent distillation and crystalline precipitation.",
                    requiredRP = 750,
                    iconResName = "filter_drama"
                ),
                ApparatusBadgeEntity(
                    badgeId = "SPECTROMETER",
                    title = "Quantum Spectrometer",
                    apparatusName = "Mass Spectrometer Ion Beam",
                    description = "Unlocks molecular ionization mass-to-charge (m/z) spectral detection.",
                    requiredRP = 1000,
                    iconResName = "bolt"
                )
            )
            dao.insertBadgesInitial(defaultBadges)
        }
    }

    suspend fun awardPoints(pointsToAdd: Int, eventType: ProgressEventType): List<ApparatusBadgeEntity> {
        ensureInitialized()
        val current = dao.getProgressOnce() ?: UserProgressEntity()
        val newRP = current.researchPoints + pointsToAdd

        val (levelNum, rankTitle) = calculateRank(newRP)

        val updatedProgress = current.copy(
            researchPoints = newRP,
            levelNumber = levelNum,
            rankTitle = rankTitle,
            experimentsCompleted = if (eventType == ProgressEventType.EXPERIMENT_COMPLETED) current.experimentsCompleted + 1 else current.experimentsCompleted,
            articlesRead = if (eventType == ProgressEventType.DEEP_RESEARCH_READ) current.articlesRead + 1 else current.articlesRead,
            mathProofsCompleted = if (eventType == ProgressEventType.MATH_PROOF_SOLVED) current.mathProofsCompleted + 1 else current.mathProofsCompleted
        )

        dao.insertOrUpdateProgress(updatedProgress)

        // Check Badge Unlocks
        val allBadges = dao.getAllBadgesOnce()
        val newlyUnlocked = mutableListOf<ApparatusBadgeEntity>()

        for (badge in allBadges) {
            if (!badge.isUnlocked && newRP >= badge.requiredRP) {
                val unlockedBadge = badge.copy(
                    isUnlocked = true,
                    unlockedAt = System.currentTimeMillis()
                )
                dao.updateBadge(unlockedBadge)
                newlyUnlocked.add(unlockedBadge)
                _unlockEventFlow.emit(BadgeUnlockNotification(unlockedBadge, newRP))
            }
        }

        return newlyUnlocked
    }

    private fun calculateRank(rp: Int): Pair<Int, String> {
        return when {
            rp < 100 -> Pair(1, "Lab Novice")
            rp < 250 -> Pair(2, "Apprentice Chemist")
            rp < 500 -> Pair(3, "Synthesis Specialist")
            rp < 1000 -> Pair(4, "Senior Investigator")
            else -> Pair(5, "Quantum Fellow")
        }
    }
}
