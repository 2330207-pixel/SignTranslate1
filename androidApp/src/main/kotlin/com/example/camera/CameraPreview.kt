package com.example.signtranslate.camera

import android.view.ViewGroup
import androidx.camera.view.PreviewView
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.viewinterop.AndroidView
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.compose.LocalLifecycleOwner

/**
 * AndroidView wrapper for CameraX PreviewView.
 * Inject this into SignToTextTab via the cameraPreviewContent lambda.
 */
@Composable
fun CameraPreview(
    cameraManager: CameraManager,
    modifier: Modifier = Modifier
) {
    val lifecycleOwner: LifecycleOwner = LocalLifecycleOwner.current

    AndroidView(
        factory = { ctx ->
            PreviewView(ctx).apply {
                layoutParams = ViewGroup.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.MATCH_PARENT
                )
                implementationMode = PreviewView.ImplementationMode.COMPATIBLE
                cameraManager.initialize(lifecycleOwner, this)
            }
        },
        modifier = modifier,
        update = { /* no-op: lifecycle handled by CameraManager */ }
    )
}