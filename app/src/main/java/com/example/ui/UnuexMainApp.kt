package com.example.ui

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.shape.CutCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.QrCodeScanner
import androidx.compose.material.icons.filled.ShoppingBag
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material.icons.filled.ViewInAr
import androidx.compose.material3.Badge
import androidx.compose.material3.BadgedBox
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.data.UserProfileEntity
import com.example.ui.components.CyberCreditBadge
import com.example.ui.components.CyberNoticeToast
import com.example.ui.screens.AiCoreScreen
import com.example.ui.screens.ArPreviewScreen
import com.example.ui.screens.CartAndOrdersScreen
import com.example.ui.screens.CyberScannerScreen
import com.example.ui.screens.ShowroomScreen
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
fun UnuexMainApp(
    viewModel: UnuexViewModel
) {
    val currentTab by viewModel.currentTab.collectAsStateWithLifecycle()
    val userProfile by viewModel.userProfile.collectAsStateWithLifecycle()
    val cartItems by viewModel.cartItems.collectAsStateWithLifecycle()
    val noticeMessage by viewModel.userNotice.collectAsStateWithLifecycle()

    val profile = userProfile ?: UserProfileEntity()
    val cartCount = cartItems.sumOf { it.quantity }

    Scaffold(
        modifier = Modifier
            .fillMaxSize()
            .background(CyberBackground),
        topBar = {
            CyberTopAppBar(
                profile = profile,
                onRecharge = { viewModel.rechargeCredits(25000L) }
            )
        },
        bottomBar = {
            CyberBottomNavBar(
                currentTab = currentTab,
                cartCount = cartCount,
                onSelectTab = { viewModel.selectTab(it) }
            )
        },
        containerColor = CyberBackground
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            AnimatedContent(
                targetState = currentTab,
                transitionSpec = { fadeIn() togetherWith fadeOut() },
                label = "ScreenTransition"
            ) { tab ->
                when (tab) {
                    UnuexTab.SHOWROOM -> ShowroomScreen(viewModel = viewModel)
                    UnuexTab.AR_PREVIEW -> ArPreviewScreen(viewModel = viewModel)
                    UnuexTab.SCANNER -> CyberScannerScreen(viewModel = viewModel)
                    UnuexTab.AI_CORE -> AiCoreScreen(viewModel = viewModel)
                    UnuexTab.CART_CREDITS -> CartAndOrdersScreen(viewModel = viewModel)
                }
            }

            // Notice Toast Overlay
            CyberNoticeToast(
                message = noticeMessage,
                onDismiss = { viewModel.clearNotice() }
            )
        }
    }
}

@Composable
fun CyberTopAppBar(
    profile: UserProfileEntity,
    onRecharge: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(CyberSurface)
            .border(1.dp, CyberBorderSubtle)
            .statusBarsPadding()
            .padding(horizontal = 16.dp, vertical = 10.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(
                    modifier = Modifier
                        .size(36.dp)
                        .clip(CutCornerShape(8.dp))
                        .background(CyberCyan)
                        .padding(2.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "UNX",
                        style = MaterialTheme.typography.labelLarge.copy(
                            color = CyberBackground,
                            fontWeight = FontWeight.Bold,
                            fontSize = 11.sp
                        )
                    )
                }

                Spacer(modifier = Modifier.width(10.dp))

                Column {
                    Text(
                        text = "UNUEX 2050",
                        style = MaterialTheme.typography.titleLarge.copy(
                            color = CyberTextPrimary,
                            fontSize = 16.sp,
                            letterSpacing = 1.2.sp
                        )
                    )
                    Text(
                        text = "SYNC RATE: %.1f%%".format(profile.neuralSyncRate),
                        style = MaterialTheme.typography.labelSmall.copy(
                            color = BioGreen,
                            fontSize = 9.sp,
                            fontFamily = FontFamily.Monospace
                        )
                    )
                }
            }

            CyberCreditBadge(
                amount = profile.creditBalance,
                onClick = onRecharge
            )
        }
    }
}

@Composable
fun CyberBottomNavBar(
    currentTab: UnuexTab,
    cartCount: Int,
    onSelectTab: (UnuexTab) -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(CyberSurface)
            .border(1.dp, CyberBorderSubtle)
            .windowInsetsPadding(WindowInsets.navigationBars)
            .padding(vertical = 8.dp),
        horizontalArrangement = Arrangement.SpaceAround,
        verticalAlignment = Alignment.CenterVertically
    ) {
        NavItem(
            label = "SHOWROOM",
            icon = Icons.Default.ShoppingBag,
            isSelected = currentTab == UnuexTab.SHOWROOM,
            onClick = { onSelectTab(UnuexTab.SHOWROOM) },
            testTag = "nav_tab_showroom"
        )

        NavItem(
            label = "3D AR",
            icon = Icons.Default.ViewInAr,
            isSelected = currentTab == UnuexTab.AR_PREVIEW,
            onClick = { onSelectTab(UnuexTab.AR_PREVIEW) },
            testTag = "nav_tab_ar_preview"
        )

        NavItem(
            label = "SCANNER",
            icon = Icons.Default.QrCodeScanner,
            isSelected = currentTab == UnuexTab.SCANNER,
            onClick = { onSelectTab(UnuexTab.SCANNER) },
            testTag = "nav_tab_scanner"
        )

        NavItem(
            label = "AI CORE",
            icon = Icons.Default.AutoAwesome,
            isSelected = currentTab == UnuexTab.AI_CORE,
            onClick = { onSelectTab(UnuexTab.AI_CORE) },
            accentColor = HyperMagenta,
            testTag = "nav_tab_ai_core"
        )

        NavItem(
            label = "CART",
            icon = Icons.Default.ShoppingCart,
            isSelected = currentTab == UnuexTab.CART_CREDITS,
            onClick = { onSelectTab(UnuexTab.CART_CREDITS) },
            badgeCount = cartCount,
            accentColor = QuantumGold,
            testTag = "nav_tab_cart"
        )
    }
}

@Composable
fun NavItem(
    label: String,
    icon: ImageVector,
    isSelected: Boolean,
    onClick: () -> Unit,
    badgeCount: Int = 0,
    accentColor: Color = CyberCyan,
    testTag: String
) {
    val activeColor = if (isSelected) accentColor else CyberTextSecondary

    Column(
        modifier = Modifier
            .testTag(testTag)
            .clip(CutCornerShape(6.dp))
            .clickable(onClick = onClick)
            .padding(horizontal = 8.dp, vertical = 6.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        if (badgeCount > 0) {
            BadgedBox(
                badge = {
                    Badge(
                        containerColor = HyperMagenta,
                        contentColor = CyberTextPrimary
                    ) {
                        Text("$badgeCount", fontSize = 10.sp)
                    }
                }
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = label,
                    tint = activeColor,
                    modifier = Modifier.size(22.dp)
                )
            }
        } else {
            Icon(
                imageVector = icon,
                contentDescription = label,
                tint = activeColor,
                modifier = Modifier.size(22.dp)
            )
        }

        Spacer(modifier = Modifier.height(2.dp))

        Text(
            text = label,
            style = MaterialTheme.typography.labelSmall.copy(
                color = activeColor,
                fontSize = 9.sp,
                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                fontFamily = FontFamily.Monospace
            )
        )
    }
}
