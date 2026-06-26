package com.example.signtranslate.mediapipe

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.DrawScope
import com.example.signtranslate.data.translator.HandDetectionResult

/**
 * Overlay de landmarks de MediaPipe.
 *
 * showHandPoints:
 *   Se conecta a SettingsViewModel.
 *   Si es false no se dibuja nada.
 */
@Composable
fun HandLandmarksOverlay(
    detections: List<HandDetectionResult>,
    showHandPoints: Boolean,
    modifier: Modifier = Modifier.fillMaxSize()
) {

    if (!showHandPoints) return

    Canvas(modifier = modifier) {
        detections.forEach { hand ->
            drawHandConnections(hand, this)
            drawHandLandmarks(hand, this)
        }
    }
}

/**
 * Conexiones estándar de MediaPipe Hand Landmarker.
 */
private val HAND_CONNECTIONS = listOf(
    // Pulgar
    0 to 1, 1 to 2, 2 to 3, 3 to 4,

    // Índice
    0 to 5, 5 to 6, 6 to 7, 7 to 8,

    // Medio
    0 to 9, 9 to 10, 10 to 11, 11 to 12,

    // Anular
    0 to 13, 13 to 14, 14 to 15, 15 to 16,

    // Meñique
    0 to 17, 17 to 18, 18 to 19, 19 to 20,

    // Palma
    5 to 9,
    9 to 13,
    13 to 17
)

private fun drawHandConnections(
    hand: HandDetectionResult,
    scope: DrawScope
) {

    HAND_CONNECTIONS.forEach { (startIdx, endIdx) ->

        val start = hand.landmarks.getOrNull(startIdx)
            ?: return@forEach

        val end = hand.landmarks.getOrNull(endIdx)
            ?: return@forEach

        scope.drawLine(
            color = Color(0xFF69F0AE).copy(alpha = 0.8f),

            start = Offset(
                start.x * scope.size.width,
                start.y * scope.size.height
            ),

            end = Offset(
                end.x * scope.size.width,
                end.y * scope.size.height
            ),

            strokeWidth = 4f,
            cap = StrokeCap.Round
        )
    }
}

private fun drawHandLandmarks(
    hand: HandDetectionResult,
    scope: DrawScope
) {

    hand.landmarks.forEach { lm ->

        val center = Offset(
            lm.x * scope.size.width,
            lm.y * scope.size.height
        )

        // Anillo exterior
        scope.drawCircle(
            color = Color.White,
            radius = 8f,
            center = center
        )

        // Punto interior
        scope.drawCircle(
            color = Color(0xFF00E676),
            radius = 5f,
            center = center
        )
    }
}