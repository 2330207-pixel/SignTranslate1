package com.example.signtranslate.ui.screens
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.signtranslate.ui.theme.*
import com.example.signtranslate.viewmodel.AuthViewModel
import org.jetbrains.compose.resources.painterResource
import signtranslate.shared.generated.resources.Res
import signtranslate.shared.generated.resources.google
import signtranslate.shared.generated.resources.logo

private val BgPurpleTop    = Color(0xFF7B5EA7)
private val BgPurpleBottom = Color(0xFF4C2D99)
private val CardLavender   = Color(0xCCF0EDF8)
private val FieldBorder    = Color(0xFFDDDAF0)

@Composable
fun LoginScreen(
    viewModel: AuthViewModel,                                  // ← recibe el VM real
    onLoginSuccess: (nombre: String, email: String) -> Unit = { _, _ -> },
    onDismiss:      () -> Unit = {}
) {
    // ── Estado del ViewModel ──────────────────────────────────────────────────
    val uiState by viewModel.uiState.collectAsState()

    // ── Estado local de la UI ─────────────────────────────────────────────────
    var isLoginMode     by remember { mutableStateOf(true) }
    var name            by remember { mutableStateOf("") }
    var email           by remember { mutableStateOf("") }
    var password        by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }
    var passwordVisible by remember { mutableStateOf(false) }
    var confirmVisible  by remember { mutableStateOf(false) }
    var rememberMe      by remember { mutableStateOf(false) }

    // ── Navegar cuando la autenticación sea exitosa ───────────────────────────
    LaunchedEffect(uiState.isAuthenticated) {
        if (uiState.isAuthenticated && uiState.currentUser != null) {
            onLoginSuccess(uiState.currentUser!!.name, uiState.currentUser!!.email)
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Brush.verticalGradient(listOf(BgPurpleTop, BgPurpleBottom)))
    ) {
        // Fondo decorativo
        Canvas(modifier = Modifier.fillMaxSize()) {
            val w = size.width; val h = size.height
            drawOval(color = Color.White.copy(alpha = 0.13f),
                topLeft = Offset(-w * 0.45f, -h * 0.05f), size = Size(w, h * 0.45f))
            drawOval(color = Color(0xFF5B2D9E).copy(alpha = 0.60f),
                topLeft = Offset(w * 0.30f, h * 0.68f), size = Size(w, h * 0.45f))
            drawCircle(color = Color.White.copy(alpha = 0.08f),
                radius = w * 0.12f, center = Offset(w * 0.92f, h * 0.94f))
        }

        Column(
            modifier            = Modifier.fillMaxSize().verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Card(
                modifier  = Modifier.padding(horizontal = 10.dp)
                    .padding(top = 70.dp, bottom = 30.dp)
                    .fillMaxWidth(0.9f),
                shape     = RoundedCornerShape(20.dp),
                colors    = CardDefaults.cardColors(containerColor = CardLavender),
                elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
            ) {
                Box {
                    Column(
                        modifier            = Modifier.padding(horizontal = 16.dp, vertical = 4.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Spacer(Modifier.height(32.dp))

                        Image(
                            painter            = painterResource(Res.drawable.logo),
                            contentDescription = "SignTranslate Logo",
                            modifier           = Modifier.size(250.dp),
                            contentScale       = ContentScale.Fit
                        )

                        Text(
                            text       = if (isLoginMode) "Iniciar sesión" else "Crear cuenta",
                            fontSize   = 30.sp,
                            fontWeight = FontWeight.Bold,
                            color      = TextDark,
                            textAlign  = TextAlign.Center
                        )
                        Spacer(Modifier.height(4.dp))
                        Text(
                            text      = if (isLoginMode) "Accede a tu cuenta para continuar"
                            else "Crea tu cuenta gratis",
                            fontSize  = 13.sp,
                            color     = TextBody,
                            textAlign = TextAlign.Center
                        )

                        Spacer(Modifier.height(12.dp))

                        // ── Tabs ──────────────────────────────────────────
                        Row(
                            modifier              = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.Center
                        ) {
                            TabButton("Iniciar sesión", isLoginMode) {
                                isLoginMode = true
                                viewModel.clearError()
                            }
                            TabButton("Registrarse", !isLoginMode) {
                                isLoginMode = false
                                viewModel.clearError()
                            }
                        }

                        Spacer(Modifier.height(12.dp))

                        // ── Campos ────────────────────────────────────────
                        if (!isLoginMode) {
                            AuthTextField(
                                value         = name,
                                onValueChange = { name = it; viewModel.clearError() },
                                label         = "Nombre completo",
                                icon          = Icons.Default.Person
                            )
                            Spacer(Modifier.height(14.dp))
                        }

                        AuthTextField(
                            value         = email,
                            onValueChange = { email = it; viewModel.clearError() },
                            label         = "Correo electrónico",
                            placeholder   = "ejemplo@correo.com",
                            icon          = Icons.Default.Email
                        )
                        Spacer(Modifier.height(14.dp))

                        AuthTextField(
                            value            = password,
                            onValueChange    = { password = it; viewModel.clearError() },
                            label            = "Contraseña",
                            icon             = Icons.Default.Lock,
                            isPassword       = true,
                            passwordVisible  = passwordVisible,
                            onTogglePassword = { passwordVisible = !passwordVisible }
                        )

                        if (!isLoginMode) {
                            Spacer(Modifier.height(14.dp))
                            AuthTextField(
                                value            = confirmPassword,
                                onValueChange    = { confirmPassword = it; viewModel.clearError() },
                                label            = "Confirmar contraseña",
                                icon             = Icons.Default.Lock,
                                isPassword       = true,
                                passwordVisible  = confirmVisible,
                                onTogglePassword = { confirmVisible = !confirmVisible }
                            )
                        }

                        // ── Recordarme ────────────────────────────────────
                        if (isLoginMode) {
                            Spacer(Modifier.height(10.dp))
                            Row(
                                modifier              = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment     = Alignment.CenterVertically
                            ) {
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    modifier          = Modifier.offset(x = (-12).dp)
                                ) {
                                    Checkbox(
                                        checked         = rememberMe,
                                        onCheckedChange = { rememberMe = it },
                                        colors          = CheckboxDefaults.colors(
                                            checkedColor   = BrandPurple,
                                            uncheckedColor = TextBody
                                        )
                                    )
                                    Text("Recordarme", fontSize = 13.sp,
                                        color = TextDark, fontWeight = FontWeight.Medium)
                                }
                                TextButton(onClick = {}, contentPadding = PaddingValues(0.dp)) {
                                    Text("¿Olvidaste tu contraseña?",
                                        color = BrandPurple, fontSize = 13.sp,
                                        fontWeight = FontWeight.SemiBold)
                                }
                            }
                        }

                        Spacer(Modifier.height(22.dp))

                        // ── Botón principal ───────────────────────────────
                        Button(
                            onClick = {
                                if (isLoginMode) {
                                    viewModel.login(email.trim(), password)
                                } else {
                                    // Validación local de confirmación de contraseña
                                    if (password != confirmPassword) {
                                        // Esta validación sigue siendo local (no necesita el server)
                                        return@Button
                                    }
                                    viewModel.register(name.trim(), email.trim(), password)
                                }
                            },
                            enabled   = !uiState.isLoading,   // deshabilitar mientras carga
                            modifier  = Modifier.fillMaxWidth().height(54.dp),
                            shape     = RoundedCornerShape(27.dp),
                            colors    = ButtonDefaults.buttonColors(containerColor = BrandPurple),
                            elevation = ButtonDefaults.buttonElevation(
                                defaultElevation = 6.dp, pressedElevation = 10.dp
                            )
                        ) {
                            if (uiState.isLoading) {
                                // Spinner mientras el API responde
                                CircularProgressIndicator(
                                    modifier  = Modifier.size(24.dp),
                                    color     = Color.White,
                                    strokeWidth = 2.dp
                                )
                            } else {
                                Text(
                                    text       = if (isLoginMode) "Iniciar sesión" else "Crear cuenta",
                                    fontSize   = 16.sp,
                                    fontWeight = FontWeight.Bold,
                                    color      = Color.White
                                )
                            }
                        }

                        // ── Error del servidor ────────────────────────────
                        if (uiState.errorMessage.isNotEmpty()) {
                            Spacer(Modifier.height(8.dp))
                            Text(
                                text      = uiState.errorMessage,
                                color     = Color(0xFFE24B4A),
                                fontSize  = 13.sp,
                                textAlign = TextAlign.Center,
                                modifier  = Modifier.fillMaxWidth()
                            )
                        }

                        // ── Validación local: contraseñas no coinciden ────
                        if (!isLoginMode && password.isNotEmpty()
                            && confirmPassword.isNotEmpty()
                            && password != confirmPassword) {
                            Spacer(Modifier.height(8.dp))
                            Text(
                                text      = "Las contraseñas no coinciden",
                                color     = Color(0xFFE24B4A),
                                fontSize  = 13.sp,
                                textAlign = TextAlign.Center,
                                modifier  = Modifier.fillMaxWidth()
                            )
                        }

                        // ── Divisor + Google (solo login) ─────────────────
                        if (isLoginMode) {
                            Spacer(Modifier.height(22.dp))
                            Row(modifier = Modifier.fillMaxWidth(),
                                verticalAlignment = Alignment.CenterVertically) {
                                HorizontalDivider(modifier = Modifier.weight(1f), color = FieldBorder)
                                Text("  o continúa con  ", color = TextBody, fontSize = 13.sp)
                                HorizontalDivider(modifier = Modifier.weight(1f), color = FieldBorder)
                            }
                            Spacer(Modifier.height(14.dp))

                            OutlinedButton(
                                onClick  = { /* TODO: Google Sign-In */ },
                                modifier = Modifier.fillMaxWidth().height(54.dp),
                                shape    = RoundedCornerShape(14.dp),
                                border   = BorderStroke(1.dp, FieldBorder),
                                colors   = ButtonDefaults.outlinedButtonColors(containerColor = Color.White)
                            ) {
                                Row(verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.Center) {
                                    Image(painter = painterResource(Res.drawable.google),
                                        contentDescription = "Google",
                                        modifier = Modifier.size(24.dp),
                                        contentScale = ContentScale.Fit)
                                    Spacer(Modifier.width(10.dp))
                                    Text("Continuar con Google",
                                        color = TextDark, fontSize = 15.sp,
                                        fontWeight = FontWeight.Medium)
                                }
                            }
                        }

                        Spacer(Modifier.height(22.dp))

                        Row(verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.Center) {
                            Text(
                                text     = if (isLoginMode) "¿No tienes cuenta? " else "¿Ya tienes cuenta? ",
                                color    = TextBody,
                                fontSize = 14.sp
                            )
                            TextButton(
                                onClick        = { isLoginMode = !isLoginMode; viewModel.clearError() },
                                contentPadding = PaddingValues(0.dp)
                            ) {
                                Text(
                                    text       = if (isLoginMode) "Regístrate" else "Inicia sesión",
                                    color      = BrandPurple,
                                    fontSize   = 14.sp,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                        }

                        Spacer(Modifier.height(16.dp))
                    }

                    IconButton(
                        onClick  = { onDismiss() },
                        modifier = Modifier.align(Alignment.TopEnd).padding(8.dp)
                    ) {
                        Icon(imageVector = Icons.Default.Close,
                            contentDescription = "Cerrar", tint = TextBody)
                    }
                }
            }
        }
    }
}

// ─────────────────────────────────────────────────────────────────────────────
// COMPONENTES REUTILIZABLES (sin cambios respecto al original)
// ─────────────────────────────────────────────────────────────────────────────

@Composable
private fun TabButton(text: String, selected: Boolean, onClick: () -> Unit) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        TextButton(onClick = onClick, contentPadding = PaddingValues(horizontal = 16.dp, vertical = 4.dp)) {
            Text(text = text, fontSize = 15.sp,
                fontWeight = if (selected) FontWeight.Bold else FontWeight.Normal,
                color = if (selected) BrandPurple else TextBody)
        }
        if (selected) {
            Box(modifier = Modifier.width(80.dp).height(2.dp)
                .background(BrandPurple, RoundedCornerShape(1.dp)))
        }
    }
}

@Composable
private fun AuthTextField(
    value:            String,
    onValueChange:    (String) -> Unit,
    label:            String,
    placeholder:      String = "",
    icon:             androidx.compose.ui.graphics.vector.ImageVector,
    isPassword:       Boolean = false,
    passwordVisible:  Boolean = false,
    onTogglePassword: () -> Unit = {}
) {
    OutlinedTextField(
        value         = value,
        onValueChange = onValueChange,
        modifier      = Modifier.fillMaxWidth(),
        label         = { Text(label) },
        placeholder   = { if (placeholder.isNotEmpty()) Text(placeholder, color = TextHint) },
        leadingIcon   = { Icon(imageVector = icon, contentDescription = null,
            tint = BrandPurple.copy(alpha = 0.65f)) },
        trailingIcon  = if (isPassword) ({
            IconButton(onClick = onTogglePassword) {
                Icon(imageVector = if (passwordVisible) Icons.Default.VisibilityOff
                else Icons.Default.Visibility,
                    contentDescription = if (passwordVisible) "Ocultar" else "Mostrar",
                    tint = TextBody)
            }
        }) else null,
        visualTransformation = if (isPassword && !passwordVisible)
            PasswordVisualTransformation() else VisualTransformation.None,
        shape      = RoundedCornerShape(14.dp),
        singleLine = true,
        colors     = OutlinedTextFieldDefaults.colors(
            focusedContainerColor   = Color.White.copy(alpha = 0.85f),
            unfocusedContainerColor = Color.White.copy(alpha = 0.75f),
            focusedBorderColor      = BrandPurple,
            unfocusedBorderColor    = Color(0xFFDDDAF0),
            focusedLabelColor       = BrandPurple,
            unfocusedLabelColor     = Color(0xFF6B6B8A),
            cursorColor             = BrandPurple
        )
    )
}