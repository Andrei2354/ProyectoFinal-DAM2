package screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import modelo.Producto
import modelo.Usuario
import network.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.clickable
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.ui.text.style.TextOverflow
import kotlinx.coroutines.launch

class ProductoDetalleScreen(val producto: Producto, val usuario: Usuario? = null): Screen {
    @Composable
    override fun Content() {
        val navigator = LocalNavigator.current
        val blanco = Color(0xFFefeff2)
        val lila = Color(0xFFa69eb0)
        val pastel = Color(0xFFf2e2cd)
        val gris = Color(0xFFdadae3)
        val negro = Color(0xFF011f4b)
        val purpura = Color(0xFFa69eb0)

        var productosRelacionadosMarca by remember { mutableStateOf<List<Producto>>(emptyList()) }
        var productosRelacionadosCategoria by remember { mutableStateOf<List<Producto>>(emptyList()) }
        var cargandoRelacionados by remember { mutableStateOf(true) }

        val snackbarHostState = remember { SnackbarHostState() }
        val scope = rememberCoroutineScope()

        LaunchedEffect(producto.id_producto) {
            apiGetProductos { todosLosProductos ->
                productosRelacionadosMarca = todosLosProductos
                    .filter { it.id_marca == producto.id_marca && it.id_producto != producto.id_producto }
                    .take(5)

                productosRelacionadosCategoria = todosLosProductos
                    .filter { it.id_categoria == producto.id_categoria && it.id_producto != producto.id_producto }
                    .take(5)

                cargandoRelacionados = false
            }
        }

        Box(modifier = Modifier.fillMaxSize()) {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .background(purpura),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                item {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 16.dp, horizontal = 20.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        IconButton(
                            onClick = {
                                usuario?.let { user ->
                                    navigator?.push(ShopScreen(user))
                                }
                            },
                            modifier = Modifier
                                .size(40.dp)
                                .border(
                                    width = 1.dp,
                                    color = pastel,
                                    shape = CircleShape
                                )
                        ) {
                            Icon(
                                imageVector = Icons.Default.ArrowBack,
                                contentDescription = "Volver",
                                tint = negro,
                                modifier = Modifier.size(24.dp)
                            )
                        }

                        Text(
                            text = "Detalle del Producto",
                            color = blanco,
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold
                        )

                        IconButton(
                            onClick = {
                                usuario?.let { user ->
                                    navigator?.push(CarritoScreen(user))
                                }
                            },
                            modifier = Modifier
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

                item {
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 20.dp)
                            .padding(bottom = 20.dp),
                        shape = RoundedCornerShape(16.dp),
                        colors = CardDefaults.cardColors(
                            containerColor = blanco
                        ),
                        elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
                    ) {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(20.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(bottom = 20.dp),
                                horizontalArrangement = Arrangement.spacedBy(16.dp)
                            ) {
                                Card(
                                    modifier = Modifier
                                        .size(250.dp),
                                    shape = RoundedCornerShape(12.dp),
                                    colors = CardDefaults.cardColors(
                                        containerColor = gris
                                    )
                                ) {
                                    Box(
                                        modifier = Modifier.fillMaxSize(),
                                        contentAlignment = Alignment.Center
                                    ) {
                                        if (producto.imagen_url.isNotEmpty()) {
                                            LoadImage(
                                                url = producto.imagen_url,
                                                contentDescription = producto.nombre,
                                                modifier = Modifier
                                                    .fillMaxSize()
                                                    .clip(RoundedCornerShape(12.dp)),
                                                contentScale = ContentScale.Crop,
                                                loadingContent = {
                                                    Box(
                                                        modifier = Modifier.fillMaxSize(),
                                                        contentAlignment = Alignment.Center
                                                    ) {
                                                        CircularProgressIndicator(
                                                            color = purpura,
                                                            modifier = Modifier.size(30.dp)
                                                        )
                                                    }
                                                },
                                                errorContent = {
                                                    Box(
                                                        modifier = Modifier.fillMaxSize(),
                                                        contentAlignment = Alignment.Center
                                                    ) {
                                                        Text(
                                                            text = "Imagen no disponible",
                                                            color = Color.Gray,
                                                            fontSize = 12.sp,
                                                            textAlign = TextAlign.Center
                                                        )
                                                    }
                                                }
                                            )
                                        } else {
                                            Text(
                                                text = "Imagen no disponible",
                                                color = Color.Gray,
                                                fontSize = 12.sp,
                                                textAlign = TextAlign.Center
                                            )
                                        }
                                    }
                                }

                                Column(
                                    modifier = Modifier
                                        .weight(1f)
                                        .fillMaxHeight(),
                                    verticalArrangement = Arrangement.spacedBy(8.dp)
                                ) {
                                    Text(
                                        text = producto.nombre,
                                        color = negro,
                                        fontSize = 20.sp,
                                        fontWeight = FontWeight.Bold,
                                        textAlign = TextAlign.Start
                                    )

                                    Card(
                                        shape = RoundedCornerShape(8.dp),
                                        colors = CardDefaults.cardColors(
                                            containerColor = pastel
                                        )
                                    ) {
                                        Text(
                                            text = "${producto.precio}€",
                                            color = negro,
                                            fontSize = 24.sp,
                                            fontWeight = FontWeight.ExtraBold,
                                            modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp)
                                        )
                                    }

                                    Column(
                                        verticalArrangement = Arrangement.spacedBy(4.dp)
                                    ) {
                                        Text(
                                            text = "Categoría: ${producto.id_categoria}",
                                            color = negro,
                                            fontSize = 16.sp,
                                            modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp)
                                        )
                                        Text(
                                            text = "Marca: ${producto.id_marca}",
                                            color = negro,
                                            fontSize = 16.sp,
                                            modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp)
                                        )
                                        Text(
                                            text = "Descripción: ${producto.descripcion}",
                                            color = negro,
                                            fontSize = 16.sp,
                                            modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp)
                                        )

                                        Button(
                                            onClick = {
                                                usuario?.let { user ->
                                                    apiAgregarAlCarrito(
                                                        usuarioId = user.id,
                                                        productoId = producto.id_producto,
                                                        cantidad = 1,
                                                        precioUnitario = producto.precio
                                                    ) { success, mensaje ->
                                                        scope.launch {
                                                            if (success) {
                                                                CarritoRefresh.notifyUpdate()
                                                                snackbarHostState.showSnackbar(
                                                                    message = "¡${producto.nombre} agregado al carrito!",
                                                                    withDismissAction = true
                                                                )
                                                            } else {
                                                                snackbarHostState.showSnackbar(
                                                                    message = "Error: $mensaje",
                                                                    withDismissAction = true
                                                                )
                                                            }
                                                        }
                                                    }
                                                } ?: run {
                                                    scope.launch {
                                                        snackbarHostState.showSnackbar(
                                                            message = "Debes iniciar sesión para agregar productos al carrito",
                                                            withDismissAction = true
                                                        )
                                                    }
                                                }
                                            },
                                            modifier = Modifier
                                                .fillMaxWidth()
                                                .height(50.dp),
                                            colors = ButtonDefaults.buttonColors(
                                                containerColor = purpura
                                            ),
                                            shape = RoundedCornerShape(12.dp)
                                        ) {
                                            Icon(
                                                imageVector = Icons.Default.ShoppingCart,
                                                contentDescription = "Añadir al carrito",
                                                modifier = Modifier.size(20.dp),
                                                tint = blanco
                                            )
                                            Spacer(modifier = Modifier.width(8.dp))
                                            Text(
                                                text = "Añadir al Carrito",
                                                fontSize = 16.sp,
                                                fontWeight = FontWeight.Bold,
                                                color = blanco
                                            )
                                        }
                                    }
                                }
                            }
                        }
                    }
                }

                if (!cargandoRelacionados && productosRelacionadosMarca.isNotEmpty()) {
                    item {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 20.dp)
                                .padding(bottom = 16.dp)
                        ) {
                            Text(
                                text = "Productos de la misma marca",
                                color = blanco,
                                fontSize = 18.sp,
                                fontWeight = FontWeight.Bold,
                                modifier = Modifier.padding(bottom = 12.dp)
                            )

                            LazyRow(
                                horizontalArrangement = Arrangement.spacedBy(12.dp),
                                contentPadding = PaddingValues(horizontal = 4.dp)
                            ) {
                                items(productosRelacionadosMarca) { productoRelacionado ->
                                    ProductoRelacionadoCard(
                                        producto = productoRelacionado,
                                        onProductClick = {
                                            navigator?.push(ProductoDetalleScreen(productoRelacionado, usuario))
                                        },
                                        onAddToCart = {
                                            usuario?.let { user ->
                                                apiAgregarAlCarrito(
                                                    usuarioId = user.id,
                                                    productoId = productoRelacionado.id_producto,
                                                    cantidad = 1,
                                                    precioUnitario = productoRelacionado.precio
                                                ) { success, mensaje ->
                                                    scope.launch {
                                                        if (success) {
                                                            CarritoRefresh.notifyUpdate()
                                                            snackbarHostState.showSnackbar(
                                                                message = "¡${productoRelacionado.nombre} agregado al carrito!",
                                                                withDismissAction = true
                                                            )
                                                        } else {
                                                            snackbarHostState.showSnackbar(
                                                                message = "Error: $mensaje",
                                                                withDismissAction = true
                                                            )
                                                        }
                                                    }
                                                }
                                            }
                                        },
                                        colors = Triple(blanco, negro, purpura)
                                    )
                                }
                            }
                        }
                    }
                }

                if (!cargandoRelacionados && productosRelacionadosCategoria.isNotEmpty()) {
                    item {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 20.dp)
                                .padding(bottom = 20.dp)
                        ) {
                            Text(
                                text = "Productos de la misma categoría",
                                color = blanco,
                                fontSize = 18.sp,
                                fontWeight = FontWeight.Bold,
                                modifier = Modifier.padding(bottom = 12.dp)
                            )

                            LazyRow(
                                horizontalArrangement = Arrangement.spacedBy(12.dp),
                                contentPadding = PaddingValues(horizontal = 4.dp)
                            ) {
                                items(productosRelacionadosCategoria) { productoRelacionado ->
                                    ProductoRelacionadoCard(
                                        producto = productoRelacionado,
                                        onProductClick = {
                                            navigator?.push(ProductoDetalleScreen(productoRelacionado, usuario))
                                        },
                                        onAddToCart = {
                                            usuario?.let { user ->
                                                apiAgregarAlCarrito(
                                                    usuarioId = user.id,
                                                    productoId = productoRelacionado.id_producto,
                                                    cantidad = 1,
                                                    precioUnitario = productoRelacionado.precio
                                                ) { success, mensaje ->
                                                    scope.launch {
                                                        if (success) {
                                                            CarritoRefresh.notifyUpdate()
                                                            snackbarHostState.showSnackbar(
                                                                message = "¡${productoRelacionado.nombre} agregado al carrito!",
                                                                withDismissAction = true
                                                            )
                                                        } else {
                                                            snackbarHostState.showSnackbar(
                                                                message = "Error: $mensaje",
                                                                withDismissAction = true
                                                            )
                                                        }
                                                    }
                                                }
                                            }
                                        },
                                        colors = Triple(blanco, negro, purpura)
                                    )
                                }
                            }
                        }
                    }
                }
            }

            SnackbarHost(
                hostState = snackbarHostState,
                modifier = Modifier.align(Alignment.BottomCenter)
            ) { data ->
                Snackbar(
                    snackbarData = data,
                    containerColor = purpura,
                    contentColor = blanco
                )
            }
        }
    }
}

@Composable
fun ProductoRelacionadoCard(
    producto: Producto,
    onProductClick: () -> Unit,
    onAddToCart: () -> Unit,
    colors: Triple<Color, Color, Color>
) {
    val (fondoColor, textoColor, acentoColor) = colors

    Card(
        modifier = Modifier
            .width(160.dp)
            .height(260.dp)
            .clickable { onProductClick() },
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(
            containerColor = fondoColor
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(
            modifier = Modifier.fillMaxSize()
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(120.dp)
                    .background(Color.LightGray),
                contentAlignment = Alignment.Center
            ) {
                if (producto.imagen_url.isNotEmpty()) {
                    LoadImage(
                        url = producto.imagen_url,
                        contentDescription = producto.nombre,
                        modifier = Modifier.fillMaxSize(),
                        contentScale = ContentScale.Crop,
                        loadingContent = {
                            CircularProgressIndicator(
                                color = acentoColor,
                                modifier = Modifier.size(20.dp)
                            )
                        },
                        errorContent = {
                            Text(
                                text = "Sin imagen",
                                color = Color.Gray,
                                fontSize = 10.sp,
                                textAlign = TextAlign.Center
                            )
                        }
                    )
                } else {
                    Text(
                        text = "Sin imagen",
                        color = Color.Gray,
                        fontSize = 10.sp,
                        textAlign = TextAlign.Center
                    )
                }
            }

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(12.dp),
                verticalArrangement = Arrangement.SpaceBetween
            ) {
                Column {
                    Text(
                        text = producto.nombre,
                        color = textoColor,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold,
                        maxLines = 2,
                        overflow = TextOverflow.Ellipsis
                    )

                    Text(
                        text = "${producto.precio}€",
                        color = acentoColor,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.ExtraBold,
                        modifier = Modifier.padding(top = 4.dp)
                    )
                }

                Button(
                    onClick = onAddToCart,
                    colors = ButtonDefaults.buttonColors(
                        containerColor = acentoColor
                    ),
                    shape = RoundedCornerShape(8.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(36.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.ShoppingCart,
                        contentDescription = "Añadir al carrito",
                        modifier = Modifier.size(16.dp),
                        tint = fondoColor
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = "Añadir",
                        fontSize = 12.sp,
                        color = fondoColor,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }
    }
}