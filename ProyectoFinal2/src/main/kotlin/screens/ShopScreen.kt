package screens

import androidx.compose.foundation.Image
import androidx.compose.runtime.Composable
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.core.screen.Screen
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import modelo.Usuario
import modelo.Categoria
import modelo.Producto
import network.apiGetCategorias
import network.apiGetProductos
import network.apiGetMarcas
import network.apiAgregarAlCarrito
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Snackbar
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.sp
import modelo.Marca
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asImageBitmap
import io.ktor.client.HttpClient
import io.ktor.client.request.get
import io.ktor.client.statement.readBytes
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlinx.coroutines.launch
import org.jetbrains.skia.Image as SkiaImage

class ShopScreen(val usuario: Usuario): Screen {
    @Composable
    override fun Content() {
        val navigator = LocalNavigator.current
        val blanco = Color(0xFFefeff2)
        val lila = Color(0xFFa69eb0)
        val pastel = Color(0xFFf2e2cd)
        val gris = Color(0xFFdadae3)
        val negro = Color(0xFF011f4b)
        val purpura = Color(0xFFa69eb0)

        var productos by remember { mutableStateOf<List<Producto>>(emptyList()) }
        var categorias by remember { mutableStateOf<List<Categoria>>(emptyList()) }
        var marcas by remember { mutableStateOf<List<Marca>>(emptyList()) }
        var categoriaSeleccionada by remember { mutableStateOf<Int?>(null) }
        var marcaSeleccionada by remember { mutableStateOf<Int?>(null) }
        var busqueda by remember { mutableStateOf("") }
        var cargando by remember { mutableStateOf(true) }

        // Para mostrar mensajes de carrito
        val snackbarHostState = remember { SnackbarHostState() }
        val scope = rememberCoroutineScope()

        LaunchedEffect(key1 = true) {
            apiGetCategorias { categoriasRecibidas ->
                categorias = categoriasRecibidas
            }

            apiGetMarcas { marcasRecibidas ->
                marcas = marcasRecibidas
            }

            apiGetProductos { productosRecibidos ->
                productos = productosRecibidos
                cargando = false
            }
        }

        val productosFiltrados = productos.filter { producto ->
            (categoriaSeleccionada == null || producto.id_categoria == categoriaSeleccionada) &&
                    (marcaSeleccionada == null || producto.id_marca == marcaSeleccionada) &&
                    (busqueda.isEmpty() || producto.nombre.contains(busqueda, ignoreCase = true))
        }

        Box(modifier = Modifier.fillMaxSize()) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .background(purpura),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 16.dp, horizontal = 20.dp),
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
                        horizontalArrangement = Arrangement.spacedBy(20.dp)
                    ) {
                        IconButton(
                            onClick = { navigator?.push(ProfileScreen(usuario)) },
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
                        Box(contentAlignment = Alignment.Center) {
                            IconButton(
                                onClick = { navigator?.push(CarritoScreen(usuario)) },
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
                }

                TextField(
                    value = busqueda,
                    onValueChange = { busqueda = it },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 20.dp)
                        .clip(RoundedCornerShape(8.dp)),
                    colors = TextFieldDefaults.colors(
                        focusedContainerColor = blanco,
                        unfocusedContainerColor = blanco,
                        disabledContainerColor = blanco,
                        focusedIndicatorColor = Color.Transparent,
                        unfocusedIndicatorColor = Color.Transparent
                    ),
                    placeholder = { Text("Buscar productos") },
                    leadingIcon = {
                        Icon(
                            imageVector = Icons.Default.Search,
                            contentDescription = "Buscar",
                            tint = negro
                        )
                    }
                )

                Spacer(modifier = Modifier.height(16.dp))

                LazyRow(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    item {
                        CategoriaPill(
                            nombre = "Todos",
                            selected = categoriaSeleccionada == null,
                            onSelected = { categoriaSeleccionada = null },
                            colors = Triple(blanco, pastel, negro)
                        )
                    }

                    items(categorias) { categoria ->
                        CategoriaPill(
                            nombre = categoria.nombre,
                            selected = categoriaSeleccionada == categoria.id,
                            onSelected = { categoriaSeleccionada = categoria.id },
                            colors = Triple(blanco, pastel, negro)
                        )
                    }
                }

                Spacer(modifier = Modifier.height(8.dp))

                LazyRow(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    item {
                        CategoriaPill(
                            nombre = "Todas las marcas",
                            selected = marcaSeleccionada == null,
                            onSelected = { marcaSeleccionada = null },
                            colors = Triple(blanco, pastel, negro)
                        )
                    }

                    items(marcas) { marca ->
                        CategoriaPill(
                            nombre = marca.nombre,
                            selected = marcaSeleccionada == marca.id,
                            onSelected = { marcaSeleccionada = marca.id },
                            colors = Triple(blanco, pastel, negro)
                        )
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                if (cargando) {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(32.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator(color = blanco)
                    }
                } else if (productosFiltrados.isEmpty()) {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(32.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "No se encontraron productos",
                            color = blanco,
                            fontSize = 18.sp
                        )
                    }
                } else {
                    LazyColumn(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(horizontal = 16.dp),
                        verticalArrangement = Arrangement.spacedBy(16.dp),
                        contentPadding = PaddingValues(bottom = 16.dp)
                    ) {
                        items(productosFiltrados.chunked(5)) { fila ->
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                fila.forEach { producto ->
                                    ProductoCard(
                                        producto = producto,
                                        modifier = Modifier.weight(1f),
                                        colors = Triple(blanco, negro, pastel),
                                        onAddToCart = {
                                            apiAgregarAlCarrito(
                                                usuarioId = usuario.id,
                                                productoId = producto.id_producto,
                                                cantidad = 1,
                                                precioUnitario = producto.precio
                                            ) { success, mensaje ->
                                                scope.launch {
                                                    if (success) {
                                                        // AGREGAR ESTA LÍNEA CLAVE:
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
                                        },
                                        onProductClick = {
                                            navigator?.push(ProductoDetalleScreen(producto))
                                        }
                                    )
                                }

                                repeat(5 - fila.size) {
                                    Spacer(modifier = Modifier.weight(1f))
                                }
                            }
                        }
                    }
                }
            }

            // SnackbarHost para mostrar mensajes
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
fun CategoriaPill(
    nombre: String,
    selected: Boolean,
    onSelected: () -> Unit,
    colors: Triple<Color, Color, Color>
) {
    val (fondoColor, bordeColor, textoColor) = colors
    val backgroundColor = if (selected) bordeColor else fondoColor
    val textColor = if (selected) fondoColor else textoColor

    Box(
        modifier = Modifier
            .clip(RoundedCornerShape(50))
            .background(backgroundColor)
            .border(
                width = 1.dp,
                color = bordeColor,
                shape = RoundedCornerShape(50)
            )
            .clickable { onSelected() }
            .padding(horizontal = 16.dp, vertical = 8.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = nombre,
            color = textColor,
            fontSize = 14.sp,
            fontWeight = FontWeight.Medium
        )
    }
}

@Composable
fun LoadImage(
    url: String,
    contentDescription: String,
    modifier: Modifier = Modifier,
    contentScale: ContentScale = ContentScale.Fit,
    loadingContent: @Composable () -> Unit = {
        CircularProgressIndicator()
    },
    errorContent: @Composable () -> Unit = {
        Text("Error de carga")
    }
) {
    var bitmap by remember { mutableStateOf<ImageBitmap?>(null) }
    var isLoading by remember { mutableStateOf(true) }
    var error by remember { mutableStateOf(false) }

    LaunchedEffect(url) {
        isLoading = true
        error = false

        try {
            val client = HttpClient()
            val bytes = client.get(url).readBytes()
            client.close()

            withContext(Dispatchers.Default) {
                val skiaImage = SkiaImage.makeFromEncoded(bytes)
                bitmap = skiaImage.asImageBitmap()
            }

            isLoading = false
        } catch (e: Exception) {
            isLoading = false
            error = true
        }
    }

    when {
        isLoading -> loadingContent()
        error -> errorContent()
        bitmap != null -> Image(
            bitmap = bitmap!!,
            contentDescription = contentDescription,
            modifier = modifier,
            contentScale = contentScale
        )
    }
}

@Composable
fun ProductoCard(
    producto: Producto,
    modifier: Modifier = Modifier,
    colors: Triple<Color, Color, Color>,
    onAddToCart: () -> Unit,
    onProductClick: () -> Unit
) {
    val (fondoColor, textoColor, acentoColor) = colors
    Card(
        modifier = modifier
            .fillMaxWidth()
            .height(100.dp)
            .clickable { onProductClick()},
        shape = RoundedCornerShape(8.dp),
        colors = CardDefaults.cardColors(
            containerColor = fondoColor
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxSize()
        ) {
            Box(
                modifier = Modifier
                    .fillMaxHeight()
                    .width(100.dp)
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
                            Box(
                                modifier = Modifier.fillMaxSize(),
                                contentAlignment = Alignment.Center
                            ) {
                                CircularProgressIndicator(
                                    color = Color.Gray,
                                    modifier = Modifier.size(14.dp)
                                )
                            }
                        },
                        errorContent = {
                            Box(
                                modifier = Modifier.fillMaxSize(),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = "!",
                                    color = Color.Gray,
                                    fontSize = 10.sp
                                )
                            }
                        }
                    )
                } else {
                    Text(
                        text = "!",
                        color = Color.Gray,
                        fontSize = 10.sp
                    )
                }
            }

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(8.dp),
                verticalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = producto.nombre,
                    color = textoColor,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )

                Text(
                    text = "Precio: ${producto.precio}€",
                    color = textoColor,
                    fontSize = 10.sp,
                    fontWeight = FontWeight.ExtraBold
                )

                Button(
                    onClick = { onAddToCart() },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = acentoColor
                    ),
                    shape = RoundedCornerShape(4.dp),
                    modifier = Modifier.height(32.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.ShoppingCart,
                        contentDescription = "Añadir al carrito",
                        modifier = Modifier.size(15.dp)
                    )
                    Spacer(modifier = Modifier.width(2.dp))
                    Text(
                        text = "Añadir",
                        fontSize = 12.sp
                    )
                }
            }
        }
    }
}