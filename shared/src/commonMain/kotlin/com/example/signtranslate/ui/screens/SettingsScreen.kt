package com.example.signtranslate.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.AutoAwesome
import androidx.compose.material.icons.outlined.ChevronRight
import androidx.compose.material.icons.outlined.Contrast
import androidx.compose.material.icons.outlined.Info
import androidx.compose.material.icons.outlined.LightMode
import androidx.compose.material.icons.outlined.Logout
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material.icons.outlined.PhotoCamera
import androidx.compose.material.icons.outlined.SignLanguage
import androidx.compose.material.icons.outlined.Speed
import androidx.compose.material.icons.outlined.TextFields
import androidx.compose.material.icons.outlined.Visibility
import androidx.compose.material.icons.outlined.VolumeUp
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.signtranslate.ui.theme.BrandPurple
import com.example.signtranslate.ui.theme.BrandPurpleLight
import com.example.signtranslate.ui.theme.CardBg
import com.example.signtranslate.ui.theme.DividerColor
import com.example.signtranslate.ui.theme.ErrorRed
import com.example.signtranslate.ui.theme.ErrorRedBg
import com.example.signtranslate.ui.theme.NavBarBg
import com.example.signtranslate.ui.theme.TextBody
import com.example.signtranslate.ui.theme.TextDark

// ── Modelo simple de usuario autenticado ──────────────────────────────────────
data class UserProfile(
    val name: String,
    val email: String,
    val initials: String,
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen(
    innerPadding: PaddingValues = PaddingValues(),
    // En tu app real conecta esto a tu AuthViewModel
    userProfile: UserProfile? = UserProfile(
        name     = "María González",
        email    = "maria.gonzalez@email.com",
        initials = "MG",
    ),
    onLoginClick: () -> Unit = {},
    onLogoutClick: () -> Unit = {},
) {
    // Estados locales de los toggles
    var temaClaro         by remember { mutableStateOf(true) }
    var textoGrande       by remember { mutableStateOf(false) }
    var altoContraste     by remember { mutableStateOf(false) }
    var mostrarPuntos     by remember { mutableStateOf(true) }
    var reconocimientoAuto by remember { mutableStateOf(true) }
    var volumenVoz        by remember { mutableFloatStateOf(0.7f) }

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        text       = "Configuración",
                        fontSize   = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color      = TextDark,
                    )
                },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                    containerColor = NavBarBg,
                )
            )
        },
    ) { scaffoldPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(scaffoldPadding)
                .padding(bottom = innerPadding.calculateBottomPadding())
                .background(CardBg)
                .verticalScroll(rememberScrollState())
        ) {

            // ── SECCIÓN DE PERFIL ─────────────────────────────────────────────
            if (userProfile != null) {
                ProfileCard(
                    profile        = userProfile,
                    onLogoutClick  = onLogoutClick,
                )
            } else {
                LoginCard(onLoginClick = onLoginClick)
            }

            Spacer(Modifier.height(16.dp))

            // ── GENERAL ───────────────────────────────────────────────────────
            SettingsSection(title = "GENERAL") {
                SettingsToggleItem(
                    icon    = Icons.Outlined.LightMode,
                    label   = "Tema de la aplicación",
                    value   = if (temaClaro) "Claro" else "Oscuro",
                    checked = temaClaro,
                    onCheckedChange = { temaClaro = it },
                )
                SettingsDivider()
                SettingsNavItem(
                    icon    = Icons.Outlined.SignLanguage,
                    label   = "Lenguaje de señas",
                    value   = "Lengua de Señas Mexicana (LSM)",
                    onClick = {},
                )
            }

            Spacer(Modifier.height(12.dp))

            // ── SONIDO Y VOZ ──────────────────────────────────────────────────
            SettingsSection(title = "SONIDO Y VOZ") {
                SettingsNavItem(
                    icon    = Icons.Outlined.Speed,
                    label   = "Velocidad de voz del avatar",
                    value   = "Normal",
                    onClick = {},
                )
                SettingsDivider()
                SettingsSliderItem(
                    icon    = Icons.Outlined.VolumeUp,
                    label   = "Volumen de la voz",
                    value   = volumenVoz,
                    onValueChange = { volumenVoz = it },
                )
            }

            Spacer(Modifier.height(12.dp))

            // ── CÁMARA ────────────────────────────────────────────────────────
            SettingsSection(title = "CÁMARA") {
                SettingsNavItem(
                    icon    = Icons.Outlined.PhotoCamera,
                    label   = "Resolución de la cámara",
                    value   = "1080p",
                    onClick = {},
                )
                SettingsDivider()
                SettingsToggleItem(
                    icon    = Icons.Outlined.Visibility,
                    label   = "Mostrar puntos de la mano",
                    subtitle = "Muestra los puntos de detección",
                    checked = mostrarPuntos,
                    onCheckedChange = { mostrarPuntos = it },
                )
                SettingsDivider()
                SettingsToggleItem(
                    icon    = Icons.Outlined.AutoAwesome,
                    label   = "Reconocimiento automático",
                    subtitle = "Detectar señas automáticamente",
                    checked = reconocimientoAuto,
                    onCheckedChange = { reconocimientoAuto = it },
                )
            }

            Spacer(Modifier.height(12.dp))

            // ── ACCESIBILIDAD ─────────────────────────────────────────────────
            SettingsSection(title = "ACCESIBILIDAD") {
                SettingsToggleItem(
                    icon    = Icons.Outlined.TextFields,
                    label   = "Texto grande",
                    checked = textoGrande,
                    onCheckedChange = { textoGrande = it },
                )
                SettingsDivider()
                SettingsToggleItem(
                    icon    = Icons.Outlined.Contrast,
                    label   = "Alto contraste",
                    checked = altoContraste,
                    onCheckedChange = { altoContraste = it },
                )
            }

            Spacer(Modifier.height(12.dp))

            // ── ACERCA DE ─────────────────────────────────────────────────────
            SettingsSection(title = "ACERCA DE") {
                SettingsNavItem(
                    icon    = Icons.Outlined.Info,
                    label   = "Acerca de SignTranslate",
                    value   = "Versión 1.0.0",
                    onClick = {},
                )
            }

            Spacer(Modifier.height(32.dp))
        }
    }
}

// ─────────────────────────────────────────────────────────────────────────────
// PERFIL — usuario logueado
// ─────────────────────────────────────────────────────────────────────────────
@Composable
private fun ProfileCard(
    profile: UserProfile,
    onLogoutClick: () -> Unit,
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp),
        shape     = RoundedCornerShape(20.dp),
        colors    = CardDefaults.cardColors(containerColor = NavBarBg),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
    ) {
        Row(
            modifier          = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(14.dp),
        ) {
            // Avatar con iniciales
            Box(
                modifier         = Modifier
                    .size(60.dp)
                    .clip(CircleShape)
                    .background(BrandPurpleLight),
                contentAlignment = Alignment.Center,
            ) {
                Text(
                    text       = profile.initials,
                    fontSize   = 22.sp,
                    fontWeight = FontWeight.Bold,
                    color      = BrandPurple,
                )
            }

            // Nombre y email
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text       = profile.name,
                    fontSize   = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color      = TextDark,
                )
                Text(
                    text     = profile.email,
                    fontSize = 13.sp,
                    color    = TextBody,
                )
            }

            // Botón cerrar sesión
            IconButton(
                onClick  = onLogoutClick,
                modifier = Modifier
                    .background(ErrorRedBg, CircleShape)
                    .size(40.dp),
            ) {
                Icon(
                    imageVector        = Icons.Outlined.Logout,
                    contentDescription = "Cerrar sesión",
                    tint               = ErrorRed,
                    modifier           = Modifier.size(20.dp),
                )
            }
        }
    }
}

// ─────────────────────────────────────────────────────────────────────────────
// PERFIL — sin sesión iniciada
// ─────────────────────────────────────────────────────────────────────────────
@Composable
private fun LoginCard(onLoginClick: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp),
        shape     = RoundedCornerShape(20.dp),
        colors    = CardDefaults.cardColors(containerColor = NavBarBg),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
    ) {
        Row(
            modifier          = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(14.dp),
        ) {
            // Avatar vacío
            Box(
                modifier         = Modifier
                    .size(60.dp)
                    .clip(CircleShape)
                    .background(BrandPurpleLight),
                contentAlignment = Alignment.Center,
            ) {
                Icon(
                    imageVector        = Icons.Outlined.Person,
                    contentDescription = null,
                    tint               = BrandPurple,
                    modifier           = Modifier.size(30.dp),
                )
            }

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text       = "Sin sesión iniciada",
                    fontSize   = 15.sp,
                    fontWeight = FontWeight.Bold,
                    color      = TextDark,
                )
                Text(
                    text     = "Inicia sesión para guardar tu progreso",
                    fontSize = 12.sp,
                    color    = TextBody,
                )
            }

            Button(
                onClick = onLoginClick,
                colors  = ButtonDefaults.buttonColors(containerColor = BrandPurple),
                shape   = RoundedCornerShape(12.dp),
                contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
            ) {
                Text("Entrar", fontSize = 13.sp, color = NavBarBg)
            }
        }
    }
}

// ─────────────────────────────────────────────────────────────────────────────
// SECCIÓN CONTENEDORA
// ─────────────────────────────────────────────────────────────────────────────
@Composable
private fun SettingsSection(
    title: String,
    content: @Composable ColumnScope.() -> Unit,
) {
    Column(modifier = Modifier.fillMaxWidth()) {
        Text(
            text     = title,
            fontSize = 11.sp,
            fontWeight = FontWeight.Bold,
            color    = BrandPurple,
            modifier = Modifier.padding(horizontal = 28.dp, vertical = 6.dp),
        )
        Card(
            modifier  = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
            shape     = RoundedCornerShape(20.dp),
            colors    = CardDefaults.cardColors(containerColor = NavBarBg),
            elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
        ) {
            Column(modifier = Modifier.fillMaxWidth()) {
                content()
            }
        }
    }
}

// ─────────────────────────────────────────────────────────────────────────────
// ITEM — con toggle (Switch)
// ─────────────────────────────────────────────────────────────────────────────
@Composable
private fun SettingsToggleItem(
    icon: ImageVector,
    label: String,
    subtitle: String? = null,
    value: String? = null,
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit,
) {
    Row(
        modifier          = Modifier
            .fillMaxWidth()
            .clickable { onCheckedChange(!checked) }
            .padding(horizontal = 16.dp, vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(12.dp),
    ) {
        Icon(icon, contentDescription = null, tint = BrandPurple, modifier = Modifier.size(22.dp))
        Column(modifier = Modifier.weight(1f)) {
            Text(label, fontSize = 14.sp, color = TextDark, fontWeight = FontWeight.Medium)
            if (subtitle != null) {
                Text(subtitle, fontSize = 12.sp, color = TextBody)
            }
            if (value != null) {
                Text(value, fontSize = 12.sp, color = TextBody)
            }
        }
        Switch(
            checked         = checked,
            onCheckedChange = onCheckedChange,
            colors          = SwitchDefaults.colors(
                checkedThumbColor       = NavBarBg,
                checkedTrackColor       = BrandPurple,
                uncheckedThumbColor     = NavBarBg,
                uncheckedTrackColor     = DividerColor,
                uncheckedBorderColor    = DividerColor,
            )
        )
    }
}

// ─────────────────────────────────────────────────────────────────────────────
// ITEM — con flecha (navegación)
// ─────────────────────────────────────────────────────────────────────────────
@Composable
private fun SettingsNavItem(
    icon: ImageVector,
    label: String,
    value: String? = null,
    onClick: () -> Unit,
) {
    Row(
        modifier          = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
            .padding(horizontal = 16.dp, vertical = 14.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(12.dp),
    ) {
        Icon(icon, contentDescription = null, tint = BrandPurple, modifier = Modifier.size(22.dp))
        Column(modifier = Modifier.weight(1f)) {
            Text(label, fontSize = 14.sp, color = TextDark, fontWeight = FontWeight.Medium)
            if (value != null) {
                Text(value, fontSize = 12.sp, color = TextBody)
            }
        }
        Icon(
            Icons.Outlined.ChevronRight,
            contentDescription = null,
            tint     = TextBody,
            modifier = Modifier.size(20.dp),
        )
    }
}

// ─────────────────────────────────────────────────────────────────────────────
// ITEM — con slider
// ─────────────────────────────────────────────────────────────────────────────
@Composable
private fun SettingsSliderItem(
    icon: ImageVector,
    label: String,
    value: Float,
    onValueChange: (Float) -> Unit,
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 12.dp),
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(12.dp),
        ) {
            Icon(icon, contentDescription = null, tint = BrandPurple, modifier = Modifier.size(22.dp))
            Text(label, fontSize = 14.sp, color = TextDark, fontWeight = FontWeight.Medium)
        }
        Slider(
            value         = value,
            onValueChange = onValueChange,
            modifier      = Modifier
                .fillMaxWidth()
                .padding(start = 34.dp, top = 4.dp),
            colors        = SliderDefaults.colors(
                thumbColor       = BrandPurple,
                activeTrackColor = BrandPurple,
                inactiveTrackColor = BrandPurpleLight,
            )
        )
    }
}

// ─────────────────────────────────────────────────────────────────────────────
// DIVISOR INTERNO
// ─────────────────────────────────────────────────────────────────────────────
@Composable
private fun SettingsDivider() {
    HorizontalDivider(
        modifier  = Modifier.padding(start = 50.dp),
        thickness = 0.5.dp,
        color     = DividerColor,
    )
}