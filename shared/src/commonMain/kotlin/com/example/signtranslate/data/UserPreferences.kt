package com.example.signtranslate.data

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.*
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.map
import kotlinx.serialization.json.Json

class UserPreferences(
    private val dataStore: DataStore<Preferences>
) {

    companion object {

        // ── Auth ───────────────────────────────────────────────
        private val AUTH_TOKEN = stringPreferencesKey("auth_token")
        private val CURRENT_USER = stringPreferencesKey("current_user")

        // ── Apariencia ────────────────────────────────────────
        private val DARK_MODE = booleanPreferencesKey("dark_mode")
        private val LARGE_TEXT = booleanPreferencesKey("large_text")
        private val HIGH_CONTRAST = booleanPreferencesKey("high_contrast")

        // ── Traducción ────────────────────────────────────────
        private val AUTO_RECOGNITION = booleanPreferencesKey("auto_recognition")
        private val REALTIME_TRANSLATION = booleanPreferencesKey("realtime_translation")
        private val SHOW_CONFIDENCE = booleanPreferencesKey("show_confidence")
        private val SIGN_LANGUAGE = stringPreferencesKey("sign_language")

        // ── Cámara ────────────────────────────────────────────
        private val SHOW_HAND_POINTS = booleanPreferencesKey("show_hand_points")
        private val TWO_HANDS_DETECTION = booleanPreferencesKey("two_hands_detection")
        private val CAMERA_RESOLUTION = stringPreferencesKey("camera_resolution")

        // ── Avatar ────────────────────────────────────────────
        private val SHOW_AVATAR = booleanPreferencesKey("show_avatar")

        // ── Voz ───────────────────────────────────────────────
        private val VOICE_VOLUME = floatPreferencesKey("voice_volume")
        private val VOICE_SPEED = floatPreferencesKey("voice_speed")
    }

    // ==========================================================
    // AUTH
    // ==========================================================

    val authToken: Flow<String?> =
        dataStore.data.map { it[AUTH_TOKEN] }

    val currentUser: Flow<User?> =
        dataStore.data.map { prefs ->
            prefs[CURRENT_USER]?.let {
                runCatching {
                    Json.decodeFromString<User>(it)
                }.getOrNull()
            }
        }

    suspend fun saveToken(token: String) {
        dataStore.edit {
            it[AUTH_TOKEN] = token
        }
    }

    suspend fun getToken(): String? {
        return dataStore.data
            .map { it[AUTH_TOKEN] }
            .firstOrNull()
    }

    suspend fun saveUser(user: User) {
        dataStore.edit {
            it[CURRENT_USER] = Json.encodeToString(User.serializer(), user)
        }
    }

    suspend fun getUser(): User? {
        val json = dataStore.data
            .map { it[CURRENT_USER] }
            .firstOrNull()
            ?: return null

        return try {
            Json.decodeFromString<User>(json)
        } catch (e: Exception) {
            null
        }
    }

    suspend fun clearSession() {
        dataStore.edit { prefs ->
            prefs.remove(AUTH_TOKEN)
            prefs.remove(CURRENT_USER)
        }
    }

    // ==========================================================
    // APARIENCIA
    // ==========================================================

    val darkMode: Flow<Boolean> =
        dataStore.data.map { it[DARK_MODE] ?: false }

    val largeText: Flow<Boolean> =
        dataStore.data.map { it[LARGE_TEXT] ?: false }

    val highContrast: Flow<Boolean> =
        dataStore.data.map { it[HIGH_CONTRAST] ?: false }

    suspend fun setDarkMode(enabled: Boolean) {
        dataStore.edit { it[DARK_MODE] = enabled }
    }

    suspend fun setLargeText(enabled: Boolean) {
        dataStore.edit { it[LARGE_TEXT] = enabled }
    }

    suspend fun setHighContrast(enabled: Boolean) {
        dataStore.edit { it[HIGH_CONTRAST] = enabled }
    }

    // ==========================================================
    // TRADUCCIÓN
    // ==========================================================

    val autoRecognition: Flow<Boolean> =
        dataStore.data.map { it[AUTO_RECOGNITION] ?: true }

    val realtimeTranslation: Flow<Boolean> =
        dataStore.data.map { it[REALTIME_TRANSLATION] ?: true }

    val showConfidence: Flow<Boolean> =
        dataStore.data.map { it[SHOW_CONFIDENCE] ?: true }

    val signLanguage: Flow<String> =
        dataStore.data.map { it[SIGN_LANGUAGE] ?: "LSM" }

    suspend fun setAutoRecognition(enabled: Boolean) {
        dataStore.edit { it[AUTO_RECOGNITION] = enabled }
    }

    suspend fun setRealtimeTranslation(enabled: Boolean) {
        dataStore.edit { it[REALTIME_TRANSLATION] = enabled }
    }

    suspend fun setShowConfidence(enabled: Boolean) {
        dataStore.edit { it[SHOW_CONFIDENCE] = enabled }
    }

    suspend fun setSignLanguage(language: String) {
        dataStore.edit { it[SIGN_LANGUAGE] = language }
    }

    // ==========================================================
    // CÁMARA
    // ==========================================================

    val showHandPoints: Flow<Boolean> =
        dataStore.data.map { it[SHOW_HAND_POINTS] ?: true }

    val twoHandsDetection: Flow<Boolean> =
        dataStore.data.map { it[TWO_HANDS_DETECTION] ?: false }

    val cameraResolution: Flow<String> =
        dataStore.data.map { it[CAMERA_RESOLUTION] ?: "720p" }

    suspend fun setShowHandPoints(enabled: Boolean) {
        dataStore.edit { it[SHOW_HAND_POINTS] = enabled }
    }

    suspend fun setTwoHandsDetection(enabled: Boolean) {
        dataStore.edit { it[TWO_HANDS_DETECTION] = enabled }
    }

    suspend fun setCameraResolution(resolution: String) {
        dataStore.edit { it[CAMERA_RESOLUTION] = resolution }
    }

    // ==========================================================
    // AVATAR
    // ==========================================================

    val showAvatar: Flow<Boolean> =
        dataStore.data.map { it[SHOW_AVATAR] ?: true }

    suspend fun setShowAvatar(enabled: Boolean) {
        dataStore.edit { it[SHOW_AVATAR] = enabled }
    }

    // ==========================================================
    // VOZ
    // ==========================================================

    val voiceVolume: Flow<Float> =
        dataStore.data.map { it[VOICE_VOLUME] ?: 1.0f }

    val voiceSpeed: Flow<Float> =
        dataStore.data.map { it[VOICE_SPEED] ?: 1.0f }

    suspend fun setVoiceVolume(volume: Float) {
        dataStore.edit { it[VOICE_VOLUME] = volume }
    }

    suspend fun setVoiceSpeed(speed: Float) {
        dataStore.edit { it[VOICE_SPEED] = speed }
    }
}