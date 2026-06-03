package com.example.signtranslate

import androidx.compose.runtime.Composable
import com.example.signtranslate.ui.screens.TranslatorScreen
import com.example.signtranslate.ui.theme.SignTranslateTheme

@Composable
fun App() {
    SignTranslateTheme {
        TranslatorScreen()
    }
}