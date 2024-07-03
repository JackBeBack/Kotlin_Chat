import Static.appColors
import Viewmodel.WindowViewModel
import Views.ChatView
import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.WindowPlacement
import androidx.compose.ui.window.WindowPosition
import androidx.compose.ui.window.application
import androidx.compose.ui.window.rememberWindowState
import androidx.compose.runtime.*
import java.awt.Toolkit


@Composable
@Preview
fun App() {
    MaterialTheme(appColors) {
        ChatView()
    }
}

fun main() = application {
    val windowTitle by WindowViewModel.instance.windowTitle.collectAsState()
    val toolkit = Toolkit.getDefaultToolkit()
    val screenSize = toolkit.screenSize
    val screenWidth = screenSize.width.dp
    val screenHeight = screenSize.height.dp

    //Gap on Each Side of the Window
    val gap = 128.dp

    val state = rememberWindowState(
        placement = WindowPlacement.Floating,
        position = WindowPosition(x = gap, y = gap),
        size = DpSize(screenWidth - (gap*2), screenHeight - (gap*2))
    )
    Window(onCloseRequest = ::exitApplication, state = state, title = windowTitle) {
        App()
    }
}
