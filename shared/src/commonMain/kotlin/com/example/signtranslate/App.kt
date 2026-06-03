package com.example.signtranslate

import androidx.compose.runtime.Composable
import com.example.signtranslate.ui.navigation.AppNavigation
import com.example.signtranslate.ui.theme.SignTranslateTheme

@Composable
fun App() {
    SignTranslateTheme {
        AppNavigation()
    }
}