package com.example.signtranslate.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.CameraAlt
import androidx.compose.material.icons.outlined.ChevronRight
import androidx.compose.material.icons.outlined.Edit
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material.icons.outlined.Mic
import androidx.compose.material.icons.outlined.PersonOutline
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import org.jetbrains.compose.resources.painterResource
import signtranslate.shared.generated.resources.Res
import signtranslate.shared.generated.resources.avatar
import signtranslate.shared.generated.resources.logo

// NOTA: se eliminaron los imports fijos de BrandPurple / BrandPurpleLight /
// CardBg / NavBarBg / TextBody / TextDark como FONDOS y TEXTOS de la pantalla.
// Esos tokens siguen existiendo en Color.kt y se usan en LightColors/DarkColors
// (Theme.kt), pero la pantalla ahora lee MaterialTheme.colorScheme para que
// el fondo, las tarjetas y los textos cambien solos al activar Modo oscuro.
//
// Los acentos de marca (BrandPurple) se conservan SOLO para icon tint, ya que
// son un acento de marca, no una superficie — pero se obtienen de
// MaterialTheme.colorScheme.primary para que también respondan al tema si
// algún día cambia.

// Acentos secundarios de las opciones del menú (no cambian con el tema,
// son colores semánticos de cada acción — se atenúan automáticamente
// en fondo oscuro porque sus "Light" pasan a ser superficies oscuras).
private val GreenAccent = Color(0xFF4CAF50)
private val TealAccent  = Color(0xFF26C6DA)

data class MenuOption(
    val icon:      ImageVector,
    val title:     String,
    val subtitle:  String,
    val iconColor: Color,
    val route:     String,
)

@Composable
fun InicioScreen(
    innerPadding:           PaddingValues = PaddingValues(),
    onNavigateToTranslator: () -> Unit    = {},
    onNavigateToAvatar:     () -> Unit    = {},
    onNavigateToTextToSign: () -> Unit    = {},
    onNavigateToSettings:   () -> Unit    = {},
    onRegisterClick:        () -> Unit    = {},
) {
    val primary = MaterialTheme.colorScheme.primary

    val menuOptions = listOf(
        MenuOption(
            icon      = Icons.Outlined.CameraAlt,
            title     = "Traducir señas a texto",
            subtitle  = "Usa la cámara para traducir señas en tiempo real.",
            iconColor = primary,
            route     = "translator",
        ),
        MenuOption(
            icon      = Icons.Outlined.Mic,
            title     = "Traducir voz a LSM",
            subtitle  = "Habla y el avatar lo traducirá a Lengua de Señas Mexicana.",
            iconColor = GreenAccent,
            route     = "avatar",
        ),
        MenuOption(
            icon      = Icons.Outlined.Edit,
            title     = "Traducir texto a LSM",
            subtitle  = "Escribe un texto y el avatar lo traducirá a LSM.",
            iconColor = TealAccent,
            route     = "text_to_sign",
        ),
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(innerPadding)
            .background(MaterialTheme.colorScheme.background)
            .verticalScroll(rememberScrollState())
    ) {
        HeroSection(onRegisterClick = onRegisterClick)

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(MaterialTheme.colorScheme.background)
                .padding(horizontal = 20.dp)
                .padding(top = 12.dp, bottom = 32.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Text(
                text       = "¿Qué deseas hacer hoy?",
                fontSize   = 18.sp,
                fontWeight = FontWeight.Bold,
                color      = MaterialTheme.colorScheme.onBackground,
                modifier   = Modifier.padding(bottom = 4.dp)
            )
            menuOptions.forEach { option ->
                MenuCard(
                    option  = option,
                    onClick = {
                        when (option.route) {
                            "translator"   -> onNavigateToTranslator()
                            "avatar"       -> onNavigateToAvatar()
                            "text_to_sign" -> onNavigateToTextToSign()
                        }
                    }
                )
            }
        }
    }
}

// ─────────────────────────────────────────────────────────────────────────────
// HERO SECTION
// ─────────────────────────────────────────────────────────────────────────────
@Composable
private fun HeroSection(onRegisterClick: () -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(MaterialTheme.colorScheme.background)
    ) {
        // ── Top bar: tarjeta redondeada ────────────────────────────────────
        Card(
            modifier  = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp)
                .padding(top = 48.dp, bottom = 16.dp),
            shape     = RoundedCornerShape(24.dp),
            colors    = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
            elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        ) {
            Row(
                modifier              = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp, vertical = 14.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment     = Alignment.CenterVertically,
            ) {
                Image(
                    painter            = painterResource(Res.drawable.logo),
                    contentDescription = "SignTranslate Logo",
                    modifier           = Modifier
                        .weight(1f)
                        .height(80.dp),
                    contentScale       = ContentScale.Fit,
                    alignment          = Alignment.CenterStart,
                )

                Box(
                    modifier         = Modifier
                        .size(52.dp)
                        .clip(CircleShape)
                        .background(MaterialTheme.colorScheme.primaryContainer)
                        .clickable { onRegisterClick() },
                    contentAlignment = Alignment.Center,
                ) {
                    Icon(
                        imageVector        = Icons.Outlined.PersonOutline,
                        contentDescription = "Registrarse / Perfil",
                        tint               = MaterialTheme.colorScheme.primary,
                        modifier           = Modifier.size(28.dp),
                    )
                }
            }
        }

        // ── Hero: tarjeta bienvenida + avatar ─────────────────────────────
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(250.dp),
        ) {
            // Círculo difuso detrás del avatar
            Box(
                modifier = Modifier
                    .align(Alignment.CenterEnd)
                    .padding(end = 10.dp)
                    .size(240.dp)
                    .clip(CircleShape)
                    .background(MaterialTheme.colorScheme.primary.copy(alpha = 0.08f))
            )

            // Tarjeta bienvenida con piquito apuntando a la derecha (hacia el avatar)
            SpeechBubbleCard(
                modifier = Modifier
                    .align(Alignment.CenterStart)
                    .padding(start = 20.dp)
                    .width(200.dp)
            )

            // Avatar — sobresale del Box hacia arriba
            Image(
                painter            = painterResource(Res.drawable.avatar),
                contentDescription = "Avatar LSM",
                modifier           = Modifier
                    .align(Alignment.BottomEnd)
                    .width(230.dp)
                    .height(360.dp)
                    .offset(x = 4.dp, y = (-30).dp),
                contentScale       = ContentScale.Fit,
            )
        }
    }
}

// ─────────────────────────────────────────────────────────────────────────────
// SPEECH BUBBLE CARD — tarjeta con piquito apuntando al avatar (derecha)
// ─────────────────────────────────────────────────────────────────────────────
@Composable
private fun SpeechBubbleCard(modifier: Modifier = Modifier) {
    val bgColor      = MaterialTheme.colorScheme.surface
    val borderColor  = MaterialTheme.colorScheme.outlineVariant
    val textColor    = MaterialTheme.colorScheme.onSurface
    val bodyColor    = MaterialTheme.colorScheme.onSurfaceVariant
    val accentColor  = MaterialTheme.colorScheme.primary
    val tipWidthDp   = 18.dp
    val tipHeightDp  = 14.dp
    val strokeWidthDp = 1.5.dp

    Box(modifier = modifier) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(end = tipWidthDp)
                .drawBehind {
                    val r    = 20.dp.toPx()
                    val w    = size.width
                    val h    = size.height
                    val tipW = tipWidthDp.toPx()
                    val tipH = tipHeightDp.toPx()
                    val midY = h * 0.42f
                    val strokeW = strokeWidthDp.toPx()

                    val path = Path().apply {
                        moveTo(r, 0f)
                        lineTo(w - r, 0f)
                        cubicTo(w, 0f, w, 0f, w, r)           // ← esquina sup-der
                        lineTo(w, midY - tipH / 2f)
                        lineTo(w + tipW, midY)                 // punta del piquito
                        lineTo(w, midY + tipH / 2f)
                        lineTo(w, h - r)
                        cubicTo(w, h, w, h, w - r, h)         // ← esquina inf-der
                        lineTo(r, h)
                        cubicTo(0f, h, 0f, h, 0f, h - r)      // ← esquina inf-izq
                        lineTo(0f, r)
                        cubicTo(0f, 0f, 0f, 0f, r, 0f)        // ← esquina sup-izq
                        close()
                    }
                    // Relleno
                    drawPath(path, color = bgColor)
                    // Contorno — evita que la tarjeta se camufle con el fondo
                    // cuando ambos son claros (o ambos oscuros) en el tema activo.
                    drawPath(
                        path = path,
                        color = borderColor,
                        style = androidx.compose.ui.graphics.drawscope.Stroke(width = strokeW)
                    )
                }
        ) {
            Column(
                modifier = Modifier.padding(
                    start  = 20.dp,
                    top    = 20.dp,
                    end    = 16.dp,
                    bottom = 20.dp,
                ),
                verticalArrangement = Arrangement.spacedBy(10.dp),
            ) {
                Text(
                    text       = "¡Bienvenido! 👋",
                    fontSize   = 16.sp,
                    fontWeight = FontWeight.ExtraBold,
                    color      = textColor,
                )
                Text(
                    text       = "Comunicación sin barreras con Lengua de Señas Mexicana.",
                    fontSize   = 12.sp,
                    color      = bodyColor,
                    lineHeight = 18.sp,
                )
                Icon(
                    imageVector        = Icons.Outlined.FavoriteBorder,
                    contentDescription = null,
                    tint               = accentColor,
                    modifier           = Modifier.size(22.dp),
                )
            }
        }
    }
}

// ─────────────────────────────────────────────────────────────────────────────
// MENU CARD
// ─────────────────────────────────────────────────────────────────────────────
@Composable
private fun MenuCard(
    option:  MenuOption,
    onClick: () -> Unit,
) {
    Card(
        modifier  = Modifier
            .fillMaxWidth()
            .clickable { onClick() },
        shape     = RoundedCornerShape(16.dp),
        colors    = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
    ) {
        Row(
            modifier              = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 16.dp),
            verticalAlignment     = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(14.dp),
        ) {
            Box(
                modifier         = Modifier
                    .size(52.dp)
                    .clip(RoundedCornerShape(14.dp))
                    .background(option.iconColor.copy(alpha = 0.14f)),
                contentAlignment = Alignment.Center,
            ) {
                Icon(
                    imageVector        = option.icon,
                    contentDescription = option.title,
                    tint               = option.iconColor,
                    modifier           = Modifier.size(28.dp),
                )
            }

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text       = option.title,
                    fontSize   = 15.sp,
                    fontWeight = FontWeight.Bold,
                    color      = option.iconColor,
                )
                Spacer(modifier = Modifier.height(3.dp))
                Text(
                    text       = option.subtitle,
                    fontSize   = 12.sp,
                    color      = MaterialTheme.colorScheme.onSurfaceVariant,
                    lineHeight = 17.sp,
                )
            }

            Icon(
                imageVector        = Icons.Outlined.ChevronRight,
                contentDescription = "Ir",
                tint               = option.iconColor,
                modifier           = Modifier.size(22.dp),
            )
        }
    }
}