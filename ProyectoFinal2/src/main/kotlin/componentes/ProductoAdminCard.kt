package componentes

import androidx.compose.runtime.Composable
import androidx.compose.foundation.background
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.ui.text.font.FontWeight
import modelo.Categoria
import modelo.Marca
import modelo.ProductoCompleto

@Composable
fun ProductoAdminCard(
    producto: ProductoCompleto,
    categorias: List<Categoria>,
    marcas: List<Marca>,
    onEditar: () -> Unit,
    onEliminar: () -> Unit,
    colors: Triple<Color, Color, Color>
) {
    val (blanco, negro, pastel) = colors

    val categoriaNombre = categorias.find { it.id == producto.id_categoria }?.nombre ?: "Sin categoría"
    val marcaNombre = marcas.find { it.id == producto.id_marca }?.nombre ?: "Sin marca"

    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = blanco.copy(alpha = 0.6f))
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = producto.nombre,
                    style = MaterialTheme.typography.titleMedium,
                    color = negro,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = "$${String.format("%.2f", producto.precio)}",
                    style = MaterialTheme.typography.bodyMedium,
                    color = negro,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = "$categoriaNombre • $marcaNombre",
                    style = MaterialTheme.typography.bodySmall,
                    color = negro.copy(alpha = 0.7f)
                )
            }

            Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                IconButton(
                    onClick = onEditar,
                    modifier = Modifier
                        .size(36.dp)
                        .background(pastel.copy(alpha = 0.7f), CircleShape)
                ) {
                    Icon(
                        imageVector = Icons.Default.Edit,
                        contentDescription = "Editar",
                        tint = negro,
                        modifier = Modifier.size(20.dp)
                    )
                }

                IconButton(
                    onClick = onEliminar,
                    modifier = Modifier
                        .size(36.dp)
                        .background(pastel.copy(alpha = 0.7f), CircleShape)
                ) {
                    Icon(
                        imageVector = Icons.Default.Delete,
                        contentDescription = "Eliminar",
                        tint = Color.Red,
                        modifier = Modifier.size(20.dp)
                    )
                }
            }
        }
    }
}