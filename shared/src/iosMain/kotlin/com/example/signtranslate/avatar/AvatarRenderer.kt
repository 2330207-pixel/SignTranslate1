package com.example.signtranslate.avatar

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color

@Composable
actual fun AvatarRenderer(
    modifier: Modifier,
    allowRotation: Boolean,
    allowZoom: Boolean,
    showBadge: Boolean
) {
    Box(
        modifier = modifier.background(Color.LightGray),
        contentAlignment = Alignment.Center
    ) {
        Text("Avatar 3D (Solo en Android)")
    }
}
