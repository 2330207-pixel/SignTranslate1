package com.example.signtranslate.ui.screens

import androidx.compose.animation.Crossfade
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.ContentCopy
import androidx.compose.material.icons.outlined.Info
import androidx.compose.material.icons.outlined.VolumeUp
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
// Asegúrate de que este import coincida con tu paquete de colores
import com.example.signtranslate.ui.theme.* @OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TranslatorScreen() {
    var selectedTab by remember { mutableIntStateOf(0) }

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text("Traductor", fontSize = 18.sp, fontWeight = FontWeight.Bold) },
                actions = { IconButton(onClick = {}) { Icon(Icons.Outlined.Info, null) } },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(containerColor = Color.White)
            )
        },
        bottomBar = { SimpleBottomNav() }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .background(Color(0xFFF2F2F7))
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

@Composable
fun HeaderTabs(selectedTab: Int, onTabSelected: (Int) -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color.White)
            .padding(horizontal = 16.dp, vertical = 12.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        val tabs = listOf(
            Triple("Señas → Texto", Icons.Default.PanTool, 0),
            Triple("Voz → LSM", Icons.Default.Mic, 1),
            Triple("Texto → LSM", Icons.Default.Keyboard, 2)
        )
        tabs.forEach { (label, icon, index) ->
            val isSelected = selectedTab == index
            Surface(
                modifier = Modifier
                    .weight(1f)
                    .height(40.dp)
                    .clip(RoundedCornerShape(12.dp))
                    .clickable { onTabSelected(index) },
                shape = RoundedCornerShape(12.dp),
                color = if (isSelected) BrandPurple else Color.White,
                border = if (!isSelected) BorderStroke(1.dp, Color(0xFFE5E5EA)) else null
            ) {
                Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.Center) {
                    Icon(icon, null, tint = if (isSelected) Color.White else Color.Gray, modifier = Modifier.size(16.dp))
                    Spacer(Modifier.width(4.dp))
                    Text(label, fontSize = 9.sp, fontWeight = FontWeight.Bold, color = if (isSelected) Color.White else Color.Gray)
                }
            }
        }
    }
}

@Composable
fun SignToTextTab() {
    Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        Box(modifier = Modifier.fillMaxWidth().height(320.dp).clip(RoundedCornerShape(24.dp)).background(Color.DarkGray)) {
            Box(Modifier.fillMaxSize().padding(16.dp).border(2.dp, Color(0xFF34C759), RoundedCornerShape(20.dp)))
            Surface(Modifier.padding(24.dp), color = Color.Black.copy(0.6f), shape = RoundedCornerShape(20.dp)) {
                Row(Modifier.padding(horizontal = 10.dp, vertical = 5.dp), verticalAlignment = Alignment.CenterVertically) {
                    Box(Modifier.size(8.dp).background(Color(0xFF34C759), CircleShape))
                    Spacer(Modifier.width(8.dp))
                    Text("Cámara activa", color = Color.White, fontSize = 11.sp)
                }
            }
        }
        Spacer(Modifier.height(16.dp))
        Card(
            shape = RoundedCornerShape(24.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White),
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(Modifier.padding(20.dp)) {
                Text("Seña detectada", color = BrandPurple, fontSize = 12.sp, fontWeight = FontWeight.Bold)
                Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
                    Text("HOLA", fontSize = 32.sp, fontWeight = FontWeight.Black)
                    IconButton(onClick = {}, modifier = Modifier.background(BrandPurpleLight, CircleShape)) {
                        Icon(Icons.Outlined.VolumeUp, null, tint = BrandPurple)
                    }
                }
                Text("Confianza: 96%", fontSize = 12.sp, color = Color.Gray)
                LinearProgressIndicator(progress = { 0.96f }, modifier = Modifier.fillMaxWidth().padding(vertical = 12.dp).height(6.dp).clip(CircleShape), color = BrandPurple, trackColor = BrandPurpleLight)
                Divider(color = Color(0xFFF2F2F7))
                Spacer(Modifier.height(12.dp))
                Text("Frase formada", color = BrandPurple, fontSize = 12.sp, fontWeight = FontWeight.Bold)
                Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                    Text("Hola, ¿cómo estás?", fontSize = 18.sp, fontWeight = FontWeight.Bold)
                    Icon(Icons.Outlined.ContentCopy, null, tint = BrandPurple, modifier = Modifier.size(20.dp))
                }
            }
        }
    }
}

@Composable
fun VoiceToLsmTab() {
    Column(modifier = Modifier.fillMaxSize()) {
        Box(modifier = Modifier.fillMaxWidth().weight(1.3f).background(Brush.verticalGradient(listOf(Color(0xFFE8EAF6), Color(0xFFD1D9FF)))), contentAlignment = Alignment.Center) {
            Text("👤", fontSize = 180.sp)
        }
        Surface(modifier = Modifier.fillMaxWidth().weight(1f), color = Color.White, shape = RoundedCornerShape(topStart = 32.dp, topEnd = 32.dp)) {
            Column(Modifier.padding(24.dp), horizontalAlignment = Alignment.CenterHorizontally) {
                Text("Texto detectado", color = BrandPurple, fontSize = 13.sp, fontWeight = FontWeight.Bold, modifier = Modifier.align(Alignment.Start))
                Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                    Text("Buenos días", fontSize = 28.sp, fontWeight = FontWeight.Bold)
                    Icon(Icons.Outlined.VolumeUp, null, tint = Color.Gray)
                }
                Spacer(modifier = Modifier.weight(1f))
                Row(verticalAlignment = Alignment.CenterVertically) {
                    WaveFormBars(); Spacer(Modifier.width(20.dp))
                    FloatingActionButton(onClick = {}, containerColor = BrandPurple, shape = CircleShape, modifier = Modifier.size(72.dp)) {
                        Icon(Icons.Default.Mic, null, tint = Color.White)
                    }
                    Spacer(Modifier.width(20.dp)); WaveFormBars()
                }
                Text("Presiona para hablar", color = Color.Gray, fontSize = 14.sp, modifier = Modifier.padding(top = 12.dp))
            }
        }
    }
}

@Composable
fun TextToLsmTab() {
    Column(modifier = Modifier.fillMaxSize()) {
        Box(modifier = Modifier.fillMaxWidth().weight(1.3f).background(Brush.verticalGradient(listOf(Color(0xFFE8EAF6), Color(0xFFD1D9FF)))), contentAlignment = Alignment.Center) {
            Text("👤", fontSize = 180.sp)
        }
        Surface(modifier = Modifier.fillMaxWidth().weight(1f), color = Color.White, shape = RoundedCornerShape(topStart = 32.dp, topEnd = 32.dp)) {
            Column(Modifier.padding(24.dp)) {
                Text("Escribe el texto que deseas traducir a LSM", color = BrandPurple, fontSize = 13.sp, fontWeight = FontWeight.Bold)
                Spacer(Modifier.height(16.dp))
                OutlinedTextField(
                    value = "", onValueChange = {},
                    placeholder = { Text("Escribe aquí...", color = Color.LightGray) },
                    modifier = Modifier.fillMaxWidth().height(140.dp),
                    shape = RoundedCornerShape(16.dp),
                    colors = OutlinedTextFieldDefaults.colors(unfocusedBorderColor = Color(0xFFE5E5EA))
                )
                Text("0/200", color = Color.Gray, fontSize = 12.sp, modifier = Modifier.align(Alignment.End))
            }
        }
    }
}

@Composable
fun WaveFormBars() {
    Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(3.dp)) {
        val heights = listOf(8, 14, 10, 18, 12, 16)
        heights.forEach { h ->
            Box(Modifier.width(2.dp).height(h.dp).background(BrandPurple.copy(alpha = 0.3f), CircleShape))
        }
    }
}

@Composable
fun SimpleBottomNav() {
    NavigationBar(containerColor = Color.White) {
        NavigationBarItem(selected = true, onClick = {}, icon = { Icon(Icons.Default.Translate, null) }, label = { Text("Traductor") })
        NavigationBarItem(selected = false, onClick = {}, icon = { Icon(Icons.Default.Face, null) }, label = { Text("Avatar") })
        NavigationBarItem(selected = false, onClick = {}, icon = { Icon(Icons.Default.Settings, null) }, label = { Text("Ajustes") })
    }
}