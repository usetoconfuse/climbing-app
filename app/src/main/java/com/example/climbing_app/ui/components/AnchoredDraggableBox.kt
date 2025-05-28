package com.example.climbing_app.ui.components

import androidx.compose.animation.core.tween
import androidx.compose.animation.rememberSplineBasedDecay
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.gestures.AnchoredDraggableState
import androidx.compose.foundation.gestures.DraggableAnchors
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.anchoredDraggable
import androidx.compose.foundation.gestures.animateTo
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import kotlin.math.roundToInt


@OptIn(ExperimentalFoundationApi::class)
@Composable
fun AnchoredDraggableBox(
    modifier: Modifier,
    firstContent: @Composable (modifier: Modifier) -> Unit,
    secondContent: @Composable (modifier: Modifier) -> Unit,
    secondContentCover: @Composable (modifier: Modifier) -> Unit,
    offsetSize: Dp,
    onDragComplete: () -> Unit
) {
    val density = LocalDensity.current
    val positionalThresholds: (totalDistance: Float) -> Float =
        { totalDistance -> totalDistance * 0.95f }
    val velocityThreshold: () -> Float = { with(density) { 100.dp.toPx() } }
    val decayAnimationSpec = rememberSplineBasedDecay<Float>()

    val state = remember {
        AnchoredDraggableState(
            initialValue = DragAnchors.Start,
            anchors = with(density) {
                DraggableAnchors {
                    DragAnchors.Start at 0.dp.toPx()
                    DragAnchors.End at -offsetSize.toPx()
                }
            },
            positionalThresholds,
            velocityThreshold,
            tween(),
            decayAnimationSpec
        )
    }

    // When the user drags all the way, fling back to the starting position and trigger callback
    LaunchedEffect(state.settledValue) {
        if (state.settledValue == DragAnchors.End) {
            onDragComplete()
            state.animateTo(DragAnchors.Start)
        }
    }

    Box(
        modifier = modifier
    ) {
        firstContent(
            Modifier
                .fillMaxWidth()
                .align(Alignment.Center)
                .offset {
                    IntOffset(
                        state.requireOffset().roundToInt(), 0)
                }
                .anchoredDraggable(state, Orientation.Horizontal)
        )
        secondContent(
            Modifier
                .align(Alignment.CenterEnd)
                .offset {
                    IntOffset(
                        (state.requireOffset() + offsetSize.toPx()).roundToInt(), 0
                    )
                }
                .anchoredDraggable(state, Orientation.Horizontal)
        )
        secondContentCover(
            Modifier
                .align(Alignment.CenterEnd)
                .offset {
                    IntOffset(offsetSize.toPx().roundToInt(), 0)
                }
        )
    }
}

enum class DragAnchors {
    Start,
    End,
}