package com.chat.joycom.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val LightColorScheme = lightColorScheme(
    primary = ChipIconLight,
    surface = ChipSurfaceLight,
    onSurface = Color.Black,
)


private val DarkColorScheme = darkColorScheme(
    primary = ChipIconDark,
    surface = ChipSurfaceDark,
    onSurface = Color.White,
)

@Composable
fun JoyComChipTheme(
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

private val LightSelectColorScheme = lightColorScheme(
    primary = Color.White,
    surface = ChipSelectLight,
    onSurface = Color.White,
)


private val DarkSelectColorScheme = darkColorScheme(
    primary = Color.White,
    surface = ChipSelectDark,
    onSurface = Color.White,
)

@Composable
fun JoyComChipSelectTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    // Dynamic color is available on Android 12+
    dynamicColor: Boolean = false,
    content: @Composable () -> Unit
){
    val colorScheme = when {
        darkTheme -> DarkSelectColorScheme
        else -> LightSelectColorScheme
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}