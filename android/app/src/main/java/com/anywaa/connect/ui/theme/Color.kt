package com.anywaa.connect.ui.theme

import androidx.compose.ui.graphics.Color

// Warm dark palette
val Background       = Color(0xFF111318)   // very dark warm gray
val SurfaceDim       = Color(0xFF1A1D24)   // slightly lighter for UI surfaces
val SurfaceContainer = Color(0xFF1E2130)   // chat area background

// Brand accent (indigo)
val Primary          = Color(0xFF6366F1)   // soft indigo for buttons, icons
val PrimaryVariant   = Color(0xFF4A5BD4)   // darker shade for pressed state
val OnPrimary        = Color(0xFFFFFFFF)

// Chat bubbles
val BubbleUser       = Color(0xFF2A3560)   // deep indigo blue for user messages
val BubbleAI         = Color(0xFF1E2130)   // neutral surface for AI messages

// Text
val TextPrimary      = Color(0xFFF0F2FF)   // near‑white for main text
val TextSecondary    = Color(0xFF8A93B2)   // muted lavender‑gray for timestamps/hints
val TextDisabled     = Color(0xFF4A5070)

// Utility
val DividerColor     = Color(0xFF252840)
val ErrorColor       = Color(0xFFFF6B6B)
val OnErrorColor     = Color(0xFFFFFFFF)
val SuccessColor     = Color(0xFF4CAF82)

// Compatibility fallbacks (if any legacy code references these)
val Primary80 = Color(0xFFBAC7FF)
val Primary40 = Primary
val Primary20 = PrimaryVariant
val Secondary80 = Color(0xFFA5E6F1)
val Secondary40 = Color(0xFF38B2C8)
val Secondary20 = Color(0xFF00687A)