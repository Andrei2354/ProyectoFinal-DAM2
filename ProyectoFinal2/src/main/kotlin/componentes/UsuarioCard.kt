package componentes

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import modelo.UsuarioCompleto

@Composable
fun UsuarioCard(
    usuario: UsuarioCompleto,
    onEditar: () -> Unit,
    onEliminar: () -> Unit,
    colors: Triple<Color, Color, Color>
) {
    val (blanco, negro, pastel) = colors

    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = blanco)
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
                    text = usuario.nombre,
                    style = MaterialTheme.typography.titleMedium,
                    color = negro,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = usuario.email,
                    style = MaterialTheme.typography.bodyMedium,
                    color = negro.copy(alpha = 0.7f)
                )
                Text(
                    text = "Rol: ${usuario.rol}",
                    style = MaterialTheme.typography.bodySmall,
                    color = if (usuario.rol == "admin") Color.Red else negro.copy(alpha = 0.5f)
                )
            }

            Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                IconButton(
                    onClick = onEditar,
                    modifier = Modifier
                        .size(36.dp)
                        .background(pastel, CircleShape)
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