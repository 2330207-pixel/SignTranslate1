package com.example.signtranslate.ui.service

import com.example.signtranslate.avatar.AnimationManager
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

/**
 * AvatarService — carga avatar.glb una sola vez y lo reutiliza en toda la app.
 */
object AvatarService {

    const val AVATAR_ASSET = "avatars/avatar.glb"

    // ─── Estado ─────────────────────────────────────────────────────────────────
    enum class AvatarState { IDLE, LOADING, READY, ERROR }

    private val serviceScope = CoroutineScope(SupervisorJob() + Dispatchers.Main)

    private val _state = MutableStateFlow(AvatarState.IDLE)
    val state: StateFlow<AvatarState> = _state.asStateFlow()

    private val _currentAnimation = MutableStateFlow<String?>(null)
    val currentAnimation: StateFlow<String?> = _currentAnimation.asStateFlow()

    private val _isListening = MutableStateFlow(false)
    val isListening: StateFlow<Boolean> = _isListening.asStateFlow()

    // AnimationManager maneja la cola de animaciones LSM
    val animationManager = AnimationManager()

    // ─── API pública ────────────────────────────────────────────────────────────

    /**
     * Reproduce la seña LSM correspondiente al texto.
     * Si el texto tiene varias palabras, las encola en secuencia.
     */
    fun playAnimation(text: String) {
        if (text.isBlank()) return
        serviceScope.launch {
            animationManager.enqueueText(text) { animationName ->
                _currentAnimation.value = animationName
            }
        }
    }

    /** Activa/desactiva el indicador de escucha (micrófono). */
    fun setListening(listening: Boolean) {
        _isListening.value = listening
    }

    /** Devuelve el avatar al estado idle. */
    fun resetToIdle() {
        _currentAnimation.value = null
        animationManager.clearQueue()
    }

    /** Marca el servicio como listo (llamado desde SceneManager cuando el modelo cargó). */
    fun markReady() { _state.value = AvatarState.READY }
    fun markLoading() { _state.value = AvatarState.LOADING }
    fun markError() { _state.value = AvatarState.ERROR }
}
