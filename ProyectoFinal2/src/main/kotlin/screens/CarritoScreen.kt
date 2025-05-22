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
import androidx.compose.ui.text.style.TextDecoration
import cafe.adriel.voyager.navigator.currentOrThrow
import androidx.compose.foundation.*
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.filled.*
import androidx.compose.runtime.*
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.style.TextOverflow
import modelo.Usuario
import modelo.ItemCarrito
import network.*
import androidx.compose.runtime.derivedStateOf


object CarritoRefresh {
    var lastUpdate by mutableStateOf(0L)

    fun notifyUpdate() {
        lastUpdate = System.currentTimeMillis()
    }
}

class CarritoScreen(val usuario: Usuario? = null) : Screen {
    @Composable
    override fun Content() {
        val navigator = LocalNavigator.currentOrThrow
        val lila = Color(0xFFa69eb0)
        val pastel = Color(0xFFf2e2cd)
        val negro = Color(0xFF011f4b)
        val blanco = Color(0xFFefeff2)
        val purpura = Color(0xFFa69eb0)


        var carritoItems by remember { mutableStateOf<List<ItemCarrito>>(emptyList()) }
        var cargando by remember { mutableStateOf(true) }
        var showPaymentDialog by remember { mutableStateOf(false) }
        val lastUpdate by remember { derivedStateOf { CarritoRefresh.lastUpdate } }

        LaunchedEffect(key1 = usuario?.id) {
            usuario?.let { user ->
                apiObtenerCarrito(user.id) { items ->
                    carritoItems = items
                    cargando = false
                }
            } ?: run {
                cargando = false
            }
        }

        LaunchedEffect(lastUpdate) {
            if (lastUpdate > 0) {
                usuario?.let { user ->
                    cargando = true
                    apiObtenerCarrito(user.id) { items ->
                        carritoItems = items
                        cargando = false
                    }
                }
            }
        }

        val subtotal = carritoItems.sumOf { it.precio_unitario * it.cantidad }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(purpura)
                .padding(horizontal = 16.dp)
        ) {
            // Header
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
                        color = blanco
                    )
                )
                Badge(
                    modifier = Modifier.size(40.dp),
                    backgroundColor = blanco,
                    contentColor = lila
                ) {
                    Text(carritoItems.sumOf { it.cantidad }.toString())
                }
            }

            if (cargando) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(32.dp),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator(color = blanco)
                }
            } else if (carritoItems.isEmpty()) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(32.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.ShoppingCart,
                            contentDescription = "Carrito vacío",
                            tint = blanco,
                            modifier = Modifier.size(80.dp)
                        )
                        Text(
                            text = "Tu carrito está vacío",
                            color = blanco,
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Medium
                        )
                        Button(
                            onClick = { navigator.pop() },
                            colors = ButtonDefaults.buttonColors(
                                backgroundColor = blanco,
                                contentColor = purpura
                            ),
                            shape = RoundedCornerShape(8.dp)
                        ) {
                            Text("Seguir comprando")
                        }
                    }
                }
            } else {
                LazyColumn(
                    modifier = Modifier.weight(1f),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    items(carritoItems) { item ->
                        ItemCarritoCard(
                            item = item,
                            onIncrease = {
                                apiActualizarCantidad(item.id_carrito, item.cantidad + 1) { success ->
                                    if (success) {
                                        carritoItems = carritoItems.map {
                                            if (it.id_carrito == item.id_carrito)
                                                it.copy(cantidad = it.cantidad + 1)
                                            else it
                                        }
                                    }
                                }
                            },
                            onDecrease = {
                                if (item.cantidad > 1) {
                                    apiActualizarCantidad(item.id_carrito, item.cantidad - 1) { success ->
                                        if (success) {
                                            // Actualizar localmente para respuesta inmediata
                                            carritoItems = carritoItems.map {
                                                if (it.id_carrito == item.id_carrito)
                                                    it.copy(cantidad = it.cantidad - 1)
                                                else it
                                            }
                                        }
                                    }
                                }
                            },
                            onRemove = {
                                apiEliminarDelCarrito(item.id_carrito) { success ->
                                    if (success) {
                                        // Actualizar localmente para respuesta inmediata
                                        carritoItems = carritoItems.filter { it.id_carrito != item.id_carrito }
                                    }
                                }
                            }
                        )
                    }
                }

                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 20.dp),
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
                            Text("${"%.2f".format(subtotal)}€", style = TextStyle(fontWeight = FontWeight.Bold))
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
                                "${"%.2f".format(subtotal)}€",
                                style = TextStyle(
                                    fontSize = 18.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = lila
                                )
                            )
                        }
                        Spacer(modifier = Modifier.height(16.dp))
                        Button(
                            onClick = { showPaymentDialog = true },
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(50.dp),
                            shape = RoundedCornerShape(12.dp),
                            colors = ButtonDefaults.buttonColors(
                                backgroundColor = lila,
                                contentColor = blanco
                            ),
                            enabled = carritoItems.isNotEmpty()
                        ) {
                            Text("Proceder al pago", fontSize = 16.sp)
                        }
                    }
                }
            }
        }

        if (showPaymentDialog) {
            PaymentDialog(
                total = subtotal,
                onDismiss = { showPaymentDialog = false },
                onPaymentSuccess = {
                    showPaymentDialog = false
                    carritoItems = emptyList()
                },
                colors = CarritoColors(
                    lila = lila,
                    negro = negro,
                    blanco = blanco,
                    pastel = pastel
                )
            )
        }
    }

    @Composable
    fun ItemCarritoCard(
        item: ItemCarrito,
        onIncrease: () -> Unit,
        onDecrease: () -> Unit,
        onRemove: () -> Unit
    ) {
        val lila = Color(0xFFa69eb0)
        val negro = Color(0xFF011f4b)
        val blanco = Color(0xFFefeff2)

        Card(
            modifier = Modifier.fillMaxWidth(),
            elevation = 2.dp,
            shape = RoundedCornerShape(12.dp),
            backgroundColor = blanco
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(12.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .size(80.dp)
                        .clip(RoundedCornerShape(8.dp))
                        .background(Color.LightGray),
                    contentAlignment = Alignment.Center
                ) {
                    if (item.imagen_url.isNotEmpty()) {
                        LoadImage(
                            url = item.imagen_url,
                            contentDescription = item.nombre_producto,
                            modifier = Modifier.fillMaxSize(),
                            contentScale = ContentScale.Crop,
                            loadingContent = {
                                CircularProgressIndicator(
                                    color = lila,
                                    modifier = Modifier.size(20.dp)
                                )
                            },
                            errorContent = {
                                Icon(
                                    imageVector = Icons.Default.ShoppingBag,
                                    contentDescription = "Producto",
                                    tint = lila,
                                    modifier = Modifier.size(40.dp)
                                )
                            }
                        )
                    } else {
                        Icon(
                            imageVector = Icons.Default.ShoppingBag,
                            contentDescription = "Producto",
                            tint = lila,
                            modifier = Modifier.size(40.dp)
                        )
                    }
                }

                Column(
                    modifier = Modifier
                        .weight(1f)
                        .padding(horizontal = 12.dp)
                ) {
                    Text(
                        item.nombre_producto,
                        style = TextStyle(
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Medium
                        ),
                        maxLines = 2,
                        overflow = TextOverflow.Ellipsis
                    )
                    Text(
                        "${"%.2f".format(item.precio_unitario)}€",
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
                            item.cantidad.toString(),
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

    data class CarritoColors(
        val lila: Color,
        val negro: Color,
        val blanco: Color,
        val pastel: Color
    )
}

@Composable
fun PaymentDialog(
    total: Double,
    onDismiss: () -> Unit,
    onPaymentSuccess: () -> Unit,
    colors: CarritoScreen.CarritoColors
) {
    var nombreTarjeta by remember { mutableStateOf("") }
    var numeroTarjeta by remember { mutableStateOf("") }
    var fechaVencimiento by remember { mutableStateOf("") }
    var cvv by remember { mutableStateOf("") }
    var nombreFacturacion by remember { mutableStateOf("") }
    var direccionFacturacion by remember { mutableStateOf("") }

    var metodoPagoSeleccionado by remember { mutableStateOf("Tarjeta de Crédito") }
    val metodosPago = listOf("Tarjeta de Crédito", "PayPal", "Transferencia Bancaria")

    Dialog(onDismissRequest = onDismiss) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            shape = RoundedCornerShape(16.dp),
            backgroundColor = colors.blanco,
            elevation = 8.dp
        ) {
            LazyColumn(
                modifier = Modifier.padding(20.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                item {
                    Text(
                        text = "Completar Pago",
                        style = TextStyle(
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Bold,
                            color = colors.negro
                        ),
                        modifier = Modifier.fillMaxWidth()
                    )
                }

                item {
                    Card(
                        backgroundColor = colors.pastel.copy(alpha = 0.3f),
                        shape = RoundedCornerShape(8.dp),
                        elevation = 0.dp
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                "Total a pagar:",
                                style = TextStyle(
                                    fontSize = 16.sp,
                                    fontWeight = FontWeight.Medium
                                )
                            )
                            Text(
                                "$${"%.2f".format(total)}",
                                style = TextStyle(
                                    fontSize = 20.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = colors.lila
                                )
                            )
                        }
                    }
                }

                item {
                    Text(
                        "Método de pago",
                        style = TextStyle(
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Medium,
                            color = colors.negro
                        )
                    )

                    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                        metodosPago.forEach { metodo ->
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clickable { metodoPagoSeleccionado = metodo }
                                    .padding(vertical = 4.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                RadioButton(
                                    selected = metodoPagoSeleccionado == metodo,
                                    onClick = { metodoPagoSeleccionado = metodo },
                                    colors = RadioButtonDefaults.colors(
                                        selectedColor = colors.lila,
                                        unselectedColor = colors.negro.copy(alpha = 0.6f)
                                    )
                                )
                                Spacer(modifier = Modifier.width(8.dp))
                                Text(metodo, color = colors.negro)
                            }
                        }
                    }
                }

                if (metodoPagoSeleccionado == "Tarjeta de Crédito") {
                    item {
                        Text(
                            "Información de la tarjeta",
                            style = TextStyle(
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Medium,
                                color = colors.negro
                            )
                        )
                    }

                    item {
                        OutlinedTextField(
                            value = nombreTarjeta,
                            onValueChange = { nombreTarjeta = it },
                            label = { Text("Nombre en la tarjeta", color = colors.negro.copy(alpha = 0.7f)) },
                            leadingIcon = {
                                Icon(
                                    Icons.Default.Person,
                                    contentDescription = "Nombre",
                                    tint = colors.lila
                                )
                            },
                            colors = TextFieldDefaults.outlinedTextFieldColors(
                                focusedBorderColor = colors.lila,
                                unfocusedBorderColor = colors.negro.copy(alpha = 0.3f),
                                cursorColor = colors.lila,
                                textColor = colors.negro
                            ),
                            modifier = Modifier.fillMaxWidth()
                        )
                    }

                    item {
                        OutlinedTextField(
                            value = numeroTarjeta,
                            onValueChange = {
                                val digitsOnly = it.filter { char -> char.isDigit() }
                                if (digitsOnly.length <= 16) {
                                    numeroTarjeta = digitsOnly.chunked(4).joinToString(" ")
                                }
                            },
                            label = { Text("Número de tarjeta", color = colors.negro.copy(alpha = 0.7f)) },
                            leadingIcon = {
                                Icon(
                                    Icons.Default.CreditCard,
                                    contentDescription = "Tarjeta",
                                    tint = colors.lila
                                )
                            },
                            colors = TextFieldDefaults.outlinedTextFieldColors(
                                focusedBorderColor = colors.lila,
                                unfocusedBorderColor = colors.negro.copy(alpha = 0.3f),
                                cursorColor = colors.lila,
                                textColor = colors.negro
                            ),
                            modifier = Modifier.fillMaxWidth(),
                            placeholder = { Text("1234 5678 9012 3456") }
                        )
                    }

                    item {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            OutlinedTextField(
                                value = fechaVencimiento,
                                onValueChange = {
                                    val digitsOnly = it.filter { char -> char.isDigit() }
                                    if (digitsOnly.length <= 4) {
                                        fechaVencimiento = when (digitsOnly.length) {
                                            in 0..2 -> digitsOnly
                                            else -> "${digitsOnly.take(2)}/${digitsOnly.drop(2)}"
                                        }
                                    }
                                },
                                label = { Text("MM/YY", color = colors.negro.copy(alpha = 0.7f)) },
                                colors = TextFieldDefaults.outlinedTextFieldColors(
                                    focusedBorderColor = colors.lila,
                                    unfocusedBorderColor = colors.negro.copy(alpha = 0.3f),
                                    cursorColor = colors.lila,
                                    textColor = colors.negro
                                ),
                                modifier = Modifier.weight(1f),
                                placeholder = { Text("12/28") }
                            )

                            OutlinedTextField(
                                value = cvv,
                                onValueChange = {
                                    if (it.length <= 3 && it.all { char -> char.isDigit() }) {
                                        cvv = it
                                    }
                                },
                                label = { Text("CVV", color = colors.negro.copy(alpha = 0.7f)) },
                                colors = TextFieldDefaults.outlinedTextFieldColors(
                                    focusedBorderColor = colors.lila,
                                    unfocusedBorderColor = colors.negro.copy(alpha = 0.3f),
                                    cursorColor = colors.lila,
                                    textColor = colors.negro
                                ),
                                modifier = Modifier.weight(1f),
                                placeholder = { Text("123") }
                            )
                        }
                    }
                }

                item {
                    Text(
                        "Información de facturación",
                        style = TextStyle(
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Medium,
                            color = colors.negro
                        )
                    )
                }

                item {
                    OutlinedTextField(
                        value = nombreFacturacion,
                        onValueChange = { nombreFacturacion = it },
                        label = { Text("Nombre completo", color = colors.negro.copy(alpha = 0.7f)) },
                        leadingIcon = {
                            Icon(
                                Icons.Default.Person,
                                contentDescription = "Nombre",
                                tint = colors.lila
                            )
                        },
                        colors = TextFieldDefaults.outlinedTextFieldColors(
                            focusedBorderColor = colors.lila,
                            unfocusedBorderColor = colors.negro.copy(alpha = 0.3f),
                            cursorColor = colors.lila,
                            textColor = colors.negro
                        ),
                        modifier = Modifier.fillMaxWidth()
                    )
                }

                item {
                    OutlinedTextField(
                        value = direccionFacturacion,
                        onValueChange = { direccionFacturacion = it },
                        label = { Text("Dirección de facturación", color = colors.negro.copy(alpha = 0.7f)) },
                        leadingIcon = {
                            Icon(
                                Icons.Default.LocationOn,
                                contentDescription = "Dirección",
                                tint = colors.lila
                            )
                        },
                        colors = TextFieldDefaults.outlinedTextFieldColors(
                            focusedBorderColor = colors.lila,
                            unfocusedBorderColor = colors.negro.copy(alpha = 0.3f),
                            cursorColor = colors.lila,
                            textColor = colors.negro
                        ),
                        modifier = Modifier.fillMaxWidth()
                    )
                }

                item {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        OutlinedButton(
                            onClick = onDismiss,
                            modifier = Modifier.weight(1f),
                            border = BorderStroke(1.dp, colors.lila),
                            shape = RoundedCornerShape(8.dp),
                            colors = ButtonDefaults.outlinedButtonColors(
                                contentColor = colors.lila
                            )
                        ) {
                            Text("Cancelar")
                        }

                        Button(
                            onClick = {
                                onPaymentSuccess()
                            },
                            modifier = Modifier.weight(1f),
                            shape = RoundedCornerShape(8.dp),
                            colors = ButtonDefaults.buttonColors(
                                backgroundColor = colors.lila,
                                contentColor = colors.blanco
                            ),
                            enabled = when (metodoPagoSeleccionado) {
                                "Tarjeta de Crédito" -> nombreTarjeta.isNotBlank() &&
                                        numeroTarjeta.isNotBlank() &&
                                        fechaVencimiento.isNotBlank() &&
                                        cvv.isNotBlank() &&
                                        nombreFacturacion.isNotBlank() &&
                                        direccionFacturacion.isNotBlank()
                                else -> nombreFacturacion.isNotBlank() && direccionFacturacion.isNotBlank()
                            }
                        ) {
                            Text("Pagar ${"%.2f".format(total)}€")
                        }
                    }
                }
            }
        }
    }
}