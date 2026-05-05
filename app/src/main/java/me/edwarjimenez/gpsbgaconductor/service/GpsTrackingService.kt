package me.edwarjimenez.gpsbgaconductor.service

import android.app.*
import android.content.Intent
import android.os.*
import android.util.Log
import androidx.core.app.NotificationCompat
import com.google.android.gms.location.*
import me.edwarjimenez.gpsbgaconductor.MainActivity
import me.edwarjimenez.gpsbgaconductor.data.firebase.FirebaseRepository
import kotlinx.coroutines.*

class GpsTrackingService : Service() {

    companion object {
        const val CHANNEL_ID = "gpsbga_gps_channel"
        const val NOTIFICATION_ID = 1001
        const val ACTION_INICIAR = "INICIAR_GPS"
        const val ACTION_DETENER = "DETENER_GPS"
        const val EXTRA_BUS_ID = "bus_id"
        const val INTERVALO_GPS_MS = 3000L
        const val TAG = "GpsTrackingService"
    }

    private val scope = CoroutineScope(Dispatchers.IO + SupervisorJob())
    private val repository = FirebaseRepository()
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private lateinit var locationCallback: LocationCallback
    private var busId: String = ""
    private var latitudActual = 0.0
    private var longitudActual = 0.0
    private var velocidadActual = 0.0

    override fun onCreate() {
        super.onCreate()
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)
        crearCanalNotificacion()
        configurarLocationCallback()
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        busId = intent?.getStringExtra(EXTRA_BUS_ID) ?: ""
        when (intent?.action) {
            ACTION_INICIAR -> iniciarRastreo()
            ACTION_DETENER -> detenerServicio()
        }
        return START_STICKY
    }

    private fun iniciarRastreo() {
        startForeground(NOTIFICATION_ID, crearNotificacion("GPS activo — transmitiendo ubicación"))
        try {
            val request = LocationRequest.Builder(
                Priority.PRIORITY_HIGH_ACCURACY, INTERVALO_GPS_MS
            ).setMinUpdateIntervalMillis(2000L).build()

            fusedLocationClient.requestLocationUpdates(
                request, locationCallback, Looper.getMainLooper()
            )
            Log.d(TAG, "GPS iniciado para bus: $busId")
        } catch (e: SecurityException) {
            Log.e(TAG, "Sin permisos de ubicación: ${e.message}")
        }
    }

    private fun configurarLocationCallback() {
        locationCallback = object : LocationCallback() {
            override fun onLocationResult(result: LocationResult) {
                result.lastLocation?.let { location ->
                    latitudActual = location.latitude
                    longitudActual = location.longitude
                    velocidadActual = (location.speed * 3.6)

                    scope.launch {
                        repository.publicarUbicacion(
                            busId,
                            latitudActual,
                            longitudActual,
                            velocidadActual
                        )
                    }
                    Log.d(TAG, "Ubicación: $latitudActual, $longitudActual | ${velocidadActual} km/h")
                }
            }
        }
    }

    private fun detenerServicio() {
        fusedLocationClient.removeLocationUpdates(locationCallback)
        scope.cancel()
        stopForeground(STOP_FOREGROUND_REMOVE)
        stopSelf()
        Log.d(TAG, "Servicio GPS detenido")
    }

    private fun crearCanalNotificacion() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val canal = NotificationChannel(
                CHANNEL_ID,
                "GPS GPSBGA",
                NotificationManager.IMPORTANCE_LOW
            ).apply {
                description = "Rastreo GPS activo en segundo plano"
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
        scope.cancel()
    }
}