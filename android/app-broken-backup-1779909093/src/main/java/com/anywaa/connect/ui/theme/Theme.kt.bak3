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
    primary = DarkPrimaryAccent,
    onPrimary = DarkTextPrimary,
    primaryContainer = DarkSecondaryAccent,
    onPrimaryContainer = DarkTextPrimary,
    secondary = DarkSecondaryAccent,
    onSecondary = Color.Black,
    secondaryContainer = DarkSurfaceElevated,
    onSecondaryContainer = DarkTextPrimary,
    background = DarkBackgroundPrimary,
    onBackground = DarkTextPrimary,
    surface = DarkBackgroundSecondary,
    onSurface = DarkTextPrimary,
    surfaceVariant = DarkSurfaceElevated,
    onSurfaceVariant = DarkTextSecondary,
    error = ErrorColor,
    onError = Color.White,
    outline = DarkBorderSubtle,
    outlineVariant = DarkBorderSubtle,
    scrim = Color(0xCC000000)
)

private val LightColorScheme = lightColorScheme(
    primary = LightPrimaryAccent,
    onPrimary = Color.White,
    primaryContainer = LightSecondaryAccent,
    onPrimaryContainer = Color.White,
    secondary = LightSecondaryAccent,
    onSecondary = Color.White,
    secondaryContainer = LightSurfaceElevated,
    onSecondaryContainer = LightTextPrimary,
    background = LightBackgroundPrimary,
    onBackground = LightTextPrimary,
    surface = LightBackgroundSecondary,
    onSurface = LightTextPrimary,
    surfaceVariant = LightSurfaceElevated,
    onSurfaceVariant = LightTextSecondary,
    error = ErrorColor,
    onError = Color.White,
    outline = LightBorderSubtle,
    outlineVariant = LightBorderSubtle,
    scrim = Color(0x33000000)
)

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
        darkTheme -> DarkColorScheme
        else -> LightColorScheme
    }
    
    val view = LocalView.current
    if (!view.isInEditMode) {
        SideEffect {
            val window = (view.context as Activity).window
            window.statusBarColor = colorScheme.background.toArgb()
            window.navigationBarColor = colorScheme.background.toArgb()
            
            val insetsController = WindowCompat.getInsetsController(window, view)
            insetsController.isAppearanceLightStatusBars = !darkTheme
            insetsController.isAppearanceLightNavigationBars = !darkTheme
        }
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        shapes = Shapes,
        content = content
    )
}