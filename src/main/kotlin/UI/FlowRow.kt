package UI

import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.primarySurface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.input.pointer.pointerMoveFilter
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun <T>flowRow(modifier: Modifier, items: List<T>, itemComposable: @Composable (T) -> Unit) {
    FlowRow(modifier = modifier.padding(8.dp), horizontalArrangement = Arrangement.Start, verticalArrangement = Arrangement.SpaceEvenly) {
        items.forEach {
            itemComposable(it)
            Spacer(Modifier.size(2.dp))
        }
    }
}

@Composable
fun stringFlowRow(modifier: Modifier, strings: List<String>, onClick: (String) -> Unit, onDelete: (String) -> Unit = {}){
    flowRow(modifier, strings){
        Chip(modifier = Modifier.clickable{
            onClick(it)
        }, text = it){
            onDelete(it)
        }
    }
}


@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun Chip(
    modifier: Modifier = Modifier,
    icon: ImageVector? = null,
    text: String,
    onDelete: () -> Unit
) {
    var isHovered = remember { mutableStateOf(false) }
    Row(
        modifier = modifier
            .padding(top = 2.dp)
            .clip(RoundedCornerShape(50))
            .background(MaterialTheme.colors.secondary)
            .padding(horizontal = 5.dp, vertical = 4.dp)
            .pointerMoveFilter(
                onEnter = {
                    isHovered.value = true
                    false
                },
                onExit = {
                    isHovered.value = false
                    false
                }
            )
            .animateContentSize(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        if (icon != null) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = MaterialTheme.colors.onSecondary,
                modifier = Modifier.size(20.dp)
            )
        }

        Spacer(modifier = Modifier.width(4.dp))

        Text(
            text = text,
            maxLines = if (isHovered.value) 4 else 1,
            overflow = TextOverflow.Ellipsis,
            style = MaterialTheme.typography.body2,
            modifier = Modifier,
            color = MaterialTheme.colors.onSecondary
        )

        Spacer(modifier = Modifier.width(2.dp))

        if (isHovered.value) {
            Box(
                modifier = Modifier
                    .size(17.dp)
                    .clickable { onDelete() },
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.Close,
                    contentDescription = "Delete",
                    tint = MaterialTheme.colors.onSecondary,
                    modifier = Modifier.size(15.dp) // Adjust the icon size if needed
                )
            }
        }
    }
}