package com.example.signtranslate.ui.screens.translator

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Send
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.signtranslate.avatar.AvatarRenderer
import com.example.signtranslate.ui.service.AvatarService
import com.example.signtranslate.viewmodel.translator.TranslatorViewModel

// NOTA: se eliminaron los colores fijos Purple / PurpleLight / PurpleLighter /
// Color.White / Color(0xFFB0AEDE) / Color(0xFFDDD9FF) / Color(0xFF9996CC).
// Ahora toda la pestaña lee MaterialTheme.colorScheme, así que al activar
// Modo oscuro en Ajustes esta pestaña también cambia a negro junto con el
// resto de la app (antes se quedaba lavanda fija).

/**
 * TextToLsmTab
 * ─────────────
 * Texto → Señas LSM
 *
 * Layout:
 *   ┌─────────────────────────────────┐
 *   │   AVATAR 3D  (60% de altura)   │
 *   │   con animación de la seña      │
 *   ├─────────────────────────────────┤
 *   │   "Escribe el texto a traducir" │
 *   │   [ Campo de texto       ] [→]  │
 *   │   Contador de caracteres        │
 *   └─────────────────────────────────┘
 */
@Composable
fun TextToLsmTab(viewModel: TranslatorViewModel) {
    var inputText by remember { mutableStateOf("") }
    var lastTranslated by remember { mutableStateOf("") }
    val maxChars = 200

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Spacer(modifier = Modifier.height(12.dp))

        // ── Avatar 3D (60 % de la altura) ────────────────────────────────────
        AvatarRenderer(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight(0.60f),
            allowRotation = false,
            allowZoom = false,
            showBadge = false
        )

        Spacer(modifier = Modifier.height(16.dp))

        // ── Etiqueta de estado ───────────────────────────────────────────────
        if (lastTranslated.isNotBlank()) {
            Surface(
                shape = RoundedCornerShape(12.dp),
                color = MaterialTheme.colorScheme.primaryContainer
            ) {
                Text(
                    text = "Traduciendo: \"$lastTranslated\"",
                    color = MaterialTheme.colorScheme.primary,
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 13.sp,
                    modifier = Modifier.padding(horizontal = 14.dp, vertical = 7.dp)
                )
            }
            Spacer(modifier = Modifier.height(10.dp))
        }

        // ── Instrucción ──────────────────────────────────────────────────────
        Text(
            text = "Escribe el texto que deseas traducir a LSM",
            color = MaterialTheme.colorScheme.primary,
            fontSize = 13.sp,
            fontWeight = FontWeight.Medium,
            modifier = Modifier.align(Alignment.Start)
        )

        Spacer(modifier = Modifier.height(6.dp))

        // ── Campo de texto + botón enviar ────────────────────────────────────
        Surface(
            shape = RoundedCornerShape(16.dp),
            color = MaterialTheme.colorScheme.surfaceVariant,
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(modifier = Modifier.padding(12.dp)) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    OutlinedTextField(
                        value = inputText,
                        onValueChange = { if (it.length <= maxChars) inputText = it },
                        placeholder = {
                            Text(
                                text = "Escribe aquí...",
                                color = MaterialTheme.colorScheme.onSurfaceVariant,
                                fontSize = 14.sp
                            )
                        },
                        modifier = Modifier.weight(1f),
                        shape = RoundedCornerShape(12.dp),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = MaterialTheme.colorScheme.primary,
                            unfocusedBorderColor = MaterialTheme.colorScheme.outlineVariant,
                            focusedTextColor = MaterialTheme.colorScheme.onSurface,
                            unfocusedTextColor = MaterialTheme.colorScheme.onSurface,
                            focusedContainerColor = MaterialTheme.colorScheme.surface,
                            unfocusedContainerColor = MaterialTheme.colorScheme.surface
                        ),
                        maxLines = 3
                    )

                    // Botón enviar
                    FloatingActionButton(
                        onClick = {
                            if (inputText.isNotBlank()) {
                                lastTranslated = inputText.trim()
                                AvatarService.playAnimation(lastTranslated)
                                inputText = ""
                            }
                        },
                        containerColor = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.size(52.dp)
                    ) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.Send,
                            contentDescription = "Traducir",
                            tint = MaterialTheme.colorScheme.onPrimary
                        )
                    }
                }

                // Contador de caracteres
                Text(
                    text = "${inputText.length}/$maxChars",
                    fontSize = 11.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier
                        .align(Alignment.End)
                        .padding(top = 4.dp)
                )
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        // ── Botón limpiar ────────────────────────────────────────────────────
        if (lastTranslated.isNotBlank()) {
            TextButton(
                onClick = {
                    lastTranslated = ""
                    AvatarService.resetToIdle()
                }
            ) {
                Text(
                    text = "Limpiar",
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    fontSize = 13.sp
                )
            }
        }
    }
}