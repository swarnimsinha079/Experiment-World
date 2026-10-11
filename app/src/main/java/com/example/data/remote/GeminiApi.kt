package com.example.data.remote

import com.example.BuildConfig
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.Body
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query
import java.util.concurrent.TimeUnit

@JsonClass(generateAdapter = true)
data class GeminiRequest(
    @Json(name = "contents") val contents: List<GeminiContent>,
    @Json(name = "generationConfig") val generationConfig: GeminiGenerationConfig? = null,
    @Json(name = "systemInstruction") val systemInstruction: GeminiContent? = null
)

@JsonClass(generateAdapter = true)
data class GeminiContent(
    @Json(name = "parts") val parts: List<GeminiPart>,
    @Json(name = "role") val role: String? = null
)

@JsonClass(generateAdapter = true)
data class GeminiPart(
    @Json(name = "text") val text: String? = null
)

@JsonClass(generateAdapter = true)
data class GeminiGenerationConfig(
    @Json(name = "temperature") val temperature: Float? = 0.7f,
    @Json(name = "thinkingConfig") val thinkingConfig: GeminiThinkingConfig? = null
)

@JsonClass(generateAdapter = true)
data class GeminiThinkingConfig(
    @Json(name = "thinkingLevel") val thinkingLevel: String = "high"
)

@JsonClass(generateAdapter = true)
data class GeminiResponse(
    @Json(name = "candidates") val candidates: List<GeminiCandidate>? = null,
    @Json(name = "error") val error: GeminiError? = null
)

@JsonClass(generateAdapter = true)
data class GeminiCandidate(
    @Json(name = "content") val content: GeminiContent? = null
)

@JsonClass(generateAdapter = true)
data class GeminiError(
    @Json(name = "message") val message: String? = null,
    @Json(name = "code") val code: Int? = null
)

interface GeminiService {
    @POST("v1beta/models/{model}:generateContent")
    suspend fun generateContent(
        @Path("model") model: String,
        @Query("key") apiKey: String,
        @Body request: GeminiRequest
    ): GeminiResponse
}

object GeminiApiClient {
    private const val BASE_URL = "https://generativelanguage.googleapis.com/"

    private val moshi = Moshi.Builder()
        .add(KotlinJsonAdapterFactory())
        .build()

    private val okHttpClient = OkHttpClient.Builder()
        .connectTimeout(60, TimeUnit.SECONDS)
        .readTimeout(60, TimeUnit.SECONDS)
        .writeTimeout(60, TimeUnit.SECONDS)
        .addInterceptor(HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BASIC
        })
        .build()

    val service: GeminiService by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(MoshiConverterFactory.create(moshi))
            .build()
            .create(GeminiService::class.java)
    }

    suspend fun askSciMathProfessor(
        userPrompt: String,
        history: List<GeminiContent> = emptyList(),
        useHighThinking: Boolean = false
    ): String {
        val apiKey = BuildConfig.GEMINI_API_KEY
        if (apiKey.isBlank() || apiKey == "MY_GEMINI_API_KEY") {
            return "Note: Gemini API Key is not configured yet in Secrets. Here is an educational simulation explanation:\n\n" +
                    getSimulatedExplanation(userPrompt)
        }

        val model = if (useHighThinking) "gemini-3.1-pro-preview" else "gemini-3.5-flash"

        val systemInstruction = GeminiContent(
            parts = listOf(
                GeminiPart(
                    text = "You are Dr. Sophie Vance, a world-class STEM professor and lead scientist at VirtuLab: 3D Sci-Math Interactive. " +
                            "Explain science, chemical reaction kinetics, thermodynamics, atomic structures, and mathematical proofs clearly, " +
                            "accurately, and engagingly. Use balanced equations, step-by-step logic, and real-world industrial context."
                )
            )
        )

        val fullContents = history + listOf(
            GeminiContent(
                parts = listOf(GeminiPart(text = userPrompt)),
                role = "user"
            )
        )

        val config = if (useHighThinking) {
            GeminiGenerationConfig(
                temperature = 0.4f,
                thinkingConfig = GeminiThinkingConfig(thinkingLevel = "high")
            )
        } else {
            GeminiGenerationConfig(temperature = 0.7f)
        }

        val request = GeminiRequest(
            contents = fullContents,
            generationConfig = config,
            systemInstruction = systemInstruction
        )

        return try {
            val response = service.generateContent(model, apiKey, request)
            response.candidates?.firstOrNull()?.content?.parts?.firstOrNull()?.text
                ?: response.error?.message
                ?: "No response generated by Dr. Vance."
        } catch (e: Exception) {
            "Connection note: ${e.localizedMessage ?: "Unable to contact API"}. Simulated insight: \n" +
                    getSimulatedExplanation(userPrompt)
        }
    }

    private fun getSimulatedExplanation(prompt: String): String {
        val lower = prompt.lowercase()
        return when {
            lower.contains("sodium") || lower.contains("water") || lower.contains("na + h2o") -> {
                "**Thermodynamics & Reaction Kinetics of Na + H₂O:**\n\n" +
                        "1. **Equation:** `2Na(s) + 2H₂O(l) → 2NaOH(aq) + H₂(g) ↑ (ΔH = -368 kJ/mol)`\n" +
                        "2. **Electron Transfer:** Sodium's outermost 3s¹ valence electron is impulsively yielded into the polar water matrix.\n" +
                        "3. **Thermal Spike:** Enthalpy release immediately melts the sodium metal (MP: 97.8°C), forming a silvery sphere skating over the water.\n" +
                        "4. **Hydrogen Deflagration:** The high thermal energy ignites the evolved H₂ gas in ambient oxygen (`2H₂ + O₂ → 2H₂O`), creating the iconic yellow-orange flame and acoustic burst!"
            }
            lower.contains("(a + b)") || lower.contains("algebra") || lower.contains("square") -> {
                "**Geometric Decomposition of (a + b)²:**\n\n" +
                        "1. **Identity:** `(a + b)² = a² + 2ab + b²`\n" +
                        "2. **Geometric Area Model:** A square of side length (a + b) has an area of (a + b)².\n" +
                        "3. **Partition:** Dividing this area along lengths 'a' and 'b' creates 4 distinct sections:\n" +
                        "   - One square of area `a × a = a²`\n" +
                        "   - Two identical rectangles of area `a × b` and `b × a` (totaling `2ab`)\n" +
                        "   - One square of area `b × b = b²`\n" +
                        "4. **Synthesis:** Summing the individual components gives `a² + 2ab + b²`, matching the total area identically!"
            }
            lower.contains("pythagor") || lower.contains("triangle") -> {
                "**The Pythagorean Theorem (a² + b² = c²):**\n\n" +
                        "In Euclidean 2D geometry, constructing physical squares on each side of a right triangle reveals that the area of the square on leg 'a' plus the area of the square on leg 'b' precisely equals the area of the square on hypotenuse 'c'."
            }
            else -> {
                "**Scientific Insight from Dr. Vance:**\n\n" +
                        "In both chemistry and mathematics, macroscopic phenomena directly reflect microscopic geometry and conservation laws. " +
                        "Mass, energy, and geometric volume remain rigorously conserved across physical transformations!"
            }
        }
    }
}
