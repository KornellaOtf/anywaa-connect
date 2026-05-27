package com.anywaa.connect.ui.theme

import androidx.compose.ui.graphics.Color

// Dark Theme Palette
val DarkBackgroundPrimary = Color(0xFF0A0A1F) // deep purple black
val DarkBackgroundSecondary = Color(0xFF12121A) // cards, sidebars
val DarkSurfaceElevated = Color(0xFF1E1E2E) // elevated surface
val DarkPrimaryAccent = Color(0xFF00BFA5) // teal
val DarkSecondaryAccent = Color(0xFF00E5FF) // neon blue
val DarkTextPrimary = Color(0xFFFFFFFF)
val DarkTextSecondary = Color(0xFFA0A0B0)
val DarkBorderSubtle = Color(0xFF2A2A35)

// Light Theme Palette
val LightBackgroundPrimary = Color(0xFFF5F5F7) // off white
val LightBackgroundSecondary = Color(0xFFFFFFFF) // cards, sidebars
val LightSurfaceElevated = Color(0xFFF0F0F2) // elevated surface
val LightPrimaryAccent = Color(0xFF00897B) // teal
val LightSecondaryAccent = Color(0xFF0097A7) // neon blue
val LightTextPrimary = Color(0xFF1A1A1E)
val LightTextSecondary = Color(0xFF5A5A6E)
val LightBorderSubtle = Color(0xFFD0D0D8)

// Shared Colors
val UserBubbleGradientStart = Color(0xFF00BFA5)
val UserBubbleGradientEnd = Color(0xFF009688)
val ErrorColor = Color(0xFFFF5C5C) // Using Dark Theme error, Material3 will map it
val SuccessColor = Color(0xFF4CAF50)

// Backward compatibility (if any legacy code relies on these, mapping to new colors)
val Primary = DarkPrimaryAccent
val PrimaryVariant = DarkSecondaryAccent
val OnPrimary = Color.White
val Background = DarkBackgroundPrimary
val SurfaceDim = DarkBackgroundSecondary
val SurfaceContainer = DarkSurfaceElevated
val TextPrimary = DarkTextPrimary
val TextSecondary = DarkTextSecondary
val DividerColor = DarkBorderSubtle
val OnErrorColor = Color.White