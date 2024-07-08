package Views

import UI.SegmentedControl
import Viewmodel.ChatsModel
import Viewmodel.ModelType
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.Send
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import androidx.compose.runtime.*

@Composable
fun SettingsView(modifier: Modifier, onBack: () -> Unit) {
    val currentModel by ChatsModel.instance.currentModel.collectAsState()

    val options = listOf(
        ModelType.OPENAI_GPT3_TURBO,
        ModelType.OLLAMA_LLAMA_3,
        ModelType.OLLAMA_LLAMA_2,
        ModelType.OLLAMA_WIZARD_VICUNA,
    )

    val currentModelIndex = options.indexOf(currentModel)

    Column(modifier.fillMaxSize(0.7f)
        .clip(RoundedCornerShape(35.dp))
        .background(MaterialTheme.colors.surface), verticalArrangement = Arrangement.spacedBy(8.dp)) {
        Box(Modifier.fillMaxWidth().padding(top = 8.dp, start = 8.dp)){
            Text("Settings", color = MaterialTheme.colors.onSurface, modifier = Modifier.align(Alignment.Center))
            Icon(
                Icons.AutoMirrored.Default.ArrowBack,
                contentDescription = "Right Button",
                tint = MaterialTheme.colors.onSurface,
                modifier = Modifier.align(Alignment.CenterStart).clickable{
                    onBack()
                }
            )
        }
        Row(Modifier.fillMaxWidth().padding(start = 8.dp), verticalAlignment = Alignment.CenterVertically){
            Text("Current Model:", color = MaterialTheme.colors.onSurface)
            Spacer(Modifier.size(8.dp))
            SegmentedControl(Modifier, options.map { it.modelName }, selectedState = currentModelIndex){
                ChatsModel.instance.initModle(options[it])
            }
        }


    }
}