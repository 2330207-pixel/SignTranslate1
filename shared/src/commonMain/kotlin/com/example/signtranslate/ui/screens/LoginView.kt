
package com.example.signtranslate.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.*
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.signtranslate.viewmodel.AuthService

// ── Paleta de colores ────────────────────────────────────────────────────────
val LavenderBg    = Color(0xFFE8E0F7)
val PurpleGradTop = Color(0xFF7B5EA7)
val PurpleGradBot = Color(0xFF4A3080)
val PurpleAccent  = Color(0xFF6650A4)
val TealLogo      = Color(0xFF3ABFBF)
val TextGray      = Color(0xFF888888)
val BorderActive  = Color(0xFF7B5EA7)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LoginView(onLoginExitoso: () -> Unit) {

    val auth        = remember { AuthService() }
    var selectedTab by remember { mutableStateOf(0) }
    var email       by remember { mutableStateOf("") }
    var password    by remember { mutableStateOf("") }
    var verPass     by remember { mutableStateOf(false) }
    var recordarme  by remember { mutableStateOf(false) }
    var error       by remember { mutableStateOf(false) }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    colorStops = arrayOf(
                        0.00f to PurpleGradTop,
                        0.35f to Color(0xFF9B7DC8),
                        1.00f to PurpleGradBot
                    )
                )
            )
            .windowInsetsPadding(WindowInsets.systemBars)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(Modifier.height(48.dp))

            // ── Logo ─────────────────────────────────────────────────────
            Box(
                modifier = Modifier
                    .size(100.dp)
                    .clip(CircleShape)
                    .background(Color.White.copy(alpha = 0.15f)),
                contentAlignment = Alignment.Center
            ) {
                // TODO: reemplaza con tu Image() cuando tengas el logo:
                // Image(
                //     painter = painterResource(id = R.drawable.logo_signtranslate),
                //     contentDescription = "Logo",
                //     modifier = Modifier.size(80.dp)
                // )
                Text("🤟", fontSize = 46.sp)
            }

            Spacer(Modifier.height(12.dp))

            Text(
                text = "SignTranslate",
                fontSize = 30.sp,
                fontWeight = FontWeight.Bold,
                color = TealLogo
            )
            Text(
                text = "Comunicación sin barreras",
                fontSize = 13.sp,
                color = Color.White.copy(alpha = 0.85f),
                letterSpacing = 0.5.sp
            )

            Spacer(Modifier.height(32.dp))

            // ── Tarjeta principal ─────────────────────────────────────────
            Card(
                shape = RoundedCornerShape(28.dp),
                colors = CardDefaults.cardColors(containerColor = LavenderBg),
                elevation = CardDefaults.cardElevation(0.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp)
            ) {
                Column(
                    modifier = Modifier.padding(horizontal = 24.dp, vertical = 28.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {

                    // Título
                    Text(
                        text = if (selectedTab == 0) "Iniciar sesion" else "Registrarse",
                        fontSize = 24.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF1A1A2E)
                    )
                    Spacer(Modifier.height(4.dp))
                    Text(
                        text = if (selectedTab == 0)
                            "Accede a tu cuenta para continuar"
                        else
                            "Crea tu cuenta para comenzar",
                        fontSize = 13.sp,
                        color = TextGray
                    )

                    Spacer(Modifier.height(20.dp))

                    // ── Tabs ──────────────────────────────────────────────
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceEvenly
                    ) {
                        listOf("Iniciar sesion", "Registrarse").forEachIndexed { index, label ->
                            Column(
                                horizontalAlignment = Alignment.CenterHorizontally,
                                modifier = Modifier
                                    .weight(1f)
                                    .clickable { selectedTab = index; error = false }
                                    .padding(vertical = 8.dp)
                            ) {
                                Text(
                                    text = label,
                                    fontSize = 14.sp,
                                    fontWeight = if (selectedTab == index) FontWeight.SemiBold else FontWeight.Normal,
                                    color = if (selectedTab == index) PurpleAccent else TextGray
                                )
                                Spacer(Modifier.height(4.dp))
                                if (selectedTab == index) {
                                    Box(
                                        modifier = Modifier
                                            .height(2.dp)
                                            .fillMaxWidth(0.6f)
                                            .background(PurpleAccent, RoundedCornerShape(1.dp))
                                    )
                                }
                            }
                        }
                    }

                    Spacer(Modifier.height(20.dp))

                    // ── Campo Email ───────────────────────────────────────
                    OutlinedTextField(
                        value = email,
                        onValueChange = { email = it; error = false },
                        label = { Text("Correo electronico", fontSize = 12.sp) },
                        leadingIcon = {
                            Icon(
                                imageVector = Icons.Default.Email,
                                contentDescription = null,
                                tint = PurpleAccent
                            )
                        },
                        modifier = Modifier.fillMaxWidth(),
                        singleLine = true,
                        shape = RoundedCornerShape(14.dp),
                        colors = OutlinedTextFieldDefaults.colors(
                            unfocusedContainerColor = Color.White,
                            focusedContainerColor   = Color.White,
                            focusedBorderColor      = BorderActive,
                            unfocusedBorderColor    = Color.Transparent
                        )
                    )

                    Spacer(Modifier.height(12.dp))

                    // ── Campo Contraseña ──────────────────────────────────
                    OutlinedTextField(
                        value = password,
                        onValueChange = { password = it; error = false },
                        label = { Text("Contrasena", fontSize = 12.sp) },
                        leadingIcon = {
                            Icon(
                                imageVector = Icons.Default.Lock,
                                contentDescription = null,
                                tint = PurpleAccent
                            )
                        },
                        visualTransformation = if (verPass) VisualTransformation.None
                        else PasswordVisualTransformation(),
                        trailingIcon = {
                            IconButton(onClick = { verPass = !verPass }) {
                                Icon(
                                    imageVector = if (verPass) Icons.Default.VisibilityOff
                                    else Icons.Default.Visibility,
                                    contentDescription = if (verPass) "Ocultar" else "Mostrar",
                                    tint = PurpleAccent
                                )
                            }
                        },
                        modifier = Modifier.fillMaxWidth(),
                        singleLine = true,
                        shape = RoundedCornerShape(14.dp),
                        colors = OutlinedTextFieldDefaults.colors(
                            unfocusedContainerColor = Color.White,
                            focusedContainerColor   = Color.White,
                            focusedBorderColor      = BorderActive,
                            unfocusedBorderColor    = Color.Transparent
                        )
                    )

                    // ── Mensaje de error (oculto por defecto) ─────────────
                    AnimatedVisibility(
                        visible = error,
                        enter = fadeIn(),
                        exit = fadeOut()
                    ) {
                        Text(
                            text = "Correo o contraseña incorrectos",
                            color = Color(0xFFB00020),
                            fontSize = 12.sp,
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(top = 6.dp)
                        )
                    }

                    Spacer(Modifier.height(12.dp))

                    // ── Recordarme + ¿Olvidaste? ──────────────────────────
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Checkbox(
                                checked = recordarme,
                                onCheckedChange = { recordarme = it },
                                colors = CheckboxDefaults.colors(
                                    checkedColor   = PurpleAccent,
                                    uncheckedColor = TextGray
                                ),
                                modifier = Modifier.size(20.dp)
                            )
                            Spacer(Modifier.width(6.dp))
                            Text("Recordarme", fontSize = 13.sp, color = Color(0xFF444444))
                        }
                        Text(
                            text = "¿Olvidaste tu contrasena?",
                            fontSize = 13.sp,
                            color = PurpleAccent,
                            fontWeight = FontWeight.Medium,
                            modifier = Modifier.clickable { /* TODO: recuperar contraseña */ }
                        )
                    }

                    Spacer(Modifier.height(20.dp))

                    // ── Botón principal ───────────────────────────────────
                    Button(
                        onClick = {
                            if (selectedTab == 0) {
                                if (auth.validar(email, password)) onLoginExitoso()
                                else error = true
                            } else {
                                if (email.isNotBlank() && password.isNotBlank()) onLoginExitoso()
                                else error = true
                            }
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(52.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = PurpleAccent),
                        shape = RoundedCornerShape(26.dp),
                        elevation = ButtonDefaults.buttonElevation(6.dp)
                    ) {
                        Text(
                            text = if (selectedTab == 0) "Iniciar sesion" else "Registrarse",
                            fontSize = 16.sp,
                            fontWeight = FontWeight.SemiBold
                        )
                    }

                    Spacer(Modifier.height(20.dp))

                    // ── Divisor ───────────────────────────────────────────
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        HorizontalDivider(modifier = Modifier.weight(1f), color = Color(0xFFCCCCCC))
                        Text("  o continua con  ", fontSize = 12.sp, color = TextGray)
                        HorizontalDivider(modifier = Modifier.weight(1f), color = Color(0xFFCCCCCC))
                    }

                    Spacer(Modifier.height(16.dp))

                    // ── Botón Google ──────────────────────────────────────
                    OutlinedButton(
                        onClick = { /* TODO: Google Sign-In */ },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(50.dp),
                        shape = RoundedCornerShape(14.dp),
                        colors = ButtonDefaults.outlinedButtonColors(
                            containerColor = Color.White,
                            contentColor   = Color(0xFF444444)
                        ),
                        border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFFDDDDDD))
                    ) {
                        Text(
                            text = "G  ",
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF4285F4)
                        )
                        Text("Continuar con Google", fontSize = 14.sp)
                    }

                    Spacer(Modifier.height(20.dp))

                    // ── ¿No tienes cuenta? ────────────────────────────────
                    Text(
                        text = buildAnnotatedString {
                            withStyle(SpanStyle(color = TextGray, fontSize = 13.sp)) {
                                append(if (selectedTab == 0) "¿No tienes cuenta? " else "¿Ya tienes cuenta? ")
                            }
                            withStyle(
                                SpanStyle(
                                    color          = PurpleAccent,
                                    fontSize       = 13.sp,
                                    fontWeight     = FontWeight.SemiBold,
                                    textDecoration = TextDecoration.Underline
                                )
                            ) {
                                append(if (selectedTab == 0) "Registrate" else "Inicia sesion")
                            }
                        },
                        modifier = Modifier.clickable {
                            selectedTab = if (selectedTab == 0) 1 else 0
                            error = false
                        },
                        textAlign = TextAlign.Center
                    )
                }
            }

            Spacer(Modifier.height(32.dp))
        }
    }
}