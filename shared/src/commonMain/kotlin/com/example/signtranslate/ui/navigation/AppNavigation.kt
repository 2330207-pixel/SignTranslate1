package com.example.signtranslate.ui.navigation

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.signtranslate.ui.screens.AvatarScreen
import com.example.signtranslate.ui.screens.InicioScreen
import com.example.signtranslate.ui.screens.SettingsScreen
import com.example.signtranslate.ui.screens.TranslatorScreen

@Composable
fun AppNavigation() {
    val navController = rememberNavController()
    val backStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = backStackEntry?.destination?.route

    val screensWithBottomBar = listOf("inicio", "traductor", "avatar", "ajustes")

    Scaffold(
        bottomBar = {
            if (currentRoute in screensWithBottomBar) {
                BottomNavBar(
                    currentRoute = currentRoute,
                    onNavigate = { route ->
                        navController.navigate(route) {
                            popUpTo("inicio") { saveState = true }
                            launchSingleTop = true
                            restoreState = true
                        }
                    }
                )
            }
        }
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = "inicio",
        ) {
            composable("inicio") {
                InicioScreen(
                    innerPadding = innerPadding,
                    onNavigateToTranslator = { navController.navigate("traductor") },
                    onNavigateToAvatar = { navController.navigate("avatar") },
                    onNavigateToTextToSign = { navController.navigate("traductor") },
                    onNavigateToSettings = { navController.navigate("ajustes") },
                    onNotificationsClick = { },
                )
            }
            composable("traductor") {
                TranslatorScreen(innerPadding = innerPadding)
            }
            composable("avatar") {
                AvatarScreen(innerPadding = innerPadding)
            }
            composable("ajustes") {
                SettingsScreen(innerPadding = innerPadding)
            }
        }
    }
}