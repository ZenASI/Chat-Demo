package com.chat.joycom.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable

private val LightColorScheme = lightColorScheme(
    surface = TopBarLight,
    onSurface = OnTopBarLight,
    onSurfaceVariant = OnTopBarLight,
)


private val DarkColorScheme = darkColorScheme(
    surface = TopBarDark,
    onSurface = OnTopBarDark,
    onSurfaceVariant = OnTopBarLight,
)

@Composable
fun JoyComTopBarTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    // Dynamic color is available on Android 12+
    dynamicColor: Boolean = false,
    content: @Composable () -> Unit
) {
    val colorScheme = when {
        darkTheme -> DarkColorScheme
        else -> LightColorScheme
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}