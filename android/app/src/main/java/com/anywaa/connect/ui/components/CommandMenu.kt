package com.anywaa.connect.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.PopupProperties

/**
 * CommandMenu component - triggered by "/" in input field
 * Displays available commands: /image, /think, /web, /code, /project
 */
@Composable
fun CommandMenu(
    isVisible: Boolean,
    onCommandSelected: (String) -> Unit,
    onDismiss: () -> Unit,
    modifier: Modifier = Modifier
) {
    if (!isVisible) return
    
    val commands = listOf(
        Command(
            name = "/image",
            description = "Generate an image",
            icon = Icons.Filled.Image
        ),
        Command(
            name = "/think",
            description = "Show reasoning steps",
            icon = Icons.Filled.Psychology
        ),
        Command(
            name = "/web",
            description = "Enable web search",
            icon = Icons.Filled.Language
        ),
        Command(
            name = "/code",
            description = "Format as code",
            icon = Icons.Filled.Code
        ),
        Command(
            name = "/project",
            description = "Switch project",
            icon = Icons.Filled.Folder
        )
    )
    
    var selectedIndex by remember { mutableStateOf(0) }
    
    Surface(
        modifier = modifier
            .fillMaxWidth()
            .heightIn(max = 300.dp)
            .padding(Spacing.md),
        shape = RoundedCornerShape(8.dp),
        color = MaterialTheme.colorScheme.surface,
        tonalElevation = Elevation.prominent
    ) {
        LazyColumn(
            modifier = Modifier.fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(Spacing.xs)
        ) {
            itemsIndexed(commands) { index, command ->
                CommandMenuItem(
                    command = command,
                    isSelected = index == selectedIndex,
                    onClick = {
                        onCommandSelected(command.name)
                        onDismiss()
                    }
                )
            }
        }
    }
}

/**
 * Single command menu item
 */
@Composable
fun CommandMenuItem(
    command: Command,
    isSelected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Surface(
        modifier = modifier
            .fillMaxWidth()
            .height(56.dp)
            .clickable { onClick() }
            .background(
                if (isSelected) {
                    MaterialTheme.colorScheme.surfaceVariant
                } else {
                    MaterialTheme.colorScheme.surface
                }
            ),
        color = if (isSelected) {
            MaterialTheme.colorScheme.surfaceVariant
        } else {
            MaterialTheme.colorScheme.surface
        }
    ) {
        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(Spacing.md),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(Spacing.md)
        ) {
            Icon(
                imageVector = command.icon,
                contentDescription = command.name,
                modifier = Modifier.size(ComponentSizes.iconMedium),
                tint = if (isSelected) {
                    MaterialTheme.colorScheme.primary
                } else {
                    MaterialTheme.colorScheme.onSurface
                }
            )
            
            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(Spacing.xs)
            ) {
                Text(
                    command.name,
                    style = MaterialTheme.typography.labelMedium,
                    color = if (isSelected) {
                        MaterialTheme.colorScheme.primary
                    } else {
                        MaterialTheme.colorScheme.onSurface
                    }
                )
                Text(
                    command.description,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
    }
}

/**
 * Command data class
 */
data class Command(
    val name: String,
    val description: String,
    val icon: ImageVector
)
