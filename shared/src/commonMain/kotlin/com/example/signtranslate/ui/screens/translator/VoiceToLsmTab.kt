package com.example.signtranslate.ui.screens.translator

import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Mic
import androidx.compose.material.icons.filled.MicOff
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.signtranslate.avatar.AvatarRenderer
import com.example.signtranslate.ui.service.AvatarService
import com.example.signtranslate.viewmodel.translator.TranslatorViewModel

// NOTA: se eliminaron los colores fijos Purple / PurpleSoft / Color.White /
// Color(0xFF1A1A2E) / Color(0xFF9996CC). Ahora toda la pestaña lee
// MaterialTheme.colorScheme, así que al activar Modo oscuro en Ajustes esta
// pestaña también cambia a negro junto con el resto de la app.

/**
 * VoiceToLsmTab
 * ─────────────
 * Voz → Señas LSM
 *
 * Layout:
 *   ┌─────────────────────────────────┐
 *   │   AVATAR 3D (siempre visible)   │
 *   ├─────────────────────────────────┤
 *   │   Texto reconocido por voz      │
 *   │   Indicador de escucha          │
 *   │   [ Botón de micrófono ]        │
 *   │   "Presiona para hablar"        │
 *   └─────────────────────────────────┘
 *
 * Conecta con el SpeechRecognizerManager a través del ViewModel.
 */
@Composable
fun VoiceToLsmTab(
    viewModel: TranslatorViewModel
) {
    val uiState by viewModel.uiState.collectAsState()
    val recognizedText = uiState.voiceResult.recognizedText
    val isListening = uiState.voiceResult.isListening

    val primary = MaterialTheme.colorScheme.primary

    // Sincroniza el estado de escucha con el servicio del avatar para animaciones
    LaunchedEffect(isListening) {
        AvatarService.setListening(isListening)
    }

    // Reproduce la animación cuando cambia el texto reconocido
    LaunchedEffect(recognizedText) {
        if (recognizedText.isNotBlank()) {
            AvatarService.playAnimation(recognizedText)
        }
    }

    // Pulsación animada del botón de micrófono
    val pulseScale by animateFloatAsState(
        targetValue = if (isListening) 1.12f else 1f,
        animationSpec = if (isListening) infiniteRepeatable(
            animation = tween(600, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ) else spring(),
        label = "micPulse"
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Spacer(modifier = Modifier.height(12.dp))

        // ── Avatar 3D siempre visible ────────────────────────────────────────
        AvatarRenderer(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight(0.55f),
            allowRotation = false,
            allowZoom = false
        )

        Spacer(modifier = Modifier.height(14.dp))

        // ── Texto detectado ──────────────────────────────────────────────────
        Surface(
            shape = RoundedCornerShape(16.dp),
            color = MaterialTheme.colorScheme.surfaceVariant,
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(
                modifier = Modifier.padding(horizontal = 16.dp, vertical = 12.dp)
            ) {
                Text(
                    text = "Texto detectado",
                    color = MaterialTheme.colorScheme.primary,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.SemiBold
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = recognizedText.ifBlank { "—" },
                    color = MaterialTheme.colorScheme.onSurface,
                    fontSize = 22.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // ── Indicador de escucha (barras animadas) ───────────────────────────
        WaveformIndicator(isActive = isListening, activeColor = primary)

        Spacer(modifier = Modifier.height(14.dp))

        // ── Botón micrófono ──────────────────────────────────────────────────
        Box(contentAlignment = Alignment.Center) {
            // Halo exterior cuando escucha
            if (isListening) {
                Box(
                    modifier = Modifier
                        .size(80.dp)
                        .scale(pulseScale)
                        .background(primary.copy(alpha = 0.15f), CircleShape)
                )
            }
            FloatingActionButton(
                onClick = {
                    if (isListening) {
                        viewModel.stopListening()
                        AvatarService.resetToIdle()
                    } else {
                        viewModel.startListening()
                    }
                },
                containerColor = primary,
                modifier = Modifier.size(64.dp)
            ) {
                Icon(
                    imageVector = if (isListening) Icons.Filled.Mic else Icons.Filled.MicOff,
                    contentDescription = if (isListening) "Detener" else "Hablar",
                    tint = MaterialTheme.colorScheme.onPrimary,
                    modifier = Modifier.size(28.dp)
                )
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = if (isListening) "Escuchando..." else "Presiona para hablar",
            color = if (isListening) primary else MaterialTheme.colorScheme.onSurfaceVariant,
            fontSize = 13.sp,
            fontWeight = if (isListening) FontWeight.SemiBold else FontWeight.Normal,
            textAlign = TextAlign.Center
        )
    }
}

// ─── Waveform animado ─────────────────────────────────────────────────────────
@Composable
private fun WaveformIndicator(
    isActive: Boolean,
    activeColor: androidx.compose.ui.graphics.Color
) {
    val barCount = 9
    Row(
        horizontalArrangement = Arrangement.spacedBy(4.dp),
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.height(36.dp)
    ) {
        repeat(barCount) { index ->
            val delay = index * 80
            val animatedHeight by animateFloatAsState(
                targetValue = if (isActive) (0.3f + ((index % 3) * 0.35f)) else 0.15f,
                animationSpec = if (isActive) infiniteRepeatable(
                    animation = tween(400 + delay, easing = FastOutSlowInEasing),
                    repeatMode = RepeatMode.Reverse
                ) else tween(300),
                label = "bar$index"
            )
            Box(
                modifier = Modifier
                    .width(3.dp)
                    .fillMaxHeight(animatedHeight)
                    .background(
                        color = if (isActive) activeColor else activeColor.copy(alpha = 0.3f),
                        shape = RoundedCornerShape(2.dp)
                    )
            )
        }
    }
}