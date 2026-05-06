package me.edwarjimenez.gpsbgaconductor.ui.profile

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun HistorialScreen(onBackClick: () -> Unit) {
    val bgDark = Color(0xFF0A0F1E)
    val bgCard = Color(0xFF0D1830)
    val cyanPrimary = Color(0xFF00C6FF)
    val blueMuted = Color(0xFF4A7FC0)
    val blueBorder = Color(0xFF1E2D5A)
    val textPrimary = Color(0xFFE0EEFF)
    val greenPrimary = Color(0xFF00C66B)

    val viajes = listOf(
        listOf("Hoy 08:30", "Ruta 36 · Igsabelar", "24 km · 45 min"),
        listOf("Hoy 06:00", "Ruta 7 · Limoncito", "23 km · 40 min"),
        listOf("Ayer 14:00", "Ruta 27 · Caracolí", "27 km · 55 min"),
        listOf("Ayer 08:30", "Ruta 36 · Igsabelar", "24 km · 42 min"),
        listOf("04/05 07:00", "Ruta 7 · Limoncito", "23 km · 38 min")
    )

    Surface(modifier = Modifier.fillMaxSize(), color = bgDark) {
        Column(modifier = Modifier.fillMaxSize()) {
            Row(
                modifier = Modifier.fillMaxWidth().padding(16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(onClick = onBackClick, modifier = Modifier.size(48.dp)) {
                    Icon(Icons.AutoMirrored.Filled.ArrowBack, null, tint = cyanPrimary)
                }
                Column {
                    Text("Historial de Viajes", fontSize = 18.sp, fontWeight = FontWeight.Bold, color = textPrimary)
                    Text("${viajes.size} viajes realizados", fontSize = 11.sp, color = blueMuted)
                }
            }

            HorizontalDivider(color = blueBorder, thickness = 0.5.dp)

            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(viajes) { viaje ->
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        colors = CardDefaults.cardColors(containerColor = bgCard),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth().padding(16.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column(modifier = Modifier.weight(1f)) {
                                Text(
                                    text = viaje[1],
                                    fontSize = 14.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = textPrimary
                                )
                                Text(text = viaje[2], fontSize = 11.sp, color = blueMuted)
                            }
                            Text(
                                text = viaje[0],
                                fontSize = 11.sp,
                                color = greenPrimary,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                }
            }
        }
    }
}