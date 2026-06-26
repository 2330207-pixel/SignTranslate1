package com.example.signtranslate.ui.screens.translator

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import com.example.signtranslate.data.translator.TranslationResult
import com.example.signtranslate.ui.components.translator.*
import com.example.signtranslate.ui.theme.CameraSurface
import com.example.signtranslate.ui.theme.LocalIsDarkTheme
import com.example.signtranslate.ui.theme.SuccessGreen
import com.example.signtranslate.viewmodel.translator.TranslatorViewModel

// NOTA sobre el recuadro de cámara:
// - Modo claro: fondo Color.Black (vía CameraSurface.background) sin contorno
//   propio — ya contrasta solo contra el fondo blanco, igual que antes.
// - Modo oscuro: fondo gris translúcido (DarkCameraBg) + contorno permanente
//   (DarkCameraBorder) para que el cuadro se distinga del fondo negro de la
//   pantalla, tal como se ve en modo claro donde el cuadro siempre se nota.
// El borde verde de "cámara activa" (SuccessGreen) se dibuja ENCIMA del
// contorno base cuando isCameraActive = true, en ambos temas — son capas
// independientes, no se pisan entre sí.

@Composable
fun SignToTextTab(
    viewModel: TranslatorViewModel,
    cameraPreviewContent: @Composable BoxScope.() -> Unit,
    landmarksOverlayContent: @Composable BoxScope.() -> Unit
) {
    val uiState by viewModel.uiState.collectAsState()
    val isDark = LocalIsDarkTheme.current

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {

        // ───────────────── CAMERA BOX ─────────────────
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
                .padding(horizontal = 12.dp, vertical = 6.dp)
                .clip(RoundedCornerShape(20.dp))
                .background(CameraSurface.background(isDark))
                .border(
                    width = CameraSurface.borderWidth(isDark),
                    color = CameraSurface.border(isDark),
                    shape = RoundedCornerShape(20.dp)
                )
                .then(
                    if (uiState.isCameraActive)
                        Modifier.border(
                            2.dp,
                            SuccessGreen,
                            RoundedCornerShape(20.dp)
                        )
                    else Modifier
                )
        ) {

            // Mostrar preview y landmarks SOLO cuando la cámara está activa
            if (uiState.isCameraActive) {
                cameraPreviewContent()
                landmarksOverlayContent()
            }

            // Indicador de cámara activa
            if (uiState.isCameraActive) {
                CameraActiveIndicator(
                    modifier = Modifier
                        .align(Alignment.TopStart)
                        .padding(12.dp)
                )
            }

            // Controles de cámara
            CameraControls(
                isCameraActive = uiState.isCameraActive,
                isFrontCamera = uiState.isFrontCamera,
                onToggleCamera = {
                    if (uiState.isCameraActive) {
                        viewModel.stopCamera()
                    } else {
                        viewModel.startCamera()
                    }
                },
                onFlipCamera = {
                    viewModel.flipCamera()
                },
                modifier = Modifier
                    .align(Alignment.BottomEnd)
                    .padding(12.dp)
            )
        }

        Spacer(modifier = Modifier.height(6.dp))

        // ───────────────── TARJETA DE TRADUCCIÓN ─────────────────
        TranslationCard(
            result = uiState.translationResult ?: TranslationResult.EMPTY,
            isHandDetected = uiState.handDetections.isNotEmpty(),
            onSpeakClick = {
                uiState.translationResult?.label?.displayText?.let { viewModel.speakText(it) }
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp)
        )

        Spacer(modifier = Modifier.height(12.dp))
    }
}