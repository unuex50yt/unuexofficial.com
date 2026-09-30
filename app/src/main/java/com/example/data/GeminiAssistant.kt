package com.example.data

import com.example.BuildConfig
import com.google.firebase.ai.FirebaseAI
import com.google.firebase.ai.type.content
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

data class ChatMessage(
    val sender: String, // "USER" or "UNUEX_CORE"
    val text: String,
    val timestamp: Long = System.currentTimeMillis()
)

object GeminiAssistant {

    private val systemPrompt = """
        You are UNUEX Core 2050, the hyper-advanced artificial intelligence sales and cybernetic compatibility advisor for UNUEX - Neo Cyberpunk Store.
        You assist users in selecting futuristic 2050 technology including:
        - NeuralLink Matrix v9 (NLX-9000-BIO)
        - Aero-Glide Hoverboard 2050 (AGH-2050-ION)
        - Titanium Exo-Frame Harness (EXO-TITAN-X)
        - Compact Plasma Fusion Cell (PFC-MINI-2050)
        - Ocular HUD Smart Cyber Lenses (OCU-HUD-2050)
        - Pulse Ion Jet-Boots
        
        Keep your persona futuristic, sleek, precise, and tech-savvy (using cybernetic jargon like 'synaptic bandwidth', 'quantum power cells', 'biomechanical sync rate', 'cranial overclocking', 'UNUEX credits ⟁').
        Respond concisely in 2-4 sentences with futuristic enthusiasm.
    """.trimIndent()

    suspend fun generateCyberResponse(userPrompt: String): String = withContext(Dispatchers.IO) {
        try {
            val apiKey = BuildConfig.GEMINI_API_KEY
            if (!apiKey.isNullOrBlank() && apiKey != "MY_GEMINI_API_KEY") {
                val model = FirebaseAI.getInstance().getGenerativeModel("gemini-2.5-flash")
                val response = model.generateContent(
                    content {
                        text(systemPrompt)
                        text("User Inquiry: $userPrompt")
                    }
                )
                val textResult = response.text
                if (!textResult.isNullOrBlank()) {
                    return@withContext textResult.trim()
                }
            }
        } catch (e: Exception) {
            // Fallback to internal AI engine below
        }

        // Advanced local 2050 AI engine fallback
        val promptLower = userPrompt.lowercase()
        when {
            promptLower.contains("neural") || promptLower.contains("link") || promptLower.contains("brain") -> {
                "UNUEX Core Diagnostic: NeuralLink Matrix v9 provides 120 Tbps synaptical bandwidth with 0.04ms latency. Recommended cranial sync calibration: 99.4%. Fully compatible with Ocular HUD Lenses."
            }
            promptLower.contains("hoverboard") || promptLower.contains("fly") || promptLower.contains("mobility") -> {
                "UNUEX Mobility Advisory: Aero-Glide Hoverboard 2050 features dual ion thrusters hovering at 15 meters altitude. Reaches 220 km/h with zero ground friction. Estimated 750 kW power draw."
            }
            promptLower.contains("exo") || promptLower.contains("suit") || promptLower.contains("strength") -> {
                "UNUEX Combat Loadout: Titanium Exo-Frame Harness amplifies physical payload weight capacity by +1500 kg while dispersing 95% kinetic impact through force-field capacitors."
            }
            promptLower.contains("fusion") || promptLower.contains("plasma") || promptLower.contains("battery") || promptLower.contains("power") -> {
                "UNUEX Energy Analysis: Compact Plasma Fusion Cell yields 500 kW continuous zero-emission power with a 100-year cryo-contained tokamak core lifespan. Perfect for high-draw exo-harnesses."
            }
            promptLower.contains("loadout") || promptLower.contains("recommend") || promptLower.contains("best") -> {
                "UNUEX S-Class Recommended Loadout: NeuralLink Matrix v9 + Titanium Exo-Frame Harness + Compact Plasma Fusion Cell. Total Synergistic Compatibility Score: 99.8%. Total Cost: ⟁ 45,200 UNUEX Credits."
            }
            else -> {
                "UNUEX Core 2050 operational. Neural telemetry scan active for prompt: '$userPrompt'. All UNUEX cybernetics meet 2050 orbital defense safety standards. How can I augment your loadout today?"
            }
        }
    }
}
