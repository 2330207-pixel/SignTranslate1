package com.example.signtranslate.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable

private val LightColors = lightColorScheme(
    primary          = BrandPurple,
    onPrimary        = androidx.compose.ui.graphics.Color.White,
    primaryContainer = BrandPurpleLight,
    onPrimaryContainer = TextDark,
    background       = BgGradientTop,
    onBackground     = TextDark,
    surface          = androidx.compose.ui.graphics.Color.White,
    onSurface        = TextDark,
    surfaceVariant   = BgGradientTop,
    onSurfaceVariant = TextBody,
    outline          = DividerColor,
    error            = ErrorRed,
    onError          = androidx.compose.ui.graphics.Color.White,
)

private val DarkColors = darkColorScheme(
    primary          = BrandPurpleMid,
    onPrimary        = TextDark,
    onPrimaryContainer = BrandPurpleLight,
    onBackground     = androidx.compose.ui.graphics.Color.White,
    onSurface        = androidx.compose.ui.graphics.Color.White,
    onSurfaceVariant = TextBody,
    outline          = DividerColor,
    error            = ErrorRed,
    onError          = androidx.compose.ui.graphics.Color.White,
)

@Composable
fun SignTranslateTheme(
    darkTheme: Boolean = false,
    content: @Composable () -> Unit
) {
    MaterialTheme(
        colorScheme = if (darkTheme) DarkColors else LightColors,
        content     = content
    )
}