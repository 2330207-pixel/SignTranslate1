package com.example.signtranslate.avatar

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import com.example.signtranslate.ui.service.AvatarService
import io.github.sceneview.SceneView

private val Purple = Color(0xFF6C63FF)

@Composable
actual fun AvatarRenderer(
    modifier: Modifier,
    allowRotation: Boolean,
    allowZoom: Boolean,
    showBadge: Boolean
) {
    val context = LocalContext.current
    val avatarState by AvatarService.state.collectAsState()
    var sceneManager by remember { mutableStateOf<SceneManager?>(null) }

    val readyAlpha by animateFloatAsState(
        targetValue = if (avatarState == AvatarService.AvatarState.READY) 1f else 0f,
        animationSpec = tween(600),
        label = "avatarFade"
    )

    val backgroundBrush = Brush.verticalGradient(
        colors = listOf(Color(0xFFEDE9FF), Color(0xFFD6D0FF), Color(0xFFC2BFFF))
    )

    Box(
        modifier = modifier
            .clip(RoundedCornerShape(28.dp))
            .background(brush = backgroundBrush),
        contentAlignment = Alignment.Center
    ) {
        AndroidView(
            factory = { ctx ->
                SceneView(ctx).also { sv ->
                    val mgr = SceneManager(ctx, sv)
                    sceneManager = mgr
                    mgr.setup()
                }
            },
            modifier = Modifier
                .fillMaxSize()
                .alpha(readyAlpha),
            onRelease = { sceneManager?.destroy() }
        )

        if (avatarState == AvatarService.AvatarState.LOADING) {
            CircularProgressIndicator(
                color = Purple,
                modifier = Modifier.size(40.dp)
            )
        }

        if (avatarState == AvatarService.AvatarState.ERROR) {
            Text(
                text = "Error al cargar avatar",
                color = Color.White,
                fontSize = 14.sp
            )
        }

        if (showBadge && avatarState == AvatarService.AvatarState.READY) {
            Surface(
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .padding(bottom = 12.dp),
                shape = RoundedCornerShape(20.dp),
                color = Color.White.copy(alpha = 0.88f),
                tonalElevation = 2.dp
            ) {
                Text(
                    text = "✦ Avatar listo para traducir LSM",
                    color = Purple,
                    fontSize = 11.sp,
                    modifier = Modifier.padding(horizontal = 14.dp, vertical = 5.dp)
                )
            }
        }
    }

    DisposableEffect(Unit) {
        onDispose { sceneManager?.destroy() }
    }
}
