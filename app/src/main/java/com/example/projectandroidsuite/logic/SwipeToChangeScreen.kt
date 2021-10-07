package com.example.projectandroidsuite.logic

import android.util.Log
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.AnimationVector1D
import androidx.compose.animation.core.calculateTargetValue
import androidx.compose.animation.splineBasedDecay
import androidx.compose.foundation.gestures.awaitFirstDown
import androidx.compose.foundation.gestures.horizontalDrag
import androidx.compose.foundation.layout.offset
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.input.pointer.consumePositionChange
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.input.pointer.positionChange
import androidx.compose.ui.input.pointer.util.VelocityTracker
import androidx.compose.ui.unit.IntOffset
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch
import kotlin.math.absoluteValue
import kotlin.math.roundToInt



fun Modifier.swipeToChangeScreen(
    offset: Animatable<Float, AnimationVector1D>,
    onSwiped: () -> Unit,
): Modifier = composed {

    var direction by remember { mutableStateOf(SwipeDirections.RIGHT) }

    pointerInput(Unit) {
        val decay = splineBasedDecay<Float>(this)
        while (true) {
            coroutineScope {
                val pointerId = awaitPointerEventScope { awaitFirstDown().id }
                offset.stop()
                awaitPointerEventScope {
                    horizontalDrag(pointerId) { change ->
                        val horizontalDragOffset = offset.value + change.positionChange().x
                        launch {
                            when (direction) {
                                SwipeDirections.RIGHT -> if (horizontalDragOffset < 0) {
                                    offset.snapTo(horizontalDragOffset)
                                }
                                SwipeDirections.LEFT -> if (horizontalDragOffset > 0) {
                                    offset.snapTo(horizontalDragOffset)
                                }
                            }
                        }
                    }
                }
                val velocityTracker = VelocityTracker()
                val velocity = velocityTracker.calculateVelocity().x
                val targetOffsetX = decay.calculateTargetValue(offset.value, velocity)

                launch {
                    if (targetOffsetX.absoluteValue <= size.width / 4) {
                        offset.animateTo(targetValue = 0f, initialVelocity = velocity)
                    } else {
                        when (direction) {
                            SwipeDirections.RIGHT -> offset.animateTo(
                                targetValue = -size.width.toFloat(),
                                initialVelocity = velocity
                            )
                            SwipeDirections.LEFT -> offset.animateTo(
                                targetValue = 0F,
                                initialVelocity = velocity / 3
                            )
                        }
                        onSwiped()
                        when (direction) {
                            SwipeDirections.RIGHT -> if (offset.value > 400) direction = SwipeDirections.LEFT
                            SwipeDirections.LEFT -> if (offset.value < 400F) direction = SwipeDirections.LEFT
                        }
                        Log.d("swipeToChangeScreen", direction.toString())
                    }
                }
            }
        }
    }
}


enum class SwipeDirections {
    RIGHT, LEFT
}
