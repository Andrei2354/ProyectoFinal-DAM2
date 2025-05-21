package screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.runtime.Composable
import cafe.adriel.voyager.core.screen.Screen
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import cafe.adriel.voyager.navigator.LocalNavigator
import androidx.compose.ui.text.TextStyle
import androidx.compose.material.icons.Icons
import androidx.compose.material3.Icon
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.*
import androidx.compose.material.icons.filled.*
import androidx.compose.ui.graphics.vector.ImageVector
import cafe.adriel.voyager.navigator.currentOrThrow
import androidx.compose.runtime.*
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.window.Dialog
import modelo.ProfileColors
import network.apiEditProfile

class ProfileScreen(val user: modelo.Usuario) : Screen {
    @Composable
    override fun Content() {
        val navigator = LocalNavigator.currentOrThrow
        val colors = ProfileColors()

        // Estado para manejar los datos del usuario
        var usuario by remember { mutableStateOf(user) }

        // Estado para controlar la visibilidad del dialog
        var showEditDialog by remember { mutableStateOf(false) }

        // Estado para mostrar mensaje de éxito o error
        var showSnackbar by remember { mutableStateOf(false) }
        var snackbarMessage by remember { mutableStateOf("") }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(colors.pupura)
                .padding(horizontal = 16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Encabezado
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 20.dp, bottom = 16.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(
                    onClick = { navigator.pop() },
                    modifier = Modifier.size(40.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.ArrowBack,
                        contentDescription = "Volver",
                        tint = colors.negro
                    )
                }
            }

            // Avatar y nombre
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.padding(vertical = 20.dp)
            ) {
                Text(
                    "Mi Perfil",
                    style = TextStyle(
                        fontSize = 24.sp,
                        fontWeight = FontWeight.Bold,
                        color = colors.negro
                    )
                )
                Spacer(modifier = Modifier.size(40.dp))
                Box(
                    modifier = Modifier
                        .size(120.dp)
                        .clip(CircleShape)
                        .background(colors.pastel.copy(alpha = 0.3f))
                        .border(2.dp, colors.pastel, CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.Person,
                        contentDescription = "Avatar",
                        tint = colors.negro,
                        modifier = Modifier.size(60.dp)
                    )
                }
                Spacer(modifier = Modifier.height(16.dp))
                Text(
                    usuario.nombre, // Usa el usuario del estado
                    style = TextStyle(
                        fontSize = 22.sp,
                        fontWeight = FontWeight.Bold,
                        color = colors.negro
                    )
                )
            }

            // Sección de información
            Card(
                modifier = Modifier
                    .width(500.dp)
                    .padding(vertical = 8.dp),
                elevation = 4.dp,
                shape = RoundedCornerShape(12.dp),
                backgroundColor = colors.blanco
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    InfoRow(
                        icon = {
                            Icon(
                                imageVector = Icons.Default.Person,
                                contentDescription = "Nombre",
                                tint = colors.lila
                            )
                        },
                        title = "Nombre",
                        value = usuario.nombre // Usa el usuario del estado
                    )
                    Divider(modifier = Modifier.padding(vertical = 8.dp))
                    InfoRow(
                        icon = {
                            Icon(
                                imageVector = Icons.Default.Email,
                                contentDescription = "Email",
                                tint = colors.lila
                            )
                        },
                        title = "Email",
                        value = usuario.email // Usa el usuario del estado
                    )
                    Divider(modifier = Modifier.padding(vertical = 8.dp))
                    InfoRow(
                        icon = {
                            Icon(
                                imageVector = Icons.Default.Phone,
                                contentDescription = "Teléfono",
                                tint = colors.lila
                            )
                        },
                        title = "Teléfono",
                        value = usuario.telefono // Usa el usuario del estado
                    )
                    Divider(modifier = Modifier.padding(vertical = 8.dp))
                    InfoRow(
                        icon = {
                            Icon(
                                imageVector = Icons.Default.LocationOn,
                                contentDescription = "Dirección",
                                tint = colors.lila
                            )
                        },
                        title = "Dirección",
                        value = usuario.direccion // Usa el usuario del estado
                    )
                }
            }

            // Botones de acciones
            Column(
                modifier = Modifier
                    .width(500.dp)
                    .padding(vertical = 20.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Button(
                    onClick = { showEditDialog = true },
                    modifier = Modifier
                        .width(500.dp)
                        .height(50.dp),
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.buttonColors(
                        backgroundColor = colors.lila,
                        contentColor = colors.blanco
                    )
                ) {
                    Text("Editar Perfil", fontSize = 16.sp)
                }

                Button(
                    onClick = {
                        navigator.push(LoginScreen())
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(50.dp),
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.buttonColors(
                        backgroundColor = colors.blanco,
                        contentColor = colors.lila
                    ),
                    border = BorderStroke(1.dp, colors.lila)
                ) {
                    Text("Cerrar Sesión", fontSize = 16.sp)
                }
            }
        }

        // Dialog de edición
        if (showEditDialog) {
            EditProfileDialog(
                usuario = usuario,
                onDismiss = { showEditDialog = false },
                onSave = { nombre, email, telefono, direccion ->
                    // Crear un nuevo objeto Usuario con los datos actualizados para la API
                    val updatedUser = modelo.Usuario(
                        id = usuario.id,
                        nombre = nombre,
                        email = email,
                        telefono = telefono,
                        direccion = direccion
                    )

                    // Debug para verificar qué datos estamos enviando
                    println("Enviando datos para actualizar: $updatedUser")

                    // Llamar a la API para actualizar el perfil
                    apiEditProfile(
                        userId = usuario.id,
                        nombre = nombre,
                        email = email,
                        telefono = telefono,
                        direccion = direccion
                    ) { success, updatedUserResponse ->
                        if (success) {
                            // IMPORTANTE: Actualizar el estado con los nuevos datos
                            // Esto forzará la recomposición de la interfaz
                            if (updatedUserResponse != null) {
                                usuario = updatedUserResponse
                                println("Usuario actualizado en UI: $usuario")
                            } else {
                                // Si la API devuelve success pero no devuelve el usuario,
                                // usar los datos que enviamos
                                usuario = updatedUser
                                println("Usuario actualizado en UI (con datos locales): $usuario")
                            }

                            showSnackbar = true
                            snackbarMessage = "Perfil actualizado correctamente"
                        } else {
                            showSnackbar = true
                            snackbarMessage = "Error al actualizar el perfil"
                        }
                        showEditDialog = false
                    }
                },
                colors = colors
            )
        }

        // Mostrar Snackbar con mensaje de éxito o error
        if (showSnackbar) {
            Snackbar(
                modifier = Modifier.padding(16.dp),
                action = {
                    TextButton(onClick = { showSnackbar = false }) {
                        Text("OK")
                    }
                }
            ) {
                Text(snackbarMessage)
            }
        }
    }

    @Composable
    fun InfoRow(icon: @Composable () -> Unit, title: String, value: String) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(modifier = Modifier.size(24.dp)) {
                icon()
            }
            Spacer(modifier = Modifier.width(16.dp))
            Column {
                Text(
                    title,
                    style = TextStyle(
                        fontSize = 12.sp,
                        color = Color.Gray
                    )
                )
                Text(
                    value,
                    style = TextStyle(
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Medium
                    ),
                    modifier = Modifier.padding(top = 4.dp)
                )
            }
        }
    }
}
@Composable
fun EditProfileDialog(
    usuario: modelo.Usuario,
    onDismiss: () -> Unit,
    onSave: (String, String, String, String) -> Unit,
    colors: ProfileColors
) {
    // Estados para los campos de texto
    var nombre by remember { mutableStateOf(usuario.nombre) }
    var email by remember { mutableStateOf(usuario.email) }
    var telefono by remember { mutableStateOf(usuario.telefono) }
    var direccion by remember { mutableStateOf(usuario.direccion) }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Text(
                text = "Editar Perfil",
                style = TextStyle(
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = colors.negro
                ),
                modifier = Modifier.fillMaxWidth(),
                textAlign = TextAlign.Center
            )
        },
        text = {
            Column(
                modifier = Modifier.padding(8.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                // Campo de nombre
                OutlinedTextField(
                    value = nombre,
                    onValueChange = { nombre = it },
                    label = { Text("Nombre", color = colors.negro.copy(alpha = 0.7f)) },
                    leadingIcon = {
                        Icon(
                            Icons.Default.Person,
                            contentDescription = "Nombre",
                            tint = colors.lila
                        )
                    },
                    colors = TextFieldDefaults.outlinedTextFieldColors(
                        focusedBorderColor = colors.lila,
                        unfocusedBorderColor = colors.negro.copy(alpha = 0.3f),
                        cursorColor = colors.lila,
                        textColor = colors.negro
                    ),
                    modifier = Modifier.fillMaxWidth()
                )

                // Campo de email
                OutlinedTextField(
                    value = email,
                    onValueChange = { email = it },
                    label = { Text("Email", color = colors.negro.copy(alpha = 0.7f)) },
                    leadingIcon = {
                        Icon(
                            Icons.Default.Email,
                            contentDescription = "Email",
                            tint = colors.lila
                        )
                    },
                    colors = TextFieldDefaults.outlinedTextFieldColors(
                        focusedBorderColor = colors.lila,
                        unfocusedBorderColor = colors.negro.copy(alpha = 0.3f),
                        cursorColor = colors.lila,
                        textColor = colors.negro
                    ),
                    modifier = Modifier.fillMaxWidth()
                )

                // Campo de teléfono
                OutlinedTextField(
                    value = telefono,
                    onValueChange = { telefono = it },
                    label = { Text("Teléfono", color = colors.negro.copy(alpha = 0.7f)) },
                    leadingIcon = {
                        Icon(
                            Icons.Default.Phone,
                            contentDescription = "Teléfono",
                            tint = colors.lila
                        )
                    },
                    colors = TextFieldDefaults.outlinedTextFieldColors(
                        focusedBorderColor = colors.lila,
                        unfocusedBorderColor = colors.negro.copy(alpha = 0.3f),
                        cursorColor = colors.lila,
                        textColor = colors.negro
                    ),
                    modifier = Modifier.fillMaxWidth()
                )

                // Campo de dirección
                OutlinedTextField(
                    value = direccion,
                    onValueChange = { direccion = it },
                    label = { Text("Dirección", color = colors.negro.copy(alpha = 0.7f)) },
                    leadingIcon = {
                        Icon(
                            Icons.Default.LocationOn,
                            contentDescription = "Dirección",
                            tint = colors.lila
                        )
                    },
                    colors = TextFieldDefaults.outlinedTextFieldColors(
                        focusedBorderColor = colors.lila,
                        unfocusedBorderColor = colors.negro.copy(alpha = 0.3f),
                        cursorColor = colors.lila,
                        textColor = colors.negro
                    ),
                    modifier = Modifier.fillMaxWidth()
                )
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    onSave(
                        nombre.trim(),
                        email.trim(),
                        telefono.trim(),
                        direccion.trim()
                    )
                },
                shape = RoundedCornerShape(8.dp),
                colors = ButtonDefaults.buttonColors(
                    backgroundColor = colors.lila,
                    contentColor = colors.blanco
                )
            ) {
                Text("Guardar")
            }
        },
        dismissButton = {
            OutlinedButton(
                onClick = onDismiss,
                border = BorderStroke(1.dp, colors.lila),
                shape = RoundedCornerShape(8.dp),
                colors = ButtonDefaults.outlinedButtonColors(
                    contentColor = colors.lila
                )
            ) {
                Text("Cancelar")
            }
        },
        backgroundColor = colors.blanco,
        shape = RoundedCornerShape(16.dp)
    )
}