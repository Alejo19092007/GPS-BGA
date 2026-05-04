package me.edwarjimenez.gpsbgaconductor.data.model

data class Usuario(
    private var id: String = "",
    private var nombreCompleto: String = "",
    private var email: String = "",
    private var cedula: String = "",
    private var telefono: String = "",
    private var fotoUrl: String = "",
    private var calificacion: Float = 5.0f,
    private var totalViajes: Int = 0,
    private var busAsignado: String = ""
) {
    fun getId(): String = id
    fun getNombreCompleto(): String = nombreCompleto
    fun getEmail(): String = email
    fun getCedula(): String = cedula
    fun getTelefono(): String = telefono
    fun getFotoUrl(): String = fotoUrl
    fun getCalificacion(): Float = calificacion
    fun getTotalViajes(): Int = totalViajes
    fun getBusAsignado(): String = busAsignado

    fun setId(value: String) { id = value }
    fun setNombreCompleto(value: String) { nombreCompleto = value }
    fun setEmail(value: String) { email = value }
    fun setCedula(value: String) { cedula = value }
    fun setTelefono(value: String) { telefono = value }
    fun setFotoUrl(value: String) { fotoUrl = value }
    fun setCalificacion(value: Float) { calificacion = value }
    fun setTotalViajes(value: Int) { totalViajes = value }
    fun setBusAsignado(value: String) { busAsignado = value }

    fun getNombreCorto(): String {
        val partes = nombreCompleto.split(" ")
        return if (partes.size >= 2) "${partes[0]} ${partes[1]}" else nombreCompleto
    }

    fun toMap(): Map<String, Any?> = mapOf(
        "id" to id,
        "nombreCompleto" to nombreCompleto,
        "email" to email,
        "cedula" to cedula,
        "telefono" to telefono,
        "fotoUrl" to fotoUrl,
        "calificacion" to calificacion,
        "totalViajes" to totalViajes,
        "busAsignado" to busAsignado
    )
}