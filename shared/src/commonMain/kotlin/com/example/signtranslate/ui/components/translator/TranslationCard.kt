package com.example.signtranslate.ui.components.translator

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.VolumeUp
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.signtranslate.data.translator.ClassifierSource
import com.example.signtranslate.data.translator.SignCategory
import com.example.signtranslate.data.translator.SignLabel
import com.example.signtranslate.data.translator.TranslationResult

/**
 * Tarjeta de traducción que muestra:
 *  - La seña detectada (o "Esperando señas...")
 *  - Nivel de confianza como barra de progreso + porcentaje
 *  - Indicador de fuente (Reglas | IA) para diferenciar Fase 1 / Fase 2
 */
@Composable
fun TranslationCard(
    result: TranslationResult,
    isHandDetected: Boolean,
    onSpeakClick: (() -> Unit)? = null,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp),
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(
            modifier = Modifier
                .padding(20.dp)
                .fillMaxWidth()
        ) {
            // ── Fila superior: icono de estado + fuente ───────────────────
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                HandStatusIndicator(isHandDetected)
                ClassifierSourceBadge(result.source)
            }

            Spacer(modifier = Modifier.height(12.dp))

            // ── Texto principal (seña detectada) ─────────────────────────
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    AnimatedContent(
                        targetState = result.displayText,
                        transitionSpec = { fadeIn() togetherWith fadeOut() },
                        label = "sign_text"
                    ) { text ->
                        Text(
                            text = text,
                            style = MaterialTheme.typography.headlineLarge.copy(
                                fontWeight = FontWeight.Bold,
                                fontSize = 36.sp
                            ),
                            color = if (result.label == SignLabel.UNKNOWN)
                                MaterialTheme.colorScheme.onSurface.copy(alpha = 0.4f)
                            else
                                MaterialTheme.colorScheme.primary
                        )
                    }

                    // ── Categoría (Palabra / Letra) ───────────────────────────────
                    if (result.label != SignLabel.UNKNOWN) {
                        Text(
                            text = when (result.label.category) {
                                SignCategory.WORD   -> "Palabra"
                                SignCategory.LETTER -> "Letra"
                                SignCategory.NONE   -> ""
                            },
                            style = MaterialTheme.typography.labelMedium,
                            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                        )
                    }
                }

                if (onSpeakClick != null && result.label != SignLabel.UNKNOWN) {
                    IconButton(onClick = onSpeakClick) {
                        Icon(
                            imageVector = Icons.Default.VolumeUp,
                            contentDescription = "Escuchar",
                            tint = MaterialTheme.colorScheme.primary
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // ── Barra de confianza ────────────────────────────────────────
            if (result.label != SignLabel.UNKNOWN) {
                ConfidenceBar(confidence = result.confidence)
            }
        }
    }
}

@Composable
private fun ConfidenceBar(confidence: Float) {
    val color = when {
        confidence >= 0.80f -> Color(0xFF4CAF50)   // Verde
        confidence >= 0.65f -> Color(0xFFFF9800)   // Naranja
        else                -> Color(0xFFF44336)   // Rojo
    }

    Column {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = "Confianza",
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
            )
            Text(
                text = "${(confidence * 100).toInt()}%",
                style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
                color = color
            )
        }
        Spacer(modifier = Modifier.height(4.dp))
        LinearProgressIndicator(
            progress = { confidence },
            modifier = Modifier
                .fillMaxWidth()
                .height(6.dp),
            color = color,
            trackColor = color.copy(alpha = 0.2f)
        )
    }
}

@Composable
private fun HandStatusIndicator(isHandDetected: Boolean) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Surface(
            shape = RoundedCornerShape(50),
            color = if (isHandDetected) Color(0xFF4CAF50) else Color(0xFFBDBDBD),
            modifier = Modifier.size(8.dp)
        ) {}
        Spacer(modifier = Modifier.width(6.dp))
        Text(
            text = if (isHandDetected) "Mano detectada" else "Sin mano",
            style = MaterialTheme.typography.labelSmall,
            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
        )
    }
}

@Composable
private fun ClassifierSourceBadge(source: ClassifierSource) {
    val (label, color) = when (source) {
        ClassifierSource.RULE_BASED -> Pair("Reglas", Color(0xFF2196F3))
        ClassifierSource.ML_MODEL   -> Pair("IA", Color(0xFF9C27B0))
    }
    Surface(
        shape = RoundedCornerShape(50),
        color = color.copy(alpha = 0.15f)
    ) {
        Text(
            text = label,
            modifier = Modifier.padding(horizontal = 8.dp, vertical = 2.dp),
            style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.SemiBold),
            color = color
        )
    }
}