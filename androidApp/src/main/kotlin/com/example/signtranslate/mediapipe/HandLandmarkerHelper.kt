package com.example.signtranslate.mediapipe

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Matrix
import android.os.SystemClock
import androidx.camera.core.ImageProxy
import com.example.signtranslate.data.translator.HandDetectionResult
import com.example.signtranslate.data.translator.HandLandmark
import com.google.mediapipe.framework.image.BitmapImageBuilder
import com.google.mediapipe.tasks.core.BaseOptions
import com.google.mediapipe.tasks.vision.core.RunningMode
import com.google.mediapipe.tasks.vision.handlandmarker.HandLandmarker
import com.google.mediapipe.tasks.vision.handlandmarker.HandLandmarkerResult

class HandLandmarkerHelper(
    private val context: Context,
    private val onResults: (List<HandDetectionResult>) -> Unit,
    private val onError: (String) -> Unit
) {
    companion object {
        const val MODEL_HAND_LANDMARKER = "hand_landmarker.task"
        const val NUM_HANDS             = 2
        const val MIN_DETECTION_CONF    = 0.5f
        const val MIN_TRACKING_CONF     = 0.5f
        const val MIN_PRESENCE_CONF     = 0.5f
    }

    private var handLandmarker: HandLandmarker? = null

    init {
        setupHandLandmarker()
    }

    private fun setupHandLandmarker() {
        try {
            val baseOptions = BaseOptions.builder()
                .setModelAssetPath(MODEL_HAND_LANDMARKER)
                .build()

            val options = HandLandmarker.HandLandmarkerOptions.builder()
                .setBaseOptions(baseOptions)
                .setMinHandDetectionConfidence(MIN_DETECTION_CONF)
                .setMinTrackingConfidence(MIN_TRACKING_CONF)
                .setMinHandPresenceConfidence(MIN_PRESENCE_CONF)
                .setNumHands(NUM_HANDS)
                .setRunningMode(RunningMode.LIVE_STREAM)
                .setResultListener(::returnLivestreamResult)
                .setErrorListener { error -> onError(error.message ?: "MediaPipe error") }
                .build()

            handLandmarker = HandLandmarker.createFromOptions(context, options)
        } catch (e: Exception) {
            onError("Error inicializando HandLandmarker: ${e.message}")
        }
    }

    fun detectLiveStream(imageProxy: ImageProxy, isFrontCamera: Boolean) {
        imageProxy.use { proxy ->
            if (handLandmarker == null) return
            
            val frameTime = SystemClock.uptimeMillis()

            // Usar el método oficial de CameraX para obtener el Bitmap
            val bitmap = try {
                proxy.toBitmap()
            } catch (e: Exception) {
                return
            }

            val matrix = Matrix().apply {
                postRotate(proxy.imageInfo.rotationDegrees.toFloat())
                if (isFrontCamera) postScale(-1f, 1f, bitmap.width / 2f, bitmap.height / 2f)
            }

            val rotatedBitmap = Bitmap.createBitmap(
                bitmap, 0, 0, bitmap.width, bitmap.height, matrix, true
            )

            val mpImage = BitmapImageBuilder(rotatedBitmap).build()
            handLandmarker?.detectAsync(mpImage, frameTime)
        }
    }

    private fun returnLivestreamResult(result: HandLandmarkerResult, input: com.google.mediapipe.framework.image.MPImage) {
        val detections = result.landmarks().mapIndexed { handIndex, landmarks ->
            val confidence = if (result.handednesses().size > handIndex)
                result.handednesses()[handIndex].firstOrNull()?.score() ?: 0f
            else 0f

            val handedness = if (result.handednesses().size > handIndex)
                result.handednesses()[handIndex].firstOrNull()?.categoryName() ?: "Unknown"
            else "Unknown"

            val mappedLandmarks = landmarks.mapIndexed { i, lm ->
                HandLandmark(
                    id    = i,
                    x     = lm.x(),
                    y     = lm.y(),
                    z     = lm.z(),
                    label = landmarkLabel(i)
                )
            }

            HandDetectionResult(
                landmarks  = mappedLandmarks,
                confidence = confidence,
                handedness = handedness
            )
        }

        onResults(detections)
    }

    private fun landmarkLabel(index: Int): String = when (index) {
        0  -> "WRIST"
        1  -> "THUMB_CMC"; 2  -> "THUMB_MCP"; 3  -> "THUMB_IP"; 4  -> "THUMB_TIP"
        5  -> "INDEX_MCP";  6  -> "INDEX_PIP"; 7  -> "INDEX_DIP"; 8  -> "INDEX_TIP"
        9  -> "MIDDLE_MCP"; 10 -> "MIDDLE_PIP";11 -> "MIDDLE_DIP";12 -> "MIDDLE_TIP"
        13 -> "RING_MCP";   14 -> "RING_PIP";  15 -> "RING_DIP"; 16 -> "RING_TIP"
        17 -> "PINKY_MCP";  18 -> "PINKY_PIP"; 19 -> "PINKY_DIP";20 -> "PINKY_TIP"
        else -> "UNKNOWN"
    }

    fun clearHandLandmarker() {
        handLandmarker?.close()
        handLandmarker = null
    }
}