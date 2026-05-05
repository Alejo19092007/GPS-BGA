package me.edwarjimenez.gpsbgaconductor.ui.stops

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import me.edwarjimenez.gpsbgaconductor.ui.map.rutasMapa

@Composable
fun ParadasScreen(
    onBackClick: () -> Unit,
    rutaCodigo: String = "36"
) {
    val bgDark = Color(0xFF0A0F1E)
    val bgCard = Color(0xFF0D1830)
    val bluePrimary = Color(0xFF0078FF)
    val cyanPrimary = Color(0xFF00C6FF)
    val blueMuted = Color(0xFF4A7FC0)
    val blueBorder = Color(0xFF1E2D5A)
    val textPrimary = Color(0xFFE0EEFF)
    val greenPrimary = Color(0xFF00C66B)
    val greenBg = Color(0xFF003A1A)
    val redPrimary = Color(0xFFFF3B57)
    val redBg = Color(0xFF3A0005)

    val rutaActual = rutasMapa.find { it.codigo == rutaCodigo } ?: rutasMapa[0]
    val db = remember { FirebaseDatabase.getInstance() }
    var paradaActual by remember { mutableStateOf(0) }

    LaunchedEffect(rutaCodigo) {
        db.getReference("buses")
            .addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    snapshot.children.firstOrNull { bus ->
                        bus.child("rutaId").getValue(String::class.java) == rutaCodigo
                    }?.let { bus ->
                        paradaActual = bus.child("paradaActual").getValue(Int::class.java) ?: 0
                    }
                }
                override fun onCancelled(error: DatabaseError) {}
            })
    }

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = bgDark
    ) {
        Column(modifier = Modifier.fillMaxSize()) {

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(
                    onClick = onBackClick,
                    modifier = Modifier.size(48.dp)
                ) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = "Volver",
                        tint = cyanPrimary
                    )
                }
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = "Paradas",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = textPrimary
                    )
                    Text(
                        text = "Ruta ${rutaActual.codigo} · ${rutaActual.nombre}",
                        fontSize = 11.sp,
                        color = blueMuted
                    )
                }
                Text(
                    text = "${rutaActual.paradas.size} paradas",
                    fontSize = 11.sp,
                    color = blueMuted
                )
            }

            HorizontalDivider(color = blueBorder, thickness = 0.5.dp)

            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                itemsIndexed(rutaActual.paradas) { index, (ubicacion, nombre) ->
                    val esInicio = index == 0
                    val esFinal = index == rutaActual.paradas.size - 1
                    val esActual = index == paradaActual
                    val esPasada = index < paradaActual

                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        colors = CardDefaults.cardColors(
                            containerColor = when {
                                esActual -> bluePrimary.copy(alpha = 0.15f)
                                esPasada -> bgCard.copy(alpha = 0.5f)
                                else -> bgCard
                            }
                        ),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(14.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(32.dp)
                                    .clip(RoundedCornerShape(50))
                                    .background(
                                        when {
                                            esInicio && esPasada -> greenBg.copy(alpha = 0.5f)
                                            esInicio -> greenBg
                                            esFinal -> redBg
                                            esActual -> bluePrimary
                                            esPasada -> bgDark.copy(alpha = 0.5f)
                                            else -> bgDark
                                        }
                                    ),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = when {
                                        esInicio -> "I"
                                        esFinal -> "F"
                                        esPasada -> "✓"
                                        else -> "${index + 1}"
                                    },
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = when {
                                        esInicio -> greenPrimary
                                        esFinal -> redPrimary
                                        esActual -> Color.White
                                        esPasada -> blueMuted.copy(alpha = 0.5f)
                                        else -> blueMuted
                                    }
                                )
                            }

                            Spacer(modifier = Modifier.width(12.dp))

                            Column(modifier = Modifier.weight(1f)) {
                                Text(
                                    text = nombre,
                                    fontSize = 14.sp,
                                    fontWeight = if (esActual) FontWeight.Bold else FontWeight.Normal,
                                    color = if (esPasada) textPrimary.copy(alpha = 0.4f) else textPrimary
                                )
                                Text(
                                    text = when {
                                        esInicio && esPasada -> "Superada"
                                        esInicio -> "Inicio de ruta"
                                        esFinal -> "Final de ruta"
                                        esActual -> "Parada actual"
                                        esPasada -> "Superada"
                                        else -> "Parada ${index + 1}"
                                    },
                                    fontSize = 11.sp,
                                    color = when {
                                        esInicio -> greenPrimary
                                        esFinal -> redPrimary
                                        esActual -> cyanPrimary
                                        esPasada -> blueMuted.copy(alpha = 0.4f)
                                        else -> blueMuted
                                    }
                                )
                            }

                            Text(
                                text = if (esPasada) "✓" else "${index * 5} min",
                                fontSize = 12.sp,
                                color = if (esPasada) greenPrimary.copy(alpha = 0.5f) else blueMuted,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                }
            }
        }
    }
}