package UI

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.FractionalThreshold
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.List
import androidx.compose.material.primarySurface
import androidx.compose.material.rememberSwipeableState
import androidx.compose.material.swipeable
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Alignment.Companion.CenterVertically
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.input.key.Key.Companion.R
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun SegmentedControl(
    modifier: Modifier = Modifier,
    states: List<String>,
    selectedState: Int = 0,
    itemWidth: Dp = 120.dp,
    onSelectionChange: (index: Int) -> Unit = {}
) {
    val primaryColor = MaterialTheme.colors.secondary
    val backgroundColor = MaterialTheme.colors.background

    var selectedOption by remember { mutableIntStateOf(selectedState) }
    val optionWidthPx = itemWidth.toPx() // Adjust as necessary
    val canvasWidth = itemWidth * states.size
    val height = 60.dp
    val anchors = states.mapIndexed { index, _ ->
        index * optionWidthPx to index
    }.toMap()
    val animatedXOffset = remember {
        Animatable(0f)
    }
    val cornerRadius = 24.dp.toPx()
    val padding = 2.dp.toPx()

    val swipeableState = rememberSwipeableState(initialValue = selectedState)
    val coroutineScope = rememberCoroutineScope()

    LaunchedEffect(swipeableState.targetValue) {
        animatedXOffset.animateTo(
            targetValue = optionWidthPx * swipeableState.targetValue,
            animationSpec = tween(durationMillis = 300, easing = LinearOutSlowInEasing)
        )
        selectedOption = swipeableState.targetValue
        onSelectionChange(selectedOption)
    }

    Box(modifier = modifier.swipeable(
        state = swipeableState,
        anchors = anchors,
        orientation = androidx.compose.foundation.gestures.Orientation.Horizontal,
        thresholds = { _, _ -> FractionalThreshold(0.3f) }
    )) {
        Canvas(
            modifier = Modifier
                .width(canvasWidth)
                .height(height)
        ) {
            // Draw background for each state
            drawRoundRect(
                color = backgroundColor,
                size = Size(this.size.width, this.size.height),
                cornerRadius = CornerRadius(cornerRadius, cornerRadius)
            )

            // Draw animated selected background
            drawRoundRect(
                color = primaryColor,
                topLeft = Offset(animatedXOffset.value + padding, padding),
                size = Size(optionWidthPx - (2 * padding), this.size.height - (2 * padding)),
                cornerRadius = CornerRadius(cornerRadius, cornerRadius)
            )
        }

        // Inside your Box composable
        Row(
            modifier = Modifier
                .width(canvasWidth)
                .height(height)
                .clip(RoundedCornerShape(24.dp))
        ) {
            states.forEachIndexed { index, text ->
                Box(
                    modifier = Modifier
                        .fillMaxHeight()
                        .width(itemWidth)
                        .clickable {
                            // Update swipeable state instead of directly changing the selectedOption
                            coroutineScope.launch {
                                swipeableState.animateTo(index)
                            }
                        },
                    contentAlignment = Alignment.Center
                ) {
                    Row(verticalAlignment = CenterVertically) {
                        if (index == swipeableState.targetValue) {
                            Icon(
                                Icons.Default.Check,
                                contentDescription = "Right Button",
                                tint = MaterialTheme.colors.onPrimary,
                                modifier = Modifier.size(18.dp)
                            )
                            Spacer(modifier = Modifier.size(4.dp))
                        }
                        Text(
                            modifier = Modifier
                                .clickable {
                                    coroutineScope.launch {
                                        swipeableState.animateTo(index)
                                    }
                                }.padding(2.dp),
                            text = text,
                            color = if (index == swipeableState.targetValue) MaterialTheme.colors.onPrimary else MaterialTheme.colors.onSurface
                        )
                    }
                }
            }
        }

    }
}

@Composable
fun Dp.toPx(): Float {
    val density = LocalDensity.current
    return this.value * density.density
}