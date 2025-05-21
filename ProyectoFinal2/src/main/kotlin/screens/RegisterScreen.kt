package screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.TextUnitType
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import network.apiRegister

class RegisterScreen : Screen {
    @Composable
    override fun Content() {
        val navigator = LocalNavigator.current
        var nombre by remember { mutableStateOf("") }
        var email by remember { mutableStateOf("") }
        var direccion by remember { mutableStateOf("") }
        var telefono by remember { mutableStateOf("") }
        var password by remember { mutableStateOf("") }
        var confirmPassword by remember { mutableStateOf("") }
        var isLoading by remember { mutableStateOf(false) }
        var errorMessage by remember { mutableStateOf("") }
        val scope = rememberCoroutineScope()

        val blanco = Color(0xFFefeff2)
        val pupura = Color(0xFFa69eb0)
        val pastel = Color(0xFFf2e2cd)
        val gris = Color(0xFFdadae3)

        Column(
            modifier = Modifier.fillMaxSize().background(pupura),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Column(
                modifier = Modifier
                    .shadow(8.dp, shape = RoundedCornerShape(8.dp))
                    .background(blanco)
                    .padding(10.dp),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    "Crear Cuenta",
                    modifier = Modifier.padding(20.dp),
                    fontSize = TextUnit(value = 30f, type = TextUnitType.Sp)
                )

                OutlinedTextField(
                    value = nombre,
                    onValueChange = { nombre = it },
                    label = { Text("Nombre") },
                    modifier = Modifier.padding(vertical = 4.dp),
                    colors = TextFieldDefaults.outlinedTextFieldColors(
                        focusedBorderColor = pastel,
                        focusedLabelColor = pastel
                    )
                )

                OutlinedTextField(
                    value = email,
                    onValueChange = { email = it },
                    label = { Text("Email") },
                    modifier = Modifier.padding(vertical = 4.dp),
                    colors = TextFieldDefaults.outlinedTextFieldColors(
                        focusedBorderColor = pastel,
                        focusedLabelColor = pastel
                    )
                )

                OutlinedTextField(
                    value = direccion,
                    onValueChange = { direccion = it },
                    label = { Text("Dirección") },
                    modifier = Modifier.padding(vertical = 4.dp),
                    colors = TextFieldDefaults.outlinedTextFieldColors(
                        focusedBorderColor = pastel,
                        focusedLabelColor = pastel
                    )
                )

                OutlinedTextField(
                    value = telefono,
                    onValueChange = { telefono = it },
                    label = { Text("Teléfono") },
                    modifier = Modifier.padding(vertical = 4.dp),
                    colors = TextFieldDefaults.outlinedTextFieldColors(
                        focusedBorderColor = pastel,
                        focusedLabelColor = pastel
                    )
                )

                OutlinedTextField(
                    value = password,
                    onValueChange = { password = it },
                    label = { Text("Contraseña") },
                    modifier = Modifier.padding(vertical = 4.dp),
                    colors = TextFieldDefaults.outlinedTextFieldColors(
                        focusedBorderColor = pastel,
                        focusedLabelColor = pastel
                    )
                )

                OutlinedTextField(
                    value = confirmPassword,
                    onValueChange = { confirmPassword = it },
                    label = { Text("Confirmar Contraseña") },
                    modifier = Modifier.padding(vertical = 4.dp),
                    colors = TextFieldDefaults.outlinedTextFieldColors(
                        focusedBorderColor = pastel,
                        focusedLabelColor = pastel
                    )
                )

                if (errorMessage.isNotEmpty()) {
                    Text(
                        text = errorMessage,
                        color = Color.Red,
                        modifier = Modifier.padding(top = 8.dp)
                    )
                }

                Row(
                    modifier = Modifier.padding(20.dp),
                    horizontalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    Button(
                        onClick = {
                            navigator?.pop()
                        },
                        colors = ButtonDefaults.buttonColors(backgroundColor = gris)
                    ) {
                        Text("Cancelar", color = Color.White)
                    }

                    Button(
                        onClick = {
                            when {
                                nombre.isEmpty() || email.isEmpty() || direccion.isEmpty() ||
                                        password.isEmpty() || confirmPassword.isEmpty() -> {
                                    errorMessage = "Todos los campos son obligatorios"
                                }
                                password != confirmPassword -> {
                                    errorMessage = "Las contraseñas no coinciden"
                                }
                                !email.contains("@") -> {
                                    errorMessage = "El email no es válido"
                                }
                                else -> {
                                    isLoading = true
                                    errorMessage = ""
                                    apiRegister(nombre, email, direccion, telefono, password) { success ->
                                        isLoading = false
                                        if (success) {
                                            navigator?.pop()
                                        } else {
                                            errorMessage = "Error al registrar. El email podría estar en uso."
                                        }
                                    }
                                }
                            }
                        },
                        colors = ButtonDefaults.buttonColors(backgroundColor = pastel),
                        enabled = !isLoading
                    ) {
                        if (isLoading) {
                            CircularProgressIndicator(
                                color = Color.White,
                                modifier = Modifier.padding(end = 8.dp),
                                strokeWidth = 2.dp
                            )
                        }
                        Text("Registrar", color = Color.White)
                    }
                }
            }
        }
    }
}