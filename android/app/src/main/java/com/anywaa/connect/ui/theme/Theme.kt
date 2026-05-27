package com.anywaa.connect.ui.theme

import android.app.Activity
import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat
import com.anywaa.connect.data.ThemeMode

private val DarkColorScheme = darkColorScheme(
    // Core brand
    primary          = Primary,
    onPrimary        = OnPrimary,
    primaryContainer = PrimaryVariant,
    onPrimaryContainer = TextPrimary,

    // Secondary (teal)
    secondary = Secondary40,
    onSecondary = Color.Black,
    secondaryContainer = Color(0xFF1A3540),
    onSecondaryContainer = TextPrimary,

    // Backgrounds
    background       = Background,
    onBackground     = TextPrimary,
    surface          = SurfaceDim,
    onSurface        = TextPrimary,
    surfaceVariant   = SurfaceContainer,
    onSurfaceVariant = TextSecondary,

    // Error
    error            = ErrorColor,
    onError          = OnErrorColor,

    // Misc
    outline          = DividerColor,
    outlineVariant   = TextDisabled,
    scrim            = Color(0xCC000000)
)

// We use the same elegant dark scheme for light mode for this premium AI aesthetic
// (or you can expand it if you want a true light mode later)
private val LightColorScheme = DarkColorScheme

@Composable
fun AnywaaConnectTheme(
    themeMode: ThemeMode = ThemeMode.SYSTEM,
    // Force false to preserve our custom premium palette over system dynamic colors
    dynamicColor: Boolean = false, 
    content: @Composable () -> Unit
) {
    val darkTheme = when (themeMode) {
        ThemeMode.LIGHT -> false
        ThemeMode.DARK -> true
        ThemeMode.SYSTEM -> isSystemInDarkTheme()
    }
    
    val colorScheme = when {
        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            val context = LocalContext.current
            if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
        }
        else -> DarkColorScheme // Use our premium dark theme regardless of light/dark toggle
    }
    
    val view = LocalView.current
    if (!view.isInEditMode) {
        SideEffect {
            val window = (view.context as Activity).window
            window.statusBarColor = colorScheme.background.toArgb()
            window.navigationBarColor = colorScheme.background.toArgb()
            
            val insetsController = WindowCompat.getInsetsController(window, view)
            insetsController.isAppearanceLightStatusBars = false
            insetsController.isAppearanceLightNavigationBars = false
        }
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        shapes = Shapes,
        content = content
    )
}