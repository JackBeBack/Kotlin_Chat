package UI

import Static.AppMarkdownColors
import Static.AppTypography
import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Alignment.Companion.Center
import androidx.compose.ui.Alignment.Companion.TopEnd
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.unit.dp
import com.mikepenz.markdown.compose.Markdown
import dev.langchain4j.data.message.ChatMessage
import dev.langchain4j.data.message.ChatMessageType

@Composable
fun ChatMessage(modifier: Modifier = Modifier, message: ChatMessage) {
    var copied by remember{ mutableStateOf(false) }
    val clipboardManager = LocalClipboardManager.current
    if (message.type() != ChatMessageType.SYSTEM) {
        Row(
            modifier = modifier.padding(start = 8.dp, end = 8.dp),
            verticalAlignment = Alignment.Top
        ) {
            if (message.type() != ChatMessageType.USER) {
                Box(modifier = Modifier.size(40.dp).clip(CircleShape).background(MaterialTheme.colors.primaryVariant)) {
                    Image(
                        modifier = Modifier.align(Center),
                        painter = if (message.type() == ChatMessageType.AI) painterResource("img/ai.png") else painterResource(
                            "img/settings.png"
                        ),
                        contentDescription = ""
                    )
                }
                Spacer(Modifier.size(10.dp))
            }
            Box(
                modifier = Modifier.clip(RoundedCornerShape(8.dp)).background(MaterialTheme.colors.surface).animateContentSize()
                    .fillMaxWidth()
            ) {
                Markdown(
                    modifier = Modifier.padding(8.dp),
                    content = message.text(),
                    colors = AppMarkdownColors(),
                    typography = AppTypography()
                )
                if (message.type() == ChatMessageType.AI) {
                    Icon(painter = if (copied) {
                        rememberVectorPainter(image = Icons.Default.Check)
                    } else {
                        painterResource("/img/copy.png")
                    }, contentDescription = "", modifier = Modifier.padding(16.dp).align(
                        TopEnd).size(20.dp).clickable {
                            copied = true
                        clipboardManager.setText(AnnotatedString(message.text()))
                    }, tint = MaterialTheme.colors.onSurface)
                }

            }
        }
    }
}