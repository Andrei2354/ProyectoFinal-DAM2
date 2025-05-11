package screens

import androidx.compose.runtime.Composable
import cafe.adriel.voyager.core.screen.Screen
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
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
import androidx.compose.ui.layout.ContentScale
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.*
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Remove
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextDecoration
import cafe.adriel.voyager.navigator.currentOrThrow

class CarritoScreen : Screen {
    @Composable
    override fun Content(){
        val navigator = LocalNavigator.currentOrThrow
        val lila = Color(0xFFa69eb0)
        val pastel = Color(0xFFf2e2cd)
        val negro = Color(0xFF011f4b)
        val blanco = Color(0xFFefeff2)
        val pupura = Color(0xFFa69eb0)

        // Estado del carrito (simulado)
        val carrito = listOf(
            ProductoCarrito("Zapatos deportivos", "$59.99", 1),/* R.drawable.zapatos),*/
            ProductoCarrito("Camiseta básica", "$19.99", 2),/* R.drawable.camiseta),*/
            ProductoCarrito("Gorra", "$14.99", 1)/*, R.drawable.gorra)*/
        )
        val subtotal = carrito.sumOf { it.precio.removePrefix("$").toDouble() * it.cantidad }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(pupura)
                .padding(horizontal = 16.dp)
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
                Text(
                    "Mi Carrito",
                    style = TextStyle(
                        fontSize = 24.sp,
                        fontWeight = FontWeight.Bold,
                        color = negro
                    )
                )
                Badge(
                    modifier = Modifier.size(40.dp),
                    backgroundColor = lila,
                    contentColor = blanco
                ) {
                    Text(carrito.size.toString())
                }
            }

            // Lista de productos
            /*
            LazyColumn(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(carrito) { producto ->
                    ItemCarrito(
                        producto = producto,
                        onIncrease = {  },
                        onDecrease = {  },
                        onRemove = { }
                    )
                }
            }*/

            // Resumen de compra
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 20.dp),
                elevation = 8.dp,
                shape = RoundedCornerShape(12.dp),
                backgroundColor = blanco
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text("Subtotal", style = TextStyle(fontSize = 16.sp))
                        Text("$${"%.2f".format(subtotal)}", style = TextStyle(fontWeight = FontWeight.Bold))
                    }
                    Spacer(modifier = Modifier.height(8.dp))
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text("Envío", style = TextStyle(fontSize = 16.sp))
                        Text("Gratis", style = TextStyle(color = negro))
                    }
                    Divider(modifier = Modifier.padding(vertical = 12.dp))
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(
                            "Total",
                            style = TextStyle(
                                fontSize = 18.sp,
                                fontWeight = FontWeight.Bold
                            )
                        )
                        Text(
                            "$${"%.2f".format(subtotal)}",
                            style = TextStyle(
                                fontSize = 18.sp,
                                fontWeight = FontWeight.Bold,
                                color = lila
                            )
                        )
                    }
                    Spacer(modifier = Modifier.height(16.dp))
                    Button(
                        onClick = {},
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(50.dp),
                        shape = RoundedCornerShape(12.dp),
                        colors = ButtonDefaults.buttonColors(
                            backgroundColor = lila,
                            contentColor = blanco
                        )
                    ) {
                        Text("Proceder al pago", fontSize = 16.sp)
                    }
                }
            }
        }
    }

    @Composable
    fun ItemCarrito(
        producto: ProductoCarrito,
        onIncrease: () -> Unit,
        onDecrease: () -> Unit,
        onRemove: () -> Unit
    ) {
        val lila = Color(0xFFa69eb0)
        val negro = Color(0xFF011f4b)

        Card(
            modifier = Modifier.fillMaxWidth(),
            elevation = 2.dp,
            shape = RoundedCornerShape(12.dp)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(12.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                /*
                Image(
                    painter = painterResource(id = producto.imagen),
                    contentDescription = producto.nombre,
                    modifier = Modifier
                        .size(80.dp)
                        .clip(RoundedCornerShape(8.dp)),
                    contentScale = ContentScale.Crop
                )*/

                Column(
                    modifier = Modifier
                        .weight(1f)
                        .padding(horizontal = 12.dp)
                ) {
                    Text(
                        producto.nombre,
                        style = TextStyle(
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Medium
                        )
                    )
                    Text(
                        producto.precio,
                        style = TextStyle(
                            fontSize = 14.sp,
                            color = negro.copy(alpha = 0.7f)
                        ),
                        modifier = Modifier.padding(top = 4.dp)
                    )
                }

                Column(
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {

                    Row(
                        modifier = Modifier
                            .border(1.dp, lila, RoundedCornerShape(20.dp))
                            .padding(horizontal = 8.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        IconButton(
                            onClick = onDecrease,
                            modifier = Modifier.size(24.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.Remove,
                                contentDescription = "Reducir",
                                tint = lila,
                                modifier = Modifier.size(16.dp)
                            )
                        }
                        Text(
                            producto.cantidad.toString(),
                            modifier = Modifier.padding(horizontal = 4.dp),
                            fontSize = 16.sp
                        )
                        IconButton(
                            onClick = onIncrease,
                            modifier = Modifier.size(24.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.Add,
                                contentDescription = "Aumentar",
                                tint = lila,
                                modifier = Modifier.size(16.dp)
                            )
                        }
                    }

                    // Botón eliminar
                    Text(
                        "Eliminar",
                        style = TextStyle(
                            color = lila,
                            fontSize = 12.sp,
                            textDecoration = TextDecoration.Underline
                        ),
                        modifier = Modifier
                            .padding(top = 8.dp)
                            .clickable(onClick = onRemove)
                    )
                }
            }
        }
    }

    data class ProductoCarrito(
        val nombre: String,
        val precio: String,
        var cantidad: Int,
        /*@DrawableRes val imagen: Int*/
    )
}