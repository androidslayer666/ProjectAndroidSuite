package com.example.projectandroidsuite.ui.scaffold

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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.projectandroidsuite.R

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun CustomScaffold(
    navController: NavHostController,
    viewModel: ScaffoldViewModel,
    content: @Composable () -> Unit,
) {
    var showFabOptions by remember { mutableStateOf(false) }
    var showUserOptions by remember { mutableStateOf(false) }

    val self by viewModel.user.collectAsState()

    val navigation = ScaffoldNavigation(navController = navController)

    val currentRoute = navController.currentBackStackEntry?.destination?.route

    // Hide scaffold on Login Page
    if (currentRoute == "Login") {
        content()
    } else {

        // This shouldn't be called before portal address is known
        viewModel.getUser()

        Box {
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
                                .weight(12F)
                                .padding(start = 12.dp)
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                            ) {
                                Text(
                                    text = self?.displayName ?: "",
                                    color = MaterialTheme.colors.onPrimary
                                )
                                Image(
                                    painterResource(R.drawable.ic_baseline_arrow_drop_down_24),
                                    "",
                                    modifier = Modifier
                                        .clickable { showUserOptions = !showUserOptions }
                                        .height(50.dp)
                                        .padding(start = 12.dp, end = 12.dp)
                                        .weight(2F)
                                )
                            }
                        }

                        Image(
                            painterResource(R.drawable.ic_baseline_search_24), "",
                            modifier = Modifier
                                .clickable { navigation.navigateToSearch() }
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
                        AnimateExtendedFabVisibility(
                            condition = showFabOptions,
                            addOffset = true,
                        ) {
                            FloatingActionButton(
                                modifier = Modifier.padding(bottom = 12.dp),
                                onClick = { navigation.navigateToCreateProject() }
                            ) {
                                Icon(painterResource(R.drawable.clipboard_list_outline), "")
                            }
                        }
                        AnimateExtendedFabVisibility(
                            condition = showFabOptions,
                            addOffset = false,
                        ) {
                            FloatingActionButton(
                                modifier = Modifier.padding(bottom = 12.dp),
                                onClick = { navigation.navigateToCreateTask() }
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

            if (showUserOptions) {
                Button(modifier =
                Modifier.padding(start = 200.dp, top = 50.dp), onClick = {
                    viewModel.logOut()
                    navigation.navigateToLogin()

                }) {
                    Text("Log Out")
                }
            }
        }
    }
}


@OptIn(ExperimentalAnimationApi::class)
@Composable
fun AnimateExtendedFabVisibility(
    condition: Boolean,
    addOffset: Boolean,
    content: @Composable () -> Unit
) {
    AnimatedVisibility(
        condition,
        enter = slideInVertically(
            // Enters by sliding down from offset -fullHeight to 0.
            initialOffsetY = { fullHeight -> if (addOffset) fullHeight * 2 else fullHeight },
            animationSpec = tween(durationMillis = 250)
        ),
        exit = slideOutVertically(
            // Exits by sliding up from offset 0 to -fullHeight.
            targetOffsetY = { fullHeight -> if (addOffset) fullHeight * 2 else fullHeight },
            animationSpec = tween(durationMillis = 250)
        )
    ) {
        content()
    }
}