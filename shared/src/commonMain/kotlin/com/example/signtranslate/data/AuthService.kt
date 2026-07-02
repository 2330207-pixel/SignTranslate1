package com.example.signtranslate.data

import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*
import io.ktor.serialization.kotlinx.json.*
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json


@Serializable
data class User(
    val id:        String,
    val name:      String,
    val email:     String,
    val createdAt: String
)

@Serializable
data class AuthResponse(
    val message: String,
    val token:   String,
    val user:    User
)

@Serializable
data class ErrorResponse(
    val error: String
)

@Serializable
data class RegisterRequest(
    val name:     String,
    val email:    String,
    val password: String
)

@Serializable
data class LoginRequest(
    val email:    String,
    val password: String
)

// ─────────────────────────────────────────────────────────────────────────────
// RESULTADO SELLADO — evita lanzar excepciones en el ViewModel
// ─────────────────────────────────────────────────────────────────────────────

sealed class AuthResult<out T> {
    data class Success<T>(val data: T)  : AuthResult<T>()
    data class Error(val message: String) : AuthResult<Nothing>()
}

// ─────────────────────────────────────────────────────────────────────────────
// AUTH SERVICE — consume el endpoint real del backend
// ─────────────────────────────────────────────────────────────────────────────

class AuthService {

    companion object {
        // Cambia esta URL si tu servidor corre en otro puerto o host
        private const val BASE_URL = "http://10.179.77.39:8080/api/auth"
        //  └─ 10.179.77.39 = IP actual de tu red (Hotspot/Nueva Red)
        //     10.0.2.2 = localhost desde el emulador Android
    }

    private val client = HttpClient {
        install(ContentNegotiation) {
            json(Json {
                ignoreUnknownKeys = true
                isLenient = true
            })
        }
    }

    // ── Registro ──────────────────────────────────────────────────────────────
    suspend fun register(
        name:     String,
        email:    String,
        password: String
    ): AuthResult<AuthResponse> {
        return try {
            val response = client.post("$BASE_URL/register") {
                contentType(ContentType.Application.Json)
                setBody(RegisterRequest(name, email, password))
            }

            if (response.status.isSuccess()) {
                AuthResult.Success(response.body<AuthResponse>())
            } else {
                // El backend respondió pero con error (ej. correo ya registrado)
                val error = response.body<ErrorResponse>()
                AuthResult.Error(error.error)
            }
        } catch (e: Exception) {
            // No se pudo ni siquiera contactar al servidor (sin red, servidor apagado, etc.)
            AuthResult.Error("No se pudo conectar al servidor: ${e.message}")
        }
    }

    // ── Login ─────────────────────────────────────────────────────────────────
    suspend fun login(
        email:    String,
        password: String
    ): AuthResult<AuthResponse> {
        return try {
            val response = client.post("$BASE_URL/login") {
                contentType(ContentType.Application.Json)
                setBody(LoginRequest(email, password))
            }

            if (response.status.isSuccess()) {
                AuthResult.Success(response.body<AuthResponse>())
            } else {
                // Credenciales incorrectas u otro error controlado por el backend
                val error = response.body<ErrorResponse>()
                AuthResult.Error(error.error)
            }
        } catch (e: Exception) {
            AuthResult.Error("No se pudo conectar al servidor: ${e.message}")
        }
    }

    // ── Obtener perfil ────────────────────────────────────────────────────────
    suspend fun getProfile(token: String): AuthResult<User> {
        return try {
            val response = client.get("$BASE_URL/profile") {
                header(HttpHeaders.Authorization, "Bearer $token")
            }

            if (response.status.isSuccess()) {
                AuthResult.Success(response.body<User>())
            } else {
                AuthResult.Error("Token inválido o expirado")
            }
        } catch (e: Exception) {
            AuthResult.Error("No se pudo conectar al servidor: ${e.message}")
        }
    }
}