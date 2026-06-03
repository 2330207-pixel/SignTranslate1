package com.example.signtranslate.ui.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.material.icons.outlined.Translate
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.signtranslate.ui.theme.*

@Composable
fun BottomNavBar(
    currentRoute: String?,
    onNavigate: (String) -> Unit,
) {
    val navColors = NavigationBarItemDefaults.colors(
        selectedIconColor   = BrandPurple,
        selectedTextColor   = BrandPurple,
        indicatorColor      = BrandPurpleLight,
        unselectedIconColor = TextBody,
        unselectedTextColor = TextBody,
    )

    NavigationBar(
        containerColor = NavBarBg,
        tonalElevation = 0.dp,
    ) {
        NavigationBarItem(
            selected = currentRoute == "inicio",
            onClick  = { onNavigate("inicio") },
            icon     = { Icon(Icons.Outlined.Home, contentDescription = "Inicio") },
            label    = { Text("Inicio", fontSize = 11.sp) },
            colors   = navColors,
        )
        NavigationBarItem(
            selected = currentRoute == "traductor",
            onClick  = { onNavigate("traductor") },
            icon     = { Icon(Icons.Outlined.Translate, contentDescription = "Traductor") },
            label    = { Text("Traductor", fontSize = 11.sp) },
            colors   = navColors,
        )
        NavigationBarItem(
            selected = currentRoute == "avatar",
            onClick  = { onNavigate("avatar") },
            icon     = { Icon(Icons.Outlined.Person, contentDescription = "Avatar") },
            label    = { Text("Avatar", fontSize = 11.sp) },
            colors   = navColors,
        )
        NavigationBarItem(
            selected = currentRoute == "ajustes",
            onClick  = { onNavigate("ajustes") },
            icon     = { Icon(Icons.Outlined.Settings, contentDescription = "Ajustes") },
            label    = { Text("Ajustes", fontSize = 11.sp) },
            colors   = navColors,
        )
    }
}