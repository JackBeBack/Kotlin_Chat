package Viewmodel

import androidx.compose.runtime.mutableStateOf
import dev.ai4j.openai4j.chat.AssistantMessage
import dev.langchain4j.data.message.AiMessage
import dev.langchain4j.data.message.ChatMessage
import dev.langchain4j.data.message.ChatMessageType
import dev.langchain4j.data.message.SystemMessage
import dev.langchain4j.data.message.UserMessage
import dev.langchain4j.model.StreamingResponseHandler
import dev.langchain4j.model.output.Response
import dev.langchain4j.model.openai.OpenAiStreamingChatModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import java.time.Duration.ofSeconds

class ChatsModel() {
    private val model: OpenAiStreamingChatModel

    private val _currentChat = MutableStateFlow<List<ChatMessage>>(listOf())
    val currentChat: StateFlow<List<ChatMessage>> = _currentChat

    private val _followUpQuestions = MutableStateFlow<List<String>>(listOf())
    val followUpQuestions: StateFlow<List<String>> = _followUpQuestions

    init {
        val apiKey = System.getenv("OPENAI_API_KEY")

        model = OpenAiStreamingChatModel.builder()
            .apiKey(apiKey)
            .timeout(ofSeconds(100))
            .build()
    }

    companion object {
        val instance: ChatsModel = ChatsModel()
    }

    fun removeFollowUpQuestion(remove: String){
        _followUpQuestions.tryEmit(_followUpQuestions.value.filter { it != remove })
    }

    fun appendUserMessage(message: String) {
        val newHistory = _currentChat.value.toMutableList().apply {
            add(UserMessage.from(message))
        }
        _currentChat.tryEmit(newHistory)
    }

    fun setSystemMessage(message: String) {
        val newHistory = _currentChat.value.toMutableList().apply {
            add(SystemMessage.from(message))
        }
        _currentChat.tryEmit(newHistory)
    }


    fun appendAssistantMessage(message: String) {
        val lastType = _currentChat.value.last().type()
        if (lastType == ChatMessageType.USER) {
            //when last message is from User Append
            val newHistory = _currentChat.value.toMutableList().apply {
                add(AiMessage.from(message))
            }
            _currentChat.tryEmit(newHistory)
        }else if(lastType == ChatMessageType.AI){
            //when last message is from AI Replace it
            val newHistory = _currentChat.value.dropLast(1).toMutableList().apply {
                add(AiMessage.from(message))
            }

            _currentChat.tryEmit(newHistory)
        }
    }

    fun generateChat(onNewToken: (String) -> Unit = {}, autoGenFollowUpQuestions: Boolean = false, onFinish: (String) -> Unit = {}){
        if (currentChat.value.isNotEmpty()) {
            var currentText = ""
            model.generate(currentChat.value, object : StreamingResponseHandler<AiMessage> {

                override fun onNext(token: String) {
                    onNewToken(token)
                    currentText += token
                    appendAssistantMessage(currentText)
                }

                override fun onComplete(response: Response<AiMessage>) {
                    onFinish(response.content().text())
                    appendAssistantMessage(response.content().text())
                    if (autoGenFollowUpQuestions) generateFollowUpQuestions()
                }

                override fun onError(error: Throwable) {
                    error.printStackTrace()
                }
            })
        }
    }

    fun generate(userMessage: String, onNewToken: (String) -> Unit, onFinish: (String) -> Unit) {
        val history: List<ChatMessage> = listOf(
            UserMessage.from(userMessage)
        )

        model.generate(history, object : StreamingResponseHandler<AiMessage> {

            override fun onNext(token: String) {
                onNewToken(token)
            }

            override fun onComplete(response: Response<AiMessage>) {
                onFinish(response.content().text())
            }

            override fun onError(error: Throwable) {
                error.printStackTrace()
            }
        })
    }

    fun generateFollowUpQuestions(){
        val history: MutableList<ChatMessage> = mutableListOf(
            SystemMessage.from("You are a FollowUp Question Generator, you receive a Chat History " +
                    "and your Task is to Generate a series of questions that further explore the topic." +
                    "Only answer with the Questions where each question is in a new line")
        )

        _currentChat.value.forEach{
            if (it.type() != ChatMessageType.SYSTEM){
                history.add(it)
            }
        }

        model.generate(history, object : StreamingResponseHandler<AiMessage> {

            override fun onNext(token: String) {

            }

            override fun onComplete(response: Response<AiMessage>) {
                println(response.content().text())
                val questions = response.content().text().split("\n").filter { !it.isBlank() }
                _followUpQuestions.tryEmit(questions)
            }

            override fun onError(error: Throwable) {
                error.printStackTrace()
            }
        })
    }
}