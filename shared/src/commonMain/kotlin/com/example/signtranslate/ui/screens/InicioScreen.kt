package com.example.signtranslate.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.NotificationsNone
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.signtranslate.ui.theme.BrandPurple
import com.example.signtranslate.ui.theme.BrandPurpleLight
import com.example.signtranslate.ui.theme.CardBg
import com.example.signtranslate.ui.theme.NavBarBg
import com.example.signtranslate.ui.theme.TextBody
import com.example.signtranslate.ui.theme.TextDark

// ── Colores locales sin equivalente en Color.kt ───────────────────────────────
private val GreenAccent = Color(0xFF4CAF50)
private val GreenLight  = Color(0xFFE8F5E9)
private val TealAccent  = Color(0xFF26C6DA)
private val TealLight   = Color(0xFFE0F7FA)

// ── Modelo de datos para tarjetas ─────────────────────────────────────────────
data class MenuOption(
    val icon: String,
    val title: String,
    val subtitle: String,
    val iconBg: Color,
    val iconColor: Color,
    val route: String,
)

@Composable
fun InicioScreen(
    innerPadding: PaddingValues = PaddingValues(),
    onNavigateToTranslator: () -> Unit = {},
    onNavigateToAvatar: () -> Unit = {},
    onNavigateToTextToSign: () -> Unit = {},
    onNavigateToSettings: () -> Unit = {},
    onNotificationsClick: () -> Unit = {},
) {
    val menuOptions = listOf(
        MenuOption(
            icon      = "👋",
            title     = "Traducir señas a texto",
            subtitle  = "Usa la cámara para traducir\nseñas en tiempo real.",
            iconBg    = BrandPurpleLight,
            iconColor = BrandPurple,
            route     = "translator",
        ),
        MenuOption(
            icon      = "🎤",
            title     = "Traducir voz a LSM",
            subtitle  = "Habla y el avatar lo traducirá\na Lengua de Señas Mexicana.",
            iconBg    = GreenLight,
            iconColor = GreenAccent,
            route     = "avatar",
        ),
        MenuOption(
            icon      = "📝",
            title     = "Traducir texto a LSM",
            subtitle  = "Escribe un texto y el avatar\nlo traducirá a LSM.",
            iconBg    = TealLight,
            iconColor = TealAccent,
            route     = "text_to_sign",
        ),
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(innerPadding)
            .background(CardBg)
            .verticalScroll(rememberScrollState())
    ) {
        HeroSection(onNotificationsClick = onNotificationsClick)

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp)
                .padding(top = 24.dp, bottom = 32.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            Text(
                text       = "¿Qué deseas hacer hoy?",
                fontSize   = 18.sp,
                fontWeight = FontWeight.Bold,
                color      = TextDark,
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
private fun HeroSection(onNotificationsClick: () -> Unit) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .background(
                brush = Brush.verticalGradient(
                    colors = listOf(BrandPurpleLight, CardBg)
                )
            )
    ) {
        Column(modifier = Modifier.fillMaxWidth()) {

            // Top bar
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp)
                    .padding(top = 52.dp, bottom = 16.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment     = Alignment.CenterVertically,
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier         = Modifier
                            .size(40.dp)
                            .clip(CircleShape)
                            .background(BrandPurple),
                        contentAlignment = Alignment.Center,
                    ) {
                        Text(text = "👋", fontSize = 20.sp)
                    }
                    Spacer(modifier = Modifier.width(10.dp))
                    Column {
                        Text(
                            text = buildAnnotatedString {
                                withStyle(SpanStyle(color = TextDark, fontWeight = FontWeight.Bold)) {
                                    append("Sign")
                                }
                                withStyle(SpanStyle(color = BrandPurple, fontWeight = FontWeight.Bold)) {
                                    append("Translate")
                                }
                            },
                            fontSize = 20.sp,
                        )
                        Text(
                            text     = "Lengua de Señas Mexicana",
                            fontSize = 11.sp,
                            color    = TextBody,
                        )
                    }
                }

                Box(
                    modifier         = Modifier
                        .size(42.dp)
                        .clip(CircleShape)
                        .background(NavBarBg)
                        .clickable { onNotificationsClick() },
                    contentAlignment = Alignment.Center,
                ) {
                    Icon(
                        imageVector        = Icons.Outlined.NotificationsNone,
                        contentDescription = "Notificaciones",
                        tint               = BrandPurple,
                        modifier           = Modifier.size(24.dp),
                    )
                }
            }

            // Bienvenida + Avatar
            Row(
                modifier              = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp)
                    .padding(bottom = 24.dp),
                verticalAlignment     = Alignment.Bottom,
                horizontalArrangement = Arrangement.SpaceBetween,
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text       = "¡Bienvenido! 👋",
                        fontSize   = 26.sp,
                        fontWeight = FontWeight.ExtraBold,
                        color      = TextDark,
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text       = "Comunicación sin barreras\ncon Lengua de Señas Mexicana.",
                        fontSize   = 14.sp,
                        color      = TextBody,
                        lineHeight = 20.sp,
                    )
                }
                AvatarPlaceholder()
            }
        }
    }
}

// ─────────────────────────────────────────────────────────────────────────────
// AVATAR PLACEHOLDER
// ─────────────────────────────────────────────────────────────────────────────
@Composable
private fun AvatarPlaceholder() {
    Box(
        modifier         = Modifier.size(width = 130.dp, height = 160.dp),
        contentAlignment = Alignment.Center,
    ) {
        Box(
            modifier = Modifier
                .size(110.dp)
                .clip(CircleShape)
                .background(
                    brush = Brush.radialGradient(
                        colors = listOf(
                            BrandPurple.copy(alpha = 0.18f),
                            BrandPurple.copy(alpha = 0.04f),
                        )
                    )
                )
        )
        // NOTA: reemplazar con Image(painter = painterResource(...))
        //       cuando tengas el recurso del avatar 3D
        Text(text = "🧑", fontSize = 80.sp)
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
        colors    = CardDefaults.cardColors(containerColor = NavBarBg),
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
                    .size(50.dp)
                    .clip(RoundedCornerShape(14.dp))
                    .background(option.iconBg),
                contentAlignment = Alignment.Center,
            ) {
                Text(text = option.icon, fontSize = 24.sp)
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
                    color      = TextBody,
                    lineHeight = 17.sp,
                )
            }

            Box(
                modifier         = Modifier
                    .size(32.dp)
                    .clip(CircleShape)
                    .background(option.iconBg),
                contentAlignment = Alignment.Center,
            ) {
                Text(
                    text       = "›",
                    fontSize   = 20.sp,
                    color      = option.iconColor,
                    fontWeight = FontWeight.Bold,
                )
            }
        }
    }
}