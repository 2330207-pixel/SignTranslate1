package com.example.signtranslate.ui.navigation

import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.signtranslate.ui.screens.InicioScreen
import com.example.signtranslate.ui.screens.LoginScreen
import com.example.signtranslate.ui.screens.SettingsScreen
import com.example.signtranslate.ui.screens.avatar.AvatarScreen
import com.example.signtranslate.ui.screens.translator.TranslatorScreen
import com.example.signtranslate.viewmodel.AuthViewModel
import com.example.signtranslate.viewmodel.SettingsViewModel
import com.example.signtranslate.viewmodel.avatar.AvatarViewModel
import com.example.signtranslate.viewmodel.translator.TranslatorViewModel

sealed class Screen(val route: String) {
    object Login : Screen("login")
    object Home : Screen("inicio")
    object Translator : Screen("traductor")
    object Avatar : Screen("avatar")
    object Settings : Screen("ajustes")
}

@Composable
fun AppNavigation(
    navController: NavHostController,
    authViewModel: AuthViewModel,
    translatorViewModel: TranslatorViewModel,
    avatarViewModel: AvatarViewModel,
    settingsViewModel: SettingsViewModel,
    cameraPreviewContent: @Composable BoxScope.() -> Unit = {},
    landmarksOverlayContent: @Composable BoxScope.() -> Unit = {},
    modifier: Modifier = Modifier
) {

    NavHost(
        navController = navController,
        startDestination = Screen.Home.route,
        modifier = modifier
    ) {

        composable(Screen.Login.route) {

            LoginScreen(
                viewModel = authViewModel,

                onLoginSuccess = { _, _ ->

                    navController.navigate(Screen.Home.route) {

                        popUpTo(Screen.Login.route) {
                            inclusive = true
                        }

                        launchSingleTop = true
                    }
                },

                onDismiss = {
                    navController.popBackStack()
                }
            )
        }

        composable(Screen.Home.route) {

            InicioScreen(
                innerPadding = PaddingValues(),

                onNavigateToTranslator = {
                    navController.navigate(Screen.Translator.route)
                },

                onNavigateToAvatar = {
                    navController.navigate(Screen.Avatar.route)
                },

                onNavigateToTextToSign = {
                    navController.navigate(Screen.Translator.route)
                },

                onNavigateToSettings = {
                    navController.navigate(Screen.Settings.route)
                },

                onRegisterClick = {
                    navController.navigate(Screen.Login.route)
                }
            )
        }

        composable(Screen.Translator.route) {

            TranslatorScreen(
                viewModel = translatorViewModel,

                onNavigate = { route ->
                    navController.navigate(route)
                },

                cameraPreviewContent = cameraPreviewContent,

                landmarksOverlayContent =
                    landmarksOverlayContent
            )
        }

        composable(Screen.Avatar.route) {

            AvatarScreen(
                viewModel = avatarViewModel
            )
        }

        composable(Screen.Settings.route) {

            SettingsScreen(
                authViewModel = authViewModel,
                settingsViewModel = settingsViewModel,
                onNavigateToLogin = {
                    navController.navigate(Screen.Login.route)
                }
            )
        }
    }
}