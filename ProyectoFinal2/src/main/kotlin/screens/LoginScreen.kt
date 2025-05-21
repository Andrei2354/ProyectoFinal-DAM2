package screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
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
import cafe.adriel.voyager.navigator.Navigator

class LoginScreen: Screen {
    @Composable
    override fun Content(){
        val navigator = LocalNavigator.current
        var usuario by remember { mutableStateOf("") }
        var passwd by remember { mutableStateOf("") }
        val blanco = Color(0xFFefeff2)
        val pupura = Color(0xFFa69eb0)
        val pastel = Color(0xFFf2e2cd)
        val gris = Color(0xFFdadae3)
        val negro = Color(0xFF011f4b)
        Column(modifier = Modifier.fillMaxSize().background(pupura), verticalArrangement = Arrangement.Center, horizontalAlignment = Alignment.CenterHorizontally) {
            Column(modifier = Modifier.shadow(8.dp, shape = RoundedCornerShape(8.dp)).background(blanco).padding(10.dp), verticalArrangement = Arrangement.Center, horizontalAlignment = Alignment.CenterHorizontally) {
                Text("Login", modifier = Modifier.padding(20.dp), fontSize = TextUnit(value = 40f, type = TextUnitType.Sp))
                OutlinedTextField(
                    value = usuario,
                    onValueChange = {usuario = it},
                    label = { Text("Username") },
                    modifier = Modifier,
                    colors = TextFieldDefaults.outlinedTextFieldColors(
                        focusedBorderColor = pastel,
                        focusedLabelColor = pastel
                    )
                )
                OutlinedTextField(
                    value = passwd,
                    onValueChange = {passwd = it},
                    label = { Text("Password") },
                    modifier = Modifier,
                    colors = TextFieldDefaults.outlinedTextFieldColors(
                        focusedBorderColor = pastel,
                        focusedLabelColor = pastel
                    )
                )
                Button(
                    onClick = {
                        navigator?.push(ShopScreen())
                    },
                    modifier = Modifier.padding(20.dp),
                    colors = ButtonDefaults.buttonColors(backgroundColor = gris)
                ) {
                    Text("Iniciar Sesión", color = Color.White)
                }
            }
        }
    }
}