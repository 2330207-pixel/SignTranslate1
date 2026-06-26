package com.example.signtranslate.data.translator

data class VoiceResult(
    val recognizedText: String = "",
    val isListening: Boolean = false,
    val error: String? = null
)