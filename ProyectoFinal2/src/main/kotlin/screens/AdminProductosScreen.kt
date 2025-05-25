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
import modelo.*
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
import componentes.EditarProductoPopup
import componentes.ProductoAdminCard
import kotlinx.coroutines.launch
import network.*

class AdminProductosScreen(val usuario: Usuario): Screen {
    @Composable
    override fun Content() {
        val navigator = LocalNavigator.current
        val purpura = Color(0xFFa69eb0)
        val blanco = Color(0xFFefeff2)
        val negro = Color(0xFF011f4b)
        val pastel = Color(0xFFf2e2cd)

        var productos by remember { mutableStateOf<List<ProductoCompleto>>(emptyList()) }
        var categorias by remember { mutableStateOf<List<Categoria>>(emptyList()) }
        var marcas by remember { mutableStateOf<List<Marca>>(emptyList()) }
        var cargando by remember { mutableStateOf(true) }
        var mostrarPopupEditar by remember { mutableStateOf(false) }
        var productoSeleccionado by remember { mutableStateOf<ProductoCompleto?>(null) }
        var mostrarDialogoEliminar by remember { mutableStateOf(false) }

        val snackbarHostState = remember { SnackbarHostState() }
        val scope = rememberCoroutineScope()

        LaunchedEffect(Unit) {
            apiGetProductos { productosRecibidos ->
                productos = productosRecibidos.map { producto ->
                    ProductoCompleto(
                        id_producto = producto.id_producto,
                        nombre = producto.nombre,
                        descripcion = producto.descripcion,
                        id_categoria = producto.id_categoria,
                        id_marca = producto.id_marca,
                        precio = producto.precio,
                        imagen_url = producto.imagen_url
                    )
                }
                cargando = false
            }

            apiGetCategorias { categoriasRecibidas ->
                categorias = categoriasRecibidas
            }

            apiGetMarcas { marcasRecibidas ->
                marcas = marcasRecibidas
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
                        text = "Gestión de Productos",
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
                        items(productos) { producto ->
                            ProductoAdminCard(
                                producto = producto,
                                categorias = categorias,
                                marcas = marcas,
                                onEditar = {
                                    productoSeleccionado = producto
                                    mostrarPopupEditar = true
                                },
                                onEliminar = {
                                    productoSeleccionado = producto
                                    mostrarDialogoEliminar = true
                                },
                                colors = Triple(blanco, negro, pastel)
                            )
                        }
                    }
                }
            }

            if (mostrarPopupEditar && productoSeleccionado != null) {
                EditarProductoPopup(
                    producto = productoSeleccionado!!,
                    categorias = categorias,
                    marcas = marcas,
                    onDismiss = {
                        mostrarPopupEditar = false
                        productoSeleccionado = null
                    },
                    onGuardar = { productoEditado ->
                        apiEditarProducto(productoEditado) { success, message ->
                            scope.launch {
                                snackbarHostState.showSnackbar(message)
                                if (success) {
                                    productos = productos.map {
                                        if (it.id_producto == productoEditado.id_producto) productoEditado else it
                                    }
                                    mostrarPopupEditar = false
                                    productoSeleccionado = null
                                }
                            }
                        }
                    },
                    colors = Triple(blanco, negro, pastel)
                )
            }

            if (mostrarDialogoEliminar && productoSeleccionado != null) {
                AlertDialog(
                    onDismissRequest = {
                        mostrarDialogoEliminar = false
                        productoSeleccionado = null
                    },
                    title = { Text("Confirmar eliminación") },
                    text = { Text("¿Estás seguro de que quieres eliminar el producto ${productoSeleccionado!!.nombre}?") },
                    confirmButton = {
                        TextButton(
                            onClick = {
                                apiEliminarProducto(productoSeleccionado!!.id_producto) { success, message ->
                                    scope.launch {
                                        snackbarHostState.showSnackbar(message)
                                        if (success) {
                                            productos = productos.filter { it.id_producto != productoSeleccionado!!.id_producto }
                                        }
                                        mostrarDialogoEliminar = false
                                        productoSeleccionado = null
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
                                productoSeleccionado = null
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