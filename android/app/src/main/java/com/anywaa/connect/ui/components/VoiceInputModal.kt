package com.anywaa.connect.ui.components

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Mic
import androidx.compose.material.icons.filled.Stop
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties

/**
 * VoiceInputModal - full-screen modal for voice input recording
 * Shows waveform visualizer, live transcription, and recording controls
 */
@Composable
fun VoiceInputModal(
    isVisible: Boolean,
    isRecording: Boolean,
    liveTranscription: String,
    waveformAmplitudes: List<Float> = listOf(),
    onStop: () -> Unit,
    onCancel: () -> Unit,
    modifier: Modifier = Modifier
) {
    if (!isVisible) return
    
    Dialog(
        onDismissRequest = onCancel,
        properties = DialogProperties(
            usePlatformDefaultWidth = false,
            dismissOnBackPress = true,
            dismissOnClickOutside = false
        )
    ) {
        Surface(
            modifier = modifier
                .fillMaxSize()
                .background(
                    Brush.verticalGradient(
                        listOf(
                            MaterialTheme.colorScheme.background,
                            MaterialTheme.colorScheme.surfaceVariant
                        )
                    )
                ),
            color = MaterialTheme.colorScheme.background
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(Spacing.lg),
                verticalArrangement = Arrangement.SpaceBetween,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // Close button (top right)
                Row(
                    modifier = Modifier
                        .fillMaxWidth(),
                    horizontalArrangement = Arrangement.End
                ) {
                    IconButton(onClick = onCancel) {
                        Icon(
                            Icons.Filled.Close,
                            contentDescription = "Cancel",
                            modifier = Modifier.size(ComponentSizes.iconLarge)
                        )
                    }
                }
                
                // Center content
                Column(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxWidth(),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    // Waveform visualizer
                    WaveformVisualizer(
                        amplitudes = waveformAmplitudes,
                        modifier = Modifier
                            .fillMaxWidth(0.8f)
                            .height(100.dp)
                            .padding(bottom = Spacing.xl)
                    )
                    
                    // Status text
                    Text(
                        if (isRecording) "Listening..." else "Transcribing...",
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.padding(bottom = Spacing.lg)
                    )
                    
                    // Live transcription text
                    Surface(
                        modifier = Modifier
                            .fillMaxWidth(0.9f)
                            .heightIn(min = 100.dp, max = 200.dp)
                            .padding(Spacing.md),
                        shape = RoundedCornerShape(12.dp),
                        color = MaterialTheme.colorScheme.surfaceVariant
                    ) {
                        Text(
                            liveTranscription.ifEmpty { "Waiting for speech..." },
                            style = MaterialTheme.typography.bodyLarge,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            textAlign = TextAlign.Center,
                            modifier = Modifier
                                .padding(Spacing.md)
                                .verticalScroll(rememberScrollState())
                        )
                    }
                }
                
                // Controls (bottom)
                Row(
                    modifier = Modifier
                        .fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(
                        Spacing.xl,
                        alignment = Alignment.CenterHorizontally
                    ),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // Cancel button
                    IconButton(
                        onClick = onCancel,
                        modifier = Modifier
                            .size(56.dp)
                            .background(
                                color = MaterialTheme.colorScheme.errorContainer,
                                shape = CircleShape
                            )
                    ) {
                        Icon(
                            Icons.Filled.Close,
                            contentDescription = "Cancel",
                            modifier = Modifier.size(ComponentSizes.iconLarge),
                            tint = MaterialTheme.colorScheme.error
                        )
                    }
                    
                    // Stop button (primary)
                    Button(
                        onClick = onStop,
                        modifier = Modifier
                            .size(80.dp),
                        shape = CircleShape,
                        colors = ButtonDefaults.buttonColors(
                            containerColor = MaterialTheme.colorScheme.primary
                        )
                    ) {
                        Icon(
                            Icons.Filled.Stop,
                            contentDescription = "Stop Recording",
                            modifier = Modifier.size(ComponentSizes.iconLarge),
                            tint = MaterialTheme.colorScheme.onPrimary
                        )
                    }
                }
                
                Spacer(modifier = Modifier.height(Spacing.lg))
            }
        }
    }
}

/**
 * Waveform visualizer - animated bars reacting to amplitudes
 */
@Composable
fun WaveformVisualizer(
    amplitudes: List<Float>,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.spacedBy(4.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Generate 20 bars
        repeat(20) { index ->
            val amplitude = if (index < amplitudes.size) {
                amplitudes[index].coerceIn(0f, 1f)
            } else {
                0.3f // Default small bar
            }
            
            val animatedHeight by animateFloatAsState(
                targetValue = amplitude,
                label = "waveform_bar_$index"
            )
            
            Surface(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxHeight(animatedHeight)
                    .clip(RoundedCornerShape(2.dp)),
                color = MaterialTheme.colorScheme.primary,
                tonalElevation = Elevation.subtle
            ) {}
        }
    }
}
