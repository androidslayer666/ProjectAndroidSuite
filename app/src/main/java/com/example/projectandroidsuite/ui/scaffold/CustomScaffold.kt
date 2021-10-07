package com.example.projectandroidsuite.ui.scaffold

import android.util.Log
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.core.tween
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.example.projectandroidsuite.R
import com.example.projectandroidsuite.logic.makeToast
import com.example.projectandroidsuite.ui.parts.CreateUpdateProjectDialog
import com.example.projectandroidsuite.ui.taskdetailpage.CreateUpdateTaskDialog
import com.example.projectandroidsuite.ui.search.SearchDialog

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun CustomScaffold(
    onFilterClick: (() -> Unit)? = null,
    navController: NavHostController,
    viewModel: ScaffoldViewModel,
    content: @Composable () -> Unit,
) {
    var showSearch by remember { mutableStateOf(false) }
    var showCreateTaskDialog by remember { mutableStateOf(false) }
    var showCreateProjectDialog by remember { mutableStateOf(false) }
    var showFabOptions by remember { mutableStateOf(false) }
    val context = LocalContext.current

    val self by viewModel.self.observeAsState()

    Scaffold(
        topBar = {
            Row(
                modifier = Modifier
                    .height(50.dp)
                    .background(MaterialTheme.colors.primary),
                verticalAlignment = Alignment.CenterVertically
            )

            {
                Column(
                    modifier = Modifier
                        .weight(if (onFilterClick != null) 10F else 12F)
                        .padding(start = 12.dp)
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                    ) {
                        Text(
                            text = self?.displayName ?: "",
                            color = MaterialTheme.colors.onPrimary
                        )
                    }
                }

                if (onFilterClick != null) {
                    Image(
                        painterResource(R.drawable.ic_baseline_filter_alt_24),
                        "",
                        modifier = Modifier
                            .clickable { onFilterClick() }
                            .height(50.dp)
                            .padding(start = 12.dp, end = 12.dp)
                            .weight(2F)
                    )
                }
                Image(
                    painterResource(R.drawable.ic_baseline_search_24), "",
                    modifier = Modifier
                        .clickable { showSearch = true }
                        .height(50.dp)
                        .padding(start = 12.dp, end = 12.dp)
                        .weight(2F)
                )
            }
        },
        modifier = if (showFabOptions) Modifier.clickable {
            showFabOptions = false
        } else Modifier.padding(),
        floatingActionButton = {
            Column {

                AnimatedVisibility(
                    showFabOptions,
                    enter = slideInVertically(
                        // Enters by sliding down from offset -fullHeight to 0.
                        initialOffsetY = { fullHeight -> fullHeight * 2 },
                        animationSpec = tween(durationMillis = 250)
                    ),
                    exit = slideOutVertically(
                        // Exits by sliding up from offset 0 to -fullHeight.
                        targetOffsetY = { fullHeight -> fullHeight * 2 },
                        animationSpec = tween(durationMillis = 250)
                    )
                ) {
                    FloatingActionButton(
                        modifier = Modifier.padding(bottom = 12.dp),
                        onClick = { showCreateProjectDialog = true }
                    ) {
                        Icon(painterResource(R.drawable.clipboard_list_outline), "")
                    }
                }
                AnimatedVisibility(
                    showFabOptions,
                    enter = slideInVertically(
                        // Enters by sliding down from offset -fullHeight to 0.
                        initialOffsetY = { fullHeight -> fullHeight },
                        animationSpec = tween(durationMillis = 250)
                    ),
                    exit = slideOutVertically(
                        // Exits by sliding up from offset 0 to -fullHeight.
                        targetOffsetY = { fullHeight -> fullHeight },
                        animationSpec = tween(durationMillis = 250)
                    )
                ) {
                    FloatingActionButton(
                        modifier = Modifier.padding(bottom = 12.dp),
                        onClick = { showCreateTaskDialog = true }
                    ) {
                        Icon(painterResource(R.drawable.calendar_check_outline), "")
                    }
                }

                FloatingActionButton(
                    onClick = { showFabOptions = !showFabOptions }
                ) {
                    Icon(Icons.Filled.Add, "")
                }
            }
        }
    ) {
        Box(Modifier.background(MaterialTheme.colors.background)) {
            content()
            // catching click outside for hiding fabs
            if (showFabOptions) {
                Surface(
                    Modifier
                        .fillMaxSize()
                        .clickable(enabled = showFabOptions) {
                            showFabOptions = false
                        },
                    color = Color.Black.copy(alpha = 0f)
                ) {}
            }
        }
    }

    if (showCreateProjectDialog) {
        CreateUpdateProjectDialog(
            viewModel = hiltViewModel(),
            closeDialog = { showCreateProjectDialog = false },
            onSuccessProjectCreation = { string ->
                Log.d("CustomScaffold", string)
                makeToast(string, context)
            })
    }

    if (showCreateTaskDialog) {
        CreateUpdateTaskDialog(
            viewModel = hiltViewModel(),
            closeDialog = { showCreateTaskDialog = false },
            onTaskDeletedOrEdited = { string ->
                Log.d("CustomScaffold", string)
                makeToast(string, context)
            })

    }

    if (showSearch) {
        SearchDialog(
            viewModel = hiltViewModel(),
            closeDialog = { showSearch = false },
            navController = navController
        )
    }
}