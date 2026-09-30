package com.example.ui

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.CartItemEntity
import com.example.data.ChatMessage
import com.example.data.GeminiAssistant
import com.example.data.OrderEntity
import com.example.data.Product
import com.example.data.ProductRepository
import com.example.data.UnuexDatabase
import com.example.data.UserProfileEntity
import com.example.data.WishlistItemEntity
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.util.UUID

enum class UnuexTab {
    SHOWROOM,
    AR_PREVIEW,
    SCANNER,
    AI_CORE,
    CART_CREDITS
}

class UnuexViewModel(application: Application) : AndroidViewModel(application) {

    private val dao = UnuexDatabase.getDatabase(application).unuexDao()

    // Navigation state
    private val _currentTab = MutableStateFlow(UnuexTab.SHOWROOM)
    val currentTab: StateFlow<UnuexTab> = _currentTab.asStateFlow()

    // Search and filter
    private val _selectedCategory = MutableStateFlow("ALL")
    val selectedCategory: StateFlow<String> = _selectedCategory.asStateFlow()

    private val _searchQuery = MutableStateFlow("")
    val searchQuery: StateFlow<String> = _searchQuery.asStateFlow()

    // AR Preview & Customizer
    private val _selectedProductForAr = MutableStateFlow(ProductRepository.sampleProducts.first())
    val selectedProductForAr: StateFlow<Product> = _selectedProductForAr.asStateFlow()

    private val _selectedPlasmaCore = MutableStateFlow("CYBER CYAN")
    val selectedPlasmaCore: StateFlow<String> = _selectedPlasmaCore.asStateFlow()

    private val _powerWattage = MutableStateFlow(5000)
    val powerWattage: StateFlow<Int> = _powerWattage.asStateFlow()

    // Cyber Scanner
    private val _isScanning = MutableStateFlow(false)
    val isScanning: StateFlow<Boolean> = _isScanning.asStateFlow()

    private val _scanProgress = MutableStateFlow(0f)
    val scanProgress: StateFlow<Float> = _scanProgress.asStateFlow()

    private val _scanDiagnosticResult = MutableStateFlow<String?>(null)
    val scanDiagnosticResult: StateFlow<String?> = _scanDiagnosticResult.asStateFlow()

    // AI Core Chat
    private val _aiMessages = MutableStateFlow<List<ChatMessage>>(
        listOf(
            ChatMessage(
                sender = "UNUEX_CORE",
                text = "UNUEX Core 2050 Online. Neural link telemetry active. Ask me for custom cybernetic loadouts, tech specifications, or compatibility assessments."
            )
        )
    )
    val aiMessages: StateFlow<List<ChatMessage>> = _aiMessages.asStateFlow()

    private val _isAiThinking = MutableStateFlow(false)
    val isAiThinking: StateFlow<Boolean> = _isAiThinking.asStateFlow()

    // Notification toast / message
    private val _userNotice = MutableStateFlow<String?>(null)
    val userNotice: StateFlow<String?> = _userNotice.asStateFlow()

    // Room Persistent Flows
    val userProfile: StateFlow<UserProfileEntity?> = dao.getUserProfile().stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = null
    )

    val cartItems: StateFlow<List<CartItemEntity>> = dao.getCartItems().stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = emptyList()
    )

    val wishlistItems: StateFlow<List<WishlistItemEntity>> = dao.getWishlistItems().stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = emptyList()
    )

    val orders: StateFlow<List<OrderEntity>> = dao.getOrders().stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = emptyList()
    )

    init {
        // Initialize default user profile if null
        viewModelScope.launch {
            dao.insertOrUpdateProfile(
                UserProfileEntity(
                    id = 1,
                    creditBalance = 50000L,
                    cyberRank = "S-CLASS RUNNER",
                    neuralSyncRate = 99.2f,
                    unlockedTechCount = 18
                )
            )
        }
    }

    fun selectTab(tab: UnuexTab) {
        _currentTab.value = tab
    }

    fun setCategory(category: String) {
        _selectedCategory.value = category
    }

    fun setSearchQuery(query: String) {
        _searchQuery.value = query
    }

    fun openArCustomizerForProduct(product: Product) {
        _selectedProductForAr.value = product
        _selectedPlasmaCore.value = product.availableCores.firstOrNull() ?: "CYBER CYAN"
        _powerWattage.value = product.defaultWattage
        _currentTab.value = UnuexTab.AR_PREVIEW
    }

    fun setPlasmaCore(core: String) {
        _selectedPlasmaCore.value = core
    }

    fun setPowerWattage(wattage: Int) {
        _powerWattage.value = wattage
    }

    fun addToCart(
        product: Product,
        plasmaCore: String = _selectedPlasmaCore.value,
        wattage: Int = _powerWattage.value
    ) {
        viewModelScope.launch {
            dao.insertCartItem(
                CartItemEntity(
                    productId = product.id,
                    title = product.name,
                    priceCredits = product.priceCredits,
                    quantity = 1,
                    plasmaCoreColor = plasmaCore,
                    powerWattage = wattage
                )
            )
            showNotice("ADDED TO CYBER CART: ${product.name}")
        }
    }

    fun updateCartQuantity(id: Long, newQty: Int) {
        viewModelScope.launch {
            if (newQty <= 0) {
                dao.deleteCartItem(id)
            } else {
                dao.updateCartQuantity(id, newQty)
            }
        }
    }

    fun deleteCartItem(id: Long) {
        viewModelScope.launch {
            dao.deleteCartItem(id)
        }
    }

    fun toggleWishlist(product: Product) {
        viewModelScope.launch {
            val exists = wishlistItems.value.any { it.productId == product.id }
            if (exists) {
                dao.deleteWishlist(product.id)
                showNotice("REMOVED FROM SAVED TECH")
            } else {
                dao.insertWishlist(
                    WishlistItemEntity(
                        productId = product.id,
                        title = product.name,
                        category = product.category,
                        priceCredits = product.priceCredits,
                        rating = product.rating
                    )
                )
                showNotice("SAVED TO NEURAL WISHLIST")
            }
        }
    }

    fun checkoutCart() {
        viewModelScope.launch {
            val currentCart = cartItems.value
            if (currentCart.isEmpty()) return@launch

            val totalCost = currentCart.sumOf { it.priceCredits * it.quantity }
            val currentProfile = userProfile.value ?: UserProfileEntity()

            if (currentProfile.creditBalance < totalCost) {
                showNotice("INSUFFICIENT CREDITS! TAP RECHARGE TO CLAIM BONUS.")
                return@launch
            }

            val rowsDeducted = dao.deductCredits(totalCost)
            if (rowsDeducted > 0) {
                val newOrder = OrderEntity(
                    orderId = "UNX-" + UUID.randomUUID().toString().take(8).uppercase(),
                    totalCredits = totalCost,
                    itemCount = currentCart.sumOf { it.quantity },
                    status = "QUANTUM DISPATCHED",
                    deliveryEtaSeconds = 30
                )
                dao.insertOrder(newOrder)
                dao.clearCart()
                showNotice("ORDER CONFIRMED! TELEPORT DRONE DISPATCHED.")
            }
        }
    }

    fun rechargeCredits(amount: Long = 25000L) {
        viewModelScope.launch {
            dao.addCredits(amount)
            showNotice("+⟁ $amount QUANTUM CREDITS GRANTED!")
        }
    }

    fun startCompatibilityScan() {
        if (_isScanning.value) return
        viewModelScope.launch {
            _isScanning.value = true
            _scanProgress.value = 0f
            _scanDiagnosticResult.value = null

            for (i in 1..10) {
                delay(180)
                _scanProgress.value = i * 0.1f
            }

            _isScanning.value = false
            _scanDiagnosticResult.value = """
                BIO-MECHANICAL TELEMETRY DIAGNOSTIC
                ----------------------------------------
                Neural Sync Calibration: 99.4% [OPTIMAL]
                Bio-Energy Voltage: 4.8 kW [STABLE]
                Exo-Skeletal Load Capacity: 880 kg [READY]
                
                COMPATIBILITY VERDICT: 99.8% SYNERGY
                Recommended Augmentation: NeuralLink Matrix v9 & Plasma Cell PFC-2050
            """.trimIndent()
        }
    }

    fun sendAiMessage(userPrompt: String) {
        if (userPrompt.isBlank() || _isAiThinking.value) return

        val userMsg = ChatMessage(sender = "USER", text = userPrompt)
        _aiMessages.value = _aiMessages.value + userMsg
        _isAiThinking.value = true

        viewModelScope.launch {
            val aiResponseText = GeminiAssistant.generateCyberResponse(userPrompt)
            _isAiThinking.value = false
            val aiMsg = ChatMessage(sender = "UNUEX_CORE", text = aiResponseText)
            _aiMessages.value = _aiMessages.value + aiMsg
        }
    }

    fun clearNotice() {
        _userNotice.value = null
    }

    private fun showNotice(msg: String) {
        viewModelScope.launch {
            _userNotice.value = msg
            delay(3000)
            if (_userNotice.value == msg) {
                _userNotice.value = null
            }
        }
    }
}
