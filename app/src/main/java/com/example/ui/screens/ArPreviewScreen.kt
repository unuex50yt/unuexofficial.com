package com.example.ui.screens

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.gestures.detectTransformGestures
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
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.ui.UnuexViewModel
import com.example.ui.components.CyberButton
import com.example.ui.components.CyberGlassPanel
import com.example.ui.components.CyberSectionHeader
import com.example.ui.theme.CyberBackground
import com.example.ui.theme.CyberBorderSubtle
import com.example.ui.theme.CyberCyan
import com.example.ui.theme.CyberSurface
import com.example.ui.theme.CyberTextPrimary
import com.example.ui.theme.CyberTextSecondary
import com.example.ui.theme.HyperMagenta
import com.example.ui.theme.NeonViolet
import com.example.ui.theme.QuantumGold

@Composable
fun ArPreviewScreen(
    viewModel: UnuexViewModel,
    modifier: Modifier = Modifier
) {
    val product by viewModel.selectedProductForAr.collectAsStateWithLifecycle()
    val selectedCore by viewModel.selectedPlasmaCore.collectAsStateWithLifecycle()
    val powerWattage by viewModel.powerWattage.collectAsStateWithLifecycle()

    var rotationX by remember { mutableFloatStateOf(0f) }
    var rotationY by remember { mutableFloatStateOf(0f) }

    val coreColor = when (selectedCore) {
        "HYPER MAGENTA" -> HyperMagenta
        "NEON VIOLET" -> NeonViolet
        "QUANTUM GOLD" -> QuantumGold
        else -> CyberCyan
    }

    val infiniteTransition = rememberInfiniteTransition(label = "HoloPulse")
    val pulseAlpha by infiniteTransition.animateFloat(
        initialValue = 0.3f,
        targetValue = 0.9f,
        animationSpec = infiniteRepeatable(
            animation = tween(1200, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "pulseAlpha"
    )

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .background(CyberBackground),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Holo Viewer Title
        item {
            CyberSectionHeader(
                title = "3D HOLOGRAM PREVIEW",
                codeTag = product.codeName,
                accentColor = coreColor
            )
        }

        // Hologram Canvas Stage
        item {
            CyberGlassPanel(
                modifier = Modifier.fillMaxWidth(),
                borderColor = coreColor,
                cornerSize = 16.dp
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "TOUCH & DRAG TO ROTATE 360° PREVIEW",
                        style = MaterialTheme.typography.labelSmall.copy(
                            color = coreColor.copy(alpha = pulseAlpha),
                            fontFamily = FontFamily.Monospace,
                            fontSize = 10.sp
                        )
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(280.dp)
                            .clip(CutCornerShape(12.dp))
                            .background(CyberSurface)
                            .border(1.dp, coreColor.copy(alpha = 0.5f), CutCornerShape(12.dp))
                            .pointerInput(Unit) {
                                detectTransformGestures { _, pan, _, _ ->
                                    rotationY += pan.x * 0.5f
                                    rotationX += pan.y * 0.5f
                                }
                            },
                        contentAlignment = Alignment.Center
                    ) {
                        // Holographic Grid Background
                        Canvas(modifier = Modifier.fillMaxSize()) {
                            val w = size.width
                            val h = size.height
                            val gridSpacing = 30f
                            var x = 0f
                            while (x < w) {
                                drawLine(
                                    color = coreColor.copy(alpha = 0.1f),
                                    start = Offset(x, 0f),
                                    end = Offset(x, h),
                                    strokeWidth = 1f
                                )
                                x += gridSpacing
                            }
                            var y = 0f
                            while (y < h) {
                                drawLine(
                                    color = coreColor.copy(alpha = 0.1f),
                                    start = Offset(0f, y),
                                    end = Offset(w, y),
                                    strokeWidth = 1f
                                )
                                y += gridSpacing
                            }

                            // Center Target Reticle Ring
                            drawCircle(
                                color = coreColor.copy(alpha = pulseAlpha * 0.5f),
                                radius = 110.dp.toPx(),
                                style = Stroke(width = 2.dp.toPx())
                            )
                        }

                        // Product Image with Plasma Core Aura
                        Box(
                            modifier = Modifier
                                .size(190.dp)
                                .clip(CutCornerShape(16.dp))
                                .border(2.dp, coreColor, CutCornerShape(16.dp))
                        ) {
                            Image(
                                painter = painterResource(id = product.drawableResId),
                                contentDescription = product.name,
                                contentScale = ContentScale.Crop,
                                modifier = Modifier.fillMaxSize()
                            )
                        }

                        // Overlaid Holographic Specs Ticker
                        Text(
                            text = "ROTATION X: %.1f° | Y: %.1f°".format(rotationX, rotationY),
                            style = MaterialTheme.typography.labelSmall.copy(
                                color = coreColor,
                                fontFamily = FontFamily.Monospace,
                                fontSize = 10.sp
                            ),
                            modifier = Modifier
                                .align(Alignment.BottomEnd)
                                .padding(8.dp)
                        )
                    }
                }
            }
        }

        // Plasma Core Customizer
        item {
            CyberGlassPanel(
                modifier = Modifier.fillMaxWidth(),
                borderColor = coreColor,
                cornerSize = 12.dp
            ) {
                Column {
                    Text(
                        text = "SELECT PLASMA FREQUENCY CORE",
                        style = MaterialTheme.typography.labelLarge.copy(
                            color = CyberTextPrimary,
                            fontSize = 13.sp
                        )
                    )
                    Spacer(modifier = Modifier.height(10.dp))

                    LazyRow(
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        items(product.availableCores) { core ->
                            val isSelected = core == selectedCore
                            val btnColor = when (core) {
                                "HYPER MAGENTA" -> HyperMagenta
                                "NEON VIOLET" -> NeonViolet
                                "QUANTUM GOLD" -> QuantumGold
                                else -> CyberCyan
                            }

                            Box(
                                modifier = Modifier
                                    .testTag("core_option_$core")
                                    .clip(CutCornerShape(6.dp))
                                    .background(if (isSelected) btnColor else CyberSurface)
                                    .border(1.5.dp, btnColor, CutCornerShape(6.dp))
                                    .clickable { viewModel.setPlasmaCore(core) }
                                    .padding(horizontal = 14.dp, vertical = 8.dp)
                            ) {
                                Text(
                                    text = core,
                                    style = MaterialTheme.typography.labelSmall.copy(
                                        color = if (isSelected) CyberBackground else btnColor,
                                        fontWeight = FontWeight.Bold
                                    )
                                )
                            }
                        }
                    }
                }
            }
        }

        // Power Output Wattage Slider
        item {
            CyberGlassPanel(
                modifier = Modifier.fillMaxWidth(),
                borderColor = coreColor,
                cornerSize = 12.dp
            ) {
                Column {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(
                            text = "POWER FREQUENCY OUTPUT",
                            style = MaterialTheme.typography.labelLarge.copy(
                                color = CyberTextPrimary,
                                fontSize = 13.sp
                            )
                        )
                        Text(
                            text = "$powerWattage WATTS",
                            style = MaterialTheme.typography.labelLarge.copy(
                                color = QuantumGold,
                                fontFamily = FontFamily.Monospace,
                                fontWeight = FontWeight.Bold
                            )
                        )
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    Slider(
                        value = powerWattage.toFloat(),
                        onValueChange = { viewModel.setPowerWattage(it.toInt()) },
                        valueRange = 1000f..product.maxWattage.toFloat(),
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("power_wattage_slider"),
                        colors = SliderDefaults.colors(
                            thumbColor = coreColor,
                            activeTrackColor = coreColor,
                            inactiveTrackColor = CyberBorderSubtle
                        )
                    )
                }
            }
        }

        // Specs List
        item {
            CyberGlassPanel(
                modifier = Modifier.fillMaxWidth(),
                borderColor = CyberBorderSubtle,
                cornerSize = 12.dp
            ) {
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    Text(
                        text = "NEURAL TECH SPECIFICATIONS",
                        style = MaterialTheme.typography.labelLarge.copy(
                            color = CyberTextPrimary,
                            fontSize = 13.sp
                        )
                    )

                    product.specs.forEach { (specKey, specVal) ->
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(
                                text = specKey,
                                style = MaterialTheme.typography.bodyMedium.copy(
                                    color = CyberTextSecondary,
                                    fontSize = 12.sp
                                )
                            )
                            Text(
                                text = specVal,
                                style = MaterialTheme.typography.bodyMedium.copy(
                                    color = CyberCyan,
                                    fontFamily = FontFamily.Monospace,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 12.sp
                                )
                            )
                        }
                    }
                }
            }
        }

        // Action Button
        item {
            CyberButton(
                text = "ADD CUSTOMIZED CONFIG TO CART (⟁ %,d CREDITS)".format(product.priceCredits),
                onClick = { viewModel.addToCart(product) },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(52.dp),
                testTag = "add_ar_config_to_cart_btn",
                accentColor = coreColor,
                containerColor = CyberSurface
            )
        }
    }
}
