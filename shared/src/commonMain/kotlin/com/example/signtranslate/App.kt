package com.example.signtranslate

import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.signtranslate.ui.navigation.AppNavigation
import com.example.signtranslate.ui.navigation.BottomNavBar
import com.example.signtranslate.ui.navigation.Screen
import com.example.signtranslate.ui.theme.SignTranslateTheme
import com.example.signtranslate.viewmodel.AuthViewModel
import com.example.signtranslate.viewmodel.SettingsViewModel
import com.example.signtranslate.viewmodel.avatar.AvatarViewModel
import com.example.signtranslate.viewmodel.translator.TranslatorViewModel

/**
 * Raíz de la aplicación.
 *
 * El tema se actualiza automáticamente cuando cambian:
 * - darkMode
 * - highContrast
 * - largeText
 *
 * mediante SettingsViewModel + DataStore.
 */
@Composable
fun App(
    authViewModel: AuthViewModel,
    translatorViewModel: TranslatorViewModel,
    avatarViewModel: AvatarViewModel,
    settingsViewModel: SettingsViewModel,

    cameraPreviewContent: @Composable BoxScope.() -> Unit = {},
    landmarksOverlayContent: @Composable BoxScope.() -> Unit = {},
) {

    SignTranslateTheme(
        settingsViewModel = settingsViewModel
    ) {

        val navController = rememberNavController()

        val currentEntry by navController
            .currentBackStackEntryAsState()

        val currentRoute =
            currentEntry?.destination?.route

        val bottomRoutes = listOf(
            Screen.Home.route,
            Screen.Translator.route,
            Screen.Avatar.route,
            Screen.Settings.route
        )

        Scaffold(
            bottomBar = {
                if (currentRoute in bottomRoutes) {
                    BottomNavBar(navController = navController)
                }
            }
        ) { paddingValues ->

            AppNavigation(
                navController = navController,

                authViewModel = authViewModel,
                translatorViewModel = translatorViewModel,
                avatarViewModel = avatarViewModel,
                settingsViewModel = settingsViewModel,

                cameraPreviewContent = cameraPreviewContent,
                landmarksOverlayContent = landmarksOverlayContent,

                modifier = Modifier.padding(
                    paddingValues
                )
            )
        }
    }
}