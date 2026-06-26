package com.example.signtranslate.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.signtranslate.data.UserPreferences
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

// ─────────────────────────────────────────────────────────────────────────────
// Estado de UI
// ─────────────────────────────────────────────────────────────────────────────
//
// NOTA: se eliminaron del estado los campos de Traducción y Avatar
// (autoRecognition, realtimeTranslation, showConfidence, signLanguage,
// showAvatar) y de Cámara solo queda showHandPoints (se eliminó
// twoHandsDetection y cameraResolution) según el rediseño solicitado.
// Las claves correspondientes en UserPreferences pueden conservarse sin uso
// o eliminarse; aquí simplemente no se exponen en la UI.

data class SettingsUiState(
    // Apariencia
    val darkMode: Boolean      = false,
    val largeText: Boolean     = false,
    val highContrast: Boolean  = false,

    // Cámara
    val showHandPoints: Boolean = true,
)

// ─────────────────────────────────────────────────────────────────────────────
// ViewModel
// ─────────────────────────────────────────────────────────────────────────────

class SettingsViewModel(
    private val prefs: UserPreferences,
) : ViewModel() {

    val uiState: StateFlow<SettingsUiState> = combine(
        prefs.darkMode,
        prefs.largeText,
        prefs.highContrast,
        prefs.showHandPoints,
    ) { darkMode, largeText, highContrast, showHandPoints ->
        SettingsUiState(
            darkMode       = darkMode,
            largeText      = largeText,
            highContrast   = highContrast,
            showHandPoints = showHandPoints,
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5_000),
        initialValue = SettingsUiState(),
    )

    // ── Apariencia ─────────────────────────────────────────────────────────

    fun setDarkMode(enabled: Boolean) = viewModelScope.launch {
        prefs.setDarkMode(enabled)
    }

    fun setLargeText(enabled: Boolean) = viewModelScope.launch {
        prefs.setLargeText(enabled)
    }

    fun setHighContrast(enabled: Boolean) = viewModelScope.launch {
        prefs.setHighContrast(enabled)
    }

    // ── Cámara ─────────────────────────────────────────────────────────────

    fun setShowHandPoints(enabled: Boolean) = viewModelScope.launch {
        prefs.setShowHandPoints(enabled)
    }
}