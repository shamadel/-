package com.example.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable

private val DarkColorScheme = darkColorScheme(
    primary = StadiumNeonGreen,
    secondary = PitchAccent,
    tertiary = IntenseNeon,
    background = DeepTurfDark,
    surface = StadiumSurface,
    surfaceVariant = StadiumCard,
    onPrimary = DeepTurfDark,
    onSecondary = DeepTurfDark,
    onTertiary = DeepTurfDark,
    onBackground = DarkTextPrimary,
    onSurface = DarkTextPrimary,
    onSurfaceVariant = DarkTextSecondary
)

@Composable
fun MyApplicationTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    // Sports stadium dashboard is best rendered in deep dark mode
    val colorScheme = DarkColorScheme

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}
