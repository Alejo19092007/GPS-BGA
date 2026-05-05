package me.edwarjimenez.gpsbgaconductor.ui.auth

import androidx.compose.foundation.clickable



import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Lock
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
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.google.firebase.auth.FirebaseAuth

@Composable
fun RecuperarScreen(
    onBackClick: () -> Unit
) {
    val auth = remember { FirebaseAuth.getInstance() }
    val context = LocalContext.current

    var nombre by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var cedula by remember { mutableStateOf("") }
    var telefono by remember { mutableStateOf("") }
    var nuevaPassword by remember { mutableStateOf("") }
    var confirmarPassword by remember { mutableStateOf("") }
    var isLoading by remember { mutableStateOf(false) }

    val bgDark = Color(0xFF0A0F1E)
    val bgCard = Color(0xFF0D1830)
    val bluePrimary = Color(0xFF0078FF)
    val cyanPrimary = Color(0xFF00C6FF)
    val blueMuted = Color(0xFF4A7FC0)
    val blueBorder = Color(0xFF1E2D5A)
    val textPrimary = Color(0xFFE0EEFF)

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = bgDark
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(28.dp)
        ) {
            Spacer(modifier = Modifier.height(16.dp))

            // Botón atrás
            IconButton(
                onClick = onBackClick,
                modifier = Modifier.offset(x = (-12).dp)
            ) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = null,
                    tint = cyanPrimary
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = "RECUPERA CUENTA",
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                color = cyanPrimary,
                letterSpacing = 2.sp
            )

            Text(
                text = "Ingresa tus datos para recuperar el acceso",
                fontSize = 13.sp,
                color = blueMuted
            )

            Spacer(modifier = Modifier.height(28.dp))

            // Campos
            CampoRegistro(
                label = "Nombre Completo",
                value = nombre,
                onValueChange = { nombre = it },
                placeholder = "Nombre y Apellido",
                icono = Icons.Default.Person,
                bgCard = bgCard,
                blueMuted = blueMuted,
                blueBorder = blueBorder,
                bluePrimary = bluePrimary,
                textPrimary = textPrimary
            )

            Spacer(modifier = Modifier.height(16.dp))

            CampoRegistro(
                label = "Dirección Email",
                value = email,
                onValueChange = { email = it },
                placeholder = "Correo Personal",
                icono = Icons.Default.Email,
                keyboardType = KeyboardType.Email,
                bgCard = bgCard,
                blueMuted = blueMuted,
                blueBorder = blueBorder,
                bluePrimary = bluePrimary,
                textPrimary = textPrimary
            )

            Spacer(modifier = Modifier.height(16.dp))

            CampoRegistro(
                label = "Cédula de Ciudadanía",
                value = cedula,
                onValueChange = { cedula = it },
                placeholder = "1234567890",
                icono = Icons.Default.Person,
                keyboardType = KeyboardType.Number,
                bgCard = bgCard,
                blueMuted = blueMuted,
                blueBorder = blueBorder,
                bluePrimary = bluePrimary,
                textPrimary = textPrimary
            )

            Spacer(modifier = Modifier.height(16.dp))

            CampoRegistro(
                label = "Número Telefónico",
                value = telefono,
                onValueChange = { telefono = it },
                placeholder = "3234567890",
                icono = Icons.Default.Phone,
                keyboardType = KeyboardType.Phone,
                bgCard = bgCard,
                blueMuted = blueMuted,
                blueBorder = blueBorder,
                bluePrimary = bluePrimary,
                textPrimary = textPrimary
            )

            Spacer(modifier = Modifier.height(16.dp))

            CampoRegistro(
                label = "Nueva Contraseña",
                value = nuevaPassword,
                onValueChange = { nuevaPassword = it },
                placeholder = "••••••••",
                icono = Icons.Default.Lock,
                esPassword = true,
                bgCard = bgCard,
                blueMuted = blueMuted,
                blueBorder = blueBorder,
                bluePrimary = bluePrimary,
                textPrimary = textPrimary
            )

            Spacer(modifier = Modifier.height(16.dp))

            CampoRegistro(
                label = "Confirmar Contraseña",
                value = confirmarPassword,
                onValueChange = { confirmarPassword = it },
                placeholder = "••••••••",
                icono = Icons.Default.Lock,
                esPassword = true,
                bgCard = bgCard,
                blueMuted = blueMuted,
                blueBorder = blueBorder,
                bluePrimary = bluePrimary,
                textPrimary = textPrimary
            )

            Spacer(modifier = Modifier.height(28.dp))

            // Botón Guardar Cambios
            Button(
                onClick = {
                    when {
                        email.isBlank() -> {
                            Toast.makeText(context, "Ingresa tu correo", Toast.LENGTH_SHORT).show()
                        }
                        nuevaPassword.length < 6 -> {
                            Toast.makeText(context, "Mínimo 6 caracteres", Toast.LENGTH_SHORT).show()
                        }
                        nuevaPassword != confirmarPassword -> {
                            Toast.makeText(context, "Las contraseñas no coinciden", Toast.LENGTH_SHORT).show()
                        }
                        else -> {
                            isLoading = true
                            auth.sendPasswordResetEmail(email)
                                .addOnCompleteListener { task ->
                                    isLoading = false
                                    if (task.isSuccessful) {
                                        Toast.makeText(
                                            context,
                                            "Correo de recuperación enviado a $email",
                                            Toast.LENGTH_LONG
                                        ).show()
                                        onBackClick()
                                    } else {
                                        Toast.makeText(
                                            context,
                                            "Error: ${task.exception?.message}",
                                            Toast.LENGTH_LONG
                                        ).show()
                                    }
                                }
                        }
                    }
                },
                enabled = !isLoading,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(52.dp),
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(containerColor = bluePrimary)
            ) {
                if (isLoading) {
                    CircularProgressIndicator(color = Color.White, modifier = Modifier.size(24.dp))
                } else {
                    Text(
                        text = "Guardar Cambios",
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Link iniciar sesión
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center
            ) {
                Text(text = "¿Ya tienes cuenta? ", color = blueMuted, fontSize = 13.sp)
                Text(
                    text = "Inicia Sesión",
                    color = cyanPrimary,
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.clickable { onBackClick() }
                )
            }

            Spacer(modifier = Modifier.height(40.dp))
        }
    }
}