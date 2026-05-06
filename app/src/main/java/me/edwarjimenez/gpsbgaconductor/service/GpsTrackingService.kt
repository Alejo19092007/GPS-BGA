package me.edwarjimenez.gpsbgaconductor.service

import android.app.*
import android.content.Intent
import android.os.*
import android.util.Log
import androidx.core.app.NotificationCompat
import me.edwarjimenez.gpsbgaconductor.MainActivity
import me.edwarjimenez.gpsbgaconductor.data.firebase.FirebaseRepository
import kotlinx.coroutines.*
import kotlinx.coroutines.tasks.await

class GpsTrackingService : Service() {

    companion object {
        const val CHANNEL_ID = "gpsbga_gps_channel"
        const val NOTIFICATION_ID = 1001
        const val ACTION_INICIAR = "INICIAR_GPS"
        const val ACTION_DETENER = "DETENER_GPS"
        const val EXTRA_BUS_ID = "bus_id"
        const val EXTRA_RUTA_CODIGO = "ruta_codigo"
        const val TAG = "GpsTrackingService"

        val coordenadasRutas = mapOf(
            "7" to listOf(
                Pair(7.1390, -73.1180),
                Pair(7.1320, -73.1190),
                Pair(7.1250, -73.1200),
                Pair(7.1180, -73.1210),
                Pair(7.1100, -73.1190),
                Pair(7.1050, -73.1150),
                Pair(7.0980, -73.1180),
                Pair(7.0920, -73.1160),
                Pair(7.0850, -73.1140),
                Pair(7.0780, -73.1120),
                Pair(7.0650, -73.1090)
            ),
            "36" to listOf(
                Pair(7.0550, -73.0980),
                Pair(7.0620, -73.1020),
                Pair(7.0720, -73.1080),
                Pair(7.0820, -73.1120),
                Pair(7.0920, -73.1160),
                Pair(7.1020, -73.1180),
                Pair(7.1120, -73.1200),
                Pair(7.1200, -73.1210),
                Pair(7.1150, -73.1190),
                Pair(7.1050, -73.1170),
                Pair(7.0950, -73.1150),
                Pair(7.0750, -73.1100)
            ),
            "27" to listOf(
                Pair(7.0900, -73.0850),
                Pair(7.0980, -73.0950),
                Pair(7.1050, -73.1050),
                Pair(7.1100, -73.1150),
                Pair(7.1120, -73.1180),
                Pair(7.1150, -73.1200),
                Pair(7.1200, -73.1220),
                Pair(7.1250, -73.1230),
                Pair(7.1220, -73.1210),
                Pair(7.1180, -73.1200)
            )
        )
    }

    private val scope = CoroutineScope(Dispatchers.IO + SupervisorJob())
    private val repository = FirebaseRepository()
    private var busId = ""
    private var rutaCodigo = "36"
    private var simulacionJob: Job? = null

    override fun onCreate() {
        super.onCreate()
        crearCanalNotificacion()
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        busId = intent?.getStringExtra(EXTRA_BUS_ID) ?: ""
        rutaCodigo = intent?.getStringExtra(EXTRA_RUTA_CODIGO) ?: "36"
        when (intent?.action) {
            ACTION_INICIAR -> iniciarSimulacion()
            ACTION_DETENER -> detenerServicio()
        }
        return START_STICKY
    }

    private fun iniciarSimulacion() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            startForeground(NOTIFICATION_ID, crearNotificacion("GPS activo — simulando recorrido"), android.content.pm.ServiceInfo.FOREGROUND_SERVICE_TYPE_SPECIAL_USE)
        } else {
            startForeground(NOTIFICATION_ID, crearNotificacion("GPS activo — simulando recorrido"))
        }

        val coordenadas = coordenadasRutas[rutaCodigo] ?: coordenadasRutas["36"]!!
        var indice = 0
        var distanciaTotal = 0.0
        var tiempoSegundos = 0L

        simulacionJob = scope.launch {
            while (isActive) {
                val (lat, lng) = coordenadas[indice]
                val velocidad = (20..45).random().toDouble()

                if (indice > 0) {
                    val (latAnt, lngAnt) = coordenadas[indice - 1]
                    distanciaTotal += calcularDistancia(latAnt, lngAnt, lat, lng)
                }

                tiempoSegundos += 5
                repository.publicarUbicacion(busId, lat, lng, velocidad)
                actualizarEstadisticas(
                    busId = busId,
                    rutaId = rutaCodigo,
                    velocidad = velocidad,
                    distancia = distanciaTotal,
                    tiempo = tiempoSegundos,
                    paradaActual = indice
                )

                Log.d(TAG, "Simulando parada $indice: $lat, $lng | ${velocidad}km/h")
                indice = (indice + 1) % coordenadas.size
                delay(5000L)
            }
        }
    }

    private suspend fun actualizarEstadisticas(
        busId: String,
        rutaId: String,
        velocidad: Double,
        distancia: Double,
        tiempo: Long,
        paradaActual: Int
    ) {
        try {
            val db = com.google.firebase.database.FirebaseDatabase.getInstance()
            val datos = mapOf(
                "velocidad" to velocidad,
                "distanciaRecorrida" to String.format("%.1f", distancia),
                "tiempoEnRuta" to tiempo,
                "paradaActual" to paradaActual,
                "rutaId" to rutaId,
                "estado" to "EN_SERVICIO",
                "timestamp" to System.currentTimeMillis()
            )
            db.getReference("buses").child(busId).updateChildren(datos).await()
        } catch (e: Exception) {
            Log.e(TAG, "Error actualizando estadísticas: ${e.message}")
        }
    }

    private fun calcularDistancia(lat1: Double, lng1: Double, lat2: Double, lng2: Double): Double {
        val r = 6371.0
        val dLat = Math.toRadians(lat2 - lat1)
        val dLng = Math.toRadians(lng2 - lng1)
        val a = Math.sin(dLat / 2) * Math.sin(dLat / 2) +
                Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2)) *
                Math.sin(dLng / 2) * Math.sin(dLng / 2)
        val c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a))
        return r * c
    }

    private fun detenerServicio() {
        simulacionJob?.cancel()
        scope.cancel()
        stopForeground(STOP_FOREGROUND_REMOVE)
        stopSelf()
        Log.d(TAG, "Simulación detenida")
    }

    private fun crearCanalNotificacion() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val canal = NotificationChannel(
                CHANNEL_ID,
                "GPS GPSBGA",
                NotificationManager.IMPORTANCE_LOW
            ).apply {
                description = "Simulación de recorrido activa"
            }
            val manager = getSystemService(NotificationManager::class.java)
            manager.createNotificationChannel(canal)
        }
    }

    private fun crearNotificacion(mensaje: String): Notification {
        val pendingIntent = PendingIntent.getActivity(
            this, 0,
            Intent(this, MainActivity::class.java),
            PendingIntent.FLAG_IMMUTABLE
        )
        return NotificationCompat.Builder(this, CHANNEL_ID)
            .setContentTitle("GPSBGA — Conductor")
            .setContentText(mensaje)
            .setSmallIcon(android.R.drawable.ic_menu_mylocation)
            .setContentIntent(pendingIntent)
            .setOngoing(true)
            .setPriority(NotificationCompat.PRIORITY_LOW)
            .build()
    }

    override fun onBind(intent: Intent?): IBinder? = null

    override fun onDestroy() {
        super.onDestroy()
        simulacionJob?.cancel()
        scope.cancel()
    }
}