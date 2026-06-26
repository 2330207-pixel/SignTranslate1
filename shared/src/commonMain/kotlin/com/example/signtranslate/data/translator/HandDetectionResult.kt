package com.example.signtranslate.data.translator

data class HandLandmark(
    val id: Int,
    val x: Float,
    val y: Float,
    val z: Float,
    val label: String
)

data class HandDetectionResult(
    val landmarks: List<HandLandmark>,
    val confidence: Float,
    val handedness: String
)