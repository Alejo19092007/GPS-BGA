package me.edwarjimenez.gpsbgaconductor.ui.auth

import android.widget.Toast
import androidx.compose.foundation.clickable
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
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.google.firebase.auth.FirebaseAuth
import me.edwarjimenez.gpsbgaconductor.data.firebase.FirebaseRepository
import me.edwarjimenez.gpsbgaconductor.data.model.Usuario
import me.edwarjimenez.gpsbgaconductor.utils.Resource
import kotlinx.coroutines.launch

@Composable
fun RegistroScreen(
    onRegistroSuccess: () -> Unit,
    onBackClick: () -> Unit
) {
    val auth = remember { FirebaseAuth.getInstance() }
    val repository = remember { FirebaseRepository() }
    val context = LocalContext.current
    val scope = rememberCoroutineScope()

    var nombre by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var cedula by remember { mutableStateOf("") }
    var telefono by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
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
                text = "CREAR CUENTA",
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                color = cyanPrimary,
                letterSpacing = 2.sp
            )

            Text(
                text = "Completa tus datos para registrarte",
                fontSize = 13.sp,
                color = blueMuted
            )

            Spacer(modifier = Modifier.height(28.dp))

            // Campos del formulario
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
                label = "Contraseña",
                value = password,
                onValueChange = { password = it },
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

            // Botón Crear Cuenta
            Button(
                onClick = {
                    when {
                        nombre.isBlank() || email.isBlank() || cedula.isBlank() ||
                                telefono.isBlank() || password.isBlank() -> {
                            Toast.makeText(context, "Completa todos los campos", Toast.LENGTH_SHORT).show()
                        }
                        password.length < 6 -> {
                            Toast.makeText(context, "La contraseña debe tener mínimo 6 caracteres", Toast.LENGTH_SHORT).show()
                        }
                        password != confirmarPassword -> {
                            Toast.makeText(context, "Las contraseñas no coinciden", Toast.LENGTH_SHORT).show()
                        }
                        cedula.length < 7 -> {
                            Toast.makeText(context, "Cédula inválida", Toast.LENGTH_SHORT).show()
                        }
                        telefono.length != 10 -> {
                            Toast.makeText(context, "Teléfono inválido (10 dígitos)", Toast.LENGTH_SHORT).show()
                        }
                        else -> {
                            isLoading = true
                            auth.createUserWithEmailAndPassword(email, password)
                                .addOnCompleteListener { task ->
                                    if (task.isSuccessful) {
                                        val uid = task.result.user?.uid ?: ""
                                        val usuario = Usuario().apply {
                                            setId(uid)
                                            setNombreCompleto(nombre)
                                            setEmail(email)
                                            setCedula(cedula)
                                            setTelefono(telefono)
                                        }
                                        scope.launch {
                                            repository.actualizarUsuario(usuario)
                                            isLoading = false
                                            onRegistroSuccess()
                                        }
                                    } else {
                                        isLoading = false
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
                        text = "Crear Cuenta",
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            // Link iniciar sesión
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center
            ) {
                Text(
                    text = "¿Ya tienes cuenta? ",
                    color = blueMuted,
                    fontSize = 13.sp
                )
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

@Composable
fun CampoRegistro(
    label: String,
    value: String,
    onValueChange: (String) -> Unit,
    placeholder: String,
    icono: ImageVector,
    keyboardType: KeyboardType = KeyboardType.Text,
    esPassword: Boolean = false,
    bgCard: Color,
    blueMuted: Color,
    blueBorder: Color,
    bluePrimary: Color,
    textPrimary: Color
) {
    Text(
        text = label,
        fontSize = 12.sp,
        color = blueMuted,
        modifier = Modifier.fillMaxWidth()
    )
    Spacer(modifier = Modifier.height(6.dp))
    TextField(
        value = value,
        onValueChange = onValueChange,
        modifier = Modifier
            .fillMaxWidth()
            .height(56.dp)
            .clip(RoundedCornerShape(10.dp)),
        placeholder = { Text(placeholder, color = blueMuted) },
        leadingIcon = { Icon(icono, null, tint = blueMuted) },
        visualTransformation = if (esPassword) PasswordVisualTransformation() else VisualTransformation.None,
        keyboardOptions = KeyboardOptions(keyboardType = keyboardType),
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
}