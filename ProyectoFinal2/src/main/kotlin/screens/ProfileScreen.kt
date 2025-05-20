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
import androidx.compose.ui.window.Dialog

class ProfileScreen : Screen {
    @Composable
    override fun Content(){
        val navigator = LocalNavigator.currentOrThrow
        val pastel = Color(0xFFf2e2cd)
        val lila = Color(0xFFa69eb0)
        val negro = Color(0xFF011f4b)
        val blanco = Color(0xFFefeff2)
        val pupura = Color(0xFFa69eb0)

        // Estado para manejar los datos del usuario
        var usuario by remember {
            mutableStateOf(
                Usuario(
                    nombre = "Ana García",
                    email = "ana@tienda.com",
                    telefono = "+34 612 345 678",
                    direccion = "Calle Principal 123, Madrid"
                )
            )
        }

        // Estado para controlar la visibilidad del dialog
        var showEditDialog by remember { mutableStateOf(false) }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(pupura)
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
                        tint = negro
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
                        color = negro
                    )
                )
                Spacer(modifier = Modifier.size(40.dp))
                Box(
                    modifier = Modifier
                        .size(120.dp)
                        .clip(CircleShape)
                        .background(pastel.copy(alpha = 0.3f))
                        .border(2.dp, pastel, CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.Person,
                        contentDescription = "Avatar",
                        tint = negro,
                        modifier = Modifier.size(60.dp)
                    )
                }
                Spacer(modifier = Modifier.height(16.dp))
                Text(
                    usuario.nombre,
                    style = TextStyle(
                        fontSize = 22.sp,
                        fontWeight = FontWeight.Bold,
                        color = negro
                    )
                )
                Text(
                    "Miembro desde 2023",
                    style = TextStyle(
                        fontSize = 14.sp,
                        color = negro.copy(alpha = 0.6f)
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
                backgroundColor = blanco
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    InfoRow(Icons.Default.Email, "Email", usuario.email)
                    Divider(modifier = Modifier.padding(vertical = 8.dp))
                    InfoRow(Icons.Default.Phone, "Teléfono", usuario.telefono)
                    Divider(modifier = Modifier.padding(vertical = 8.dp))
                    InfoRow(Icons.Default.LocationOn, "Dirección", usuario.direccion)
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
                        backgroundColor = lila,
                        contentColor = blanco
                    )
                ) {
                    Text("Editar Perfil", fontSize = 16.sp)
                }

                Button(
                    onClick = { /* Cerrar sesión */ },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(50.dp),
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.buttonColors(
                        backgroundColor = blanco,
                        contentColor = lila
                    ),
                    border = BorderStroke(1.dp, lila)
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
                onSave = { usuarioActualizado ->
                    usuario = usuarioActualizado
                    showEditDialog = false
                },
                colors = ProfileColors(
                    pastel = pastel,
                    lila = lila,
                    negro = negro,
                    blanco = blanco
                )
            )
        }
    }

    @Composable
    fun InfoRow(icon: ImageVector, title: String, value: String) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = icon,
                contentDescription = title,
                tint = Color(0xFFa69eb0),
                modifier = Modifier.size(24.dp)
            )
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

    data class Usuario(
        val nombre: String,
        val email: String,
        val telefono: String,
        val direccion: String
    )

    data class ProfileColors(
        val pastel: Color,
        val lila: Color,
        val negro: Color,
        val blanco: Color
    )
}

@Composable
fun EditProfileDialog(
    usuario: ProfileScreen.Usuario,
    onDismiss: () -> Unit,
    onSave: (ProfileScreen.Usuario) -> Unit,
    colors: ProfileScreen.ProfileColors
) {
    // Estados para los campos de texto
    var nombre by remember { mutableStateOf(usuario.nombre) }
    var email by remember { mutableStateOf(usuario.email) }
    var telefono by remember { mutableStateOf(usuario.telefono) }
    var direccion by remember { mutableStateOf(usuario.direccion) }

    Dialog(onDismissRequest = onDismiss) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            shape = RoundedCornerShape(16.dp),
            backgroundColor = colors.blanco,
            elevation = 8.dp
        ) {
            Column(
                modifier = Modifier.padding(20.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                // Título del dialog
                Text(
                    text = "Editar Perfil",
                    style = TextStyle(
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        color = colors.negro
                    ),
                    modifier = Modifier.align(Alignment.CenterHorizontally)
                )

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

                // Botones de acción
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    // Botón Cancelar
                    OutlinedButton(
                        onClick = onDismiss,
                        modifier = Modifier.weight(1f),
                        border = BorderStroke(1.dp, colors.lila),
                        shape = RoundedCornerShape(8.dp),
                        colors = ButtonDefaults.outlinedButtonColors(
                            contentColor = colors.lila
                        )
                    ) {
                        Text("Cancelar")
                    }

                    // Botón Guardar
                    Button(
                        onClick = {
                            onSave(
                                ProfileScreen.Usuario(
                                    nombre = nombre.trim(),
                                    email = email.trim(),
                                    telefono = telefono.trim(),
                                    direccion = direccion.trim()
                                )
                            )
                        },
                        modifier = Modifier.weight(1f),
                        shape = RoundedCornerShape(8.dp),
                        colors = ButtonDefaults.buttonColors(
                            backgroundColor = colors.lila,
                            contentColor = colors.blanco
                        )
                    ) {
                        Text("Guardar")
                    }
                }
            }
        }
    }
}