package me.edwarjimenez.gpsbgaconductor.data.model

data class Bus(
    private var id: String = "",
    private var placa: String = "",
    private var conductorId: String = "",
    private var rutaId: String = "",
    private var estado: EstadoBus = EstadoBus.FUERA_DE_SERVICIO,
    private var latitud: Double = 0.0,
    private var longitud: Double = 0.0,
    private var velocidad: Double = 0.0,
    private var pasajeros: Int = 0,
    private var distanciaRecorrida: Double = 0.0,
    private var tiempoEnRuta: Long = 0L
) {
    fun getId(): String = id
    fun getPlaca(): String = placa
    fun getConductorId(): String = conductorId
    fun getRutaId(): String = rutaId
    fun getEstado(): EstadoBus = estado
    fun getLatitud(): Double = latitud
    fun getLongitud(): Double = longitud
    fun getVelocidad(): Double = velocidad
    fun getPasajeros(): Int = pasajeros
    fun getDistanciaRecorrida(): Double = distanciaRecorrida
    fun getTiempoEnRuta(): Long = tiempoEnRuta

    fun setId(value: String) { id = value }
    fun setPlaca(value: String) { placa = value }
    fun setConductorId(value: String) { conductorId = value }
    fun setRutaId(value: String) { rutaId = value }
    fun setEstado(value: EstadoBus) { estado = value }
    fun setLatitud(value: Double) { latitud = value }
    fun setLongitud(value: Double) { longitud = value }
    fun setVelocidad(value: Double) { velocidad = value }
    fun setPasajeros(value: Int) { pasajeros = value }
    fun setDistanciaRecorrida(value: Double) { distanciaRecorrida = value }
    fun setTiempoEnRuta(value: Long) { tiempoEnRuta = value }

    fun estaEnServicio(): Boolean = estado == EstadoBus.EN_SERVICIO

    fun toMap(): Map<String, Any?> = mapOf(
        "id" to id,
        "placa" to placa,
        "conductorId" to conductorId,
        "rutaId" to rutaId,
        "estado" to estado.name,
        "latitud" to latitud,
        "longitud" to longitud,
        "velocidad" to velocidad,
        "pasajeros" to pasajeros,
        "distanciaRecorrida" to distanciaRecorrida,
        "tiempoEnRuta" to tiempoEnRuta
    )

    enum class EstadoBus { EN_SERVICIO, FUERA_DE_SERVICIO, PAUSA }
}