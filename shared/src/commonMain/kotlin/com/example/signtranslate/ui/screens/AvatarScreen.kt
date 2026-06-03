package com.example.signtranslate.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Check
import androidx.compose.material.icons.outlined.Checkroom
import androidx.compose.material.icons.outlined.Face
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material.icons.outlined.Watch
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.signtranslate.ui.theme.BrandPurple
import com.example.signtranslate.ui.theme.BrandPurpleLight
import com.example.signtranslate.ui.theme.CardBg
import com.example.signtranslate.ui.theme.DividerColor
import com.example.signtranslate.ui.theme.NavBarBg
import com.example.signtranslate.ui.theme.TextBody
import com.example.signtranslate.ui.theme.TextDark

// ── Modelos de datos ──────────────────────────────────────────────────────────
data class AvatarTab(val label: String, val icon: ImageVector)

enum class Sexo { HOMBRE, MUJER }

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AvatarScreen(innerPadding: PaddingValues = PaddingValues()) {

    var selectedTab    by remember { mutableIntStateOf(0) }
    var sexo           by remember { mutableStateOf(Sexo.MUJER) }
    var colorPiel      by remember { mutableIntStateOf(0) }
    var colorCabello   by remember { mutableIntStateOf(0) }
    var estiloCabello  by remember { mutableIntStateOf(1) }
    var colorFondo     by remember { mutableIntStateOf(0) }

    val tabs = listOf(
        AvatarTab("Apariencia", Icons.Outlined.Person),
        AvatarTab("Cabello",    Icons.Outlined.Face),
        AvatarTab("Ropa",       Icons.Outlined.Checkroom),
        AvatarTab("Accesorios", Icons.Outlined.Watch),
    )

    val coloresPiel = listOf(
        Color(0xFFFDDEB4), Color(0xFFF5C18A), Color(0xFFE8A96A),
        Color(0xFFBF7D4A), Color(0xFF8D4E2A), Color(0xFF5C2E0E),
    )
    val coloresCabello = listOf(
        Color(0xFF1A1A1A), Color(0xFF3B2314), Color(0xFF6B3A2A),
        Color(0xFF8B5E3C), Color(0xFFC49A6C), Color(0xFFE8C97A),
        Color(0xFFD4627A), Color(0xFFA83255),
    )
    val coloresFondo = listOf(
        Color(0xFFE8E4F5), // lavanda (default)
        Color(0xFFE4F0FB), // azul suave
        Color(0xFFE4F5EC), // verde suave
        Color(0xFFFBF0E4), // durazno
        Color(0xFFF5E4F5), // rosa suave
        Color(0xFF2C2C3E), // oscuro
    )

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text("Mi Avatar", fontSize = 18.sp, fontWeight = FontWeight.Bold, color = TextDark)
                },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(containerColor = NavBarBg),
            )
        }
    ) { scaffoldPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(scaffoldPadding)
                .padding(bottom = innerPadding.calculateBottomPadding())
                .background(CardBg)
        ) {

            // ── PREVIEW DEL AVATAR ────────────────────────────────────────────
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(220.dp)
                    .background(coloresFondo[colorFondo]),
                contentAlignment = Alignment.Center,
            ) {
                Box(
                    modifier = Modifier
                        .size(170.dp)
                        .clip(CircleShape)
                        .background(coloresFondo[colorFondo].copy(alpha = 0.6f)),
                    contentAlignment = Alignment.Center,
                ) {
                    Text(
                        text     = if (sexo == Sexo.MUJER) "👩" else "👨",
                        fontSize = 100.sp,
                    )
                }
            }

            // ── TABS ──────────────────────────────────────────────────────────
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(NavBarBg)
                    .padding(horizontal = 8.dp, vertical = 8.dp),
                horizontalArrangement = Arrangement.SpaceEvenly,
            ) {
                tabs.forEachIndexed { index, tab ->
                    val isSelected = selectedTab == index
                    Column(
                        modifier = Modifier
                            .clip(RoundedCornerShape(12.dp))
                            .clickable { selectedTab = index }
                            .background(if (isSelected) BrandPurpleLight else Color.Transparent)
                            .padding(horizontal = 12.dp, vertical = 8.dp),
                        horizontalAlignment = Alignment.CenterHorizontally,
                    ) {
                        Icon(
                            imageVector = tab.icon,
                            contentDescription = tab.label,
                            tint     = if (isSelected) BrandPurple else TextBody,
                            modifier = Modifier.size(20.dp),
                        )
                        Text(
                            text     = tab.label,
                            fontSize = 10.sp,
                            color    = if (isSelected) BrandPurple else TextBody,
                            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                        )
                    }
                }
            }

            HorizontalDivider(color = DividerColor, thickness = 0.5.dp)

            // ── CONTENIDO DEL TAB ─────────────────────────────────────────────
            Column(
                modifier = Modifier
                    .weight(1f)
                    .verticalScroll(rememberScrollState())
                    .padding(horizontal = 20.dp, vertical = 16.dp),
                verticalArrangement = Arrangement.spacedBy(20.dp),
            ) {
                when (selectedTab) {
                    0 -> AparienciaTab(
                        sexo             = sexo,
                        onSexoChange     = { sexo = it },
                        colorPiel        = colorPiel,
                        onColorPiel      = { colorPiel = it },
                        coloresPiel      = coloresPiel,
                        colorFondo       = colorFondo,
                        onColorFondo     = { colorFondo = it },
                        coloresFondo     = coloresFondo,
                    )
                    1 -> CabelloTab(
                        colorCabello     = colorCabello,
                        onColorCabello   = { colorCabello = it },
                        coloresCabello   = coloresCabello,
                        estiloCabello    = estiloCabello,
                        onEstiloCabello  = { estiloCabello = it },
                    )
                    2 -> PlaceholderTab("Elige la ropa de tu avatar")
                    3 -> PlaceholderTab("Elige los accesorios de tu avatar")
                }
            }

            // ── BOTÓN GUARDAR ─────────────────────────────────────────────────
            Button(
                onClick = {},
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp, vertical = 16.dp)
                    .height(52.dp),
                shape  = RoundedCornerShape(16.dp),
                colors = ButtonDefaults.buttonColors(containerColor = BrandPurple),
            ) {
                Text("Guardar Avatar", fontSize = 16.sp, fontWeight = FontWeight.Bold, color = NavBarBg)
            }
        }
    }
}

// ─────────────────────────────────────────────────────────────────────────────
// TAB — APARIENCIA
// ─────────────────────────────────────────────────────────────────────────────
@Composable
private fun AparienciaTab(
    sexo: Sexo, onSexoChange: (Sexo) -> Unit,
    colorPiel: Int, onColorPiel: (Int) -> Unit, coloresPiel: List<Color>,
    colorFondo: Int, onColorFondo: (Int) -> Unit, coloresFondo: List<Color>,
) {
    // Sexo
    SectionLabel("Sexo")
    Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
        SexoChip("Hombre", sexo == Sexo.HOMBRE) { onSexoChange(Sexo.HOMBRE) }
        SexoChip("Mujer",  sexo == Sexo.MUJER)  { onSexoChange(Sexo.MUJER) }
    }

    // Color de piel
    SectionLabel("Color de piel")
    ColorPalette(colors = coloresPiel, selected = colorPiel, onSelect = onColorPiel)

    // Color de fondo del avatar
    SectionLabel("Color de fondo")
    ColorPalette(colors = coloresFondo, selected = colorFondo, onSelect = onColorFondo)
}

// ─────────────────────────────────────────────────────────────────────────────
// TAB — CABELLO
// ─────────────────────────────────────────────────────────────────────────────
@Composable
private fun CabelloTab(
    colorCabello: Int, onColorCabello: (Int) -> Unit, coloresCabello: List<Color>,
    estiloCabello: Int, onEstiloCabello: (Int) -> Unit,
) {
    SectionLabel("Color de cabello")
    ColorPalette(colors = coloresCabello, selected = colorCabello, onSelect = onColorCabello)

    SectionLabel("Estilo de cabello")
    Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
        listOf("👱", "👩", "👩‍🦱").forEachIndexed { index, emoji ->
            val isSelected = estiloCabello == index
            Box(
                modifier = Modifier
                    .size(72.dp)
                    .clip(RoundedCornerShape(16.dp))
                    .background(if (isSelected) BrandPurpleLight else NavBarBg)
                    .border(
                        width = if (isSelected) 2.dp else 1.dp,
                        color = if (isSelected) BrandPurple else DividerColor,
                        shape = RoundedCornerShape(16.dp),
                    )
                    .clickable { onEstiloCabello(index) },
                contentAlignment = Alignment.Center,
            ) {
                Text(emoji, fontSize = 36.sp)
            }
        }
    }
}

// ─────────────────────────────────────────────────────────────────────────────
// TAB — PLACEHOLDER (Ropa / Accesorios)
// ─────────────────────────────────────────────────────────────────────────────
@Composable
private fun PlaceholderTab(message: String) {
    Box(
        modifier         = Modifier.fillMaxWidth().height(160.dp),
        contentAlignment = Alignment.Center,
    ) {
        Text(message, color = TextBody, fontSize = 14.sp)
    }
}

// ─────────────────────────────────────────────────────────────────────────────
// COMPONENTES REUTILIZABLES
// ─────────────────────────────────────────────────────────────────────────────
@Composable
private fun SectionLabel(text: String) {
    Text(text, fontSize = 13.sp, fontWeight = FontWeight.Bold, color = TextDark)
}

@Composable
private fun SexoChip(label: String, selected: Boolean, onClick: () -> Unit) {
    Surface(
        modifier = Modifier
            .clip(RoundedCornerShape(50.dp))
            .clickable { onClick() },
        shape  = RoundedCornerShape(50.dp),
        color  = if (selected) BrandPurple else NavBarBg,
        border = if (!selected) androidx.compose.foundation.BorderStroke(1.dp, DividerColor) else null,
    ) {
        Row(
            modifier          = Modifier.padding(horizontal = 20.dp, vertical = 10.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(6.dp),
        ) {
            if (selected) {
                Icon(Icons.Outlined.Check, null, tint = NavBarBg, modifier = Modifier.size(16.dp))
            }
            Text(
                text       = label,
                fontSize   = 14.sp,
                fontWeight = FontWeight.Medium,
                color      = if (selected) NavBarBg else TextBody,
            )
        }
    }
}

@Composable
private fun ColorPalette(
    colors: List<Color>,
    selected: Int,
    onSelect: (Int) -> Unit,
) {
    Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
        colors.forEachIndexed { index, color ->
            val isSelected = selected == index
            Box(
                modifier = Modifier
                    .size(36.dp)
                    .clip(CircleShape)
                    .background(color)
                    .border(
                        width = if (isSelected) 3.dp else 0.dp,
                        color = BrandPurple,
                        shape = CircleShape,
                    )
                    .clickable { onSelect(index) },
                contentAlignment = Alignment.Center,
            ) {
                if (isSelected) {
                    Icon(
                        Icons.Outlined.Check,
                        contentDescription = null,
                        tint     = if (color.luminance() > 0.4f) Color(0xFF333333) else NavBarBg,
                        modifier = Modifier.size(16.dp),
                    )
                }
            }
        }
    }
}

// Función de utilidad para determinar luminancia
private fun Color.luminance(): Float {
    return 0.2126f * red + 0.7152f * green + 0.0722f * blue
}