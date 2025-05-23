package screens

import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import cafe.adriel.voyager.navigator.Navigator
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import java.awt.Toolkit
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.rememberWindowState
import androidx.compose.ui.res.painterResource



@Composable
@Preview
fun App() {
    MaterialTheme {
        Navigator(LoginScreen())
    }
}

fun main() = application {
    val screenSize = Toolkit.getDefaultToolkit().screenSize
    val screenWidthDp = (screenSize.width / Toolkit.getDefaultToolkit().screenResolution.toFloat() * 96).dp
    val screenHeightDp = (screenSize.height / Toolkit.getDefaultToolkit().screenResolution.toFloat() * 96).dp

    val windowState = rememberWindowState(
        size = DpSize(screenWidthDp, screenHeightDp)
    )

    Window(
        onCloseRequest = ::exitApplication,
        title = "Tekkio",
        state = windowState,
        resizable = true,
        icon = painterResource("drawable/icono1.png")
    ) {
        App()
    }
}