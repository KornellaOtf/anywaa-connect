package com.anywaa.connect.ui.theme

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Shapes
import androidx.compose.ui.unit.dp

val Shapes = Shapes( // Renamed from AnywaaShapes to Shapes to maintain compatibility with Theme.kt
    // Chips, small badges
    extraSmall = RoundedCornerShape(8.dp),
    // Input fields, snackbars
    small      = RoundedCornerShape(12.dp),
    // Chat bubbles, buttons
    medium     = RoundedCornerShape(18.dp),
    // Cards, bottom sheets, dialogs
    large      = RoundedCornerShape(24.dp),
    // Full-screen sheets, modals
    extraLarge = RoundedCornerShape(28.dp)
)

// Convenience shapes used directly in composables
val BubbleShapeUser = RoundedCornerShape(
    topStart = 18.dp, topEnd = 4.dp,
    bottomStart = 18.dp, bottomEnd = 18.dp
)
val BubbleShapeAI = RoundedCornerShape(
    topStart = 4.dp, topEnd = 18.dp,
    bottomStart = 18.dp, bottomEnd = 18.dp
)
