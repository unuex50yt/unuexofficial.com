package com.example.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "cart_items")
data class CartItemEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val productId: String,
    val title: String,
    val priceCredits: Long,
    val quantity: Int = 1,
    val plasmaCoreColor: String = "CYBER CYAN",
    val powerWattage: Int = 5000,
    val addedAt: Long = System.currentTimeMillis()
)

@Entity(tableName = "wishlist_items")
data class WishlistItemEntity(
    @PrimaryKey
    val productId: String,
    val title: String,
    val category: String,
    val priceCredits: Long,
    val rating: Float,
    val addedAt: Long = System.currentTimeMillis()
)

@Entity(tableName = "orders")
data class OrderEntity(
    @PrimaryKey
    val orderId: String,
    val totalCredits: Long,
    val itemCount: Int,
    val timestamp: Long = System.currentTimeMillis(),
    val status: String = "QUANTUM DISPATCHED",
    val deliveryEtaSeconds: Int = 45
)

@Entity(tableName = "user_profile")
data class UserProfileEntity(
    @PrimaryKey val id: Int = 1,
    val creditBalance: Long = 50000L,
    val cyberRank: String = "S-CLASS RUNNER",
    val neuralSyncRate: Float = 99.2f,
    val unlockedTechCount: Int = 16
)
