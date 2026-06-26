package com.example.signtranslate.speech

import android.content.Context
import android.os.Bundle
import android.speech.tts.TextToSpeech
import android.speech.tts.UtteranceProgressListener
import com.example.signtranslate.data.UserPreferences
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import java.util.Locale

class TextToSpeechManager(
    context: Context,
    private val prefs: UserPreferences,
    private val scope: CoroutineScope
) {

    // ─────────────────────────────────────────────────────────
    // Estado observable
    // ─────────────────────────────────────────────────────────

    private val _isSpeaking = MutableStateFlow(false)
    val isSpeaking: StateFlow<Boolean> = _isSpeaking

    // ─────────────────────────────────────────────────────────

    private var tts: TextToSpeech? = null

    private var isReady = false
    private var currentVolume = 1.0f

    init {

        tts = TextToSpeech(context) { status ->

            if (status == TextToSpeech.SUCCESS) {

                tts?.language = Locale("es", "MX")
                tts?.setPitch(1.0f)

                isReady = true

                setupUtteranceListener()

                observePreferences()
            }
        }
    }

    // ─────────────────────────────────────────────────────────
    // Observa preferencias
    // ─────────────────────────────────────────────────────────

    private fun observePreferences() {

        scope.launch {

            prefs.voiceSpeed.collectLatest { speed ->

                tts?.setSpeechRate(speed)
            }
        }

        scope.launch {

            prefs.voiceVolume.collectLatest { volume ->

                currentVolume = volume
            }
        }
    }

    // ─────────────────────────────────────────────────────────
    // Listener de reproducción
    // ─────────────────────────────────────────────────────────

    private fun setupUtteranceListener() {

        tts?.setOnUtteranceProgressListener(
            object : UtteranceProgressListener() {

                override fun onStart(utteranceId: String?) {
                    _isSpeaking.value = true
                }

                override fun onDone(utteranceId: String?) {
                    _isSpeaking.value = false
                }

                override fun onError(utteranceId: String?) {
                    _isSpeaking.value = false
                }
            }
        )
    }

    // ─────────────────────────────────────────────────────────
    // Hablar
    // ─────────────────────────────────────────────────────────

    fun speak(text: String) {

        if (!isReady) return
        if (text.isBlank()) return

        val params = Bundle().apply {

            putFloat(
                TextToSpeech.Engine.KEY_PARAM_VOLUME,
                currentVolume
            )
        }

        tts?.speak(
            text,
            TextToSpeech.QUEUE_FLUSH,
            params,
            "ST_${System.currentTimeMillis()}"
        )
    }

    // ─────────────────────────────────────────────────────────

    fun stop() {

        tts?.stop()

        _isSpeaking.value = false
    }

    // ─────────────────────────────────────────────────────────

    fun shutdown() {

        stop()

        tts?.shutdown()

        tts = null

        isReady = false
    }
}