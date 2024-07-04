package Viewmodel

import dev.langchain4j.data.message.*
import dev.langchain4j.model.StreamingResponseHandler
import dev.langchain4j.model.chat.StreamingChatLanguageModel
import dev.langchain4j.model.ollama.OllamaStreamingChatModel
import dev.langchain4j.model.openai.OpenAiStreamingChatModel
import dev.langchain4j.model.output.Response
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import java.time.Duration.ofSeconds
import kotlin.math.max
import kotlin.math.min

enum class ModelType{
    OPENAI,
    OLLAMA
}

class ChatsModel(val type: ModelType) {
    private var model: StreamingChatLanguageModel

    private val _currentChat = MutableStateFlow<List<ChatMessage>>(listOf())
    val currentChat: StateFlow<List<ChatMessage>> = _currentChat

    private val _followUpQuestions = MutableStateFlow<List<String>>(listOf())
    val followUpQuestions: StateFlow<List<String>> = _followUpQuestions

    init {
        val apiKey = System.getenv("OPENAI_API_KEY")

        model = when(type){
            ModelType.OPENAI -> {
                OpenAiStreamingChatModel.builder()
                    .apiKey(apiKey)
                    .timeout(ofSeconds(100))
                    .build()
            }
            ModelType.OLLAMA -> {
                OllamaStreamingChatModel.builder()
                    .baseUrl("http://localhost:11434")
                    .modelName("llama3")
                    .temperature(1.0)
                    //.modelName("wizard-vicuna")
                    //.modelName("dolphin-mistral")
                    .build()
            }
        }
    }

    companion object {
        val instance: ChatsModel = ChatsModel(ModelType.OLLAMA)
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

    fun generateChatTitle(onFinish: (String) -> Unit){
        val history: MutableList<ChatMessage> = mutableListOf(
            SystemMessage.from("You are a Chat Title Generator, you receive a Chat History " +
                    "and your Task is to Generate a Title." +
                    "Only answer with the Title and nothing else")
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
                onFinish(response.content().text())
            }

            override fun onError(error: Throwable) {
                error.printStackTrace()
            }
        })
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

    fun generateChat(onNewToken: (String) -> Unit = {}, onFinish: (String) -> Unit = {}){
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

    fun generateFollowUpQuestions(max: Int = 4){
        val history: MutableList<ChatMessage> = mutableListOf()

        _currentChat.value.forEach{
            if (it.type() != ChatMessageType.SYSTEM){
                history.add(it)
            }
        }

        history.add(UserMessage.from("What are further questions to explore this topic?\" +\n" +
                "                        \"Only answer with the Questions where each question is in a new line"))


        model.generate(history.toList(), object : StreamingResponseHandler<AiMessage> {

            override fun onNext(token: String) {
            }

            override fun onComplete(response: Response<AiMessage>) {
                try {
                    println("Questions: ${response.content().text()}")
                    var questions = response.content().text().split("\n").filter { it.isNotBlank() && !it.contains(":")}.map {
                        it.replace("-", "")
                    }
                    if (questions.size > max) questions = questions.subList(0, max)

                    _followUpQuestions.tryEmit(questions)
                } catch (e: Exception){
                    //
                    println("Error: ${response.content().text()}")
                }
            }

            override fun onError(error: Throwable) {
                error.printStackTrace()
            }
        })
    }

    fun clearFollowUpQuestion() {
           _followUpQuestions.tryEmit(listOf())
    }
}