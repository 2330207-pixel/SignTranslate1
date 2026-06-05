package com.example.signtranslate.viewmodel

class AuthService {
    fun validar(email: String, password: String): Boolean {
        return email == "usuario@signtranslate.com" && password == "1234"
    }
}