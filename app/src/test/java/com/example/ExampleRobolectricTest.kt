package com.example

import android.content.Context
import androidx.test.core.app.ApplicationProvider
import com.example.data.repository.LabRepository
import com.example.data.repository.ProgressEventType
import com.example.data.repository.UserProgressRepository
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@RunWith(RobolectricTestRunner::class)
@Config(sdk = [36])
class ExampleRobolectricTest {

    @Test
    fun `read string from context`() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        val appName = context.getString(R.string.app_name)
        assertEquals("VirtuLab: 3D Sci-Math", appName)
    }

    @Test
    fun `verify sodium and water chemical reaction matching`() {
        val reaction = LabRepository.findReaction(setOf("Na", "H2O"))
        assertNotNull(reaction)
        assertEquals("RXN_NA_H2O", reaction?.id)
        assertEquals("2Na(s) + 2H₂O(l) → 2NaOH(aq) + H₂(g) ↑", reaction?.balancedEquation)
        assertEquals(4, reaction?.steps?.size)
    }

    @Test
    fun `verify sodium and chlorine table salt synthesis reaction`() {
        val reaction = LabRepository.findReaction(setOf("Na", "Cl"))
        assertNotNull(reaction)
        assertEquals("RXN_NA_CL", reaction?.id)
        assertEquals("2Na(s) + Cl₂(g) → 2NaCl(s)", reaction?.balancedEquation)
        assertEquals(4, reaction?.steps?.size)
    }

    @Test
    fun `verify periodic table element catalog`() {
        val elements = LabRepository.periodicElements
        assertTrue(elements.size >= 20)

        val sodium = elements.find { it.id == "Na" }
        assertNotNull(sodium)
        assertEquals(11, sodium?.atomicNumber)
        assertEquals("Sodium", sodium?.name)

        val chlorine = elements.find { it.id == "Cl" }
        assertNotNull(chlorine)
        assertEquals(17, chlorine?.atomicNumber)
        assertEquals("Chlorine", chlorine?.name)
    }

    @Test
    fun `verify algebraic identity numerical calculation`() {
        val a = 4.0
        val b = 3.0
        val lhs = (a + b) * (a + b)
        val rhs = (a * a) + (2 * a * b) + (b * b)
        assertEquals(49.0, lhs, 0.001)
        assertEquals(49.0, rhs, 0.001)
    }

    @Test
    fun `verify research points tracking and apparatus badge unlocks`() = runBlocking {
        val context = ApplicationProvider.getApplicationContext<Context>()
        val progressRepo = UserProgressRepository(context)

        // Award 50 points for completing a reaction
        val unlocks1 = progressRepo.awardPoints(50, ProgressEventType.EXPERIMENT_COMPLETED)
        assertEquals(1, unlocks1.size)
        assertEquals("BEAKER_MASTER", unlocks1.first().badgeId)
        assertTrue(unlocks1.first().isUnlocked)

        // Award 100 points for reading deep research
        val unlocks2 = progressRepo.awardPoints(100, ProgressEventType.DEEP_RESEARCH_READ)
        assertEquals(1, unlocks2.size)
        assertEquals("BUNSEN_PYRO", unlocks2.first().badgeId)
        assertTrue(unlocks2.first().isUnlocked)
    }
}
