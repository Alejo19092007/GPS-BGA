package me.edwarjimenez.gpsbgaconductor.ui.profile

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.ExitToApp
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.google.firebase.auth.FirebaseAuth

@Composable
fun PerfilScreen(
    onBackClick: () -> Unit,
    onLogout: () -> Unit,
    onEditarPerfil: () -> Unit = {},
    onMisRutas: () -> Unit = {},
    onHistorial: () -> Unit = {},
    onAyuda: () -> Unit = {}
) {
    val auth = remember { FirebaseAuth.getInstance() }
    val usuario = auth.currentUser

    var notificaciones by remember { mutableStateOf(true) }
    var modoNocturno by remember { mutableStateOf(false) }

    val bgDark = Color(0xFF0A0F1E)
    val bgCard = Color(0xFF0D1830)
    val bluePrimary = Color(0xFF0078FF)
    val cyanPrimary = Color(0xFF00C6FF)
    val blueMuted = Color(0xFF4A7FC0)
    val blueBorder = Color(0xFF1E2D5A)
    val textPrimary = Color(0xFFE0EEFF)
    val redPrimary = Color(0xFFFF3B57)
    val redBg = Color(0xFF3A0005)

    Surface(modifier = Modifier.fillMaxSize(), color = bgDark) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
        ) {
            Row(
                modifier = Modifier.fillMaxWidth().padding(16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(onClick = onBackClick, modifier = Modifier.size(48.dp)) {
                    Icon(Icons.AutoMirrored.Filled.ArrowBack, null, tint = cyanPrimary)
                }
                Text("Perfil / Ajustes", fontSize = 18.sp, fontWeight = FontWeight.Bold, color = textPrimary)
            }

            Column(
                modifier = Modifier.fillMaxWidth().padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Box(
                    modifier = Modifier
                        .size(72.dp)
                        .clip(RoundedCornerShape(50))
                        .background(bluePrimary.copy(alpha = 0.2f)),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = usuario?.email?.take(2)?.uppercase() ?: "CO",
                        fontSize = 24.sp,
                        fontWeight = FontWeight.Bold,
                        color = cyanPrimary
                    )
                }
                Spacer(modifier = Modifier.height(12.dp))
                Text(
                    text = usuario?.email?.substringBefore("@") ?: "Conductor",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = textPrimary
                )
                Text(text = usuario?.email ?: "", fontSize = 12.sp, color = blueMuted)
                Spacer(modifier = Modifier.height(4.dp))
                Row(
                    horizontalArrangement = Arrangement.spacedBy(4.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    repeat(5) { Text(text = "★", fontSize = 14.sp, color = Color(0xFFFFD700)) }
                    Text(text = "4.9 · 323 viajes", fontSize = 12.sp, color = blueMuted)
                }
            }

            HorizontalDivider(color = blueBorder, thickness = 0.5.dp, modifier = Modifier.padding(horizontal = 16.dp))
            Spacer(modifier = Modifier.height(8.dp))

            Column(modifier = Modifier.padding(horizontal = 16.dp)) {

                MenuItemPerfil(Icons.Default.Edit, "Editar Perfil", bgCard, blueMuted, textPrimary, onEditarPerfil)
                Spacer(modifier = Modifier.height(8.dp))
                MenuItemPerfil(Icons.Default.Map, "Mis Rutas", bgCard, blueMuted, textPrimary, onMisRutas)
                Spacer(modifier = Modifier.height(8.dp))
                MenuItemPerfil(Icons.Default.History, "Historial de Viajes", bgCard, blueMuted, textPrimary, onHistorial)
                Spacer(modifier = Modifier.height(8.dp))

                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(containerColor = bgCard),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth().padding(16.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Box(
                            modifier = Modifier.size(36.dp).clip(RoundedCornerShape(8.dp)).background(bgDark),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(Icons.Default.Notifications, null, tint = blueMuted, modifier = Modifier.size(18.dp))
                        }
                        Spacer(modifier = Modifier.width(12.dp))
                        Text("Notificaciones", fontSize = 14.sp, color = textPrimary, modifier = Modifier.weight(1f))
                        Switch(
                            checked = notificaciones,
                            onCheckedChange = { notificaciones = it },
                            colors = SwitchDefaults.colors(checkedThumbColor = Color.White, checkedTrackColor = bluePrimary)
                        )
                    }
                }

                Spacer(modifier = Modifier.height(8.dp))

                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(containerColor = bgCard),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth().padding(16.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Box(
                            modifier = Modifier.size(36.dp).clip(RoundedCornerShape(8.dp)).background(bgDark),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(Icons.Default.DarkMode, null, tint = blueMuted, modifier = Modifier.size(18.dp))
                        }
                        Spacer(modifier = Modifier.width(12.dp))
                        Text("Modo Nocturno", fontSize = 14.sp, color = textPrimary, modifier = Modifier.weight(1f))
                        Switch(
                            checked = modoNocturno,
                            onCheckedChange = { modoNocturno = it },
                            colors = SwitchDefaults.colors(checkedThumbColor = Color.White, checkedTrackColor = bluePrimary)
                        )
                    }
                }

                Spacer(modifier = Modifier.height(8.dp))
                MenuItemPerfil(Icons.Default.Help, "Ayuda / Soporte", bgCard, blueMuted, textPrimary, onAyuda)
                Spacer(modifier = Modifier.height(8.dp))

                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(containerColor = redBg),
                    shape = RoundedCornerShape(12.dp),
                    onClick = { auth.signOut(); onLogout() }
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth().padding(16.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(Icons.AutoMirrored.Filled.ExitToApp, null, tint = redPrimary, modifier = Modifier.size(20.dp))
                        Spacer(modifier = Modifier.width(12.dp))
                        Text("Cerrar Sesión", fontSize = 14.sp, fontWeight = FontWeight.Bold, color = redPrimary, modifier = Modifier.weight(1f))
                    }
                }

                Spacer(modifier = Modifier.height(40.dp))
            }
        }
    }
}

@Composable
fun MenuItemPerfil(
    icono: ImageVector,
    texto: String,
    bgCard: Color,
    blueMuted: Color,
    textPrimary: Color,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = bgCard),
        shape = RoundedCornerShape(12.dp),
        onClick = onClick
    ) {
        Row(
            modifier = Modifier.fillMaxWidth().padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier.size(36.dp).clip(RoundedCornerShape(8.dp)).background(Color(0xFF0A0F1E)),
                contentAlignment = Alignment.Center
            ) {
                Icon(icono, null, tint = blueMuted, modifier = Modifier.size(18.dp))
            }
            Spacer(modifier = Modifier.width(12.dp))
            Text(text = texto, fontSize = 14.sp, color = textPrimary, modifier = Modifier.weight(1f))
            Text(text = "›", fontSize = 18.sp, color = blueMuted)
        }
    }
}