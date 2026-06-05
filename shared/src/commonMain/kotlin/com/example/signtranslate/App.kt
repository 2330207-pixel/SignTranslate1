package com.example.signtranslate

import androidx.compose.runtime.*
import com.example.signtranslate.ui.screens.LoginView

enum class Screen { LOGIN }

@Composable
fun App() {
    var pantalla by remember { mutableStateOf(Screen.LOGIN) }

    when (pantalla) {
        Screen.LOGIN -> LoginView(
            onLoginExitoso = { /* aquí navegarás al Dashboard después */ }
        )
    }
}