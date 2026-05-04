package me.edwarjimenez.gpsbgaconductor.data.firebase

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import me.edwarjimenez.gpsbgaconductor.data.model.Bus
import me.edwarjimenez.gpsbgaconductor.data.model.Usuario
import me.edwarjimenez.gpsbgaconductor.utils.Resource
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import me.edwarjimenez.gpsbgaconductor.data.model.Ruta
import kotlin.jvm.java

class FirebaseRepository {

    private val auth = FirebaseAuth.getInstance()
    private val db = FirebaseDatabase.getInstance()

    private val refBuses = db.getReference("buses")
    private val refRutas = db.getReference("rutas")
    private val refUsuarios = db.getReference("usuarios")

    // ── AUTH ──────────────────────────────────────────────

    suspend fun loginConEmail(email: String, password: String): Resource<Usuario> {
        return try {
            val resultado = auth.signInWithEmailAndPassword(email, password).await()
            val uid = resultado.user?.uid ?: return Resource.Error("Usuario no encontrado")
            obtenerUsuario(uid)
        } catch (e: Exception) {
            Resource.Error(e.message ?: "Error al iniciar sesión")
        }
    }

    suspend fun registrarUsuario(
        email: String,
        password: String,
        usuario: Usuario
    ): Resource<Usuario> {
        return try {
            val resultado = auth.createUserWithEmailAndPassword(email, password).await()
            val uid = resultado.user?.uid ?: return Resource.Error("Error al crear usuario")
            usuario.setId(uid)
            refUsuarios.child(uid).setValue(usuario.toMap()).await()
            Resource.Success(usuario)
        } catch (e: Exception) {
            Resource.Error(e.message ?: "Error al registrar")
        }
    }

    suspend fun recuperarPassword(email: String): Resource<String> {
        return try {
            auth.sendPasswordResetEmail(email).await()
            Resource.Success("Correo enviado a $email")
        } catch (e: Exception) {
            Resource.Error(e.message ?: "Error al enviar correo")
        }
    }

    fun cerrarSesion() = auth.signOut()

    fun getUidActual(): String? = auth.currentUser?.uid

    // ── USUARIO ───────────────────────────────────────────

    suspend fun obtenerUsuario(uid: String): Resource<Usuario> {
        return try {
            val snapshot = refUsuarios.child(uid).get().await()
            val usuario = snapshot.getValue(Usuario::class.java)
                ?: return Resource.Error("Perfil no encontrado")
            Resource.Success(usuario)
        } catch (e: Exception) {
            Resource.Error(e.message ?: "Error al obtener usuario")
        }
    }

    suspend fun actualizarUsuario(usuario: Usuario): Resource<String> {
        return try {
            refUsuarios.child(usuario.getId()).updateChildren(usuario.toMap()).await()
            Resource.Success("Perfil actualizado")
        } catch (e: Exception) {
            Resource.Error(e.message ?: "Error al actualizar")
        }
    }

    // ── BUS / GPS ─────────────────────────────────────────

    suspend fun publicarUbicacion(
        busId: String,
        latitud: Double,
        longitud: Double,
        velocidad: Double
    ): Resource<String> {
        return try {
            val datos = mapOf(
                "latitud" to latitud,
                "longitud" to longitud,
                "velocidad" to velocidad,
                "timestamp" to System.currentTimeMillis()
            )
            refBuses.child(busId).updateChildren(datos).await()
            Resource.Success("OK")
        } catch (e: Exception) {
            Resource.Error(e.message ?: "Error al publicar ubicación")
        }
    }

    suspend fun entrarEnServicio(busId: String, rutaId: String): Resource<String> {
        return try {
            val datos = mapOf(
                "estado" to Bus.EstadoBus.EN_SERVICIO.name,
                "rutaId" to rutaId,
                "tiempoInicio" to System.currentTimeMillis()
            )
            refBuses.child(busId).updateChildren(datos).await()
            Resource.Success("En servicio")
        } catch (e: Exception) {
            Resource.Error(e.message ?: "Error")
        }
    }

    suspend fun finalizarRuta(busId: String): Resource<String> {
        return try {
            val datos = mapOf(
                "estado" to Bus.EstadoBus.FUERA_DE_SERVICIO.name,
                "rutaId" to "",
                "latitud" to 0.0,
                "longitud" to 0.0,
                "velocidad" to 0.0,
                "pasajeros" to 0
            )
            refBuses.child(busId).updateChildren(datos).await()
            Resource.Success("Ruta finalizada")
        } catch (e: Exception) {
            Resource.Error(e.message ?: "Error")
        }
    }

    // ── RUTAS ─────────────────────────────────────────────

    fun observarRutas(): Flow<List<Ruta>> = callbackFlow {
        val listener = object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val rutas = snapshot.children.mapNotNull {
                    it.getValue(Ruta::class.java)
                }
                trySend(rutas)
            }
            override fun onCancelled(error: DatabaseError) {
                close(error.toException())
            }
        }
        refRutas.addValueEventListener(listener)
        awaitClose { refRutas.removeEventListener(listener) }
    }

    suspend fun obtenerRuta(rutaId: String): Resource<Ruta> {
        return try {
            val snapshot = refRutas.child(rutaId).get().await()
            val ruta = snapshot.getValue(Ruta::class.java)
                ?: return Resource.Error("Ruta no encontrada")
            Resource.Success(ruta)
        } catch (e: Exception) {
            Resource.Error(e.message ?: "Error al obtener ruta")
        }
    }
}