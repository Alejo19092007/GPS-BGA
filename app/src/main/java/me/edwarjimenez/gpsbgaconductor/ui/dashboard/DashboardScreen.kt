package me.edwarjimenez.gpsbgaconductor.ui.dashboard

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.app.NotificationCompat
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import me.edwarjimenez.gpsbgaconductor.service.GpsTrackingService

data class RutaBga(
    val codigo: String,
    val nombre: String,
    val terminal: String,
    val empresa: String,
    val longitud: String,
    val paradas: List<String>
)

fun enviarNotificacionParada(context: Context, nombreParada: String) {
    val channelId = "paradas_channel"
    val manager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
        val canal = NotificationChannel(channelId, "Paradas", NotificationManager.IMPORTANCE_HIGH)
        manager.createNotificationChannel(canal)
    }
    val notif = NotificationCompat.Builder(context, channelId)
        .setContentTitle("🚌 Llegando a parada")
        .setContentText("Próxima parada: $nombreParada")
        .setSmallIcon(android.R.drawable.ic_menu_mylocation)
        .setPriority(NotificationCompat.PRIORITY_HIGH)
        .setAutoCancel(true)
        .build()
    manager.notify(System.currentTimeMillis().toInt(), notif)
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DashboardScreen(
    onNavigateToMapa: (String) -> Unit,
    onNavigateToParadas: (String) -> Unit,
    onNavigateToPerfil: () -> Unit,
    onLogout: () -> Unit
) {
    val auth = remember { FirebaseAuth.getInstance() }
    val usuario = auth.currentUser
    val context = LocalContext.current
    val db = remember { FirebaseDatabase.getInstance() }
    val busId = auth.currentUser?.uid ?: "bus_001"

    var enServicio by remember { mutableStateOf(false) }
    var dropdownExpanded by remember { mutableStateOf(false) }
    var pasajeros by remember { mutableStateOf(0) }
    var velocidad by remember { mutableStateOf("0 km/h") }
    var distancia by remember { mutableStateOf("0 km") }
    var tiempo by remember { mutableStateOf("0 min") }
    var paradaActualIndex by remember { mutableStateOf(0) }
    var ultimaParadaNotificada by remember { mutableStateOf(-1) }

    val rutas = listOf(
        RutaBga(
            codigo = "7",
            nombre = "Limoncito",
            terminal = "Los Cauchos - Estadio",
            empresa = "COTRANDER",
            longitud = "23 km",
            paradas = listOf(
                "Terminal Los Cauchos", "Servientrega", "Carrera 8",
                "Paragüitas", "Bucarica", "Transversal Oriental",
                "CC Cacique", "Megamall", "Plaza Guarín",
                "Autopista Floridablanca", "Retorno Plata Acero"
            )
        ),
        RutaBga(
            codigo = "36",
            nombre = "Igsabelar 33",
            terminal = "González Chaparro",
            empresa = "COTRANDER",
            longitud = "24 km",
            paradas = listOf(
                "González Chaparro", "Barrio La Paz", "Papi Quiero Piña",
                "Miradores San Lorenzo", "CC Cacique", "Viaducto La Flora",
                "Carrera 33", "Megamall", "Plaza Guarín",
                "Plaza Satélite", "Puente Provenza", "Autopista Cañaveral"
            )
        ),
        RutaBga(
            codigo = "27",
            nombre = "Caracolí - Centro",
            terminal = "Caracolí",
            empresa = "LUSITANIA S.A.",
            longitud = "27 km",
            paradas = listOf(
                "Terminal Caracolí", "Bucarica", "Bellavista",
                "Carretera Antigua", "Viaducto La Flora", "Carrera 33",
                "Calle 34", "Centro - Carrera 10", "Carrera 13", "Cacique Monterrey"
            )
        )
    )

    var rutaSeleccionada by remember { mutableStateOf(rutas[0]) }

    // Escuchar Firebase en tiempo real
    LaunchedEffect(enServicio) {
        if (enServicio) {
            db.getReference("buses").child(busId)
                .addValueEventListener(object : ValueEventListener {
                    override fun onDataChange(snapshot: DataSnapshot) {
                        val vel = snapshot.child("velocidad").getValue(Double::class.java)?.toInt() ?: 0
                        val dist = snapshot.child("distanciaRecorrida").getValue(String::class.java) ?: "0"
                        val t = snapshot.child("tiempoEnRuta").getValue(Long::class.java) ?: 0L
                        val parada = snapshot.child("paradaActual").getValue(Int::class.java) ?: 0
                        velocidad = "$vel km/h"
                        distancia = "$dist km"
                        tiempo = "${t / 60} min"
                        paradaActualIndex = parada

                        // Notificación cuando cambia de parada
                        if (parada != ultimaParadaNotificada && parada < rutaSeleccionada.paradas.size) {
                            ultimaParadaNotificada = parada
                            enviarNotificacionParada(context, rutaSeleccionada.paradas[parada])
                        }
                    }
                    override fun onCancelled(error: DatabaseError) {}
                })
        }
    }

    val proximaParada = if (paradaActualIndex < rutaSeleccionada.paradas.size)
        rutaSeleccionada.paradas[paradaActualIndex] else rutaSeleccionada.paradas.last()

    // Tiempo estimado a próxima parada
    val tiempoEstimado = if (enServicio) "${(paradaActualIndex + 1) * 5} min" else "--"

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

    Surface(modifier = Modifier.fillMaxSize(), color = bgDark) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(20.dp)
        ) {
            Spacer(modifier = Modifier.height(16.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(text = "Buenos días 👋", fontSize = 13.sp, color = blueMuted)
                    Text(
                        text = usuario?.email?.substringBefore("@") ?: "Conductor",
                        fontSize = 16.sp, fontWeight = FontWeight.Bold, color = textPrimary
                    )
                    Text(text = "Conductor · ${rutaSeleccionada.nombre}", fontSize = 11.sp, color = blueMuted)
                }
                IconButton(onClick = { onNavigateToPerfil() }) {
                    Icon(Icons.Default.Settings, contentDescription = null, tint = blueMuted)
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(text = "Estado del Bus", fontSize = 13.sp, color = textSecondary)
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(20.dp))
                        .background(if (enServicio) greenBg else redBg)
                        .padding(horizontal = 12.dp, vertical = 4.dp)
                ) {
                    Text(
                        text = if (enServicio) "EN SERVICIO" else "FUERA DE SERVICIO",
                        fontSize = 11.sp, fontWeight = FontWeight.Bold,
                        color = if (enServicio) greenPrimary else redPrimary
                    )
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            ExposedDropdownMenuBox(
                expanded = dropdownExpanded,
                onExpandedChange = { dropdownExpanded = !dropdownExpanded }
            ) {
                Card(
                    modifier = Modifier.fillMaxWidth().menuAnchor(),
                    colors = CardDefaults.cardColors(containerColor = bgCard),
                    shape = RoundedCornerShape(14.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth().padding(14.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column {
                            Text(
                                text = "Ruta ${rutaSeleccionada.codigo} · ${rutaSeleccionada.nombre}",
                                fontSize = 15.sp, fontWeight = FontWeight.Bold, color = textPrimary
                            )
                            Text(
                                text = "${rutaSeleccionada.terminal} · ${rutaSeleccionada.longitud}",
                                fontSize = 11.sp, color = blueMuted
                            )
                        }
                        Icon(Icons.Default.KeyboardArrowDown, contentDescription = null, tint = bluePrimary)
                    }
                }

                ExposedDropdownMenu(
                    expanded = dropdownExpanded,
                    onDismissRequest = { dropdownExpanded = false },
                    modifier = Modifier.background(bgCard)
                ) {
                    rutas.forEach { ruta ->
                        DropdownMenuItem(
                            text = {
                                Column {
                                    Text(
                                        text = "Ruta ${ruta.codigo} · ${ruta.nombre}",
                                        fontSize = 14.sp, fontWeight = FontWeight.Bold, color = textPrimary
                                    )
                                    Text(text = "${ruta.empresa} · ${ruta.longitud}", fontSize = 11.sp, color = blueMuted)
                                }
                            },
                            onClick = {
                                if (enServicio) {
                                    val intentDetener = Intent(context, GpsTrackingService::class.java).apply {
                                        action = GpsTrackingService.ACTION_DETENER
                                    }
                                    context.startService(intentDetener)
                                    val intentIniciar = Intent(context, GpsTrackingService::class.java).apply {
                                        action = GpsTrackingService.ACTION_INICIAR
                                        putExtra(GpsTrackingService.EXTRA_BUS_ID, busId)
                                        putExtra(GpsTrackingService.EXTRA_RUTA_CODIGO, ruta.codigo)
                                    }
                                    context.startForegroundService(intentIniciar)
                                }
                                rutaSeleccionada = ruta
                                paradaActualIndex = 0
                                ultimaParadaNotificada = -1
                                dropdownExpanded = false
                            },
                            modifier = Modifier.background(
                                if (ruta == rutaSeleccionada) bluePrimary.copy(alpha = 0.1f) else Color.Transparent
                            )
                        )
                        if (ruta != rutas.last()) HorizontalDivider(color = blueBorder, thickness = 0.5.dp)
                    }
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Estadísticas — Pasajeros con contador
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                // Card Pasajeros con + y -
                Card(
                    modifier = Modifier.weight(1f),
                    colors = CardDefaults.cardColors(containerColor = bgCard),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Column(
                        modifier = Modifier.padding(12.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(text = "Pasajeros", fontSize = 10.sp, color = blueMuted)
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = if (enServicio) "$pasajeros" else "--",
                            fontSize = 18.sp, fontWeight = FontWeight.Bold, color = cyanPrimary
                        )
                        if (enServicio) {
                            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                                TextButton(
                                    onClick = { if (pasajeros > 0) pasajeros-- },
                                    modifier = Modifier.size(32.dp),
                                    contentPadding = PaddingValues(0.dp)
                                ) {
                                    Text("-", fontSize = 20.sp, color = redPrimary, fontWeight = FontWeight.Bold)
                                }
                                TextButton(
                                    onClick = { pasajeros++ },
                                    modifier = Modifier.size(32.dp),
                                    contentPadding = PaddingValues(0.dp)
                                ) {
                                    Text("+", fontSize = 20.sp, color = greenPrimary, fontWeight = FontWeight.Bold)
                                }
                            }
                        }
                    }
                }

                EstadisticaCard("Tiempo", if (enServicio) tiempo else "--", bgCard, blueMuted, cyanPrimary, Modifier.weight(1f))
            }

            Spacer(modifier = Modifier.height(8.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                EstadisticaCard("Velocidad", if (enServicio) velocidad else "--", bgCard, blueMuted, cyanPrimary, Modifier.weight(1f))
                EstadisticaCard("Distancia", if (enServicio) distancia else "--", bgCard, blueMuted, cyanPrimary, Modifier.weight(1f))
            }

            Spacer(modifier = Modifier.height(16.dp))

            Button(
                onClick = {
                    val intent = Intent(context, GpsTrackingService::class.java).apply {
                        action = if (enServicio) GpsTrackingService.ACTION_DETENER else GpsTrackingService.ACTION_INICIAR
                        putExtra(GpsTrackingService.EXTRA_BUS_ID, busId)
                        putExtra(GpsTrackingService.EXTRA_RUTA_CODIGO, rutaSeleccionada.codigo)
                    }
                    if (!enServicio) {
                        context.startForegroundService(intent)
                        pasajeros = 0
                        paradaActualIndex = 0
                        ultimaParadaNotificada = -1
                    } else {
                        context.startService(intent)
                    }
                    enServicio = !enServicio
                },
                modifier = Modifier.fillMaxWidth().height(52.dp),
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(containerColor = if (enServicio) redBg else greenBg),
                border = androidx.compose.foundation.BorderStroke(1.5.dp, if (enServicio) redPrimary else greenPrimary)
            ) {
                Text(
                    text = if (enServicio) "Finalizar Ruta" else "Entrar En Servicio",
                    fontSize = 14.sp, fontWeight = FontWeight.Bold,
                    color = if (enServicio) redPrimary else greenPrimary
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Próxima parada con tiempo estimado
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = bgCard),
                shape = RoundedCornerShape(12.dp)
            ) {
                Column(modifier = Modifier.fillMaxWidth().padding(12.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
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
                            Text(text = "Próxima Parada", fontSize = 10.sp, color = blueMuted)
                            Text(
                                text = proximaParada,
                                fontSize = 13.sp, fontWeight = FontWeight.SemiBold, color = textPrimary
                            )
                        }
                        Text(
                            text = if (enServicio) "En curso" else "Esperando",
                            fontSize = 12.sp,
                            color = if (enServicio) greenPrimary else blueMuted,
                            fontWeight = FontWeight.Bold
                        )
                    }
                    if (enServicio) {
                        Spacer(modifier = Modifier.height(6.dp))
                        Text(
                            text = "⏱ Tiempo estimado: $tiempoEstimado · Parada ${paradaActualIndex + 1} de ${rutaSeleccionada.paradas.size}",
                            fontSize = 10.sp,
                            color = blueMuted
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Button(
                    onClick = { onNavigateToMapa(rutaSeleccionada.codigo) },
                    modifier = Modifier.weight(1f).height(46.dp),
                    shape = RoundedCornerShape(10.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = bgCard),
                    border = androidx.compose.foundation.BorderStroke(1.dp, blueBorder)
                ) {
                    Text(text = "Mapa", fontSize = 13.sp, color = textSecondary)
                }
                Button(
                    onClick = { onNavigateToParadas(rutaSeleccionada.codigo) },
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
            Text(text = label, fontSize = 10.sp, color = blueMuted)
            Spacer(modifier = Modifier.height(4.dp))
            Text(text = valor, fontSize = 18.sp, fontWeight = FontWeight.Bold, color = cyanPrimary)
        }
    }
}