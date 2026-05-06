package me.edwarjimenez.gpsbgaconductor.ui.profile

import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase

@Composable
fun EditarPerfilScreen(onBackClick: () -> Unit) {
    val auth = remember { FirebaseAuth.getInstance() }
    val context = LocalContext.current
    val db = remember { FirebaseDatabase.getInstance() }
    val uid = auth.currentUser?.uid ?: ""

    var nombre by remember { mutableStateOf("") }
    var telefono by remember { mutableStateOf("") }
    var cedula by remember { mutableStateOf("") }
    var isLoading by remember { mutableStateOf(false) }

    val bgDark = Color(0xFF0A0F1E)
    val bgCard = Color(0xFF0D1830)
    val bluePrimary = Color(0xFF0078FF)
    val cyanPrimary = Color(0xFF00C6FF)
    val blueMuted = Color(0xFF4A7FC0)
    val blueBorder = Color(0xFF1E2D5A)
    val textPrimary = Color(0xFFE0EEFF)

    LaunchedEffect(uid) {
        db.getReference("usuarios").child(uid).get()
            .addOnSuccessListener { snapshot ->
                nombre = snapshot.child("nombreCompleto").getValue(String::class.java) ?: ""
                telefono = snapshot.child("telefono").getValue(String::class.java) ?: ""
                cedula = snapshot.child("cedula").getValue(String::class.java) ?: ""
            }
    }

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
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(onClick = onBackClick, modifier = Modifier.size(48.dp)) {
                    Icon(Icons.AutoMirrored.Filled.ArrowBack, null, tint = cyanPrimary)
                }
                Text(
                    text = "Editar Perfil",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = textPrimary
                )
            }

            Spacer(modifier = Modifier.height(24.dp))

            Text(text = "Nombre Completo", fontSize = 12.sp, color = blueMuted)
            Spacer(modifier = Modifier.height(6.dp))
            TextField(
                value = nombre,
                onValueChange = { nombre = it },
                modifier = Modifier.fillMaxWidth().height(56.dp).clip(RoundedCornerShape(10.dp)),
                placeholder = { Text("Nombre y Apellido", color = blueMuted) },
                leadingIcon = { Icon(Icons.Default.Person, null, tint = blueMuted) },
                singleLine = true,
                colors = TextFieldDefaults.colors(
                    focusedContainerColor = bgCard,
                    unfocusedContainerColor = bgCard,
                    focusedIndicatorColor = bluePrimary,
                    unfocusedIndicatorColor = blueBorder,
                    focusedTextColor = textPrimary,
                    unfocusedTextColor = textPrimary
                )
            )

            Spacer(modifier = Modifier.height(16.dp))

            Text(text = "Número Telefónico", fontSize = 12.sp, color = blueMuted)
            Spacer(modifier = Modifier.height(6.dp))
            TextField(
                value = telefono,
                onValueChange = { telefono = it },
                modifier = Modifier.fillMaxWidth().height(56.dp).clip(RoundedCornerShape(10.dp)),
                placeholder = { Text("3234567890", color = blueMuted) },
                leadingIcon = { Icon(Icons.Default.Phone, null, tint = blueMuted) },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone),
                singleLine = true,
                colors = TextFieldDefaults.colors(
                    focusedContainerColor = bgCard,
                    unfocusedContainerColor = bgCard,
                    focusedIndicatorColor = bluePrimary,
                    unfocusedIndicatorColor = blueBorder,
                    focusedTextColor = textPrimary,
                    unfocusedTextColor = textPrimary
                )
            )

            Spacer(modifier = Modifier.height(16.dp))

            Text(text = "Cédula de Ciudadanía", fontSize = 12.sp, color = blueMuted)
            Spacer(modifier = Modifier.height(6.dp))
            TextField(
                value = cedula,
                onValueChange = { cedula = it },
                modifier = Modifier.fillMaxWidth().height(56.dp).clip(RoundedCornerShape(10.dp)),
                placeholder = { Text("1234567890", color = blueMuted) },
                leadingIcon = { Icon(Icons.Default.Person, null, tint = blueMuted) },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                singleLine = true,
                colors = TextFieldDefaults.colors(
                    focusedContainerColor = bgCard,
                    unfocusedContainerColor = bgCard,
                    focusedIndicatorColor = bluePrimary,
                    unfocusedIndicatorColor = blueBorder,
                    focusedTextColor = textPrimary,
                    unfocusedTextColor = textPrimary
                )
            )

            Spacer(modifier = Modifier.height(28.dp))

            Button(
                onClick = {
                    when {
                        nombre.isBlank() -> Toast.makeText(context, "Ingresa tu nombre", Toast.LENGTH_SHORT).show()
                        telefono.length < 7 -> Toast.makeText(context, "Teléfono inválido", Toast.LENGTH_SHORT).show()
                        cedula.length < 7 -> Toast.makeText(context, "Cédula inválida", Toast.LENGTH_SHORT).show()
                        else -> {
                            isLoading = true
                            db.getReference("usuarios").child(uid).updateChildren(
                                mapOf(
                                    "nombreCompleto" to nombre,
                                    "telefono" to telefono,
                                    "cedula" to cedula
                                )
                            ).addOnSuccessListener {
                                isLoading = false
                                Toast.makeText(context, "Perfil actualizado ✓", Toast.LENGTH_SHORT).show()
                                onBackClick()
                            }.addOnFailureListener {
                                isLoading = false
                                Toast.makeText(context, "Error al guardar", Toast.LENGTH_SHORT).show()
                            }
                        }
                    }
                },
                enabled = !isLoading,
                modifier = Modifier.fillMaxWidth().height(52.dp),
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(containerColor = bluePrimary)
            ) {
                if (isLoading) {
                    CircularProgressIndicator(color = Color.White, modifier = Modifier.size(24.dp))
                } else {
                    Text("Guardar Cambios", fontSize = 15.sp, fontWeight = FontWeight.Bold, color = Color.White)
                }
            }

            Spacer(modifier = Modifier.height(40.dp))
        }
    }
}