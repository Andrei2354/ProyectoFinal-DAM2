package componentes

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.window.Dialog
import modelo.*
import androidx.compose.material3.ExperimentalMaterial3Api

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditarProductoPopup(
    producto: ProductoCompleto,
    categorias: List<Categoria>,
    marcas: List<Marca>,
    onDismiss: () -> Unit,
    onGuardar: (ProductoCompleto) -> Unit,
    colors: Triple<Color, Color, Color>
) {
    val (blanco, negro, pastel) = colors

    var nombre by remember { mutableStateOf(producto.nombre) }
    var descripcion by remember { mutableStateOf(producto.descripcion) }
    var precio by remember { mutableStateOf(producto.precio.toString()) }
    var imagenUrl by remember { mutableStateOf(producto.imagen_url) }
    var categoriaSeleccionada by remember { mutableStateOf(producto.id_categoria) }
    var marcaSeleccionada by remember { mutableStateOf(producto.id_marca) }

    Dialog(onDismissRequest = onDismiss) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            colors = CardDefaults.cardColors(containerColor = blanco)
        ) {
            Column(
                modifier = Modifier
                    .padding(20.dp)
                    .verticalScroll(rememberScrollState()),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Text(
                    text = "Editar Producto",
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
                    value = descripcion,
                    onValueChange = { descripcion = it },
                    label = { Text("Descripción") },
                    modifier = Modifier.fillMaxWidth(),
                    maxLines = 3
                )

                OutlinedTextField(
                    value = precio,
                    onValueChange = { precio = it },
                    label = { Text("Precio") },
                    modifier = Modifier.fillMaxWidth(),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal)
                )

                OutlinedTextField(
                    value = imagenUrl,
                    onValueChange = { imagenUrl = it },
                    label = { Text("URL de la imagen") },
                    modifier = Modifier.fillMaxWidth()
                )

                var expandedCategoria by remember { mutableStateOf(false) }
                ExposedDropdownMenuBox(
                    expanded = expandedCategoria,
                    onExpandedChange = { expandedCategoria = !expandedCategoria }
                ) {
                    OutlinedTextField(
                        value = categorias.find { it.id == categoriaSeleccionada }?.nombre ?: "",
                        onValueChange = { },
                        readOnly = true,
                        label = { Text("Categoría") },
                        trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expandedCategoria) },
                        modifier = Modifier
                            .fillMaxWidth()
                            .menuAnchor()
                    )
                    ExposedDropdownMenu(
                        expanded = expandedCategoria,
                        onDismissRequest = { expandedCategoria = false }
                    ) {
                        categorias.forEach { categoria ->
                            DropdownMenuItem(
                                text = { Text(categoria.nombre) },
                                onClick = {
                                    categoriaSeleccionada = categoria.id
                                    expandedCategoria = false
                                }
                            )
                        }
                    }
                }

                var expandedMarca by remember { mutableStateOf(false) }
                ExposedDropdownMenuBox(
                    expanded = expandedMarca,
                    onExpandedChange = { expandedMarca = !expandedMarca }
                ) {
                    OutlinedTextField(
                        value = marcas.find { it.id == marcaSeleccionada }?.nombre ?: "",
                        onValueChange = { },
                        readOnly = true,
                        label = { Text("Marca") },
                        trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expandedMarca) },
                        modifier = Modifier
                            .fillMaxWidth()
                            .menuAnchor()
                    )
                    ExposedDropdownMenu(
                        expanded = expandedMarca,
                        onDismissRequest = { expandedMarca = false }
                    ) {
                        marcas.forEach { marca ->
                            DropdownMenuItem(
                                text = { Text(marca.nombre) },
                                onClick = {
                                    marcaSeleccionada = marca.id
                                    expandedMarca = false
                                }
                            )
                        }
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
                            val precioDouble = precio.toDoubleOrNull() ?: producto.precio
                            val productoEditado = producto.copy(
                                nombre = nombre,
                                descripcion = descripcion,
                                precio = precioDouble,
                                imagen_url = imagenUrl,
                                id_categoria = categoriaSeleccionada,
                                id_marca = marcaSeleccionada
                            )
                            onGuardar(productoEditado)
                        },
                        modifier = Modifier.weight(1f),
                        colors = ButtonDefaults.buttonColors(containerColor = pastel)
                    ) {
                        Text("Guardar", color = negro)
                    }
                }
            }
        }
    }
}