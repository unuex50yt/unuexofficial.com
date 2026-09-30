package com.example.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CutCornerShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.ViewInAr
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.R
import com.example.data.Product
import com.example.data.ProductRepository
import com.example.ui.UnuexViewModel
import com.example.ui.components.CyberButton
import com.example.ui.components.CyberGlassPanel
import com.example.ui.components.CyberRatingStars
import com.example.ui.components.CyberSectionHeader
import com.example.ui.theme.CyberBackground
import com.example.ui.theme.CyberBorderSubtle
import com.example.ui.theme.CyberCardBg
import com.example.ui.theme.CyberCyan
import com.example.ui.theme.CyberSurface
import com.example.ui.theme.CyberTextPrimary
import com.example.ui.theme.CyberTextSecondary
import com.example.ui.theme.HyperMagenta
import com.example.ui.theme.QuantumGold

@Composable
fun ShowroomScreen(
    viewModel: UnuexViewModel,
    modifier: Modifier = Modifier
) {
    val selectedCategory by viewModel.selectedCategory.collectAsStateWithLifecycle()
    val searchQuery by viewModel.searchQuery.collectAsStateWithLifecycle()
    val wishlistItems by viewModel.wishlistItems.collectAsStateWithLifecycle()

    val filteredProducts = ProductRepository.sampleProducts.filter { product ->
        val matchesCategory = (selectedCategory == "ALL" || product.category == selectedCategory)
        val matchesSearch = searchQuery.isBlank() ||
                product.name.contains(searchQuery, ignoreCase = true) ||
                product.codeName.contains(searchQuery, ignoreCase = true) ||
                product.description.contains(searchQuery, ignoreCase = true)
        matchesCategory && matchesSearch
    }

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .background(CyberBackground),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Hero Showcase Banner
        item {
            HeroShowroomBanner()
        }

        // Search Input
        item {
            OutlinedTextField(
                value = searchQuery,
                onValueChange = { viewModel.setSearchQuery(it) },
                placeholder = {
                    Text(
                        "Search 2050 Cybernetics & Augments...",
                        color = CyberTextSecondary,
                        fontSize = 13.sp
                    )
                },
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Default.Search,
                        contentDescription = "Search Icon",
                        tint = CyberCyan
                    )
                },
                singleLine = true,
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("search_cyberware_input"),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedContainerColor = CyberSurface,
                    unfocusedContainerColor = CyberSurface,
                    focusedBorderColor = CyberCyan,
                    unfocusedBorderColor = CyberBorderSubtle,
                    focusedTextColor = CyberTextPrimary,
                    unfocusedTextColor = CyberTextPrimary
                ),
                shape = CutCornerShape(8.dp)
            )
        }

        // Category Selection
        item {
            LazyRow(
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(ProductRepository.categories) { cat ->
                    val isSelected = cat == selectedCategory
                    Box(
                        modifier = Modifier
                            .testTag("category_chip_$cat")
                            .clip(CutCornerShape(6.dp))
                            .background(if (isSelected) CyberCyan else CyberSurface)
                            .border(
                                width = 1.dp,
                                color = if (isSelected) CyberCyan else CyberBorderSubtle,
                                shape = CutCornerShape(6.dp)
                            )
                            .clickable { viewModel.setCategory(cat) }
                            .padding(horizontal = 14.dp, vertical = 8.dp)
                    ) {
                        Text(
                            text = cat,
                            style = MaterialTheme.typography.labelSmall.copy(
                                color = if (isSelected) CyberBackground else CyberTextPrimary,
                                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium
                            )
                        )
                    }
                }
            }
        }

        // Section Header
        item {
            CyberSectionHeader(
                title = "CYBERNETIC CATALOG",
                codeTag = "${filteredProducts.size} ITEMS ACTIVE"
            )
        }

        // Products List
        items(filteredProducts) { product ->
            val isFavorite = wishlistItems.any { it.productId == product.id }
            ProductCardItem(
                product = product,
                isFavorite = isFavorite,
                onToggleFavorite = { viewModel.toggleWishlist(product) },
                onOpenArPreview = { viewModel.openArCustomizerForProduct(product) },
                onAddToCart = { viewModel.addToCart(product) }
            )
        }
    }
}

@Composable
fun HeroShowroomBanner() {
    CyberGlassPanel(
        modifier = Modifier.fillMaxWidth(),
        borderColor = CyberCyan,
        cornerSize = 16.dp
    ) {
        Column {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(160.dp)
                    .clip(CutCornerShape(12.dp))
            ) {
                Image(
                    painter = painterResource(id = R.drawable.img_hero_cyber_showroom_1786530708209),
                    contentDescription = "UNUEX 2050 Cyber Showroom",
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.fillMaxSize()
                )
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(
                            brush = Brush.verticalGradient(
                                colors = listOf(
                                    Color.Transparent,
                                    CyberBackground.copy(alpha = 0.9f)
                                )
                            )
                        )
                )
                Text(
                    text = "NEO-2050 SHOWROOM",
                    style = MaterialTheme.typography.headlineLarge.copy(
                        color = CyberCyan,
                        fontSize = 20.sp,
                        letterSpacing = 2.sp
                    ),
                    modifier = Modifier
                        .align(Alignment.BottomStart)
                        .padding(12.dp)
                )
            }
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "Welcome to UNUEX. Next-generation neural, mobility, and energy technology crafted for the year 2050.",
                style = MaterialTheme.typography.bodyMedium.copy(color = CyberTextSecondary)
            )
        }
    }
}

@Composable
fun ProductCardItem(
    product: Product,
    isFavorite: Boolean,
    onToggleFavorite: () -> Unit,
    onOpenArPreview: () -> Unit,
    onAddToCart: () -> Unit
) {
    CyberGlassPanel(
        modifier = Modifier
            .fillMaxWidth()
            .testTag("product_card_${product.id}"),
        borderColor = CyberCyan.copy(alpha = 0.4f),
        cornerSize = 12.dp
    ) {
        Column {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.Top
            ) {
                Box(
                    modifier = Modifier
                        .size(110.dp)
                        .clip(CutCornerShape(8.dp))
                        .background(CyberCardBg)
                        .border(1.dp, CyberBorderSubtle, CutCornerShape(8.dp))
                ) {
                    Image(
                        painter = painterResource(id = product.drawableResId),
                        contentDescription = product.name,
                        contentScale = ContentScale.Crop,
                        modifier = Modifier.fillMaxSize()
                    )
                }

                Spacer(modifier = Modifier.width(12.dp))

                Column(modifier = Modifier.weight(1f)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = product.codeName,
                            style = MaterialTheme.typography.labelSmall.copy(
                                color = HyperMagenta,
                                fontFamily = FontFamily.Monospace
                            )
                        )
                        IconButton(
                            onClick = onToggleFavorite,
                            modifier = Modifier
                                .size(32.dp)
                                .testTag("favorite_button_${product.id}")
                        ) {
                            Icon(
                                imageVector = if (isFavorite) Icons.Default.Favorite else Icons.Default.FavoriteBorder,
                                contentDescription = "Favorite",
                                tint = if (isFavorite) HyperMagenta else CyberTextSecondary
                            )
                        }
                    }

                    Text(
                        text = product.name,
                        style = MaterialTheme.typography.titleLarge.copy(
                            color = CyberTextPrimary,
                            fontSize = 16.sp
                        )
                    )

                    Spacer(modifier = Modifier.height(4.dp))

                    CyberRatingStars(rating = product.rating)

                    Spacer(modifier = Modifier.height(6.dp))

                    Text(
                        text = "⟁ %,d CREDITS".format(product.priceCredits),
                        style = MaterialTheme.typography.titleMedium.copy(
                            color = QuantumGold,
                            fontWeight = FontWeight.Bold,
                            fontSize = 15.sp
                        )
                    )
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = product.description,
                style = MaterialTheme.typography.bodyMedium.copy(
                    color = CyberTextSecondary,
                    fontSize = 12.sp
                ),
                maxLines = 2
            )

            Spacer(modifier = Modifier.height(12.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                CyberButton(
                    text = "3D PREVIEW",
                    onClick = onOpenArPreview,
                    modifier = Modifier.weight(1f),
                    testTag = "ar_preview_btn_${product.id}",
                    accentColor = HyperMagenta,
                    containerColor = CyberSurface
                )

                CyberButton(
                    text = "ADD TO CART",
                    onClick = onAddToCart,
                    modifier = Modifier.weight(1f),
                    testTag = "add_to_cart_btn_${product.id}",
                    accentColor = CyberCyan,
                    containerColor = CyberSurface
                )
            }
        }
    }
}
