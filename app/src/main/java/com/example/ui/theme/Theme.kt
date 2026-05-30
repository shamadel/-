package com.example.ui.theme

import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext

private val DarkColorScheme = darkColorScheme(
    primary = EmeralPrimaryDark,
    secondary = GoldSecondaryDark,
    tertiary = MintTertiaryDark,
    background = SwampBgDark,
    surface = SwampCardDark,
    onPrimary = Color(0xFF03160E),
    onSecondary = Color(0xFF1E1700),
    onTertiary = Color(0xFF002116),
    onBackground = SageTextDark,
    onSurface = SageTextDark,
    surfaceVariant = Color(0xFF22352F),
    onSurfaceVariant = Color(0xFFA1BCB3)
)

private val LightColorScheme = lightColorScheme(
    primary = EmeraldPrimary,
    secondary = GoldSecondary,
    tertiary = MintTertiary,
    background = OliveBgLight,
    surface = Color.White,
    onPrimary = Color.White,
    onSecondary = Color.White,
    onTertiary = Color.White,
    onBackground = Color(0xFF08140F),
    onSurface = Color(0xFF08140F),
    surfaceVariant = SageCardLight,
    onSurfaceVariant = Color(0xFF0C2B20)
)

@Composable
fun MyApplicationTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    dynamicColor: Boolean = false, // Set to false to enforce our beautiful custom brand colors
    content: @Composable () -> Unit,
) {
    val colorScheme = when {
        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            val context = LocalContext.current
            if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
        }
        darkTheme -> DarkColorScheme
        else -> LightColorScheme
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}
