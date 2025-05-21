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
import network.apiLogin
import kotlinx.coroutines.launch

class LoginScreen: Screen {
    @Composable
    override fun Content() {
        val navigator = LocalNavigator.current
        var usuario by remember { mutableStateOf("") }
        var passwd by remember { mutableStateOf("") }
        var isLoading by remember { mutableStateOf(false) }
        var errorMessage by remember { mutableStateOf("") }
        val scope = rememberCoroutineScope()
        val blanco = Color(0xFFefeff2)
        val pupura = Color(0xFFa69eb0)
        val pastel = Color(0xFFf2e2cd)
        val gris = Color(0xFFdadae3)
        val negro = Color(0xFF011f4b)

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
                    "Login",
                    modifier = Modifier.padding(20.dp),
                    fontSize = TextUnit(value = 40f, type = TextUnitType.Sp)
                )

                OutlinedTextField(
                    value = usuario,
                    onValueChange = { usuario = it },
                    label = { Text("Email") },
                    modifier = Modifier,
                    colors = TextFieldDefaults.outlinedTextFieldColors(
                        focusedBorderColor = pastel,
                        focusedLabelColor = pastel
                    )
                )

                OutlinedTextField(
                    value = passwd,
                    onValueChange = { passwd = it },
                    label = { Text("Password") },
                    modifier = Modifier,
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
                            if (usuario.isNotEmpty() && passwd.isNotEmpty()) {
                                isLoading = true
                                errorMessage = ""

                                println("Intentando login con: $usuario, $passwd")
                                apiLogin(usuario, passwd) { user ->
                                    navigator?.push(ShopScreen(user))
                                    usuario = ""
                                    passwd = ""
                                }

                                scope.launch {
                                    kotlinx.coroutines.delay(3000)
                                    if (isLoading) {
                                        isLoading = false
                                        errorMessage = "Error al iniciar sesión. Verifica tus credenciales."
                                    }
                                }
                            } else {
                                errorMessage = "Por favor completa todos los campos"
                            }
                        },
                        colors = ButtonDefaults.buttonColors(backgroundColor = gris),
                        enabled = !isLoading
                    ) {
                        if (isLoading) {
                            CircularProgressIndicator(
                                color = Color.White,
                                modifier = Modifier.padding(end = 8.dp),
                                strokeWidth = 2.dp
                            )
                        }
                        Text("Iniciar Sesión", color = Color.White)
                    }

                    Button(
                        onClick = {
                            navigator?.push(RegisterScreen())
                        },
                        colors = ButtonDefaults.buttonColors(backgroundColor = pastel)
                    ) {
                        Text("Crear Cuenta", color = Color.White)
                    }
                }
            }
        }
    }
}