package me.edwarjimenez.gpsbgaconductor.ui.profile

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun AyudaScreen(onBackClick: () -> Unit) {
    val bgDark = Color(0xFF0A0F1E)
    val bgCard = Color(0xFF0D1830)
    val cyanPrimary = Color(0xFF00C6FF)
    val blueMuted = Color(0xFF4A7FC0)
    val blueBorder = Color(0xFF1E2D5A)
    val textPrimary = Color(0xFFE0EEFF)
    val textSecondary = Color(0xFFB0C4E8)
    val bluePrimary = Color(0xFF0078FF)

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
                Text("Ayuda / Soporte", fontSize = 18.sp, fontWeight = FontWeight.Bold, color = textPrimary)
            }

            HorizontalDivider(color = blueBorder, thickness = 0.5.dp)

            Column(
                modifier = Modifier.fillMaxWidth().padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = "Centro de Ayuda",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color = cyanPrimary
                )

                Spacer(modifier = Modifier.height(8.dp))

                AyudaItem(
                    icono = Icons.Default.Phone,
                    titulo = "Llamar a soporte",
                    descripcion = "Línea de atención: 018000 123 456",
                    bgCard = bgCard,
                    blueMuted = blueMuted,
                    textPrimary = textPrimary,
                    textSecondary = textSecondary
                )

                AyudaItem(
                    icono = Icons.Default.Email,
                    titulo = "Correo electrónico",
                    descripcion = "soporte@gpsbga.com",
                    bgCard = bgCard,
                    blueMuted = blueMuted,
                    textPrimary = textPrimary,
                    textSecondary = textSecondary
                )

                AyudaItem(
                    icono = Icons.Default.Info,
                    titulo = "Versión de la app",
                    descripcion = "GPSBga Conductor v1.0-beta",
                    bgCard = bgCard,
                    blueMuted = blueMuted,
                    textPrimary = textPrimary,
                    textSecondary = textSecondary
                )

                AyudaItem(
                    icono = Icons.Default.LocationOn,
                    titulo = "Empresa",
                    descripcion = "COTRANDER · Bucaramanga, Colombia",
                    bgCard = bgCard,
                    blueMuted = blueMuted,
                    textPrimary = textPrimary,
                    textSecondary = textSecondary
                )

                Spacer(modifier = Modifier.height(16.dp))

                Text(
                    text = "Preguntas Frecuentes",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color = cyanPrimary
                )

                Spacer(modifier = Modifier.height(8.dp))

                listOf(
                    "¿Cómo activo el GPS?" to "Toca el botón 'Entrar En Servicio' en el Dashboard.",
                    "¿Cómo cambio de ruta?" to "Usa el dropdown de rutas en el Dashboard antes de entrar en servicio.",
                    "¿Cómo ven los pasajeros mi ubicación?" to "Automáticamente cuando estás En Servicio, tu ubicación se comparte en tiempo real.",
                    "¿Qué pasa si pierdo la señal?" to "La app guarda el último punto conocido y se sincroniza cuando recuperes la conexión."
                ).forEach { (pregunta, respuesta) ->
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        colors = CardDefaults.cardColors(containerColor = bgCard),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Column(modifier = Modifier.fillMaxWidth().padding(16.dp)) {
                            Text(
                                text = pregunta,
                                fontSize = 13.sp,
                                fontWeight = FontWeight.Bold,
                                color = textPrimary
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(text = respuesta, fontSize = 12.sp, color = blueMuted)
                        }
                    }
                    Spacer(modifier = Modifier.height(4.dp))
                }

                Spacer(modifier = Modifier.height(40.dp))
            }
        }
    }
}

@Composable
fun AyudaItem(
    icono: ImageVector,
    titulo: String,
    descripcion: String,
    bgCard: Color,
    blueMuted: Color,
    textPrimary: Color,
    textSecondary: Color
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = bgCard),
        shape = RoundedCornerShape(12.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth().padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(icono, null, tint = blueMuted, modifier = Modifier.size(20.dp))
            Spacer(modifier = Modifier.width(12.dp))
            Column {
                Text(text = titulo, fontSize = 13.sp, fontWeight = FontWeight.Bold, color = textPrimary)
                Text(text = descripcion, fontSize = 12.sp, color = blueMuted)
            }
        }
    }
}