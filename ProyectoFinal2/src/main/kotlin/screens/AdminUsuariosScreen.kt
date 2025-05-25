package screens

import androidx.compose.runtime.Composable
import androidx.compose.foundation.background
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.core.screen.Screen
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import modelo.Usuario
import modelo.UsuarioCompleto
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.text.font.FontWeight
import componentes.EditarUsuarioPopup
import componentes.UsuarioCard
import kotlinx.coroutines.launch
import network.apiEditarUsuario
import network.apiEliminarUsuario
import network.apiGetTodosUsuarios

class AdminUsuariosScreen(val usuario: Usuario): Screen {
    @Composable
    override fun Content() {
        val navigator = LocalNavigator.current
        val purpura = Color(0xFFa69eb0)
        val blanco = Color(0xFFefeff2)
        val negro = Color(0xFF011f4b)
        val pastel = Color(0xFFf2e2cd)

        var usuarios by remember { mutableStateOf<List<UsuarioCompleto>>(emptyList()) }
        var cargando by remember { mutableStateOf(true) }
        var mostrarPopupEditar by remember { mutableStateOf(false) }
        var usuarioSeleccionado by remember { mutableStateOf<UsuarioCompleto?>(null) }
        var mostrarDialogoEliminar by remember { mutableStateOf(false) }

        val snackbarHostState = remember { SnackbarHostState() }
        val scope = rememberCoroutineScope()

        LaunchedEffect(Unit) {
            apiGetTodosUsuarios { usuariosRecibidos ->
                usuarios = usuariosRecibidos
                cargando = false
            }
        }

        Box(modifier = Modifier.fillMaxSize()) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .background(purpura)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    IconButton(
                        onClick = { navigator?.pop() },
                        modifier = Modifier
                            .size(40.dp)
                            .background(blanco, CircleShape)
                    ) {
                        Icon(
                            imageVector = Icons.Default.ArrowBack,
                            contentDescription = "Volver",
                            tint = negro
                        )
                    }

                    Text(
                        text = "Gestión de Usuarios",
                        style = MaterialTheme.typography.headlineMedium,
                        color = blanco,
                        fontWeight = FontWeight.Bold
                    )

                    Spacer(modifier = Modifier.width(40.dp))
                }

                if (cargando) {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator(color = blanco)
                    }
                } else {
                    LazyColumn(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(16.dp),
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        items(usuarios) { usuarioItem ->
                            UsuarioCard(
                                usuario = usuarioItem,
                                onEditar = {
                                    usuarioSeleccionado = usuarioItem
                                    mostrarPopupEditar = true
                                },
                                onEliminar = {
                                    usuarioSeleccionado = usuarioItem
                                    mostrarDialogoEliminar = true
                                },
                                colors = Triple(blanco, negro, pastel)
                            )
                        }
                    }
                }
            }

            if (mostrarPopupEditar && usuarioSeleccionado != null) {
                EditarUsuarioPopup(
                    usuario = usuarioSeleccionado!!,
                    onDismiss = {
                        mostrarPopupEditar = false
                        usuarioSeleccionado = null
                    },
                    onGuardar = { usuarioEditado ->
                        apiEditarUsuario(usuarioEditado) { success, message ->
                            scope.launch {
                                snackbarHostState.showSnackbar(message)
                                if (success) {
                                    // Actualizar lista
                                    usuarios = usuarios.map {
                                        if (it.id == usuarioEditado.id) usuarioEditado else it
                                    }
                                    mostrarPopupEditar = false
                                    usuarioSeleccionado = null
                                }
                            }
                        }
                    },
                    colors = Triple(blanco, negro, pastel)
                )
            }

            if (mostrarDialogoEliminar && usuarioSeleccionado != null) {
                AlertDialog(
                    onDismissRequest = {
                        mostrarDialogoEliminar = false
                        usuarioSeleccionado = null
                    },
                    title = { Text("Confirmar eliminación") },
                    text = { Text("¿Estás seguro de que quieres eliminar al usuario ${usuarioSeleccionado!!.nombre}?") },
                    confirmButton = {
                        TextButton(
                            onClick = {
                                apiEliminarUsuario(usuarioSeleccionado!!.id) { success, message ->
                                    scope.launch {
                                        snackbarHostState.showSnackbar(message)
                                        if (success) {
                                            usuarios = usuarios.filter { it.id != usuarioSeleccionado!!.id }
                                        }
                                        mostrarDialogoEliminar = false
                                        usuarioSeleccionado = null
                                    }
                                }
                            }
                        ) {
                            Text("Eliminar", color = Color.Red)
                        }
                    },
                    dismissButton = {
                        TextButton(
                            onClick = {
                                mostrarDialogoEliminar = false
                                usuarioSeleccionado = null
                            }
                        ) {
                            Text("Cancelar")
                        }
                    }
                )
            }

            SnackbarHost(
                hostState = snackbarHostState,
                modifier = Modifier.align(Alignment.BottomCenter)
            )
        }
    }
}