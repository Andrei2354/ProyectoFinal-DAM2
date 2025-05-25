package componentes

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import modelo.UsuarioCompleto

@Composable
fun EditarUsuarioPopup(
    usuario: UsuarioCompleto,
    onDismiss: () -> Unit,
    onGuardar: (UsuarioCompleto) -> Unit,
    colors: Triple<Color, Color, Color>
) {
    val (blanco, negro, pastel) = colors

    var nombre by remember { mutableStateOf(usuario.nombre) }
    var email by remember { mutableStateOf(usuario.email) }
    var direccion by remember { mutableStateOf(usuario.direccion) }
    var telefono by remember { mutableStateOf(usuario.telefono) }
    var rol by remember { mutableStateOf(usuario.rol) }

    Dialog(onDismissRequest = onDismiss) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            colors = CardDefaults.cardColors(containerColor = blanco)
        ) {
            Column(
                modifier = Modifier.padding(20.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Text(
                    text = "Editar Usuario",
                    style = MaterialTheme.typography.titleLarge,
                    color = negro,
                    fontWeight = FontWeight.Bold
                )

                OutlinedTextField(
                    value = nombre,
                    onValueChange = { nombre = it },
                    label = { Text("Nombre") },
                    modifier = Modifier.fillMaxWidth()
                )

                OutlinedTextField(
                    value = email,
                    onValueChange = { email = it },
                    label = { Text("Email") },
                    modifier = Modifier.fillMaxWidth()
                )

                OutlinedTextField(
                    value = direccion,
                    onValueChange = { direccion = it },
                    label = { Text("Dirección") },
                    modifier = Modifier.fillMaxWidth()
                )

                OutlinedTextField(
                    value = telefono,
                    onValueChange = { telefono = it },
                    label = { Text("Teléfono") },
                    modifier = Modifier.fillMaxWidth()
                )

                Column {
                    Text(
                        text = "Rol",
                        style = MaterialTheme.typography.bodyMedium,
                        color = negro.copy(alpha = 0.7f),
                        modifier = Modifier.padding(bottom = 8.dp)
                    )

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        // Botón Usuario
                        FilterChip(
                            onClick = { rol = "usuario" },
                            label = { Text("Usuario") },
                            selected = rol == "usuario",
                            modifier = Modifier.weight(1f),
                            colors = FilterChipDefaults.filterChipColors(
                                selectedContainerColor = pastel.copy(alpha = 0.7f),
                                selectedLabelColor = negro
                            )
                        )

                        FilterChip(
                            onClick = { rol = "admin" },
                            label = { Text("Admin") },
                            selected = rol == "admin",
                            modifier = Modifier.weight(1f),
                            colors = FilterChipDefaults.filterChipColors(
                                selectedContainerColor = Color.Red.copy(alpha = 0.25f),
                                selectedLabelColor = Color.Red
                            )
                        )
                    }
                }

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    TextButton(
                        onClick = onDismiss,
                        modifier = Modifier.weight(1f)
                    ) {
                        Text("Cancelar")
                    }

                    Button(
                        onClick = {
                            val usuarioEditado = usuario.copy(
                                nombre = nombre,
                                email = email,
                                direccion = direccion,
                                telefono = telefono,
                                rol = rol
                            )
                            onGuardar(usuarioEditado)
                        },
                        modifier = Modifier.weight(1f),
                        colors = ButtonDefaults.buttonColors(containerColor = pastel.copy(alpha = 0.8f))
                    ) {
                        Text("Guardar", color = negro)
                    }
                }
            }
        }
    }
}