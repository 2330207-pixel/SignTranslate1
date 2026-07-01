package com.example.signtranslate.camera

import android.content.Context
import android.util.Size
import androidx.camera.core.*
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
import androidx.core.content.ContextCompat
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.lifecycleScope
import com.example.signtranslate.mediapipe.HandLandmarkerHelper
import com.example.signtranslate.data.UserPreferences
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

class CameraManager(
    private val context: Context,
    private val prefs: UserPreferences,
    private val handLandmarkerHelper: HandLandmarkerHelper
) {
    private var lifecycleOwner: LifecycleOwner? = null
    private var previewView: PreviewView? = null
    private var isPendingStart = false


    private val _isCameraActive = MutableStateFlow(false)
    val isCameraActive: StateFlow<Boolean> = _isCameraActive

    private val _isFrontCamera = MutableStateFlow(false)
    val isFrontCamera: StateFlow<Boolean> = _isFrontCamera


    private var cameraProvider: ProcessCameraProvider? = null
    private val cameraExecutor: ExecutorService =
        Executors.newSingleThreadExecutor()

    private var currentResolution = "720p"


    fun initialize(lifecycleOwner: LifecycleOwner, previewView: PreviewView) {
        this.lifecycleOwner = lifecycleOwner
        this.previewView = previewView
        if (isPendingStart) {
            isPendingStart = false
            startCamera()
        }
    }

    fun startCamera() {
        val owner = lifecycleOwner
        if (owner == null) {
            isPendingStart = true
            return
        }
        
        owner.lifecycleScope.launch {
            prefs.cameraResolution.collectLatest { resolution ->
                currentResolution = resolution
                bindCamera()
            }
        }
    }

    private fun bindCamera() {
        val owner = lifecycleOwner ?: return
        val pv = previewView ?: return

        val future = ProcessCameraProvider.getInstance(context)

        future.addListener({
            try {
                val provider = future.get()
                cameraProvider = provider
                provider.unbindAll()

                val cameraSelector =
                    if (_isFrontCamera.value)
                        CameraSelector.DEFAULT_FRONT_CAMERA
                    else
                        CameraSelector.DEFAULT_BACK_CAMERA

                val targetSize =
                    resolutionToSize(currentResolution)

                val preview = Preview.Builder()
                    .setTargetResolution(targetSize)
                    .build()
                    .also {
                        it.setSurfaceProvider(pv.surfaceProvider)
                    }

                val imageAnalysis = ImageAnalysis.Builder()
                    .setTargetResolution(targetSize)
                    .setBackpressureStrategy(
                        ImageAnalysis.STRATEGY_KEEP_ONLY_LATEST
                    )
                    .setOutputImageFormat(
                        ImageAnalysis.OUTPUT_IMAGE_FORMAT_RGBA_8888
                    )
                    .build()
                    .also { analysis ->
                        analysis.setAnalyzer(cameraExecutor) { imageProxy ->
                            handLandmarkerHelper.detectLiveStream(
                                imageProxy = imageProxy,
                                isFrontCamera = _isFrontCamera.value
                            )
                        }
                    }

                provider.bindToLifecycle(
                    owner,
                    cameraSelector,
                    preview,
                    imageAnalysis
                )

                _isCameraActive.value = true

            } catch (e: Exception) {
                e.printStackTrace()
            }

        }, ContextCompat.getMainExecutor(context))
    }


    fun flipCamera() {
        _isFrontCamera.value = !_isFrontCamera.value
        bindCamera()
    }


    fun stopCamera() {
        isPendingStart = false
        cameraProvider?.unbindAll()
        _isCameraActive.value = false
    }

    fun shutdown() {
        stopCamera()
        cameraExecutor.shutdown()
    }


    private fun resolutionToSize(
        resolution: String
    ): Size {
        return when (resolution) {
            "1080p" -> Size(1920, 1080)
            "1440p" -> Size(2560, 1440)
            else -> Size(1280, 720)
        }
    }
}
