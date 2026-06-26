package com.example.signtranslate.viewmodel.translator

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.signtranslate.classifier.ResultStabilizer
import com.example.signtranslate.classifier.RuleBasedClassifier
import com.example.signtranslate.classifier.SignClassifier
import com.example.signtranslate.data.UserPreferences
import com.example.signtranslate.data.translator.HandDetectionResult
import com.example.signtranslate.data.translator.TranslationResult
import com.example.signtranslate.data.translator.SignLabel
import com.example.signtranslate.data.translator.VoiceResult
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

enum class TranslatorTab {
    SIGN_TO_TEXT,
    VOICE_TO_LSM,
    TEXT_TO_LSM
}

data class TranslatorUiState(
    val activeTab: TranslatorTab = TranslatorTab.SIGN_TO_TEXT,
    val isCameraActive: Boolean = false,
    val isFrontCamera: Boolean = true,
    val translationResult: TranslationResult? = null,
    val error: String? = null,
    val isRecognizing: Boolean = false,
    val avatarSignWord: String = "",
    val handDetections: List<HandDetectionResult> = emptyList(),
    val voiceResult: VoiceResult = VoiceResult()
)

open class TranslatorViewModel(
    protected val userPreferences: UserPreferences
) : ViewModel() {

    protected val _internal = MutableStateFlow(TranslatorUiState())
    val uiState: StateFlow<TranslatorUiState> = _internal.asStateFlow()

    fun selectTab(tab: TranslatorTab) {
        _internal.update { it.copy(activeTab = tab) }
    }

    open fun startCamera() {
        _internal.update { it.copy(isCameraActive = true) }
    }

    open fun stopCamera() {
        _internal.update { it.copy(isCameraActive = false) }
    }

    open fun flipCamera() {
        _internal.update { it.copy(isFrontCamera = !it.isFrontCamera) }
    }

    fun speakText(text: String) {
        // Implementación de TTS
    }

    fun startListening() {
        _internal.update { it.copy(voiceResult = it.voiceResult.copy(isListening = true)) }
    }

    fun stopListening() {
        _internal.update { it.copy(voiceResult = it.voiceResult.copy(isListening = false)) }
    }
}

open class SignTranslatorViewModel(
    userPreferences: UserPreferences
) : TranslatorViewModel(userPreferences) {

    private val classifier: SignClassifier = RuleBasedClassifier()
    
    private val stabilizer = ResultStabilizer(
        windowSize = 10,
        minConsecutive = 7,
        minConfidence = 0.65f
    )

    fun onHandDetectionResult(detectionResults: List<HandDetectionResult>) {
        viewModelScope.launch {
            _internal.update { it.copy(handDetections = detectionResults) }

            val landmarks = detectionResults.firstOrNull()?.landmarks

            if (landmarks.isNullOrEmpty()) {
                onNoHandDetected()
                return@launch
            }

            _internal.update { it.copy(isRecognizing = true) }

            // El clasificador ahora usa directamente HandLandmark (clase común)
            val raw    = classifier.classify(landmarks)
            val stable = stabilizer.feed(raw)

            _internal.update {
                it.copy(
                    translationResult = if (stable.label == SignLabel.UNKNOWN) null else stable,
                    avatarSignWord    = stable.displayText,
                    isRecognizing     = false
                )
            }
        }
    }

    fun onNoHandDetected() {
        stabilizer.feed(TranslationResult.EMPTY)
        _internal.update {
            it.copy(translationResult = null, isRecognizing = false)
        }
    }

    fun resetTranslation() {
        stabilizer.reset()
        _internal.update { it.copy(translationResult = null) }
    }

    override fun onCleared() {
        super.onCleared()
        classifier.close()
    }
}
