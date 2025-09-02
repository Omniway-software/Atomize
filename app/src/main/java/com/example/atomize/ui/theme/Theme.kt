package com.example.atomize.ui.theme

import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext

private val DarkColorScheme = darkColorScheme(
    primary = LightGreen,
    onPrimary = PrimaryTextColor,
    primaryContainer = DarkGray,
    onPrimaryContainer = VeryLightGreen,

    secondary = VeryLightGreen,
    onSecondary = PrimaryTextColor,
    secondaryContainer = DarkGray,
    onSecondaryContainer = VeryLightGreen,

    tertiary = Orange,
    onTertiary = PrimaryTextColor,
    tertiaryContainer = DarkGray,
    onTertiaryContainer = Orange,

    background = PrimaryTextColor,
    onBackground = White,

    surface = DarkGray,
    onSurface = White,
    surfaceVariant = MediumGray,
    onSurfaceVariant = LightGray,

    error = Red,
    onError = White,

    outline = MediumGray,
    outlineVariant = SecondaryTextColor
)

private val LightColorScheme = lightColorScheme(
    primary = PrimaryGreen,
    onPrimary = White,
    primaryContainer = VeryLightGreen,
    onPrimaryContainer = PrimaryTextColor,

    secondary = LightGreen,
    onSecondary = White,
    secondaryContainer = LightGray,
    onSecondaryContainer = PrimaryTextColor,

    tertiary = Orange,
    onTertiary = White,
    tertiaryContainer = VeryLightGreen,
    onTertiaryContainer = Orange,

    background = LightGray,
    onBackground = PrimaryTextColor,

    surface = White,
    onSurface = PrimaryTextColor,
    surfaceVariant = VeryLightGreen,
    onSurfaceVariant = SecondaryTextColor,

    error = Red,
    onError = White,

    outline = MediumGray,
    outlineVariant = DisabledTextColor
)

@Composable
fun AtomizeTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    dynamicColor: Boolean = false,
    content: @Composable () -> Unit
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