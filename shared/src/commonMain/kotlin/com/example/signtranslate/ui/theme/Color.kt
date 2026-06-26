package com.example.signtranslate.ui.theme

import androidx.compose.ui.graphics.Color

// ── Marca principal ───────────────────────────────────────────────────────────
val BrandPurple      = Color(0xFF7051E3)
val BrandPurpleLight = Color(0xFFEEEDFE)
val BrandPurpleMid   = Color(0xFFAFA9EC)

// ── Navegación inferior ───────────────────────────────────────────────────────
val NavBarBg         = Color(0xFFFFFFFF)   // blanco puro para el fondo de la barra

// ── Fondos del gradiente de pantalla ─────────────────────────────────────────
val BgGradientTop    = Color(0xFF7B5EA7)
val BgGradientMid    = Color(0xFF6B47A8)
val BgGradientBottom = Color(0xFF4A2C8A)

// ── Card (gris lavanda suave — imagen 2) ──────────────────────────────────────
//    Formato CORRECTO en Compose: 0xFFRRGGBB  (FF = opaco al 100%)
val CardBg           = Color(0xFFF0EDF8)   // lavanda muy suave, igual al card de la imagen objetivo

// ── Textos ────────────────────────────────────────────────────────────────────
val TextDark         = Color(0xFF111625)
val TextBody         = Color(0xFF5E6577)
val TextHint         = Color(0xFFBEC5D4)

// ── Bordes y divisores ────────────────────────────────────────────────────────
val DividerColor     = Color(0xFFDDDAF0)   // lavanda suave (antes E2E8F0 azulado)

// ── Semánticos ────────────────────────────────────────────────────────────────
val SuccessGreen     = Color(0xFF1D9E75)
val SuccessGreenBg   = Color(0xFFE1F5EE)
val ErrorRed         = Color(0xFFE24B4A)
val ErrorRedBg       = Color(0xFFFCEBEB)

// ── Google (colores oficiales) ────────────────────────────────────────────────
val GoogleRed        = Color(0xFFEA4335)
val GoogleBlue       = Color(0xFF4285F4)
val GoogleYellow     = Color(0xFFFBBC05)
val GoogleGreen      = Color(0xFF34A853)

// ── Modo oscuro — negro puro de fondo + superficies con contraste real ───────
//    Objetivo: cuando darkMode = true, cada pestaña/pantalla de la app
//    (Inicio, Traductor, Avatar, Ajustes) cambia a fondo negro consistente,
//    PERO las tarjetas/cuadros deben seguir contrastando contra ese fondo
//    igual que en modo claro (donde surface blanco resalta sobre background
//    blanco gracias a la sombra). En negro no hay esa sombra visible, así
//    que la diferencia de luminosidad debe venir del color mismo.
val DarkBg            = Color(0xFF000000)  // fondo base de pantalla
val DarkSurface       = Color(0xFF1C1C1F)  // tarjetas — contraste claro contra DarkBg
val DarkSurfaceVariant= Color(0xFF232327)  // secciones agrupadoras / cuadros de info
val DarkCardBg        = Color(0xFF1C1C1F)  // equivalente oscuro de CardBg
val DarkDivider       = Color(0xFF3A3A3E)  // divisores sobre fondo negro
val DarkNavBarBg      = Color(0xFF000000)  // barra inferior en negro
val DarkTextPrimary   = Color(0xFFF5F5F5)  // texto principal sobre negro
val DarkTextSecondary = Color(0xFF9A9A9A)  // texto secundario / descripciones
val DarkOnPrimary     = Color(0xFFFFFFFF)  // texto/icono sobre BrandPurple en oscuro

// ── Cámara en modo oscuro ──────────────────────────────────────────────────
//    En claro el recuadro de cámara es negro puro y ya contrasta solo contra
//    el fondo blanco. En oscuro ese mismo negro se confunde con el fondo, así
//    que se usa un gris semitransparente + contorno para que siga siendo
//    visible como un "cuadro" diferenciado.
val DarkCameraBg       = Color(0x99202024)  // gris oscuro translúcido (60% alpha aprox)
val DarkCameraBorder   = Color(0xFF4A4A50)  // contorno visible sobre fondo negro