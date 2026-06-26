package com.example.signtranslate.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Typography
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.signtranslate.viewmodel.SettingsViewModel

// ─────────────────────────────────────────────────────────────────────────────
// Paleta clara
// ─────────────────────────────────────────────────────────────────────────────

private val LightColors = lightColorScheme(
    primary             = BrandPurple,
    onPrimary           = Color.White,
    primaryContainer    = BrandPurpleLight,
    onPrimaryContainer  = TextDark,
    secondary           = BrandPurpleMid,
    background          = Color.White,
    surface             = Color.White,
    surfaceVariant      = CardBg,
    onBackground        = TextDark,
    onSurface           = TextDark,
    onSurfaceVariant    = TextBody,
    outlineVariant      = DividerColor,
    error               = ErrorRed,
)

// ─────────────────────────────────────────────────────────────────────────────
// Paleta oscura — NEGRO PURO en todas las superficies
// ─────────────────────────────────────────────────────────────────────────────
//
// Todas las pestañas (Inicio, Traductor, Avatar, Ajustes) usan MaterialTheme,
// así que al cambiar background/surface aquí, TODAS cambian a negro de forma
// automática y consistente — no es necesario tocar cada pantalla por separado.

private val DarkColors = darkColorScheme(
    primary             = BrandPurple,
    onPrimary           = DarkOnPrimary,
    primaryContainer    = DarkSurfaceVariant,   // tarjeta "Sin sesión" en oscuro
    onPrimaryContainer  = DarkTextPrimary,
    secondary           = BrandPurpleMid,
    background          = DarkBg,               // negro puro — fondo de TODAS las pantallas
    surface             = DarkSurface,
    surfaceVariant      = DarkSurfaceVariant,    // secciones agrupadoras (tarjetas grises del mock)
    onBackground        = DarkTextPrimary,
    onSurface           = DarkTextPrimary,
    onSurfaceVariant    = DarkTextSecondary,
    outlineVariant      = DarkDivider,
    error               = ErrorRed,
)

// ─────────────────────────────────────────────────────────────────────────────
// Tipografía
// ─────────────────────────────────────────────────────────────────────────────

/**
 * Construye una tipografía escalada.
 * @param large cuando es TRUE aumenta todos los tamaños en 4sp.
 */
private fun buildTypography(large: Boolean): Typography {
    val delta = if (large) 4 else 0
    return Typography(
        bodyLarge   = TextStyle(fontSize = (16 + delta).sp),
        bodyMedium  = TextStyle(fontSize = (14 + delta).sp),
        bodySmall   = TextStyle(fontSize = (12 + delta).sp),
        titleLarge  = TextStyle(fontSize = (22 + delta).sp),
        titleMedium = TextStyle(fontSize = (16 + delta).sp),
        titleSmall  = TextStyle(fontSize = (14 + delta).sp),
        labelLarge  = TextStyle(fontSize = (14 + delta).sp),
        labelSmall  = TextStyle(fontSize = (11 + delta).sp),
    )
}

// ─────────────────────────────────────────────────────────────────────────────
// CompositionLocal — saber si el tema activo es oscuro sin pasar el booleano
// manualmente por cada función. Cualquier pantalla puede leer
// LocalIsDarkTheme.current sin necesitar SettingsViewModel como parámetro.
// ─────────────────────────────────────────────────────────────────────────────

val LocalIsDarkTheme = androidx.compose.runtime.staticCompositionLocalOf { false }

// ─────────────────────────────────────────────────────────────────────────────
// Colores de la cámara
// ─────────────────────────────────────────────────────────────────────────────
//
// El recuadro de cámara (SignToTextTab) usa Color.Black fijo en modo claro,
// donde contrasta bien contra un fondo blanco. En modo oscuro ese mismo negro
// se confunde con el fondo, así que se usa gris semitransparente + contorno.
// Se expone vía un object (en vez de colorScheme) porque MaterialTheme no
// tiene slots nativos para "fondo de cámara"; así cualquier pantalla puede
// leer CameraSurface.background(isDark) / .border(isDark) sin recibir el
// booleano de fuera — solo necesita saber si el tema activo es oscuro.
object CameraSurface {
    fun background(isDark: Boolean): Color = if (isDark) DarkCameraBg else Color.Black
    fun border(isDark: Boolean): Color = if (isDark) DarkCameraBorder else Color.Transparent
    fun borderWidth(isDark: Boolean) = if (isDark) 1.dp else 0.dp
}

// ─────────────────────────────────────────────────────────────────────────────
// Tema principal
// ─────────────────────────────────────────────────────────────────────────────

/**
 * Tema de SignTranslate.
 *
 * Lee [SettingsViewModel.uiState] para aplicar:
 * - darkMode  → cambia TODA la app (fondo, tarjetas, divisores, nav bar) a negro
 * - largeText → tipografía aumentada
 *
 * NOTA sobre highContrast: se eliminó la paleta de alto contraste separada;
 * "Alto contraste" ahora solo controla el switch en Ajustes (persistido en
 * DataStore) pero no reemplaza la paleta. Si más adelante se requiere una
 * paleta específica de alto contraste, se puede reintroducir aquí.
 */
@Composable
fun SignTranslateTheme(
    settingsViewModel: SettingsViewModel,
    content: @Composable () -> Unit,
) {
    val settings by settingsViewModel.uiState.collectAsState()

    val colorScheme = if (settings.darkMode) DarkColors else LightColors

    androidx.compose.runtime.CompositionLocalProvider(
        LocalIsDarkTheme provides settings.darkMode,
    ) {
        MaterialTheme(
            colorScheme = colorScheme,
            typography  = buildTypography(large = settings.largeText),
            content     = content,
        )
    }
}