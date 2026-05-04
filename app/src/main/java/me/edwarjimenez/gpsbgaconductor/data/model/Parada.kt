package me.edwarjimenez.gpsbgaconductor.data.model

data class Parada(
    private var id: String = "",
    private var nombre: String = "",
    private var direccion: String = "",
    private var latitud: Double = 0.0,
    private var longitud: Double = 0.0,
    private var orden: Int = 0,
    private var tiempoEstimado: Int = 0,
    private var estado: EstadoParada = EstadoParada.PENDIENTE
) {
    fun getId(): String = id
    fun getNombre(): String = nombre
    fun getDireccion(): String = direccion
    fun getLatitud(): Double = latitud
    fun getLongitud(): Double = longitud
    fun getOrden(): Int = orden
    fun getTiempoEstimado(): Int = tiempoEstimado
    fun getEstado(): EstadoParada = estado

    fun setId(value: String) { id = value }
    fun setNombre(value: String) { nombre = value }
    fun setDireccion(value: String) { direccion = value }
    fun setLatitud(value: Double) { latitud = value }
    fun setLongitud(value: Double) { longitud = value }
    fun setOrden(value: Int) { orden = value }
    fun setTiempoEstimado(value: Int) { tiempoEstimado = value }
    fun setEstado(value: EstadoParada) { estado = value }

    enum class EstadoParada { PENDIENTE, ACTUAL, COMPLETADA }
}