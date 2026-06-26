package com.example.signtranslate.ui.screens.translator

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Keyboard
import androidx.compose.material.icons.filled.Mic
import androidx.compose.material.icons.filled.PanTool
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.signtranslate.ui.navigation.Screen
import com.example.signtranslate.viewmodel.translator.TranslatorTab
import com.example.signtranslate.viewmodel.translator.TranslatorViewModel

// NOTA: se eliminó el import de Color.kt fijo (BrandPurple, TextBody, CardBg,
// Color.White) usado como FONDO/TEXTO de superficie. Ahora la pantalla entera
// lee MaterialTheme.colorScheme, así que al activar Modo oscuro en Ajustes,
// el TopAppBar, el contenedor del Scaffold y los tabs cambian a negro junto
// con el resto de la app. BrandPurple solo se conserva como acento de marca
// vía MaterialTheme.colorScheme.primary (que en el tema oscuro sigue siendo
// el morado de marca, pero ahora se referencia desde el ColorScheme en vez
// de un valor fijo).

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TranslatorScreen(
    viewModel: TranslatorViewModel,
    onNavigate: (String) -> Unit,
    // Injected from Android layer for CameraX + MediaPipe
    cameraPreviewContent: @Composable BoxScope.() -> Unit    = {},
    landmarksOverlayContent: @Composable BoxScope.() -> Unit = {}
) {
    val uiState by viewModel.uiState.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text       = "Traductor",
                        fontWeight = FontWeight.Bold,
                        fontSize   = 22.sp,
                        modifier   = Modifier.fillMaxWidth(),
                        textAlign  = androidx.compose.ui.text.style.TextAlign.Center,
                        color      = MaterialTheme.colorScheme.onSurface,
                    )
                },
                actions = {
                    IconButton(onClick = { onNavigate(Screen.Settings.route) }) {
                        Icon(
                            imageVector        = Icons.Default.Settings,
                            contentDescription = "Ajustes",
                            tint               = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.surface
                ),
                windowInsets = WindowInsets(0)
            )
        },
        containerColor = MaterialTheme.colorScheme.background
    ) { paddingValues ->

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            // ── Tab row ──────────────────────────────────────────────────────────
            TranslatorTabRow(
                selectedTab = uiState.activeTab,
                onTabSelected = { viewModel.selectTab(it) }
            )

            // Mostrar Error si existe
            uiState.error?.let { error ->
                Card(
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.errorContainer
                    ),
                    modifier = Modifier.padding(16.dp).fillMaxWidth()
                ) {
                    Text(
                        text = error,
                        color = MaterialTheme.colorScheme.onErrorContainer,
                        fontSize = 12.sp,
                        modifier = Modifier.padding(12.dp)
                    )
                }
            }

            // ── Tab content ──────────────────────────────────────────────────────
            Box(modifier = Modifier.fillMaxSize()) {
                when (uiState.activeTab) {
                    TranslatorTab.SIGN_TO_TEXT -> SignToTextTab(
                        viewModel = viewModel,
                        cameraPreviewContent = cameraPreviewContent,
                        landmarksOverlayContent = landmarksOverlayContent
                    )
                    TranslatorTab.VOICE_TO_LSM -> VoiceToLsmTab(viewModel)
                    TranslatorTab.TEXT_TO_LSM  -> TextToLsmTab(viewModel)
                }
            }
        }
    }
}

// ── Tab row component ──────────────────────────────────────────────────────────

private data class TabItem(
    val tab: TranslatorTab,
    val label: String,
    val icon: ImageVector
)

private val tabs = listOf(
    TabItem(TranslatorTab.SIGN_TO_TEXT, "Señas → Texto", Icons.Default.PanTool),
    TabItem(TranslatorTab.VOICE_TO_LSM, "Voz → LSM",     Icons.Default.Mic),
    TabItem(TranslatorTab.TEXT_TO_LSM,  "Texto → LSM",   Icons.Default.Keyboard)
)

@Composable
private fun TranslatorTabRow(
    selectedTab: TranslatorTab,
    onTabSelected: (TranslatorTab) -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 12.dp, vertical = 10.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        tabs.forEach { item ->
            val isSelected = selectedTab == item.tab
            Button(
                onClick  = { onTabSelected(item.tab) },
                shape    = RoundedCornerShape(50),
                colors   = ButtonDefaults.buttonColors(
                    containerColor = if (isSelected)
                        MaterialTheme.colorScheme.primary
                    else
                        MaterialTheme.colorScheme.surface,
                    contentColor   = if (isSelected)
                        MaterialTheme.colorScheme.onPrimary
                    else
                        MaterialTheme.colorScheme.onSurfaceVariant
                ),
                contentPadding = PaddingValues(horizontal = 12.dp, vertical = 8.dp),
                elevation = ButtonDefaults.buttonElevation(
                    defaultElevation = if (isSelected) 4.dp else 0.dp
                ),
                modifier = Modifier.weight(1f)
            ) {
                Icon(
                    imageVector        = item.icon,
                    contentDescription = null,
                    modifier           = Modifier.size(14.dp)
                )
                Spacer(modifier = Modifier.width(4.dp))
                Text(
                    text     = item.label,
                    fontSize = 11.sp,
                    maxLines = 1
                )
            }
        }
    }
}