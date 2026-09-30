package com.example.data

import com.example.R

data class Product(
    val id: String,
    val name: String,
    val codeName: String,
    val category: String,
    val priceCredits: Long,
    val rating: Float,
    val reviewsCount: Int,
    val drawableResId: Int,
    val description: String,
    val specs: List<Pair<String, String>>,
    val availableCores: List<String>,
    val defaultWattage: Int,
    val maxWattage: Int,
    val compatibilityScore: Int
)

object ProductRepository {

    val categories = listOf(
        "ALL",
        "NEURAL & BIO",
        "MOBILITY 2050",
        "EXO & ARMOR",
        "ENERGY & PLASMA"
    )

    val sampleProducts = listOf(
        Product(
            id = "unuex_001",
            name = "NeuralLink Matrix v9",
            codeName = "NLX-9000-BIO",
            category = "NEURAL & BIO",
            priceCredits = 12500L,
            rating = 4.9f,
            reviewsCount = 1420,
            drawableResId = R.drawable.img_neural_link_1786530723416,
            description = "Direct cranial neural interface with sub-millisecond synaptical bandwidth. Enables instant holographic telepathy, AR memory indexing, and cognitive overclocking up to 240%.",
            specs = listOf(
                "Sync Bandwidth" to "120 Tbps",
                "Neural Latency" to "0.04 ms",
                "Shield Rating" to "EM-Proof Level 5",
                "Power Cell" to "Micro Bio-Fusion"
            ),
            availableCores = listOf("CYBER CYAN", "HYPER MAGENTA", "NEON VIOLET", "QUANTUM GOLD"),
            defaultWattage = 4500,
            maxWattage = 9000,
            compatibilityScore = 99
        ),
        Product(
            id = "unuex_002",
            name = "Aero-Glide Hoverboard 2050",
            codeName = "AGH-2050-ION",
            category = "MOBILITY 2050",
            priceCredits = 18900L,
            rating = 4.95f,
            reviewsCount = 890,
            drawableResId = R.drawable.img_hoverboard_2050_1786530735193,
            description = "Dual ion-thrust anti-gravity levitation board with auto-balancing vector gyros. Reaches speeds of up to 220 km/h with zero ground contact friction.",
            specs = listOf(
                "Top Velocity" to "220 km/h",
                "Levitation Ceiling" to "15 Meters",
                "Gyro Matrix" to "Quad Quantum-G",
                "Core Voltage" to "750 kW"
            ),
            availableCores = listOf("HYPER MAGENTA", "CYBER CYAN", "QUANTUM GOLD"),
            defaultWattage = 7500,
            maxWattage = 15000,
            compatibilityScore = 96
        ),
        Product(
            id = "unuex_003",
            name = "Titanium Exo-Frame Harness",
            codeName = "EXO-TITAN-X",
            category = "EXO & ARMOR",
            priceCredits = 24500L,
            rating = 4.88f,
            reviewsCount = 612,
            drawableResId = R.drawable.img_exo_suit_harness_1786530747804,
            description = "Full muscular amplification harness with carbon-titanium hydraulics and force-field barrier emitters. Increases physical lifting capacity by 15x while absorbing 95% kinetic impact.",
            specs = listOf(
                "Lift Augmentation" to "+1500 kg",
                "Armor Alloy" to "Titanium-Nanotube",
                "Shield Capacitor" to "1.2 Gigajoules",
                "Weight Class" to "Ultra Light (12kg)"
            ),
            availableCores = listOf("CYBER CYAN", "NEON VIOLET", "QUANTUM GOLD"),
            defaultWattage = 12000,
            maxWattage = 25000,
            compatibilityScore = 94
        ),
        Product(
            id = "unuex_004",
            name = "Compact Plasma Fusion Cell",
            codeName = "PFC-MINI-2050",
            category = "ENERGY & PLASMA",
            priceCredits = 8200L,
            rating = 4.92f,
            reviewsCount = 2100,
            drawableResId = R.drawable.img_hero_cyber_showroom_1786530708209,
            description = "Zero-emission tokamak mini fusion cell. Provides endless clean power for exo-suits, neural rigs, and personal anti-gravity propulsion systems with 100-year core lifespan.",
            specs = listOf(
                "Power Output" to "500 kW Continuous",
                "Core Lifespan" to "100 Years",
                "Thermal Control" to "Cryo-Containment",
                "Weight" to "1.8 kg"
            ),
            availableCores = listOf("CYBER CYAN", "HYPER MAGENTA", "QUANTUM GOLD"),
            defaultWattage = 5000,
            maxWattage = 10000,
            compatibilityScore = 100
        ),
        Product(
            id = "unuex_005",
            name = "Ocular HUD Smart Cyber Lenses",
            codeName = "OCU-HUD-2050",
            category = "NEURAL & BIO",
            priceCredits = 6500L,
            rating = 4.75f,
            reviewsCount = 1850,
            drawableResId = R.drawable.img_neural_link_1786530723416,
            description = "Sub-surface bio-retinal contacts featuring thermal spectrum overlay, 100x optical zoom, real-time facial threat recognition, and direct satellite telemetry overlay.",
            specs = listOf(
                "Display Tech" to "Micro LED Quantum AR",
                "Spectral Modes" to "Thermal, Infra, UV, X-Ray",
                "Optical Zoom" to "100x Lossless",
                "Battery" to "Bio-Kinetic Charged"
            ),
            availableCores = listOf("CYBER CYAN", "NEON VIOLET"),
            defaultWattage = 1500,
            maxWattage = 3000,
            compatibilityScore = 98
        ),
        Product(
            id = "unuex_006",
            name = "Pulse Ion Jet-Boots",
            codeName = "ION-JET-BOOTS-V2",
            category = "MOBILITY 2050",
            priceCredits = 14200L,
            rating = 4.87f,
            reviewsCount = 540,
            drawableResId = R.drawable.img_hoverboard_2050_1786530735193,
            description = "Compact micro-thruster footwear designed for vertical urban navigation and high-speed emergency landing deceleration.",
            specs = listOf(
                "Thrust Vector" to "360 Deg Omnidirectional",
                "Max Lift" to "250 kg Payload",
                "Flight Duration" to "45 Mins Continuous"
            ),
            availableCores = listOf("HYPER MAGENTA", "CYBER CYAN"),
            defaultWattage = 6000,
            maxWattage = 12000,
            compatibilityScore = 92
        )
    )

    fun getProductById(id: String): Product? {
        return sampleProducts.find { it.id == id } ?: sampleProducts.firstOrNull()
    }
}
