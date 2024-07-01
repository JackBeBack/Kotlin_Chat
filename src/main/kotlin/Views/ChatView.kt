package Views

import UI.ChatMessage
import UI.CustomTextInput
import UI.TextInputOption
import Viewmodel.ChatsModel
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Button
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ExitToApp
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun ChatView(){
    val scope = rememberCoroutineScope()
    val chat = remember { ChatsModel.instance }
    val chatHistory by chat.currentChat.collectAsState()

    val leftButtonOptions = listOf<TextInputOption>(
        TextInputOption(Icons.AutoMirrored.Default.ExitToApp, "AI Respond") { chat.generateChat() }
    )
    Box(modifier = Modifier.fillMaxSize().background(MaterialTheme.colors.background)){
        CustomTextInput(
            Modifier.align(Alignment.BottomCenter).padding(8.dp),
            rightButtonClick = { text ->
                chat.appendUserMessage(text)
            },
            options = leftButtonOptions
        )
        Column(modifier = Modifier.align(Alignment.TopCenter).fillMaxWidth()){
            chatHistory.forEach { message ->
                ChatMessage(modifier = Modifier.padding(top = 2.dp), message = message)
            }
        }
    }
}