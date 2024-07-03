package UI

import Static.AppMarkdownColors
import Static.AppTypography
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Alignment.Companion.Center
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.mikepenz.markdown.compose.Markdown
import dev.langchain4j.data.message.ChatMessage
import dev.langchain4j.data.message.ChatMessageType

@Composable
fun ChatMessage(modifier: Modifier = Modifier, message: ChatMessage) {
    Row(modifier = modifier.padding(start = 8.dp, end = 8.dp), verticalAlignment = Alignment.Top){
        if(message.type() != ChatMessageType.USER){
            Box(modifier = Modifier.size(40.dp).clip(CircleShape).background(MaterialTheme.colors.primaryVariant)){
                Image(modifier = Modifier.align(Center), painter = if (message.type() == ChatMessageType.AI) painterResource("img/ai.png") else painterResource("img/settings.png"), contentDescription = "")
            }
            Spacer(Modifier.size(10.dp))
        }
        Box(modifier = Modifier.clip(RoundedCornerShape(8.dp)).background(MaterialTheme.colors.surface).fillMaxWidth()){
            Markdown(
                modifier = Modifier.padding(8.dp),
                content = message.text(),
                colors = AppMarkdownColors(),
                typography = AppTypography()
            )
        }
    }
}