package com.example.projectandroidsuite.ui.mainscreen

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.core.animateIntOffsetAsState
import androidx.compose.animation.core.tween
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
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

    var animateTo by remember { mutableStateOf(0) }

    val swipeState = rememberSwipeableState(SwipeDirections.LEFT)

    var tabState by rememberSaveable { mutableStateOf(0) }

    val offset by animateIntOffsetAsState(
        targetValue = IntOffset(
            x = swipeState.offset.value.roundToInt(),
            y = 0
        ),
        animationSpec = tween(100)
    )

    var width: Int by remember { mutableStateOf(1) }

    BackPressedHandler(showFilters) { showFilters = false }

    Box(
        Modifier.background(MaterialTheme.colors.background)
    ) {
        Column(
            Modifier
                .background(MaterialTheme.colors.background)
                .fillMaxHeight()
                .clickable(enabled = showFilters) { showFilters = false }
        ) {

            ProjectTabRow(
                swipeState = swipeState.currentValue,
                setAnimation = { int -> animateTo = int },
                tabState = tabState,
                setTabState = { int -> tabState - int }
            )

            ListsTabs(
                swipeState = swipeState,
                width = width,
                setWidth = { int -> width = int },
                animateTo = animateTo,
                discardAnimate = { animateTo = 0 },
                offset = offset,
                navController = navController
            )
        }

        // toggling filter sidebar depending of the list showed
        Filters(
            showFilters = showFilters,
            swipeState = swipeState.currentValue
        )

        FilterToggler { showFilters = !showFilters }
    }

}

@Composable
fun Filters(
    showFilters: Boolean,
    swipeState: SwipeDirections

) {
    SlideInHorizontallyTween(showFilters) {
        if (swipeState == SwipeDirections.LEFT)
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


@OptIn(ExperimentalAnimationApi::class)
@Composable
fun SlideInHorizontallyTween(
    condition: Boolean,
    content: @Composable () -> Unit
) {
    AnimatedVisibility(
        condition,
        enter = slideInHorizontally(
            initialOffsetX = { fullWidth -> fullWidth * 2 },
            animationSpec = tween(durationMillis = 400)
        ),
        exit = slideOutHorizontally(
            targetOffsetX = { fullWidth -> fullWidth * 2 },
            animationSpec = tween(durationMillis = 400)
        )
    ) {
        content()
    }
}


@Composable
fun FilterToggler(
    onClick: () -> Unit
) {
    Column {
        Row(Modifier.weight(9F)) {}
        Surface(
            elevation = 10.dp, color = Color.Black.copy(alpha = 0.3F),
            modifier = Modifier
                .weight(1F)
                .padding(all = 12.dp)
        ) {
            Image(
                painterResource(R.drawable.ic_baseline_filter_alt_24),
                "Filter Toggler",
                modifier = Modifier
                    .clickable { onClick() }
                    .height(50.dp)
                    .padding(start = 12.dp, end = 12.dp)
            )
        }

    }
}


@Composable
fun ProjectTabRow(
    setAnimation: (Int) -> Unit,
    swipeState: SwipeDirections,
    tabState: Int,
    setTabState: (Int) -> Unit
) {

    val titles = listOf("Projects", "Tasks")

    TabRow(
        selectedTabIndex = if (swipeState == SwipeDirections.LEFT) 0 else 1
    ) {
        titles.forEachIndexed { index, title ->
            Tab(
                modifier = Modifier
                    .height(50.dp)
                    .testTag("ProjectPageTab$index")
                    .background(MaterialTheme.colors.primary),
                text = {
                    Row {
                        when (index) {
                            0 -> Icon(painterResource(R.drawable.clipboard_list_outline), "")
                            1 -> Icon(painterResource(R.drawable.calendar_check_outline), "")
                        }
                        Text(title)
                    }
                },
                selected = if (index == 0) swipeState == SwipeDirections.LEFT
                else swipeState == SwipeDirections.RIGHT,
                onClick = {
                    setTabState(index)
                    if (tabState == 0)
                        setAnimation(1)
                    if (tabState == 1)
                        setAnimation(2)
                }
            )
        }
    }
}

@ExperimentalMaterialApi
@Composable
fun ListsTabs(
    swipeState: SwipeableState<SwipeDirections>,
    width: Int,
    setWidth: (Int) -> Unit,
    animateTo: Int,
    discardAnimate: () -> Unit,
    offset: IntOffset,
    navController: NavHostController
) {

    if (animateTo == 1)
        LaunchedEffect(offset) {
            swipeState.snapTo(SwipeDirections.LEFT)
            discardAnimate()
        }
    if (animateTo == 2)
        LaunchedEffect(offset) {
            swipeState.snapTo(SwipeDirections.RIGHT)
            discardAnimate()
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
                        .onSizeChanged { setWidth(it.width) }
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