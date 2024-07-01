package UI

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import dev.langchain4j.data.message.ChatMessage

@Composable
fun ChatMessage(modifier: Modifier = Modifier, message: ChatMessage) {
    Box(modifier = modifier.clip(RoundedCornerShape(8.dp)).background(Color.LightGray).fillMaxWidth()){
        Text(modifier = Modifier.padding(8.dp), text = "${message.type()} -> ${message.text()}")
    }
}