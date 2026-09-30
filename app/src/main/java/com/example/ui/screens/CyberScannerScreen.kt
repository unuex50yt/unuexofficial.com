package com.example.ui.screens

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CutCornerShape
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.ui.UnuexViewModel
import com.example.ui.components.CyberButton
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
fun CyberScannerScreen(
    viewModel: UnuexViewModel,
    modifier: Modifier = Modifier
) {
    val isScanning by viewModel.isScanning.collectAsStateWithLifecycle()
    val scanProgress by viewModel.scanProgress.collectAsStateWithLifecycle()
    val scanResult by viewModel.scanDiagnosticResult.collectAsStateWithLifecycle()

    val infiniteTransition = rememberInfiniteTransition(label = "ScanLine")
    val scanYFraction by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(1800, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "scanY"
    )

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .background(CyberBackground),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        item {
            CyberSectionHeader(
                title = "CYBERNETIC COMPATIBILITY SCANNER",
                codeTag = "HUD-SCAN-2050",
                accentColor = CyberCyan
            )
        }

        // HUD Scanner Box
        item {
            CyberGlassPanel(
                modifier = Modifier.fillMaxWidth(),
                borderColor = if (isScanning) HyperMagenta else CyberCyan,
                cornerSize = 16.dp
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(260.dp)
                            .clip(CutCornerShape(12.dp))
                            .background(CyberSurface)
                            .border(
                                1.5.dp,
                                if (isScanning) HyperMagenta else CyberCyan,
                                CutCornerShape(12.dp)
                            ),
                        contentAlignment = Alignment.Center
                    ) {
                        Canvas(modifier = Modifier.fillMaxSize()) {
                            val w = size.width
                            val h = size.height

                            // Draw Crosshair
                            drawLine(
                                color = CyberCyan.copy(alpha = 0.4f),
                                start = Offset(w / 2, 0f),
                                end = Offset(w / 2, h),
                                strokeWidth = 1f
                            )
                            drawLine(
                                color = CyberCyan.copy(alpha = 0.4f),
                                start = Offset(0f, h / 2),
                                end = Offset(w, h / 2),
                                strokeWidth = 1f
                            )

                            // Animated Scan Beam
                            val beamY = h * scanYFraction
                            drawLine(
                                color = if (isScanning) HyperMagenta else CyberCyan,
                                start = Offset(0f, beamY),
                                end = Offset(w, beamY),
                                strokeWidth = 3.dp.toPx()
                            )

                            // Target Reticle Box
                            drawRect(
                                color = if (isScanning) HyperMagenta else BioGreen,
                                topLeft = Offset(w * 0.25f, h * 0.25f),
                                size = androidx.compose.ui.geometry.Size(w * 0.5f, h * 0.5f),
                                style = Stroke(width = 2.dp.toPx())
                            )
                        }

                        Text(
                            text = if (isScanning) "SCANNING BIOMETRIC TELEMETRY..." else "ALIGN TARGET RETICLE",
                            style = MaterialTheme.typography.labelSmall.copy(
                                color = if (isScanning) HyperMagenta else CyberCyan,
                                fontFamily = FontFamily.Monospace,
                                fontSize = 11.sp
                            )
                        )
                    }

                    if (isScanning) {
                        Spacer(modifier = Modifier.height(12.dp))
                        LinearProgressIndicator(
                            progress = { scanProgress },
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(6.dp)
                                .clip(CutCornerShape(3.dp)),
                            color = HyperMagenta,
                            trackColor = CyberBorderSubtle
                        )
                    }
                }
            }
        }

        // Action Trigger
        item {
            CyberButton(
                text = if (isScanning) "SCANNING IN PROGRESS..." else "INITIATE TELEMETRY SCAN",
                onClick = { viewModel.startCompatibilityScan() },
                enabled = !isScanning,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp),
                testTag = "start_scan_button",
                accentColor = CyberCyan,
                containerColor = CyberSurface
            )
        }

        // Diagnostic Results
        if (scanResult != null) {
            item {
                CyberGlassPanel(
                    modifier = Modifier.fillMaxWidth(),
                    borderColor = BioGreen,
                    cornerSize = 12.dp
                ) {
                    Column {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Box(
                                modifier = Modifier
                                    .size(10.dp)
                                    .background(BioGreen)
                            ) { }
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = "DIAGNOSTIC REPORT GENERATED",
                                style = MaterialTheme.typography.labelLarge.copy(
                                    color = BioGreen,
                                    fontSize = 13.sp
                                )
                            )
                        }

                        Spacer(modifier = Modifier.height(10.dp))

                        Text(
                            text = scanResult!!,
                            style = MaterialTheme.typography.bodyMedium.copy(
                                color = CyberTextPrimary,
                                fontFamily = FontFamily.Monospace,
                                fontSize = 12.sp,
                                lineHeight = 18.sp
                            )
                        )
                    }
                }
            }
        }
    }
}
