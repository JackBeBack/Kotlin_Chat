package Views

import UI.ChatMessage
import UI.CustomTextInput
import UI.TextInputOption
import UI.stringFlowRow
import Viewmodel.ChatsModel
import Viewmodel.WindowViewModel
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.MaterialTheme
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ExitToApp
import androidx.compose.material.icons.automirrored.outlined.List
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import dev.langchain4j.agent.tool.JsonSchemaProperty.items
import kotlinx.coroutines.launch

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun ChatView() {
    val scope = rememberCoroutineScope()
    val chat = remember { ChatsModel.instance }
    val window = WindowViewModel.instance

    val windowTitle by window.windowTitle.collectAsState()

    val chatHistory by chat.currentChat.collectAsState()
    val followUpQuestions by chat.followUpQuestions.collectAsState()

    val listState = rememberLazyListState()

    LaunchedEffect(chatHistory) {
        if (chatHistory.size == 3 && windowTitle == "New Chat"){
            chat.generateChatTitle {
                window.windowTitle.tryEmit(it)
            }
        }
    }

    LaunchedEffect(Unit){
        chat.setSystemMessage("you are a pirate")
    }

    val leftButtonOptions = listOf<TextInputOption>(
        TextInputOption(Icons.AutoMirrored.Default.ExitToApp, "AI Respond") { chat.generateChat() },
        TextInputOption(Icons.AutoMirrored.Outlined.List, "Generate Questions") { chat.generateFollowUpQuestions() }
    )
    Box(modifier = Modifier.fillMaxSize().background(MaterialTheme.colors.background)) {

        LazyColumn(modifier = Modifier.align(Alignment.TopCenter).fillMaxWidth(), state = listState) {
            chatHistory.forEach { message ->
                item {
                    ChatMessage(modifier = Modifier.padding(top = 8.dp), message = message)
                }
            }
            item {
                Spacer(Modifier.size(200.dp))
            }
        }

        Column(Modifier.align(Alignment.BottomCenter).padding(8.dp)) {
            stringFlowRow(
                Modifier,
                followUpQuestions,
                onClick = {
                    chat.appendUserMessage(it)
                    chat.generateChat(autoGenFollowUpQuestions = true, onNewToken = {
                        scope.launch() {
                            listState.animateScrollToItem(chatHistory.size - 1)
                        }
                    })
                }
            ) {
                chat.removeFollowUpQuestion(it)
            }
            CustomTextInput(
                Modifier,
                rightButtonClick = { text ->
                    chat.appendUserMessage(text)
                    chat.generateChat(autoGenFollowUpQuestions = true, onNewToken = {
                        scope.launch() {
                            listState.animateScrollToItem(chatHistory.size - 1)
                        }
                    })
                },
                options = leftButtonOptions
            )
        }
    }
}