package com.example.signtranslate.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.*
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.signtranslate.viewmodel.AuthService

val PurpleTop    = Color(0xFF7B5EA7)
val PurpleButton = Color(0xFF6650A4)

@Composable
fun LoginView(onLoginExitoso: () -> Unit) {
    val auth     = AuthService()
    var email    by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var verPass  by remember { mutableStateOf(false) }
    var error    by remember { mutableStateOf(false) }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Brush.verticalGradient(listOf(PurpleTop, Color(0xFF4A3080))))
            .windowInsetsPadding(WindowInsets.systemBars),
        contentAlignment = Alignment.Center
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(Modifier.height(48.dp))

            // Logo SF
            Box(
                modifier = Modifier
                    .size(80.dp)
                    .background(Color.White, RoundedCornerShape(20.dp)),
                contentAlignment = Alignment.Center
            ) {
                Text("SF", fontSize = 28.sp, fontWeight = FontWeight.Bold, color = PurpleButton)
            }

            Spacer(Modifier.height(12.dp))

            Text(
                "SignTranslate",
                fontSize = 28.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White
            )

            Spacer(Modifier.height(32.dp))

            // Card formulario
            Card(
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                elevation = CardDefaults.cardElevation(8.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(24.dp)) {

                    Text("Iniciar Sesión", fontSize = 20.sp, fontWeight = FontWeight.Bold)
                    Spacer(Modifier.height(4.dp))
                    Text("Ingresa tus credenciales", fontSize = 13.sp, color = Color(0xFF666666))
                    Spacer(Modifier.height(20.dp))

                    OutlinedTextField(
                        value = email,
                        onValueChange = { email = it; error = false },
                        label = { Text("Correo electrónico") },
                        modifier = Modifier.fillMaxWidth(),
                        singleLine = true
                    )

                    Spacer(Modifier.height(12.dp))

                    OutlinedTextField(
                        value = password,
                        onValueChange = { password = it; error = false },
                        label = { Text("Contraseña") },
                        modifier = Modifier.fillMaxWidth(),
                        singleLine = true,
                        visualTransformation = if (verPass) VisualTransformation.None
                        else PasswordVisualTransformation(),
                        trailingIcon = {
                            TextButton(onClick = { verPass = !verPass }) {
                                Text(if (verPass) "Ocultar" else "Ver")
                            }
                        }
                    )

                    Spacer(Modifier.height(8.dp))

                    AnimatedVisibility(visible = error) {
                        Text(
                            "Correo o contraseña incorrectos",
                            color = Color(0xFFB00020),
                            fontSize = 13.sp
                        )
                    }

                    Spacer(Modifier.height(16.dp))

                    Button(
                        onClick = {
                            if (auth.validar(email, password)) onLoginExitoso()
                            else error = true
                        },
                        modifier = Modifier.fillMaxWidth().height(50.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = PurpleButton),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Text("Iniciar Sesión  →", fontSize = 16.sp)
                    }
                }
            }
        }
    }
}