package com.anywaa.connect.ui.components

import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.EmojiEmotions
import androidx.compose.material.icons.filled.Lightbulb
import androidx.compose.material.icons.filled.ScatterPlot
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp

/**
 * FunModeToggle - three-position slider for response style
 * Precise (temperature 0.3) | Balanced (0.7) | Fun (1.2+)
 */
@Composable
fun FunModeToggle(
    selectedMode: FunMode,
    onModeChanged: (FunMode) -> Unit,
    modifier: Modifier = Modifier
) {
    val modes = listOf(
        FunMode.PRECISE,
        FunMode.BALANCED,
        FunMode.FUN
    )
    
    Surface(
        modifier = modifier
            .fillMaxWidth()
            .height(56.dp)
            .padding(Spacing.md),
        shape = RoundedCornerShape(12.dp),
        color = MaterialTheme.colorScheme.surfaceVariant,
        tonalElevation = Elevation.subtle
    ) {
        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(Spacing.sm),
            horizontalArrangement = Arrangement.spacedBy(Spacing.sm),
            verticalAlignment = Alignment.CenterVertically
        ) {
            modes.forEach { mode ->
                FunModeButton(
                    mode = mode,
                    isSelected = mode == selectedMode,
                    onClick = { onModeChanged(mode) },
                    modifier = Modifier.weight(1f)
                )
            }
        }
    }
}

/**
 * Individual fun mode button
 */
@Composable
fun FunModeButton(
    mode: FunMode,
    isSelected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val backgroundColor by animateColorAsState(
        targetValue = if (isSelected) {
            MaterialTheme.colorScheme.primary
        } else {
            MaterialTheme.colorScheme.surface
        },
        label = "fun_mode_bg"
    )
    
    val textColor by animateColorAsState(
        targetValue = if (isSelected) {
            MaterialTheme.colorScheme.onPrimary
        } else {
            MaterialTheme.colorScheme.onSurface
        },
        label = "fun_mode_text"
    )
    
    Surface(
        modifier = modifier
            .height(40.dp)
            .clip(RoundedCornerShape(8.dp))
            .clickable { onClick() },
        shape = RoundedCornerShape(8.dp),
        color = backgroundColor,
        tonalElevation = if (isSelected) Elevation.standard else Elevation.none
    ) {
        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = Spacing.sm),
            horizontalArrangement = Arrangement.spacedBy(Spacing.xs),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = when (mode) {
                    FunMode.PRECISE -> Icons.Filled.Lightbulb
                    FunMode.BALANCED -> Icons.Filled.ScatterPlot
                    FunMode.FUN -> Icons.Filled.EmojiEmotions
                },
                contentDescription = mode.label,
                modifier = Modifier.size(ComponentSizes.iconMedium),
                tint = textColor
            )
            
            Text(
                mode.label,
                style = MaterialTheme.typography.labelSmall,
                fontWeight = FontWeight.Medium,
                color = textColor
            )
        }
    }
}

/**
 * FunMode enum with temperature settings
 */
enum class FunMode(val label: String, val temperature: Float) {
    PRECISE("Precise", 0.3f),
    BALANCED("Balanced", 0.7f),
    FUN("Fun", 1.2f)
}

/**
 * FunMode Badge - small indicator shown in top bar when Fun Mode is active
 */
@Composable
fun FunModeBadge(
    mode: FunMode,
    modifier: Modifier = Modifier
) {
    if (mode == FunMode.BALANCED) return // Don't show badge for default
    
    Surface(
        modifier = modifier
            .wrapContentSize()
            .clip(RoundedCornerShape(50)),
        shape = RoundedCornerShape(50),
        color = MaterialTheme.colorScheme.primaryContainer,
        tonalElevation = Elevation.subtle
    ) {
        Row(
            modifier = Modifier.padding(
                horizontal = Spacing.md,
                vertical = Spacing.xs
            ),
            horizontalArrangement = Arrangement.spacedBy(Spacing.xs),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = when (mode) {
                    FunMode.PRECISE -> Icons.Filled.Lightbulb
                    FunMode.BALANCED -> Icons.Filled.ScatterPlot
                    FunMode.FUN -> Icons.Filled.EmojiEmotions
                },
                contentDescription = mode.label,
                modifier = Modifier.size(ComponentSizes.iconSmall),
                tint = MaterialTheme.colorScheme.onPrimaryContainer
            )
            Text(
                mode.label,
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.onPrimaryContainer
            )
        }
    }
}
