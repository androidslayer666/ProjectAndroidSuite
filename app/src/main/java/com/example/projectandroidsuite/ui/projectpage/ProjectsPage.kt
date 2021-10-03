package com.example.projectandroidsuite.ui.projectpage

import androidx.compose.animation.*
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.example.projectandroidsuite.R
import com.example.projectandroidsuite.logic.SwipeDirections
import com.example.projectandroidsuite.logic.swipeToChangeScreen
import com.example.projectandroidsuite.ui.scaffold.CustomScaffold


@OptIn(ExperimentalAnimationApi::class)
@Composable
fun ProjectsPage(
    navController: NavHostController,
    state: Int? = null
) {

    var showFilters by remember { mutableStateOf(false) }
    var state by rememberSaveable { mutableStateOf(state ?: 0) }
    val titles = listOf("Projects", "Tasks")

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
                        Tab(
                            text = {
                                Row {
                                    if (index == 0)
                                        Icon(painterResource(R.drawable.project_icon), "")
                                    else
                                        Icon(painterResource(R.drawable.calendar_check_outline), "")
                                    Spacer(modifier = Modifier.width(10.dp))
                                    Text(title)
                                }
                            },
                            selected = state == index,
                            onClick = { state = index }
                        )
                    }
                }
                Surface(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(MaterialTheme.colors.background)
                ) {
                    AnimatedVisibility(
                        state == 0,
                        enter = slideInHorizontally(
                            initialOffsetX = { fullWidth -> -fullWidth * 2 },
                            animationSpec = tween(durationMillis = 500)
                        ),
                        exit = slideOutHorizontally(
                            targetOffsetX = { fullWidth -> -fullWidth * 2 },
                            animationSpec = tween(durationMillis = 500)
                        )
                    ) {
                        Surface(
                            Modifier
                                .background(MaterialTheme.colors.background)
                                .swipeToChangeScreen(SwipeDirections.RIGHT) { state = 1 }) {
                            ProjectList(hiltViewModel(), navController)
                        }
                    }

                    AnimatedVisibility(
                        state == 1,
                        enter = slideInHorizontally(
                            initialOffsetX = { fullWidth -> fullWidth * 2 },
                            animationSpec = tween(durationMillis = 200)
                        ),
                        exit = slideOutHorizontally(
                            targetOffsetX = { fullWidth -> fullWidth * 2 },
                            animationSpec = tween(durationMillis = 200)
                        )
                    ) {
                        Surface(
                            Modifier
                                .background(MaterialTheme.colors.background)
                                .swipeToChangeScreen(SwipeDirections.LEFT) { state = 0 }) {
                            TaskList(hiltViewModel(), navController)
                        }
                    }
                }
            }

            AnimatedVisibility(
                showFilters,
                enter = slideInHorizontally(
                    // Enters by sliding down from offset -fullHeight to 0.
                    initialOffsetX = { fullHeight -> 2*fullHeight },
                    animationSpec = tween(durationMillis = 400)
                ),
                exit = slideOutHorizontally(
                    // Exits by sliding up from offset 0 to -fullHeight.
                    targetOffsetX = { fullHeight -> 2*fullHeight },
                    animationSpec = tween(durationMillis = 400)
                )
            ) {
                Row{
                    Row(Modifier.weight(3F)) {

                    }
                    Row(Modifier.weight(2F)){
                        if (state == 0)
                            FilterProjects(viewModel = hiltViewModel())
                        if (state == 1)
                            FilterTasks(viewModel = hiltViewModel())
                    }
                }

            }
        }
    }
}
