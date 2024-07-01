package Data

import dev.langchain4j.data.message.ChatMessage
import dev.langchain4j.store.memory.chat.ChatMemoryStore

class PersistentChatMemoryStore(): ChatMemoryStore {
    override fun getMessages(p0: Any?): List<ChatMessage?>? {
        TODO("Not yet implemented")
    }

    override fun updateMessages(
        p0: Any?,
        p1: List<ChatMessage?>?
    ) {
        TODO("Not yet implemented")
    }

    override fun deleteMessages(p0: Any?) {
        TODO("Not yet implemented")
    }

}