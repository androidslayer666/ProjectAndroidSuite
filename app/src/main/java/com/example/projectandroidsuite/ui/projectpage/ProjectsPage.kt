package com.example.projectandroidsuite.ui.projectpage

import androidx.compose.animation.*
import androidx.compose.animation.core.calculateTargetValue
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.awaitFirstDown
import androidx.compose.foundation.gestures.horizontalDrag
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.consumePositionChange
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.input.pointer.positionChange
import androidx.compose.ui.input.pointer.util.VelocityTracker
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.example.projectandroidsuite.R
import com.example.projectandroidsuite.logic.BackHandler
import com.example.projectandroidsuite.logic.SwipeDirections
import com.example.projectandroidsuite.logic.expandScrollingViewportWidthBy
import com.example.projectandroidsuite.ui.scaffold.CustomScaffold
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch
import kotlin.math.absoluteValue
import kotlin.math.roundToInt


@OptIn(ExperimentalAnimationApi::class)
@Composable
fun ProjectsPage(
    navController: NavHostController
) {

    var showFilters by remember { mutableStateOf(false) }
    var state by rememberSaveable { mutableStateOf(0) }
    var initialOffset by rememberSaveable { mutableStateOf(0F) }
    val offset = remember { androidx.compose.animation.core.Animatable(initialOffset) }
    val titles = listOf("Projects", "Tasks")
    var direction by remember { mutableStateOf(SwipeDirections.RIGHT) }
    var sizeMeasured by remember { mutableStateOf(0) }
    var animateTo by remember { mutableStateOf(0) }

    BackHandler(showFilters) { showFilters = !showFilters }

    //Log.d("ProjectsPage", initialOffset.toString())

    if (animateTo == 1)
        LaunchedEffect(offset) {
            offset.animateTo(0F)
            direction = SwipeDirections.RIGHT
            animateTo = 0
        }
    if (animateTo == 2)
        LaunchedEffect(offset) {
            offset.animateTo(-sizeMeasured.toFloat())
            direction = SwipeDirections.LEFT
            animateTo = 0
        }

    CustomScaffold({ showFilters = !showFilters }, navController, viewModel = hiltViewModel()) {
        Box(Modifier.background(MaterialTheme.colors.background)) {
            Column(
                Modifier
                    .background(MaterialTheme.colors.background)
                    .fillMaxHeight()
                    .clickable(enabled = showFilters) { showFilters = false }
            ) {
                TabRow(selectedTabIndex = state) {
                    titles.forEachIndexed { index, title ->
                        Tab(modifier = Modifier
                            .height(50.dp)
                            .background(MaterialTheme.colors.primary),
                            text = {
                                Row {
                                    if (index == 0)
                                        Icon(painterResource(R.drawable.clipboard_list_outline), "")
                                    else
                                        Icon(painterResource(R.drawable.calendar_check_outline), "")
                                    Spacer(modifier = Modifier.width(10.dp))
                                    Text(title)
                                }
                            },
                            selected = state == index,
                            onClick = {
                                state = index
                                if (state == 0)
                                    animateTo = 1
                                if (state == 1)
                                    animateTo = 2
                            }
                        )
                    }
                }

                Row(
                    modifier = Modifier
                        .background(MaterialTheme.colors.background)
                        .expandScrollingViewportWidthBy(440.dp)
                        .fillMaxHeight()
                ) {
                    Card(
                        Modifier
                            .width(LocalContext.current.resources.displayMetrics.xdpi.dp - 55.dp)
                            .fillMaxHeight()
                            .background(MaterialTheme.colors.background)
                            .offset {
                                IntOffset(offset.value.roundToInt(), 0)
                            }
                            .pointerInput(Unit) {
                                sizeMeasured = size.width
                                val decay = splineBasedDecay<Float>(this)
                                while (true) {
                                    coroutineScope {
                                        val pointerId =
                                            awaitPointerEventScope { awaitFirstDown().id }
                                        offset.stop()
                                        awaitPointerEventScope {
                                            horizontalDrag(pointerId) { change ->
                                                val horizontalDragOffset =
                                                    offset.value + change.positionChange().x
                                                launch {
                                                    if (horizontalDragOffset < 0) {
                                                        offset.snapTo(horizontalDragOffset)
                                                    }
                                                }
                                                change.consumePositionChange()
                                            }
                                        }
                                        val velocityTracker = VelocityTracker()
                                        val velocity = velocityTracker.calculateVelocity().x
                                        val targetOffsetX =
                                            decay.calculateTargetValue(offset.value, velocity)

                                        launch {
                                            if (targetOffsetX.absoluteValue <= size.width / 4) {
                                                offset.animateTo(
                                                    targetValue = 0f,
                                                    initialVelocity = velocity
                                                )
                                            } else {
                                                initialOffset = -size.width.toFloat()
                                                state = 1
                                                offset.animateTo(
                                                    targetValue = -size.width.toFloat(),
                                                    initialVelocity = velocity
                                                )

                                                direction = SwipeDirections.LEFT
                                            }
                                        }
                                    }
                                }
                            }
                    ) {
                        Column {
                            ProjectList(hiltViewModel(), navController)
                        }
                    }

                    Card(
                        Modifier
                            .width(LocalContext.current.resources.displayMetrics.xdpi.dp - 55.dp)
                            .fillMaxHeight()
                            .background(MaterialTheme.colors.background)
                            .offset {
                                IntOffset(offset.value.roundToInt(), 0)
                            }
                            .pointerInput(Unit) {
                                val decay = splineBasedDecay<Float>(this)
                                while (true) {
                                    coroutineScope {
                                        val pointerId =
                                            awaitPointerEventScope { awaitFirstDown().id }
                                        offset.stop()
                                        awaitPointerEventScope {
                                            horizontalDrag(pointerId) { change ->
                                                val horizontalDragOffset =
                                                    offset.value + change.positionChange().x
                                                launch {
                                                    if (horizontalDragOffset > -size.width) {
                                                        offset.snapTo(horizontalDragOffset)
                                                    }
                                                }
                                            }
                                        }

                                        val velocityTracker = VelocityTracker()
                                        val velocity = velocityTracker.calculateVelocity().x
                                        val targetOffsetX =
                                            decay.calculateTargetValue(offset.value, velocity)

                                        launch {
                                            if (size.width - targetOffsetX.absoluteValue < size.width / 4) {
                                                offset.animateTo(
                                                    targetValue = -size.width.toFloat(),
                                                    initialVelocity = velocity / 3
                                                )
                                            } else {
                                                initialOffset = 0F
                                                state = 0
                                                offset.animateTo(
                                                    targetValue = 0F,
                                                    initialVelocity = velocity
                                                )
                                                direction = SwipeDirections.RIGHT
                                            }
                                        }
                                    }
                                }
                            }
                    ) {
                        Column {
                            TaskList(hiltViewModel(), navController)
                        }
                    }
                }
            }

            AnimatedVisibility(
                showFilters,
                enter = slideInHorizontally(

                    initialOffsetX = { fullWidth -> fullWidth * 2 },
                    animationSpec = tween(durationMillis = 400)
                ),
                exit = slideOutHorizontally(
                    targetOffsetX = { fullWidth -> fullWidth * 2 },
                    animationSpec = tween(durationMillis = 400)
                )
            ) {
                if (state == 0)
                    FilterProjects(viewModel = hiltViewModel())
                if (state == 1)
                    FilterTasks(viewModel = hiltViewModel())
            }
        }
    }
}
