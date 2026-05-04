package me.edwarjimenez.gpsbgaconductor.data.model

data class Ruta(
    private var id: String = "",
    private var nombre: String = "",
    private var origen: String = "",
    private var destino: String = "",
    private var distanciaTotal: Double = 0.0,
    private var tiempoEstimado: Int = 0,
    private var paradas: List<Parada> = emptyList()
) {
    fun getId(): String = id
    fun getNombre(): String = nombre
    fun getOrigen(): String = origen
    fun getDestino(): String = destino
    fun getDistanciaTotal(): Double = distanciaTotal
    fun getTiempoEstimado(): Int = tiempoEstimado
    fun getParadas(): List<Parada> = paradas

    fun setId(value: String) { id = value }
    fun setNombre(value: String) { nombre = value }
    fun setOrigen(value: String) { origen = value }
    fun setDestino(value: String) { destino = value }
    fun setDistanciaTotal(value: Double) { distanciaTotal = value }
    fun setTiempoEstimado(value: Int) { tiempoEstimado = value }
    fun setParadas(value: List<Parada>) { paradas = value }

    fun getTotalParadas(): Int = paradas.size

    fun toMap(): Map<String, Any?> = mapOf(
        "id" to id,
        "nombre" to nombre,
        "origen" to origen,
        "destino" to destino,
        "distanciaTotal" to distanciaTotal,
        "tiempoEstimado" to tiempoEstimado
    )
}