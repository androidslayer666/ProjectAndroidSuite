package com.example.projectandroidsuite.ui.projectpage

import android.util.Log
import androidx.compose.animation.*
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.example.projectandroidsuite.logic.SwipeDirections
import com.example.projectandroidsuite.logic.swipeToChangeScreen
import com.example.projectandroidsuite.logic.swipeToRefresh
import com.example.projectandroidsuite.ui.parts.ScaffoldTopBarWrapper


@OptIn(ExperimentalAnimationApi::class)
@Composable
fun ProjectsPage(
    navController: NavHostController,
    toggleSearch: () -> Unit,
    toggleFab: Pair<Boolean, () -> Unit>
) {
    //Log.d("deleteProject", "Recompose ProjectPage")

    var showFilters by remember { mutableStateOf(false) }
    var state by remember { mutableStateOf(0) }
    val titles = listOf("Projects", "Tasks")

    ScaffoldTopBarWrapper(
        showFilters = true,
        onFilterClick = { showFilters = !showFilters },
        toggleSearch
    ) {
        Box(
            if (toggleFab.first)
                Modifier.clickable { toggleFab.second() } else Modifier.padding()) {

            Column(Modifier.background(MaterialTheme.colors.background)) {
                TabRow(selectedTabIndex = state) {
                    titles.forEachIndexed { index, title ->
                        Tab(
                            text = { Text(title) },
                            selected = state == index,
                            onClick = { state = index }
                        )
                    }
                }
                Surface(modifier = Modifier.fillMaxWidth().background(MaterialTheme.colors.background)) {
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
                        Surface(Modifier.background(MaterialTheme.colors.background).swipeToChangeScreen(SwipeDirections.RIGHT) { state = 1 }) {
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
                        Surface(Modifier.background(MaterialTheme.colors.background).swipeToChangeScreen(SwipeDirections.LEFT) { state = 0 }) {
                            TaskList(hiltViewModel(), navController)
                        }
                    }
                }
            }

            AnimatedVisibility(
                showFilters,
                enter = slideInVertically(
                    // Enters by sliding down from offset -fullHeight to 0.
                    initialOffsetY = { fullHeight -> -fullHeight },
                    animationSpec = tween(durationMillis = 200)
                ),
                exit = slideOutVertically(
                    // Exits by sliding up from offset 0 to -fullHeight.
                    targetOffsetY = { fullHeight -> -fullHeight },
                    animationSpec = tween(durationMillis = 200)
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
