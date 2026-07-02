package com.example.signtranslate
import android.Manifest
import android.os.Bundle
import android.view.ViewGroup
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.camera.view.PreviewView
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.compose.ui.window.Dialog
import androidx.core.view.WindowCompat
import androidx.lifecycle.compose.LocalLifecycleOwner
import com.example.notifications.NotificationHelper
import com.example.signtranslate.mediapipe.HandLandmarksOverlay
import com.example.signtranslate.data.UserPreferences
import com.example.signtranslate.data.createDataStore
import com.example.signtranslate.ui.theme.BrandPurpleLight
import com.example.signtranslate.viewmodel.AndroidTranslatorViewModel
import com.example.signtranslate.viewmodel.AuthViewModel
import com.example.signtranslate.viewmodel.SettingsViewModel
import com.example.signtranslate.viewmodel.avatar.AvatarViewModel

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        enableEdgeToEdge()

        setContent {

            val context = LocalContext.current
            val lifecycleOwner = LocalLifecycleOwner.current

            val userPreferences =
                remember { UserPreferences(createDataStore()) }

            val authViewModel =
                remember { AuthViewModel(userPreferences = userPreferences) }

            val settingsViewModel =
                remember { SettingsViewModel(userPreferences) }

            val avatarViewModel =
                remember { AvatarViewModel() }

            val translatorViewModel =
                remember {
                    AndroidTranslatorViewModel(
                        context,
                        userPreferences,
                    )
                }

            // Instancia del helper de notificaciones.
            val notificationHelper =
                remember { NotificationHelper(context) }

            val permissionLauncher =
                rememberLauncherForActivityResult(
                    ActivityResultContracts.RequestMultiplePermissions()
                ) { }

            LaunchedEffect(Unit) {
                permissionLauncher.launch(
                    arrayOf(
                        Manifest.permission.CAMERA,
                        Manifest.permission.RECORD_AUDIO,
                        Manifest.permission.POST_NOTIFICATIONS
                    )
                )
            }

            val authUiState by authViewModel.uiState.collectAsState()

            LaunchedEffect(authUiState.successMessage) {
                if (authUiState.successMessage.isNotEmpty()) {
                    if (authUiState.successMessage.contains("Bienvenido")) {
                        notificationHelper.notifyRegisterSuccess()
                    }
                }
            }

            LaunchedEffect(authUiState.errorMessage) {
                if (authUiState.errorMessage.isNotEmpty()) {
                    notificationHelper.notifyConnectionError()
                }
            }

            val settings by settingsViewModel
                .uiState
                .collectAsState()

            LaunchedEffect(settings.darkMode) {

                val statusBarColor =
                    if (settings.darkMode)
                        Color.Black
                    else
                        BrandPurpleLight

                window.statusBarColor =
                    statusBarColor.toArgb()

                window.navigationBarColor =
                    if (settings.darkMode)
                        Color.Black.toArgb()
                    else
                        Color.White.toArgb()

                WindowCompat
                    .getInsetsController(
                        window,
                        window.decorView
                    )
                    .apply {
                        isAppearanceLightStatusBars =
                            !settings.darkMode

                        isAppearanceLightNavigationBars =
                            !settings.darkMode
                    }
            }


            App(
                authViewModel = authViewModel,
                translatorViewModel = translatorViewModel,
                avatarViewModel = avatarViewModel,
                settingsViewModel = settingsViewModel,

                cameraPreviewContent = {

                    AndroidView(
                        factory = { ctx ->

                            PreviewView(ctx).apply {

                                layoutParams =
                                    ViewGroup.LayoutParams(
                                        ViewGroup.LayoutParams.MATCH_PARENT,
                                        ViewGroup.LayoutParams.MATCH_PARENT
                                    )

                                implementationMode =
                                    PreviewView.ImplementationMode.COMPATIBLE

                                setBackgroundColor(
                                    android.graphics.Color.BLACK
                                )

                                translatorViewModel.setupCamera(
                                    lifecycleOwner,
                                    this
                                )
                            }
                        },
                        modifier = Modifier.fillMaxSize()
                    )
                },

                landmarksOverlayContent = {

                    val uiState by translatorViewModel
                        .uiState
                        .collectAsState()

                    HandLandmarksOverlay(
                        detections = uiState.handDetections,
                        showHandPoints = settings.showHandPoints
                    )
                }
            )

            var showTestPanel by remember { mutableStateOf(true) }

            if (showTestPanel) {
                Dialog(onDismissRequest = { showTestPanel = false }) {
                    Column(
                        modifier = Modifier
                            .padding(16.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text("Panel de prueba — Notificaciones")

                        Button(
                            modifier = Modifier.padding(top = 8.dp),
                            onClick = { notificationHelper.notifySessionExpired() }
                        ) {
                            Text("Sesión expirada")
                        }

                        Button(
                            modifier = Modifier.padding(top = 8.dp),
                            onClick = { notificationHelper.notifyTranslationReady() }
                        ) {
                            Text("Traducción lista")
                        }

                        Button(
                            modifier = Modifier.padding(top = 8.dp),
                            onClick = { notificationHelper.notifyConnectionError() }
                        ) {
                            Text("Error de conexión")
                        }

                        Button(
                            modifier = Modifier.padding(top = 8.dp),
                            onClick = { notificationHelper.notifyRegisterSuccess() }
                        ) {
                            Text("Registro exitoso")
                        }

                        Button(
                            modifier = Modifier.padding(top = 16.dp),
                            onClick = { showTestPanel = false }
                        ) {
                            Text("Cerrar panel")
                        }
                    }
                }
            }
        }
    }
}