package me.edwarjimenez.gpsbgaconductor.ui.dashboard

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.google.firebase.auth.FirebaseAuth

@Composable
fun DashboardScreen(
    onNavigateToMapa: () -> Unit,
    onNavigateToParadas: () -> Unit,
    onNavigateToPerfil: () -> Unit,
    onLogout: () -> Unit
) {
    val auth = remember { FirebaseAuth.getInstance() }
    val usuario = auth.currentUser

    var enServicio by remember { mutableStateOf(false) }

    val bgDark = Color(0xFF0A0F1E)
    val bgCard = Color(0xFF0D1830)
    val bluePrimary = Color(0xFF0078FF)
    val cyanPrimary = Color(0xFF00C6FF)
    val blueMuted = Color(0xFF4A7FC0)
    val blueBorder = Color(0xFF1E2D5A)
    val textPrimary = Color(0xFFE0EEFF)
    val textSecondary = Color(0xFFB0C4E8)
    val greenPrimary = Color(0xFF00C66B)
    val greenBg = Color(0xFF003A1A)
    val redPrimary = Color(0xFFFF3B57)
    val redBg = Color(0xFF3A0005)

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = bgDark
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(20.dp)
        ) {
            Spacer(modifier = Modifier.height(16.dp))

            // Header
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        text = "Buenos días 👋",
                        fontSize = 13.sp,
                        color = blueMuted
                    )
                    Text(
                        text = usuario?.email?.substringBefore("@") ?: "Conductor",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        color = textPrimary
                    )
                    Text(
                        text = "Conductor · Ruta Isabelar",
                        fontSize = 11.sp,
                        color = blueMuted
                    )
                }
                IconButton(onClick = { onNavigateToPerfil() }) {
                    Icon(
                        imageVector = Icons.Default.Settings,
                        contentDescription = null,
                        tint = blueMuted
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Estado del bus
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Estado del Bus",
                    fontSize = 13.sp,
                    color = textSecondary
                )
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(20.dp))
                        .background(if (enServicio) greenBg else redBg)
                        .padding(horizontal = 12.dp, vertical = 4.dp)
                ) {
                    Text(
                        text = if (enServicio) "EN SERVICIO" else "FUERA DE SERVICIO",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        color = if (enServicio) greenPrimary else redPrimary
                    )
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Ruta card
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = bgCard),
                shape = RoundedCornerShape(14.dp)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(14.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text(
                            text = "Ruta IGSABELAR",
                            fontSize = 15.sp,
                            fontWeight = FontWeight.Bold,
                            color = textPrimary
                        )
                        Text(
                            text = "La 200 · Floridablanca",
                            fontSize = 11.sp,
                            color = blueMuted
                        )
                    }
                    Text(text = "›", fontSize = 22.sp, color = bluePrimary)
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Estadísticas
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                EstadisticaCard(
                    label = "Pasajeros",
                    valor = "0",
                    bgCard = bgCard,
                    blueMuted = blueMuted,
                    cyanPrimary = cyanPrimary,
                    modifier = Modifier.weight(1f)
                )
                EstadisticaCard(
                    label = "Tiempo",
                    valor = "0 min",
                    bgCard = bgCard,
                    blueMuted = blueMuted,
                    cyanPrimary = cyanPrimary,
                    modifier = Modifier.weight(1f)
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                EstadisticaCard(
                    label = "Velocidad",
                    valor = "0 km/h",
                    bgCard = bgCard,
                    blueMuted = blueMuted,
                    cyanPrimary = cyanPrimary,
                    modifier = Modifier.weight(1f)
                )
                EstadisticaCard(
                    label = "Distancia",
                    valor = "0 km",
                    bgCard = bgCard,
                    blueMuted = blueMuted,
                    cyanPrimary = cyanPrimary,
                    modifier = Modifier.weight(1f)
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Botón entrar/finalizar
            Button(
                onClick = { enServicio = !enServicio },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(52.dp),
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = if (enServicio) redBg else greenBg
                ),
                border = androidx.compose.foundation.BorderStroke(
                    1.5.dp,
                    if (enServicio) redPrimary else greenPrimary
                )
            ) {
                Text(
                    text = if (enServicio) "Finalizar Ruta" else "Entrar En Servicio",
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold,
                    color = if (enServicio) redPrimary else greenPrimary
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Próxima parada
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = bgCard),
                shape = RoundedCornerShape(12.dp)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(12.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .size(10.dp)
                            .clip(RoundedCornerShape(50))
                            .background(bluePrimary)
                    )
                    Spacer(modifier = Modifier.width(10.dp))
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = "Próxima Parada",
                            fontSize = 10.sp,
                            color = blueMuted
                        )
                        Text(
                            text = "Centro Comercial Cañaveral",
                            fontSize = 13.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = textPrimary
                        )
                    }
                    Text(
                        text = "7 min",
                        fontSize = 12.sp,
                        color = greenPrimary,
                        fontWeight = FontWeight.Bold
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Botones de navegación
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Button(
                    onClick = { onNavigateToMapa() },
                    modifier = Modifier.weight(1f).height(46.dp),
                    shape = RoundedCornerShape(10.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = bgCard),
                    border = androidx.compose.foundation.BorderStroke(1.dp, blueBorder)
                ) {
                    Text(text = "Mapa", fontSize = 13.sp, color = textSecondary)
                }
                Button(
                    onClick = { onNavigateToParadas() },
                    modifier = Modifier.weight(1f).height(46.dp),
                    shape = RoundedCornerShape(10.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = bgCard),
                    border = androidx.compose.foundation.BorderStroke(1.dp, blueBorder)
                ) {
                    Text(text = "Paradas", fontSize = 13.sp, color = textSecondary)
                }
            }

            Spacer(modifier = Modifier.height(40.dp))
        }
    }
}

@Composable
fun EstadisticaCard(
    label: String,
    valor: String,
    bgCard: Color,
    blueMuted: Color,
    cyanPrimary: Color,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier,
        colors = CardDefaults.cardColors(containerColor = bgCard),
        shape = RoundedCornerShape(12.dp)
    ) {
        Column(
            modifier = Modifier.padding(12.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = label,
                fontSize = 10.sp,
                color = blueMuted
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = valor,
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = cyanPrimary
            )
        }
    }
}