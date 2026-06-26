package com.example.signtranslate.ui.screens.avatar

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.material.icons.outlined.Tune
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.signtranslate.avatar.AvatarRenderer
import com.example.signtranslate.ui.service.AvatarService
import com.example.signtranslate.viewmodel.avatar.AvatarViewModel

// NOTA: se eliminaron los valores fijos Purple / PurpleLight / Color.White /
// Color(0xFF1A1A2E) / Color(0xFFFAF9FF) / Color(0xFF9996CC) usados como
// superficies y textos. Ahora toda la pantalla lee MaterialTheme.colorScheme,
// así que al activar Modo oscuro en Ajustes el TopAppBar, el fondo del
// Scaffold y las tarjetas de personalización cambian a negro junto con el
// resto de la app.

/**
 * AvatarScreen
 * ────────────
 * Pantalla principal del módulo Avatar 3D.
 * • Avatar.glb centrado, rotable y con zoom
 * • Badge "Avatar listo para LSM"
 * • Botón Personalizar Avatar
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AvatarScreen(
    onNavigateToCustomization: () -> Unit = {},
    onNavigateToSettings: () -> Unit = {},
    viewModel: AvatarViewModel
) {
    val avatarState by AvatarService.state.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Mi Avatar",
                        fontWeight = FontWeight.ExtraBold,
                        fontSize = 20.sp,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                },
                actions = {
                    IconButton(onClick = onNavigateToSettings) {
                        Surface(
                            shape = RoundedCornerShape(12.dp),
                            color = MaterialTheme.colorScheme.primaryContainer
                        ) {
                            Icon(
                                imageVector = Icons.Outlined.Settings,
                                contentDescription = "Ajustes",
                                tint = MaterialTheme.colorScheme.primary,
                                modifier = Modifier.padding(8.dp)
                            )
                        }
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.surface
                )
            )
        },
        containerColor = MaterialTheme.colorScheme.background
    ) { innerPadding ->

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(horizontal = 16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            Spacer(modifier = Modifier.height(8.dp))

            // ── Visor 3D del avatar ──────────────────────────────────────────
            AvatarRenderer(
                modifier = Modifier
                    .fillMaxWidth()
                    .fillMaxHeight(0.58f),
                allowRotation = true,
                allowZoom = true,
                showBadge = true
            )

            Spacer(modifier = Modifier.height(20.dp))

            // ── Sección Personalización ──────────────────────────────────────
            Text(
                text = "Personalización",
                fontWeight = FontWeight.SemiBold,
                fontSize = 16.sp,
                color = MaterialTheme.colorScheme.onSurface,
                modifier = Modifier
                    .align(Alignment.Start)
                    .padding(bottom = 12.dp)
            )

            // Tarjetas de personalización
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                CustomizationCard(
                    icon = "💇",
                    title = "Cabello",
                    subtitle = "8 estilos",
                    modifier = Modifier.weight(1f)
                )
                CustomizationCard(
                    icon = "👕",
                    title = "Ropa",
                    subtitle = "12 prendas",
                    modifier = Modifier.weight(1f)
                )
            }

            Spacer(modifier = Modifier.height(10.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                CustomizationCard(
                    icon = "🎨",
                    title = "Color de piel",
                    subtitle = "6 tonos",
                    modifier = Modifier.weight(1f)
                )
                CustomizationCard(
                    icon = "🕶",
                    title = "Accesorios",
                    subtitle = "5 items",
                    modifier = Modifier.weight(1f)
                )
            }

            Spacer(modifier = Modifier.height(20.dp))

            // ── Botón Personalizar Avatar ────────────────────────────────────
            Button(
                onClick = onNavigateToCustomization,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(52.dp),
                shape = RoundedCornerShape(16.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    contentColor   = MaterialTheme.colorScheme.onPrimary
                )
            ) {
                Icon(
                    imageVector = Icons.Outlined.Tune,
                    contentDescription = null,
                    modifier = Modifier.size(20.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "Personalizar Avatar",
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 15.sp
                )
            }
        }
    }
}

@Composable
private fun CustomizationCard(
    icon: String,
    title: String,
    subtitle: String,
    modifier: Modifier = Modifier,
    onClick: () -> Unit = {}
) {
    Surface(
        modifier = modifier,
        shape = RoundedCornerShape(16.dp),
        color = MaterialTheme.colorScheme.surfaceVariant,
        onClick = onClick,
        tonalElevation = 0.dp
    ) {
        Row(
            modifier = Modifier.padding(12.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            Surface(
                shape = RoundedCornerShape(10.dp),
                color = MaterialTheme.colorScheme.primaryContainer,
                modifier = Modifier.size(38.dp)
            ) {
                Box(contentAlignment = Alignment.Center) {
                    Text(text = icon, fontSize = 18.sp)
                }
            }
            Column {
                Text(
                    text = title,
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 13.sp,
                    color = MaterialTheme.colorScheme.onSurface
                )
                Text(
                    text = subtitle,
                    fontSize = 11.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
    }
}