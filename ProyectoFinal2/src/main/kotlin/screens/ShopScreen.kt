package screens

import androidx.compose.foundation.Image
import androidx.compose.runtime.Composable
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import cafe.adriel.voyager.navigator.LocalNavigator
import androidx.compose.ui.text.TextStyle
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Diamond
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material.icons.filled.AddHome
import androidx.compose.material3.Icon
import cafe.adriel.voyager.core.screen.Screen
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.IconButton
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontFamily


class ShopScreen: Screen {
    @Composable
    override fun Content() {
        val navigator = LocalNavigator.current
        val blanco = Color(0xFFefeff2)
        val lila = Color(0xFFa69eb0)
        val pastel = Color(0xFFf2e2cd)
        val gris = Color(0xFFdadae3)
        val negro = Color(0xFF011f4b)
        val pupura = Color(0xFFa69eb0)

        // Datos de ejemplo para productos
        val productos = listOf(
            Producto("Zapatos deportivos", "$59.99"), /*, R.drawable.zapatos)*/
            Producto("Camiseta básica", "$19.99"),/* R.drawable.camiseta),*/
            Producto("Pantalón jeans", "$39.99"),/* R.drawable.jeans),*/
            Producto("Gorra", "$14.99")/* R.drawable.gorra)*/
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(pupura),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Encabezado
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 0.dp, horizontal = 20.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Image(
                    painter = painterResource("drawable/tekkio.png"),
                    contentDescription = "Logo",
                    modifier = Modifier.size(120.dp)
                )
                /*
                Text(
                    "Tekio",
                    style = TextStyle(
                        fontSize = 42.sp,
                        fontWeight = FontWeight.Bold,
                        color = negro,
                        fontFamily = FontFamily.Serif // Cambia aquí la fuente
                    )
                )
                */
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(20.dp) // Espacio entre iconos
                ) {
                    // Icono de perfil
                    IconButton(
                        onClick = { navigator?.push(ProfileScreen()) },
                        modifier = Modifier
                            .size(40.dp)
                            .border(
                                width = 1.dp,
                                color = pastel,
                                shape = CircleShape
                            )
                    ) {
                        Icon(
                            imageVector = Icons.Default.Person,
                            contentDescription = "Perfil",
                            tint = negro,
                            modifier = Modifier.size(24.dp)
                        )
                    }

                    // Icono de carrito (con badge)
                    Box(contentAlignment = Alignment.Center) {
                        IconButton(
                            onClick = { navigator?.push(CarritoScreen()) },
                            modifier = Modifier.size(40.dp)
                                .size(40.dp)
                                .border(
                                    width = 1.dp,
                                    color = pastel,
                                    shape = CircleShape
                                )
                        ) {
                            Icon(
                                imageVector = Icons.Default.ShoppingCart,
                                contentDescription = "Carrito",
                                tint = negro,
                                modifier = Modifier.size(24.dp)
                            )
                        }
                    }
                }
            }

            // Banner promocional
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(150.dp)
                    .padding(horizontal = 20.dp)
                    .clip(RoundedCornerShape(12.dp))
                    .background(pastel),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    "¡Oferta especial! 20% OFF",
                    style = TextStyle(
                        fontSize = 28.sp,
                        fontWeight = FontWeight.Bold,
                        color = negro
                    )
                )
            }

            // Categorías
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(20.dp),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                CategoriaItem("Ropa", Icons.Default.Person)
                CategoriaItem("Calzado", Icons.Default.AddHome)
                CategoriaItem("Accesorios", Icons.Default.Diamond)
            }

            // Lista de productos destacados
            Text(
                "Productos Destacados",
                modifier = Modifier
                    .align(Alignment.Start)
                    .padding(start = 20.dp, top = 10.dp),
                style = TextStyle(
                    fontSize = 20.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = negro
                )
            )

            LazyVerticalGrid(
                columns = GridCells.Fixed(2),
                modifier = Modifier.padding(horizontal = 10.dp)
            ) {
                /*
                items(productos) { producto ->
                    ProductoCard(producto = producto, onAddToCart = {
                        // Lógica para añadir al carrito
                    })
                }
                */

            }
        }
    }

    @Composable
    fun ProductoCard(producto: Producto, onAddToCart: () -> Unit) {
        val blanco = Color(0xFFefeff2)
        val negro = Color(0xFF011f4b)

        Card(
            modifier = Modifier
                .padding(8.dp)
                .fillMaxWidth()
                .aspectRatio(0.8f),
            elevation = 4.dp,
            shape = RoundedCornerShape(12.dp)
        ) {
            Column {
                /*
                Image(
                    painter = painterResource(id = producto.imagen),
                    contentDescription = producto.nombre,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(120.dp)
                        .clip(RoundedCornerShape(topStart = 12.dp, topEnd = 12.dp)),
                    contentScale = ContentScale.Crop
                )
                */
                Column(modifier = Modifier.padding(8.dp)) {
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
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold,
                            color = negro
                        ),
                        modifier = Modifier.padding(vertical = 4.dp)
                    )

                    Button(
                        onClick = onAddToCart,
                        modifier = Modifier.fillMaxWidth(),
                        colors = ButtonDefaults.buttonColors(
                            backgroundColor = blanco,
                            contentColor = negro
                        ),
                        elevation = ButtonDefaults.elevation(
                            defaultElevation = 0.dp,
                            pressedElevation = 0.dp
                        )
                    ) {
                        Text("Añadir al carrito")
                    }
                }
            }
        }
    }

    @Composable
    fun CategoriaItem(nombre: String, icon: ImageVector) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.clickable { /* Navegar a categoría */ }
        ) {
            Box(
                modifier = Modifier
                    .size(60.dp)
                    .clip(CircleShape)
                    .background(Color(0xFFa69eb0))
                    .padding(12.dp),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = nombre,
                    tint = Color.White,
                    modifier = Modifier.size(24.dp)
                )
            }
            Text(
                nombre,
                modifier = Modifier.padding(top = 4.dp),
                style = TextStyle(fontSize = 14.sp)
            )
        }
    }

    data class Producto(
        val nombre: String,
        val precio: String,
        /*@DrawableRes val imagen: Int*/
    )
}