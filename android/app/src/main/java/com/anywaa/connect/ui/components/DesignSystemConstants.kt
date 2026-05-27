package com.anywaa.connect.ui.components

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Shapes
import androidx.compose.ui.unit.dp

/**
 * Anywaa Connect Design System Constants
 * Based on the comprehensive UI/UX specification
 */

// Spacing Grid (8 dp base unit)
object Spacing {
    val xs = 4.dp      // minimal
    val sm = 8.dp      // base unit
    val md = 12.dp     // inner padding
    val lg = 16.dp     // main margins
    val xl = 24.dp     // large gaps
    val xxl = 32.dp    // section separator
}

// Elevation values
object Elevation {
    val none = 0.dp
    val subtle = 2.dp
    val standard = 4.dp
    val prominent = 8.dp
}

// Border values
object Borders {
    val subtle = 1.dp
    val medium = 2.dp
}

// Component sizes
object ComponentSizes {
    val inputBarMinHeight = 72.dp
    val topBarHeight = 56.dp
    val sidebarExpandedWidth = 260.dp
    val sidebarCollapsedWidth = 64.dp
    val rightPanelWidth = 320.dp
    val avatarSmall = 24.dp
    val avatarMedium = 32.dp
    val avatarLarge = 48.dp
    val iconSmall = 16.dp
    val iconMedium = 20.dp
    val iconLarge = 24.dp
    val chipHeight = 32.dp
    val buttonHeight = 40.dp
}

// Shapes for Material3
val AnywaaShapes = Shapes(
    small = RoundedCornerShape(4.dp),
    medium = RoundedCornerShape(8.dp),
    large = RoundedCornerShape(12.dp)
)

// Animation timings
object AnimationDurations {
    const val Fast = 150       // ms
    const val Normal = 300     // ms
    const val Slow = 500       // ms
    const val VerySlow = 1000  // ms
}
