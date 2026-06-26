package com.example.signtranslate.ui.components.translator

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.FlipCameraAndroid
import androidx.compose.material.icons.filled.Videocam
import androidx.compose.material.icons.filled.VideocamOff
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Composable
fun CameraControls(
    isCameraActive: Boolean,
    isFrontCamera: Boolean,
    onToggleCamera: () -> Unit,
    onFlipCamera: () -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        FloatingActionButton(
            onClick = onToggleCamera,
            containerColor = Color.White.copy(alpha = 0.85f),
            contentColor = if (isCameraActive) Color(0xFF333333) else Color(0xFF333333),
            modifier = Modifier.size(48.dp)
        ) {
            Icon(
                imageVector = if (isCameraActive) Icons.Default.VideocamOff else Icons.Default.Videocam,
                contentDescription = "Toggle Camera"
            )
        }

        if (isCameraActive) {
            FloatingActionButton(
                onClick = onFlipCamera,
                containerColor = Color.White.copy(alpha = 0.85f),
                contentColor = Color(0xFF333333),
                modifier = Modifier.size(48.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.FlipCameraAndroid,
                    contentDescription = "Flip Camera"
                )
            }
        }
    }
}