package me.edwarjimenez.gpsbgaconductor.ui.map

import android.Manifest
import android.content.pm.PackageManager
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.LocationOn
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
import androidx.core.content.ContextCompat
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.*

// Datos de rutas con coordenadas reales de Bucaramanga
data class RutaMapa(
    val codigo: String,
    val nombre: String,
    val color: Long,
    val paradas: List<Pair<LatLng, String>>,
    val polilinea: List<LatLng>
)

val rutasMapa = listOf(
    // RUTA 7 - LIMONCITO
    RutaMapa(
        codigo = "7",
        nombre = "Limoncito",
        color = 0xFFFF3B57,
        paradas = listOf(
            Pair(LatLng(7.1390, -73.1180), "Terminal Los Cauchos - Estadio"),
            Pair(LatLng(7.1320, -73.1190), "Servientrega"),
            Pair(LatLng(7.1250, -73.1200), "Carrera 8"),
            Pair(LatLng(7.1180, -73.1210), "Paragüitas"),
            Pair(LatLng(7.1100, -73.1190), "Bucarica"),
            Pair(LatLng(7.1050, -73.1150), "Transversal Oriental"),
            Pair(LatLng(7.0980, -73.1180), "CC Cacique"),
            Pair(LatLng(7.0920, -73.1160), "Megamall"),
            Pair(LatLng(7.0850, -73.1140), "Plaza Guarín"),
            Pair(LatLng(7.0780, -73.1120), "Autopista Floridablanca"),
            Pair(LatLng(7.0650, -73.1090), "Retorno Plata Acero")
        ),
        polilinea = listOf(
            LatLng(7.1390, -73.1180),
            LatLng(7.1320, -73.1190),
            LatLng(7.1250, -73.1200),
            LatLng(7.1180, -73.1210),
            LatLng(7.1100, -73.1190),
            LatLng(7.1050, -73.1150),
            LatLng(7.0980, -73.1180),
            LatLng(7.0920, -73.1160),
            LatLng(7.0850, -73.1140),
            LatLng(7.0780, -73.1120),
            LatLng(7.0650, -73.1090)
        )
    ),
    // RUTA 36 - IGSABELAR
    RutaMapa(
        codigo = "36",
        nombre = "Igsabelar 33",
        color = 0xFF0078FF,
        paradas = listOf(
            Pair(LatLng(7.0550, -73.0980), "González Chaparro"),
            Pair(LatLng(7.0620, -73.1020), "Barrio La Paz"),
            Pair(LatLng(7.0720, -73.1080), "Papi Quiero Piña"),
            Pair(LatLng(7.0820, -73.1120), "Miradores San Lorenzo"),
            Pair(LatLng(7.0920, -73.1160), "CC Cacique"),
            Pair(LatLng(7.1020, -73.1180), "Viaducto La Flora"),
            Pair(LatLng(7.1120, -73.1200), "Carrera 33"),
            Pair(LatLng(7.1200, -73.1210), "Megamall"),
            Pair(LatLng(7.1150, -73.1190), "Plaza Guarín"),
            Pair(LatLng(7.1050, -73.1170), "Plaza Satélite"),
            Pair(LatLng(7.0950, -73.1150), "Puente Provenza"),
            Pair(LatLng(7.0750, -73.1100), "Autopista Cañaveral")
        ),
        polilinea = listOf(
            LatLng(7.0550, -73.0980),
            LatLng(7.0620, -73.1020),
            LatLng(7.0720, -73.1080),
            LatLng(7.0820, -73.1120),
            LatLng(7.0920, -73.1160),
            LatLng(7.1020, -73.1180),
            LatLng(7.1120, -73.1200),
            LatLng(7.1200, -73.1210),
            LatLng(7.1150, -73.1190),
            LatLng(7.1050, -73.1170),
            LatLng(7.0950, -73.1150),
            LatLng(7.0750, -73.1100)
        )
    ),
    // RUTA 27 - CARACOLÍ
    RutaMapa(
        codigo = "27",
        nombre = "Caracolí - Centro",
        color = 0xFF00C66B,
        paradas = listOf(
            Pair(LatLng(7.0900, -73.0850), "Terminal Caracolí"),
            Pair(LatLng(7.0980, -73.0950), "Bucarica"),
            Pair(LatLng(7.1050, -73.1050), "Bellavista"),
            Pair(LatLng(7.1100, -73.1150), "Carretera Antigua"),
            Pair(LatLng(7.1120, -73.1180), "Viaducto La Flora"),
            Pair(LatLng(7.1150, -73.1200), "Carrera 33"),
            Pair(LatLng(7.1200, -73.1220), "Calle 34"),
            Pair(LatLng(7.1250, -73.1230), "Centro - Carrera 10"),
            Pair(LatLng(7.1220, -73.1210), "Carrera 13"),
            Pair(LatLng(7.1180, -73.1200), "Cacique Monterrey")
        ),
        polilinea = listOf(
            LatLng(7.0900, -73.0850),
            LatLng(7.0980, -73.0950),
            LatLng(7.1050, -73.1050),
            LatLng(7.1100, -73.1150),
            LatLng(7.1120, -73.1180),
            LatLng(7.1150, -73.1200),
            LatLng(7.1200, -73.1220),
            LatLng(7.1250, -73.1230),
            LatLng(7.1220, -73.1210),
            LatLng(7.1180, -73.1200)
        )
    )
)

@Composable
fun MapaScreen(
    onBackClick: () -> Unit,
    rutaCodigo: String = "36"
) {
    val context = LocalContext.current

    val bgDark = Color(0xFF0A0F1E)
    val bgCard = Color(0xFF0D1830)
    val cyanPrimary = Color(0xFF00C6FF)
    val blueMuted = Color(0xFF4A7FC0)
    val blueBorder = Color(0xFF1E2D5A)
    val textPrimary = Color(0xFFE0EEFF)
    val textSecondary = Color(0xFFB0C4E8)
    val redPrimary = Color(0xFFFF3B57)
    val redBg = Color(0xFF3A0005)

    val rutaActual = rutasMapa.find { it.codigo == rutaCodigo } ?: rutasMapa[0]

    val bucaramanga = LatLng(7.1193, -73.1227)
    val cameraPositionState = rememberCameraPositionState {
        position = CameraPosition.fromLatLngZoom(bucaramanga, 13f)
    }

    var tienePermiso by remember {
        mutableStateOf(
            ContextCompat.checkSelfPermission(
                context, Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        )
    }

    val launcher = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { granted -> tienePermiso = granted }

    LaunchedEffect(Unit) {
        if (!tienePermiso) launcher.launch(Manifest.permission.ACCESS_FINE_LOCATION)
    }

    Box(modifier = Modifier.fillMaxSize()) {

        GoogleMap(
            modifier = Modifier.fillMaxSize(),
            cameraPositionState = cameraPositionState,
            properties = MapProperties(
                isMyLocationEnabled = tienePermiso,
                mapType = MapType.NORMAL
            ),
            uiSettings = MapUiSettings(
                zoomControlsEnabled = false,
                myLocationButtonEnabled = false
            )
        ) {
            // Dibujar la línea de la ruta
            Polyline(
                points = rutaActual.polilinea,
                color = Color(rutaActual.color),
                width = 8f
            )

            // Marcadores de paradas
            rutaActual.paradas.forEachIndexed { index, (ubicacion, nombre) ->
                Marker(
                    state = MarkerState(position = ubicacion),
                    title = nombre,
                    snippet = "Parada ${index + 1} · Ruta ${rutaActual.codigo}"
                )
            }
        }

        // Header flotante
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
                .align(Alignment.TopStart)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(12.dp))
                    .background(bgDark.copy(alpha = 0.92f))
                    .padding(12.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(onClick = onBackClick) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = null,
                        tint = cyanPrimary
                    )
                }
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = "Mapa En Vivo",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        color = textPrimary
                    )
                    Text(
                        text = "Ruta ${rutaActual.codigo} · ${rutaActual.nombre}",
                        fontSize = 11.sp,
                        color = blueMuted
                    )
                }
            }
        }

        // Controles flotantes abajo
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
                .align(Alignment.BottomCenter),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Button(
                onClick = {},
                modifier = Modifier.weight(1f).height(46.dp),
                shape = RoundedCornerShape(10.dp),
                colors = ButtonDefaults.buttonColors(containerColor = bgCard.copy(alpha = 0.95f)),
                border = androidx.compose.foundation.BorderStroke(1.dp, blueBorder)
            ) {
                Icon(
                    imageVector = Icons.Default.LocationOn,
                    contentDescription = null,
                    tint = cyanPrimary,
                    modifier = Modifier.size(16.dp)
                )
                Spacer(modifier = Modifier.width(4.dp))
                Text(text = "Centrar", fontSize = 13.sp, color = textSecondary)
            }

            Button(
                onClick = {},
                modifier = Modifier.weight(1f).height(46.dp),
                shape = RoundedCornerShape(10.dp),
                colors = ButtonDefaults.buttonColors(containerColor = bgCard.copy(alpha = 0.95f)),
                border = androidx.compose.foundation.BorderStroke(1.dp, blueBorder)
            ) {
                Text(text = "Paradas", fontSize = 13.sp, color = textSecondary)
            }

            Button(
                onClick = { onBackClick() },
                modifier = Modifier.weight(1f).height(46.dp),
                shape = RoundedCornerShape(10.dp),
                colors = ButtonDefaults.buttonColors(containerColor = redBg.copy(alpha = 0.95f)),
                border = androidx.compose.foundation.BorderStroke(1.dp, redPrimary)
            ) {
                Text(text = "Finalizar", fontSize = 13.sp, color = redPrimary)
            }
        }
    }
}