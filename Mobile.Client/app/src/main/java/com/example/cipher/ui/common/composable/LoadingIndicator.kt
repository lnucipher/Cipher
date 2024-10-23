package com.example.cipher.ui.common.composable

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.requiredSize
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import kotlin.math.PI
import kotlin.math.absoluteValue
import kotlin.math.cos
import kotlin.math.min
import kotlin.math.roundToInt
import kotlin.math.sin
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import androidx.compose.animation.core.*
import androidx.compose.runtime.Stable
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.Layout

private const val NumDots = 5
private const val AnimationDuration = 1500
private const val AnimationSegment = AnimationDuration / 10
private val MainDotSize = 18.dp

private val Float.alphaFromRadians: Float
    get() {
        val normalized = (this / (2f * PI)).toFloat()
        return .5f + (normalized - .5f).absoluteValue
    }

@Stable
interface LoadingState {
    fun start(scope: CoroutineScope)
    operator fun get(index: Int): Float
}

class LoadingStateImpl : LoadingState {
    private val animationValues: List<MutableState<Float>> = List(NumDots) {
        mutableFloatStateOf(0f)
    }

    override operator fun get(index: Int) = animationValues[index].value

    override fun start(scope: CoroutineScope) {
        repeat(NumDots) { index ->
            scope.launch {
                animate(
                    initialValue = 0f,
                    targetValue = (2f * PI).toFloat(),
                    animationSpec = infiniteRepeatable(
                        animation = keyframes {
                            durationMillis = AnimationDuration
                            0f at 0
                            (.5 * PI).toFloat() at 2 * AnimationSegment
                            PI.toFloat() at 3 * AnimationSegment
                            (1.5 * PI).toFloat() at 4 * AnimationSegment
                            (2f * PI).toFloat() at 6 * AnimationSegment
                        },
                        repeatMode = RepeatMode.Restart,
                        initialStartOffset = StartOffset(offsetMillis = 100 * index)
                    ),
                ) { value, _ ->
                    animationValues[index].value = value
                }
            }
        }
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as LoadingStateImpl

        return animationValues == other.animationValues
    }

    override fun hashCode(): Int = animationValues.hashCode()
}

@Composable
fun rememberLoadingState(): LoadingState = remember {
    LoadingStateImpl()
}

@Composable
fun LoadingIndicator(
    modifier: Modifier = Modifier,
    color: Color = MaterialTheme.colorScheme.primary,
) {
    val state = rememberLoadingState()
    LaunchedEffect(key1 = Unit) {
        state.start(this)
    }
    Layout(
        content = {
            val minFactor = .3f
            val step = minFactor / NumDots
            repeat(NumDots) { index ->
                val size = MainDotSize * (1f - step * index)
                Dot(
                    color = color,
                    modifier = Modifier
                        .requiredSize(size),
                )
            }
        },
        modifier = modifier,
    ) { measurables, constraints ->
        val looseConstraints = constraints.copy(
            minWidth = 0,
            minHeight = 0,
        )
        val placeables = measurables.map { measurable -> measurable.measure(looseConstraints) }
        layout(
            width = constraints.maxWidth,
            height = constraints.maxHeight,
        ) {
            val radius = min(constraints.maxWidth, constraints.maxHeight) / 2f
            placeables.forEachIndexed { index, placeable ->
                val animatedValue = state[index]
                val x = (radius + radius * sin(animatedValue)).roundToInt()
                val y = (radius - radius * cos(animatedValue)).roundToInt()
                placeable.placeRelativeWithLayer(
                    x = x,
                    y = y,
                ) {
                    alpha = state[index].alphaFromRadians
                }
            }
        }
    }
}

@Composable
private fun Dot(
    color: Color,
    modifier: Modifier = Modifier,
) {
    Box(
        modifier = modifier
            .clip(shape = CircleShape)
            .background(color = color)
    )
}


