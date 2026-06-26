package com.example.signtranslate.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.signtranslate.data.AuthResult
import com.example.signtranslate.data.AuthService
import com.example.signtranslate.data.User
import com.example.signtranslate.data.UserPreferences
import com.example.signtranslate.data.createDataStore
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

// ─────────────────────────────────────────────────────────────────────────────
// ESTADO UI
// ─────────────────────────────────────────────────────────────────────────────

data class AuthUiState(
    val isLoading:      Boolean = false,
    val isAuthenticated: Boolean = false,
    val currentUser:    User?   = null,
    val errorMessage:   String  = "",
    val successMessage: String  = "",
)

// ─────────────────────────────────────────────────────────────────────────────
// VIEW MODEL
// ─────────────────────────────────────────────────────────────────────────────

class AuthViewModel(
    private val authService:     AuthService     = AuthService(),
    private val userPreferences: UserPreferences = UserPreferences(createDataStore())
) : ViewModel() {

    private val _uiState = MutableStateFlow(AuthUiState())
    val uiState: StateFlow<AuthUiState> = _uiState.asStateFlow()

    // ── Al iniciar, verifica si hay sesión guardada ────────────────────────────
    init {
        viewModelScope.launch {
            val savedToken = userPreferences.getToken()
            val savedUser  = userPreferences.getUser()

            if ((savedToken != null) && (savedUser != null)) {
                // Valida el token con el servidor
                when (val result = authService.getProfile(savedToken)) {
                    is AuthResult.Success -> {
                        _uiState.update {
                            it.copy(isAuthenticated = true, currentUser = result.data)
                        }
                    }
                    is AuthResult.Error -> {
                        // Token expirado o inválido — limpiar almacenamiento local
                        userPreferences.clearSession()
                    }
                }
            }
        }
    }

    // ── Registro ──────────────────────────────────────────────────────────────
    fun register(name: String, email: String, password: String) {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, errorMessage = "") }

            when (val result = authService.register(name, email, password)) {
                is AuthResult.Success -> {
                    // Guardar token y datos del usuario en DataStore (persistencia local)
                    userPreferences.saveToken(result.data.token)
                    userPreferences.saveUser(result.data.user)

                    _uiState.update {
                        it.copy(
                            isLoading       = false,
                            isAuthenticated = true,
                            currentUser     = result.data.user,
                            successMessage  = "¡Bienvenido, ${result.data.user.name}!"
                        )
                    }
                }
                is AuthResult.Error -> {
                    _uiState.update {
                        it.copy(isLoading = false, errorMessage = result.message)
                    }
                }
            }
        }
    }

    // ── Login ─────────────────────────────────────────────────────────────────
    fun login(email: String, password: String) {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, errorMessage = "") }

            when (val result = authService.login(email, password)) {
                is AuthResult.Success -> {
                    // Guardar sesión localmente
                    userPreferences.saveToken(result.data.token)
                    userPreferences.saveUser(result.data.user)

                    _uiState.update {
                        it.copy(
                            isLoading       = false,
                            isAuthenticated = true,
                            currentUser     = result.data.user,
                            successMessage  = "¡Hola de nuevo, ${result.data.user.name}!"
                        )
                    }
                }
                is AuthResult.Error -> {
                    _uiState.update {
                        it.copy(isLoading = false, errorMessage = result.message)
                    }
                }
            }
        }
    }

    // ── Cerrar sesión ─────────────────────────────────────────────────────────
    fun logout() {
        viewModelScope.launch {
            userPreferences.clearSession()
            _uiState.update {
                AuthUiState() // resetear al estado inicial
            }
        }
    }

    // ── Limpiar error (para que no persista tras navegar) ─────────────────────
    fun clearError() {
        _uiState.update { it.copy(errorMessage = "") }
    }
}