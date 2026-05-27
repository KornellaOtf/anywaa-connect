package com.anywaa.connect.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ContentCopy
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import com.anywaa.connect.ui.theme.*

/**
 * ChatBubble component for displaying AI or user messages.
 * Supports streaming text, citations, tool indicators, and actions.
 */
@Composable
fun ChatBubble(
    message: String,
    isUser: Boolean = false,
    timestamp: String? = null,
    isStreaming: Boolean = false,
    toolIndicator: String? = null,
    citations: List<Citation> = emptyList(),
    onCopy: () -> Unit = {},
    onRegenerate: () -> Unit = {},
    onShare: () -> Unit = {},
    modifier: Modifier = Modifier
) {
    var showActions by remember { mutableStateOf(false) }
    
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = Spacing.lg, vertical = Spacing.md),
        horizontalAlignment = if (isUser) Alignment.End else Alignment.Start
    ) {
        // Tool indicator (appears above message)
        if (toolIndicator != null) {
            ToolIndicatorPill(toolIndicator)
            Spacer(modifier = Modifier.height(Spacing.sm))
        }
        
        // Message bubble
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = if (isUser) Arrangement.End else Arrangement.Start
        ) {
            // Avatar (only for AI messages)
            if (!isUser) {
                Surface(
                    modifier = Modifier
                        .size(ComponentSizes.avatarSmall)
                        .align(Alignment.Top),
                    shape = RoundedCornerShape(50),
                    color = MaterialTheme.colorScheme.primaryContainer
                ) {
                    Box(contentAlignment = Alignment.Center) {
                        Text("A", style = MaterialTheme.typography.labelMedium)
                    }
                }
                Spacer(modifier = Modifier.width(Spacing.md))
            }
            
            // Message container
            Surface(
                modifier = Modifier
                    .widthIn(max = 600.dp)
                    .onPointerEvent {
                        showActions = true
                    },
                shape = RoundedCornerShape(12.dp),
                color = if (isUser) {
                    MaterialTheme.colorScheme.primary
                } else {
                    MaterialTheme.colorScheme.surfaceVariant
                },
                tonalElevation = Elevation.subtle
            ) {
                Column(
                    modifier = Modifier.padding(
                        horizontal = Spacing.md,
                        vertical = Spacing.md
                    )
                ) {
                    // Message text with streaming indicator
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(
                            message,
                            style = MaterialTheme.typography.bodyLarge,
                            color = if (isUser) {
                                MaterialTheme.colorScheme.onPrimary
                            } else {
                                MaterialTheme.colorScheme.onSurfaceVariant
                            },
                            modifier = Modifier.weight(1f)
                        )
                        
                        if (isStreaming) {
                            Spacer(modifier = Modifier.width(Spacing.sm))
                            TypingIndicator()
                        }
                    }
                    
                    // Citations inline
                    if (citations.isNotEmpty()) {
                        Spacer(modifier = Modifier.height(Spacing.sm))
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(Spacing.xs)
                        ) {
                            citations.forEachIndexed { index, citation ->
                                CitationMarker(
                                    number = index + 1,
                                    citation = citation
                                )
                            }
                        }
                    }
                }
            }
            
            // Actions (desktop hover, always visible on mobile)
            if (showActions) {
                Spacer(modifier = Modifier.width(Spacing.sm))
                Row(
                    modifier = Modifier.align(Alignment.CenterVertically),
                    horizontalArrangement = Arrangement.spacedBy(Spacing.xs)
                ) {
                    IconButton(
                        onClick = onCopy,
                        modifier = Modifier.size(32.dp)
                    ) {
                        Icon(
                            Icons.Filled.ContentCopy,
                            contentDescription = "Copy",
                            modifier = Modifier.size(ComponentSizes.iconMedium)
                        )
                    }
                    
                    if (!isUser) {
                        IconButton(
                            onClick = onRegenerate,
                            modifier = Modifier.size(32.dp)
                        ) {
                            Icon(
                                Icons.Filled.Refresh,
                                contentDescription = "Regenerate",
                                modifier = Modifier.size(ComponentSizes.iconMedium)
                            )
                        }
                    }
                    
                    IconButton(
                        onClick = onShare,
                        modifier = Modifier.size(32.dp)
                    ) {
                        Icon(
                            Icons.Filled.Share,
                            contentDescription = "Share",
                            modifier = Modifier.size(ComponentSizes.iconMedium)
                        )
                    }
                }
            }
        }
        
        // Timestamp
        if (timestamp != null) {
            Text(
                timestamp,
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.padding(top = Spacing.xs)
            )
        }
    }
}

/**
 * Typing indicator (three pulsing dots)
 */
@Composable
fun TypingIndicator(modifier: Modifier = Modifier) {
    Row(
        modifier = modifier.height(16.dp),
        horizontalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        repeat(3) {
            Surface(
                modifier = Modifier
                    .size(4.dp)
                    .align(Alignment.CenterVertically),
                shape = RoundedCornerShape(2.dp),
                color = MaterialTheme.colorScheme.onSurfaceVariant
            ) {}
        }
    }
}

/**
 * Tool indicator pill (e.g., "Image Generation", "Web Search")
 */
@Composable
fun ToolIndicatorPill(
    toolName: String,
    modifier: Modifier = Modifier
) {
    Surface(
        modifier = modifier
            .height(ComponentSizes.chipHeight)
            .padding(Spacing.sm),
        shape = RoundedCornerShape(16.dp),
        color = MaterialTheme.colorScheme.primaryContainer,
        tonalElevation = Elevation.subtle
    ) {
        Row(
            modifier = Modifier.padding(
                horizontal = Spacing.md,
                vertical = Spacing.sm
            ),
            horizontalArrangement = Arrangement.spacedBy(Spacing.sm),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                toolName,
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.onPrimaryContainer
            )
        }
    }
}

/**
 * Citation marker (e.g., [1], [2])
 */
@Composable
fun CitationMarker(
    number: Int,
    citation: Citation,
    modifier: Modifier = Modifier
) {
    var showPopup by remember { mutableStateOf(false) }
    
    Box(modifier = modifier) {
        Surface(
            modifier = Modifier
                .size(20.dp)
                .clickable { showPopup = !showPopup },
            shape = RoundedCornerShape(4.dp),
            color = MaterialTheme.colorScheme.primary
        ) {
            Box(contentAlignment = Alignment.Center) {
                Text(
                    "[$number]",
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.onPrimary
                )
            }
        }
        
        // Citation popup on tap
        if (showPopup) {
            CitationPopup(
                citation = citation,
                onDismiss = { showPopup = false }
            )
        }
    }
}

/**
 * Citation popup card
 */
@Composable
fun CitationPopup(
    citation: Citation,
    onDismiss: () -> Unit,
    modifier: Modifier = Modifier
) {
    Surface(
        modifier = modifier
            .padding(Spacing.md)
            .widthIn(max = 300.dp),
        shape = RoundedCornerShape(8.dp),
        color = MaterialTheme.colorScheme.surface,
        tonalElevation = Elevation.standard
    ) {
        Column(
            modifier = Modifier.padding(Spacing.md),
            verticalArrangement = Arrangement.spacedBy(Spacing.sm)
        ) {
            Text(
                citation.title,
                style = MaterialTheme.typography.labelMedium,
                color = MaterialTheme.colorScheme.primary
            )
            Text(
                citation.snippet,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurface
            )
            Text(
                citation.url,
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}

// Data classes
data class Citation(
    val title: String,
    val url: String,
    val snippet: String,
    val confidence: CitationConfidence = CitationConfidence.MEDIUM
)

enum class CitationConfidence {
    LOW, MEDIUM, HIGH
}

// Extension to detect clickable regions
private fun Modifier.onPointerEvent(action: () -> Unit): Modifier = this
