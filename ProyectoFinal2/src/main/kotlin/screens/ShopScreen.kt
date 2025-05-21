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
import modelo.Usuario


class ShopScreen(val user: Usuario): Screen {
    @Composable
    override fun Content() {
        val navigator = LocalNavigator.current
        val blanco = Color(0xFFefeff2)
        val lila = Color(0xFFa69eb0)
        val pastel = Color(0xFFf2e2cd)
        val gris = Color(0xFFdadae3)
        val negro = Color(0xFF011f4b)
        val pupura = Color(0xFFa69eb0)


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
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(20.dp) // Espacio entre iconos
                ) {
                    // Icono de perfil
                    IconButton(
                        onClick = { navigator?.push(ProfileScreen(user)) },
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
        }
    }
}