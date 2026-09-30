package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CutCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material.icons.filled.ShoppingBag
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.data.CartItemEntity
import com.example.data.OrderEntity
import com.example.data.UserProfileEntity
import com.example.ui.UnuexViewModel
import com.example.ui.components.CyberButton
import com.example.ui.components.CyberCreditBadge
import com.example.ui.components.CyberGlassPanel
import com.example.ui.components.CyberSectionHeader
import com.example.ui.theme.BioGreen
import com.example.ui.theme.CyberBackground
import com.example.ui.theme.CyberBorderSubtle
import com.example.ui.theme.CyberCyan
import com.example.ui.theme.CyberSurface
import com.example.ui.theme.CyberTextPrimary
import com.example.ui.theme.CyberTextSecondary
import com.example.ui.theme.HyperMagenta
import com.example.ui.theme.QuantumGold

@Composable
fun CartAndOrdersScreen(
    viewModel: UnuexViewModel,
    modifier: Modifier = Modifier
) {
    val userProfile by viewModel.userProfile.collectAsStateWithLifecycle()
    val cartItems by viewModel.cartItems.collectAsStateWithLifecycle()
    val orders by viewModel.orders.collectAsStateWithLifecycle()

    val profile = userProfile ?: UserProfileEntity()
    val totalCost = cartItems.sumOf { it.priceCredits * it.quantity }

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .background(CyberBackground),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // User Profile & Credits Card
        item {
            UserProfileCard(
                profile = profile,
                onRecharge = { viewModel.rechargeCredits(25000L) }
            )
        }

        // Section Header Cart
        item {
            CyberSectionHeader(
                title = "CYBER CART MANIFEST",
                codeTag = "${cartItems.size} CONFIGS",
                accentColor = CyberCyan
            )
        }

        // Cart Items List
        if (cartItems.isEmpty()) {
            item {
                CyberGlassPanel(
                    modifier = Modifier.fillMaxWidth(),
                    borderColor = CyberBorderSubtle,
                    cornerSize = 12.dp
                ) {
                    Column(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Icon(
                            imageVector = Icons.Default.ShoppingBag,
                            contentDescription = "Empty Cart",
                            tint = CyberTextSecondary,
                            modifier = Modifier.size(48.dp)
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = "CYBER CART IS EMPTY",
                            style = MaterialTheme.typography.labelLarge.copy(
                                color = CyberTextSecondary,
                                fontSize = 14.sp
                            )
                        )
                    }
                }
            }
        } else {
            items(cartItems) { item ->
                CartItemRow(
                    item = item,
                    onUpdateQuantity = { qty -> viewModel.updateCartQuantity(item.id, qty) },
                    onDelete = { viewModel.deleteCartItem(item.id) }
                )
            }

            // Checkout Summary Card
            item {
                CyberGlassPanel(
                    modifier = Modifier.fillMaxWidth(),
                    borderColor = QuantumGold,
                    cornerSize = 12.dp
                ) {
                    Column {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(
                                text = "TOTAL QUANTUM COST:",
                                style = MaterialTheme.typography.labelLarge.copy(
                                    color = CyberTextPrimary,
                                    fontSize = 14.sp
                                )
                            )
                            Text(
                                text = "⟁ %,d CREDITS".format(totalCost),
                                style = MaterialTheme.typography.labelLarge.copy(
                                    color = QuantumGold,
                                    fontFamily = FontFamily.Monospace,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 16.sp
                                )
                            )
                        }

                        Spacer(modifier = Modifier.height(12.dp))

                        CyberButton(
                            text = "EXECUTE QUANTUM CHECKOUT",
                            onClick = { viewModel.checkoutCart() },
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(50.dp),
                            testTag = "execute_checkout_btn",
                            accentColor = QuantumGold,
                            containerColor = CyberSurface
                        )
                    }
                }
            }
        }

        // Section Header Order History
        item {
            CyberSectionHeader(
                title = "TELEPORT DISPATCH LOGS",
                codeTag = "${orders.size} ORDERS",
                accentColor = BioGreen
            )
        }

        // Order History Items
        if (orders.isEmpty()) {
            item {
                Text(
                    text = "No previous teleport orders logged.",
                    style = MaterialTheme.typography.bodyMedium.copy(color = CyberTextSecondary)
                )
            }
        } else {
            items(orders) { order ->
                OrderLogRow(order)
            }
        }
    }
}

@Composable
fun UserProfileCard(
    profile: UserProfileEntity,
    onRecharge: () -> Unit
) {
    CyberGlassPanel(
        modifier = Modifier.fillMaxWidth(),
        borderColor = QuantumGold,
        cornerSize = 16.dp
    ) {
        Column {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        text = "OPERATOR TELEMETRY",
                        style = MaterialTheme.typography.labelSmall.copy(
                            color = QuantumGold,
                            fontFamily = FontFamily.Monospace,
                            fontSize = 10.sp
                        )
                    )
                    Text(
                        text = profile.cyberRank,
                        style = MaterialTheme.typography.titleLarge.copy(
                            color = CyberTextPrimary,
                            fontWeight = FontWeight.Bold
                        )
                    )
                }

                CyberCreditBadge(
                    amount = profile.creditBalance,
                    onClick = onRecharge
                )
            }

            Spacer(modifier = Modifier.height(10.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = "NEURAL SYNC: %.1f%%".format(profile.neuralSyncRate),
                    style = MaterialTheme.typography.labelSmall.copy(
                        color = BioGreen,
                        fontFamily = FontFamily.Monospace
                    )
                )
                Text(
                    text = "UNLOCKED TECH: ${profile.unlockedTechCount}",
                    style = MaterialTheme.typography.labelSmall.copy(
                        color = CyberCyan,
                        fontFamily = FontFamily.Monospace
                    )
                )
            }

            Spacer(modifier = Modifier.height(10.dp))

            CyberButton(
                text = "+CLAIM 25,000 BONUS CREDITS",
                onClick = onRecharge,
                modifier = Modifier.fillMaxWidth(),
                testTag = "recharge_credits_btn",
                accentColor = QuantumGold,
                containerColor = CyberSurface
            )
        }
    }
}

@Composable
fun CartItemRow(
    item: CartItemEntity,
    onUpdateQuantity: (Int) -> Unit,
    onDelete: () -> Unit
) {
    CyberGlassPanel(
        modifier = Modifier.fillMaxWidth(),
        borderColor = CyberCyan.copy(alpha = 0.5f),
        cornerSize = 10.dp
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = item.title,
                    style = MaterialTheme.typography.titleMedium.copy(
                        color = CyberTextPrimary,
                        fontSize = 15.sp
                    )
                )
                Text(
                    text = "Core: ${item.plasmaCoreColor} | ${item.powerWattage} W",
                    style = MaterialTheme.typography.labelSmall.copy(
                        color = HyperMagenta,
                        fontSize = 11.sp
                    )
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "⟁ %,d CREDITS".format(item.priceCredits * item.quantity),
                    style = MaterialTheme.typography.labelLarge.copy(
                        color = QuantumGold,
                        fontFamily = FontFamily.Monospace
                    )
                )
            }

            Row(verticalAlignment = Alignment.CenterVertically) {
                IconButton(
                    onClick = { onUpdateQuantity(item.quantity - 1) },
                    modifier = Modifier
                        .size(32.dp)
                        .testTag("decrease_qty_${item.id}")
                ) {
                    Icon(
                        imageVector = Icons.Default.Remove,
                        contentDescription = "Decrease",
                        tint = CyberCyan
                    )
                }

                Text(
                    text = "${item.quantity}",
                    style = MaterialTheme.typography.labelLarge.copy(
                        color = CyberTextPrimary,
                        fontFamily = FontFamily.Monospace
                    ),
                    modifier = Modifier.padding(horizontal = 8.dp)
                )

                IconButton(
                    onClick = { onUpdateQuantity(item.quantity + 1) },
                    modifier = Modifier
                        .size(32.dp)
                        .testTag("increase_qty_${item.id}")
                ) {
                    Icon(
                        imageVector = Icons.Default.Add,
                        contentDescription = "Increase",
                        tint = CyberCyan
                    )
                }

                IconButton(
                    onClick = onDelete,
                    modifier = Modifier
                        .size(32.dp)
                        .testTag("delete_cart_item_${item.id}")
                ) {
                    Icon(
                        imageVector = Icons.Default.Delete,
                        contentDescription = "Delete",
                        tint = HyperMagenta
                    )
                }
            }
        }
    }
}

@Composable
fun OrderLogRow(order: OrderEntity) {
    CyberGlassPanel(
        modifier = Modifier.fillMaxWidth(),
        borderColor = BioGreen.copy(alpha = 0.5f),
        cornerSize = 10.dp
    ) {
        Column {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = order.orderId,
                    style = MaterialTheme.typography.labelLarge.copy(
                        color = BioGreen,
                        fontFamily = FontFamily.Monospace
                    )
                )
                Text(
                    text = order.status,
                    style = MaterialTheme.typography.labelSmall.copy(
                        color = QuantumGold,
                        fontWeight = FontWeight.Bold
                    )
                )
            }

            Spacer(modifier = Modifier.height(6.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = "${order.itemCount} Items Teleported",
                    style = MaterialTheme.typography.bodyMedium.copy(color = CyberTextSecondary)
                )
                Text(
                    text = "⟁ %,d CREDITS".format(order.totalCredits),
                    style = MaterialTheme.typography.labelMedium.copy(
                        color = QuantumGold,
                        fontFamily = FontFamily.Monospace
                    )
                )
            }
        }
    }
}
