package com.example.signtranslate.ui.screens

import androidx.compose.animation.Crossfade
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Keyboard
import androidx.compose.material.icons.filled.Mic
import androidx.compose.material.icons.filled.PanTool
import androidx.compose.material.icons.outlined.ContentCopy
import androidx.compose.material.icons.outlined.Info
import androidx.compose.material.icons.outlined.VolumeUp
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.signtranslate.ui.theme.BrandPurple
import com.example.signtranslate.ui.theme.BrandPurpleLight
import com.example.signtranslate.ui.theme.BrandPurpleMid
import com.example.signtranslate.ui.theme.CardBg
import com.example.signtranslate.ui.theme.DividerColor
import com.example.signtranslate.ui.theme.NavBarBg
import com.example.signtranslate.ui.theme.SuccessGreen
import com.example.signtranslate.ui.theme.TextBody
import com.example.signtranslate.ui.theme.TextDark
import com.example.signtranslate.ui.theme.TextHint

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TranslatorScreen(innerPadding: PaddingValues = PaddingValues()) {
    var selectedTab by remember { mutableIntStateOf(0) }

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        text       = "Traductor",
                        fontSize   = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color      = TextDark,
                    )
                },
                actions = {
                    IconButton(onClick = {}) {
                        Icon(Icons.Outlined.Info, contentDescription = null, tint = TextBody)
                    }
                },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                    containerColor = NavBarBg,
                )
            )
        },
    ) { scaffoldPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(scaffoldPadding)
                .padding(bottom = innerPadding.calculateBottomPadding())
                .background(CardBg)
        ) {
            HeaderTabs(selectedTab) { selectedTab = it }

            Crossfade(targetState = selectedTab, label = "tab_transition") { tab ->
                when (tab) {
                    0 -> SignToTextTab()
                    1 -> VoiceToLsmTab()
                    2 -> TextToLsmTab()
                }
            }
        }
    }
}

// ─────────────────────────────────────────────────────────────────────────────
// TABS DE CABECERA
// ─────────────────────────────────────────────────────────────────────────────
@Composable
fun HeaderTabs(selectedTab: Int, onTabSelected: (Int) -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(NavBarBg)
            .padding(horizontal = 16.dp, vertical = 12.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        val tabs = listOf(
            Triple("Señas → Texto", Icons.Default.PanTool, 0),
            Triple("Voz → LSM",     Icons.Default.Mic,     1),
            Triple("Texto → LSM",   Icons.Default.Keyboard, 2),
        )
        tabs.forEach { (label, icon, index) ->
            val isSelected = selectedTab == index
            Surface(
                modifier = Modifier
                    .weight(1f)
                    .height(40.dp)
                    .clip(RoundedCornerShape(12.dp))
                    .clickable { onTabSelected(index) },
                shape  = RoundedCornerShape(12.dp),
                color  = if (isSelected) BrandPurple else NavBarBg,
                border = if (!isSelected) BorderStroke(1.dp, DividerColor) else null,
            ) {
                Row(
                    verticalAlignment     = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center,
                ) {
                    Icon(
                        imageVector = icon,
                        contentDescription = null,
                        tint     = if (isSelected) NavBarBg else TextBody,
                        modifier = Modifier.size(16.dp),
                    )
                    Spacer(Modifier.width(4.dp))
                    Text(
                        text       = label,
                        fontSize   = 9.sp,
                        fontWeight = FontWeight.Bold,
                        color      = if (isSelected) NavBarBg else TextBody,
                    )
                }
            }
        }
    }
}

// ─────────────────────────────────────────────────────────────────────────────
// TAB 1 — SEÑAS A TEXTO
// ─────────────────────────────────────────────────────────────────────────────
@Composable
fun SignToTextTab() {
    Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(320.dp)
                .clip(RoundedCornerShape(24.dp))
                .background(Color.DarkGray)
        ) {
            Box(
                Modifier
                    .fillMaxSize()
                    .padding(16.dp)
                    .border(2.dp, SuccessGreen, RoundedCornerShape(20.dp))
            )
            Surface(
                modifier = Modifier.padding(24.dp),
                color    = Color.Black.copy(alpha = 0.6f),
                shape    = RoundedCornerShape(20.dp),
            ) {
                Row(
                    modifier          = Modifier.padding(horizontal = 10.dp, vertical = 5.dp),
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Box(Modifier.size(8.dp).background(SuccessGreen, CircleShape))
                    Spacer(Modifier.width(8.dp))
                    Text("Cámara activa", color = NavBarBg, fontSize = 11.sp)
                }
            }
        }

        Spacer(Modifier.height(16.dp))

        Card(
            shape  = RoundedCornerShape(24.dp),
            colors = CardDefaults.cardColors(containerColor = NavBarBg),
            modifier = Modifier.fillMaxWidth(),
        ) {
            Column(Modifier.padding(20.dp)) {
                Text("Traducción", color = BrandPurple, fontSize = 12.sp, fontWeight = FontWeight.Bold)
                Row(
                    modifier              = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment     = Alignment.CenterVertically,
                ) {
                    Text("Hola", fontSize = 32.sp, fontWeight = FontWeight.Black, color = TextDark)
                    IconButton(
                        onClick  = {},
                        modifier = Modifier.background(BrandPurpleLight, CircleShape),
                    ) {
                        Icon(Icons.Outlined.VolumeUp, null, tint = BrandPurple)
                    }
                }
                Text("96% de confianza", fontSize = 12.sp, color = TextBody)
                LinearProgressIndicator(
                    progress     = { 0.96f },
                    modifier     = Modifier.fillMaxWidth().padding(vertical = 12.dp).height(6.dp).clip(CircleShape),
                    color        = BrandPurple,
                    trackColor   = BrandPurpleLight,
                )
                HorizontalDivider(color = DividerColor)
                Spacer(Modifier.height(12.dp))
                Text("Última seña", color = BrandPurple, fontSize = 12.sp, fontWeight = FontWeight.Bold)
                Row(
                    modifier              = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                ) {
                    Text("👋 Saludo", fontSize = 18.sp, fontWeight = FontWeight.Bold, color = TextDark)
                    Icon(Icons.Outlined.ContentCopy, null, tint = BrandPurple, modifier = Modifier.size(20.dp))
                }
            }
        }
    }
}

// ─────────────────────────────────────────────────────────────────────────────
// TAB 2 — VOZ A LSM
// ─────────────────────────────────────────────────────────────────────────────
@Composable
fun VoiceToLsmTab() {
    Column(modifier = Modifier.fillMaxSize()) {
        Box(
            modifier         = Modifier
                .fillMaxWidth()
                .weight(1.3f)
                .background(
                    Brush.verticalGradient(listOf(BrandPurpleLight, BrandPurpleMid))
                ),
            contentAlignment = Alignment.Center,
        ) {
            Text("👤", fontSize = 180.sp)
        }
        Surface(
            modifier = Modifier.fillMaxWidth().weight(1f),
            color    = NavBarBg,
            shape    = RoundedCornerShape(topStart = 32.dp, topEnd = 32.dp),
        ) {
            Column(
                modifier            = Modifier.padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                Text(
                    text       = "Traduciendo voz",
                    color      = BrandPurple,
                    fontSize   = 13.sp,
                    fontWeight = FontWeight.Bold,
                    modifier   = Modifier.align(Alignment.Start),
                )
                Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                    Text("Habla ahora", fontSize = 28.sp, fontWeight = FontWeight.Bold, color = TextDark)
                    Icon(Icons.Outlined.VolumeUp, null, tint = TextBody)
                }
                Spacer(modifier = Modifier.weight(1f))
                Row(verticalAlignment = Alignment.CenterVertically) {
                    WaveFormBars()
                    Spacer(Modifier.width(20.dp))
                    FloatingActionButton(
                        onClick        = {},
                        containerColor = BrandPurple,
                        shape          = CircleShape,
                        modifier       = Modifier.size(72.dp),
                    ) {
                        Icon(Icons.Default.Mic, null, tint = NavBarBg)
                    }
                    Spacer(Modifier.width(20.dp))
                    WaveFormBars()
                }
                Text(
                    text     = "Presiona para hablar",
                    color    = TextBody,
                    fontSize = 14.sp,
                    modifier = Modifier.padding(top = 12.dp),
                )
            }
        }
    }
}

// ─────────────────────────────────────────────────────────────────────────────
// TAB 3 — TEXTO A LSM
// ─────────────────────────────────────────────────────────────────────────────
@Composable
fun TextToLsmTab() {
    Column(modifier = Modifier.fillMaxSize()) {
        Box(
            modifier         = Modifier
                .fillMaxWidth()
                .weight(1.3f)
                .background(
                    Brush.verticalGradient(listOf(BrandPurpleLight, BrandPurpleMid))
                ),
            contentAlignment = Alignment.Center,
        ) {
            Text("👤", fontSize = 180.sp)
        }
        Surface(
            modifier = Modifier.fillMaxWidth().weight(1f),
            color    = NavBarBg,
            shape    = RoundedCornerShape(topStart = 32.dp, topEnd = 32.dp),
        ) {
            Column(Modifier.padding(24.dp)) {
                Text(
                    text       = "Escribe el texto que deseas traducir a LSM",
                    color      = BrandPurple,
                    fontSize   = 13.sp,
                    fontWeight = FontWeight.Bold,
                )
                Spacer(Modifier.height(16.dp))
                OutlinedTextField(
                    value          = "",
                    onValueChange  = {},
                    placeholder    = { Text("Escribe aquí...", color = TextHint) },
                    modifier       = Modifier.fillMaxWidth().height(140.dp),
                    shape          = RoundedCornerShape(16.dp),
                    colors         = OutlinedTextFieldDefaults.colors(
                        unfocusedBorderColor = DividerColor,
                        focusedBorderColor   = BrandPurple,
                    ),
                )
                Text(
                    text     = "0/200",
                    color    = TextBody,
                    fontSize = 12.sp,
                    modifier = Modifier.align(Alignment.End),
                )
            }
        }
    }
}

// ─────────────────────────────────────────────────────────────────────────────
// WAVEFORM BARS
// ─────────────────────────────────────────────────────────────────────────────
@Composable
fun WaveFormBars() {
    Row(
        verticalAlignment     = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(3.dp),
    ) {
        listOf(8, 14, 10, 18, 12, 16).forEach { h ->
            Box(
                Modifier
                    .width(2.dp)
                    .height(h.dp)
                    .background(BrandPurple.copy(alpha = 0.3f), CircleShape)
            )
        }
    }
}