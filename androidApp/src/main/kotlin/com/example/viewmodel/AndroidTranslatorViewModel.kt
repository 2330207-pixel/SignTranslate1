package com.example.signtranslate.viewmodel

import android.content.Context
import androidx.lifecycle.LifecycleOwner
import androidx.camera.view.PreviewView
import com.example.signtranslate.camera.CameraManager
import com.example.signtranslate.mediapipe.HandLandmarkerHelper
import com.example.signtranslate.viewmodel.translator.SignTranslatorViewModel
import kotlinx.coroutines.flow.update

import com.example.signtranslate.data.UserPreferences

class AndroidTranslatorViewModel(
    context: Context,
    prefs: UserPreferences
) : SignTranslatorViewModel(prefs) {

    private val handLandmarkerHelper = HandLandmarkerHelper(
        context = context,
        onResults = { results ->
            if (results.isNotEmpty()) {
                android.util.Log.d("MediaPipe", "Manos detectadas: ${results.size}")
            }
            onHandDetectionResult(results)
        },
        onError = { error ->
            android.util.Log.e("MediaPipe", "Error: $error")
            _internal.update { it.copy(error = error) }
        }
    )

    private val cameraManager = CameraManager(context, prefs, handLandmarkerHelper)
    private var previewViewRef: PreviewView? = null

    fun setupCamera(owner: LifecycleOwner, previewView: PreviewView) {
        previewViewRef = previewView
        cameraManager.initialize(owner, previewView)
        cameraManager.startCamera()
        _internal.update { it.copy(isCameraActive = true) }
    }

    override fun startCamera() {
        cameraManager.startCamera()
        _internal.update { it.copy(isCameraActive = true) }
    }

    override fun stopCamera() {
        cameraManager.stopCamera()
        previewViewRef?.setBackgroundColor(android.graphics.Color.BLACK)
        previewViewRef?.let { pv ->
            pv.bitmap?.eraseColor(android.graphics.Color.BLACK)
        }
        _internal.update { it.copy(isCameraActive = false) }
    }

    override fun flipCamera() {
        cameraManager.flipCamera()
        _internal.update { it.copy(isFrontCamera = !it.isFrontCamera) }
    }
}