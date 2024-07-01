package UI

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.selection.LocalTextSelectionColors
import androidx.compose.foundation.text.selection.TextSelectionColors
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.List
import androidx.compose.material.icons.automirrored.filled.Send
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.input.key.*
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp

@Composable
fun CustomTextInput(
    modifier: Modifier,
    hintText: String = "Enter some Text",
    isLoading: Boolean = false,
    rightButtonClick: (String) -> Unit = {},
    options: List<TextInputOption> = emptyList()
) {
    var text by remember { mutableStateOf(TextFieldValue("")) }
    var isMenuExpanded by remember { mutableStateOf(false) }
    val customTextSelectionColors = remember {
        TextSelectionColors(
            handleColor = Color.DarkGray,
            backgroundColor = Color.Black.copy(alpha = 0.2f)
        )
    }

    Column(modifier = modifier) {
        DropdownMenu(
            expanded = isMenuExpanded,
            onDismissRequest = { isMenuExpanded = false },
            modifier = Modifier
                .background(MaterialTheme.colors.surface)
        ) {
            options.forEach { option ->
                DropdownMenuItem(onClick = {
                    option.action()
                    isMenuExpanded = false
                }) {
                    Row(){
                        Icon(option.icon, "")
                        Spacer(Modifier.size(8.dp))
                        Text(text = option.text)
                    }
                }
            }
        }

        Row(
            modifier = Modifier
                .clip(RoundedCornerShape(15.dp))
                .background(MaterialTheme.colors.surface)
                .fillMaxWidth()
                .padding(8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(onClick = { isMenuExpanded = !isMenuExpanded }) {
                Icon(Icons.AutoMirrored.Default.List, contentDescription = "Left Button", tint = MaterialTheme.colors.onSurface)
            }

            Spacer(modifier = Modifier.width(8.dp))

            CompositionLocalProvider(LocalTextSelectionColors provides customTextSelectionColors) {
                Box(
                    modifier = Modifier.weight(1f),
                    contentAlignment = Alignment.CenterStart
                ) {
                    BasicTextField(
                        value = text,
                        onValueChange = { text = it },
                        modifier = Modifier
                            .fillMaxWidth()
                            .onPreviewKeyEvent { keyEvent ->
                                if (keyEvent.type == KeyEventType.KeyDown && keyEvent.key == Key.Enter && keyEvent.isShiftPressed) {
                                    // Add a line break and place the cursor at the end
                                    text = TextFieldValue(
                                        text = text.text + "\n",
                                        selection = TextRange(text.text.length + 1)
                                    )
                                    true
                                } else if (keyEvent.type == KeyEventType.KeyDown && keyEvent.key == Key.Enter) {
                                    if (text.text.isNotEmpty()) {
                                        // Trigger right button action
                                        rightButtonClick(text.text)
                                        text = TextFieldValue("")
                                    }
                                    true
                                } else {
                                    false
                                }
                            },
                        textStyle = LocalTextStyle.current.copy(color = MaterialTheme.colors.onSurface)
                    )
                    if (text.text.isEmpty()) {
                        Text(
                            text = hintText,
                            style = LocalTextStyle.current.copy(color = MaterialTheme.colors.onSurface.copy(alpha = 0.5f))
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.width(8.dp))

            if (!isLoading) {
                IconButton(onClick = {
                    if (text.text.isNotEmpty()) {
                        rightButtonClick(text.text)
                        text = TextFieldValue("")
                    }
                }) {
                    Icon(
                        Icons.AutoMirrored.Default.Send,
                        contentDescription = "Right Button",
                        tint = MaterialTheme.colors.onSurface.copy(alpha = if (text.text.isEmpty()) 0.2f else 1f)
                    )
                }
            } else {
                CircularProgressIndicator()
            }
        }
    }
}


data class TextInputOption(val icon: ImageVector, val text: String, val action: () -> Unit)