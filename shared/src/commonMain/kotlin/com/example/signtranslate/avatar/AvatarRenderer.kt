package com.example.signtranslate.avatar

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
expect fun AvatarRenderer(
    modifier: Modifier = Modifier,
    allowRotation: Boolean = true,
    allowZoom: Boolean = true,
    showBadge: Boolean = false
)
