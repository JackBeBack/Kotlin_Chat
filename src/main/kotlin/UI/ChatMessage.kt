package UI

import Static.appMarkdownColors
import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Alignment.Companion.Center
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import dev.langchain4j.data.message.ChatMessage
import dev.langchain4j.data.message.ChatMessageType
import androidx.compose.runtime.*
import com.mikepenz.markdown.compose.Markdown
import com.mikepenz.markdown.model.MarkdownColors
import org.jetbrains.skia.FontStyle
import kotlin.contracts.contract

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
            //Text(modifier = Modifier.padding(8.dp), text = "${message.text()}", color = MaterialTheme.colors.onSurface)
            Markdown(
                message.text(),
                colors = appMarkdownColors,
                typography = markdownTypography()
            )
        }
    }
}