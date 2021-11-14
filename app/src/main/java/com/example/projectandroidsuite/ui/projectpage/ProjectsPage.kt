package com.example.projectandroidsuite.ui.projectpage

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.core.animateIntOffsetAsState
import androidx.compose.animation.core.tween
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.example.projectandroidsuite.R
import com.example.projectandroidsuite.ui.scaffold.CustomScaffold
import com.example.projectandroidsuite.ui.utils.BackPressedHandler
import com.example.projectandroidsuite.ui.utils.SwipeDirections
import com.example.projectandroidsuite.ui.utils.expandScrollingViewportWidthBy
import kotlin.math.roundToInt


@OptIn(ExperimentalAnimationApi::class, ExperimentalMaterialApi::class)
@Composable
fun ProjectsPage(
    navController: NavHostController
) {

    var showFilters by remember { mutableStateOf(false) }
    var state by rememberSaveable { mutableStateOf(0) }
    val titles = listOf("Projects", "Tasks")
    var animateTo by remember { mutableStateOf(0) }
    var width: Int by remember { mutableStateOf(1) }

    val swipeState = rememberSwipeableState(SwipeDirections.LEFT)

    val offset by animateIntOffsetAsState(
        targetValue = IntOffset(
            x = swipeState.offset.value.roundToInt(),
            y = 0
        ),
        animationSpec = tween(100)
    )

    BackPressedHandler(showFilters) { showFilters = !showFilters }

    if (animateTo == 1)
        LaunchedEffect(offset) {
            swipeState.snapTo(SwipeDirections.LEFT)
            animateTo = 0
        }
    if (animateTo == 2)
        LaunchedEffect(offset) {
            swipeState.snapTo(SwipeDirections.RIGHT)
            animateTo = 0
        }

    CustomScaffold({ showFilters = !showFilters }, navController, viewModel = hiltViewModel()) {
        Box(
            Modifier.background(MaterialTheme.colors.background)
        ) {
            Column(
                Modifier
                    .background(MaterialTheme.colors.background)
                    .fillMaxHeight()
                    .clickable(enabled = showFilters) { showFilters = false }
            ) {
                TabRow(selectedTabIndex = if(swipeState.currentValue == SwipeDirections.LEFT) 0 else 1) {
                    titles.forEachIndexed { index, title ->
                        Tab(modifier = Modifier
                            .height(50.dp)
                            .testTag("ProjectPageTab$index")
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
                            selected = if (index == 0) swipeState.currentValue == SwipeDirections.LEFT
                            else swipeState.currentValue == SwipeDirections.RIGHT,
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

                BoxWithConstraints(
                    modifier = Modifier
                        .swipeable(
                            swipeState,
                            anchors = mapOf(
                                (-(width).toFloat()) to SwipeDirections.RIGHT,
                                0.0f to SwipeDirections.LEFT
                            ),
                            orientation = Orientation.Horizontal
                        )
                ) {
                    Box(
                        modifier = Modifier
                            .expandScrollingViewportWidthBy(maxWidth)
                            .offset { offset }
                            .width(maxWidth * 2)
                    ) {
                        Row {
                            Row(
                                Modifier
                                    .weight(1F)
                                    .onSizeChanged { width = it.width }
                            ) {
                                Column(
                                    Modifier.semantics {
                                        contentDescription = "ProjectListOnProjectPage"
                                    }
                                ) {
                                    ProjectList(hiltViewModel(), navController)
                                }
                            }
                            Row(Modifier.weight(1F)) {
                                Column(
                                    Modifier.semantics {
                                        contentDescription = "TaskListOnProjectPage"
                                    }
                                ) {
                                    TaskList(hiltViewModel(), navController)
                                }
                            }
                        }
                    }
                }
            }


            // toggling filter sidebar depending of the list showed
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
                if(swipeState.currentValue == SwipeDirections.LEFT)
                    Column(Modifier.semantics {
                        contentDescription = "FilterProjectsComponent"
                    }) { FilterProjects(viewModel = hiltViewModel()) }
                else
                    Column(Modifier.semantics {
                        contentDescription = "TaskProjectsComponent"
                    }) {
                        FilterTasks(viewModel = hiltViewModel())
                    }
            }
        }
    }
}
