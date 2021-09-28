package com.example.projectandroidsuite.ui.parts.customitems

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.core.tween
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountBox
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Build
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.example.projectandroidsuite.R

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun CustomScaffold(
    onSearchClick: (() -> Unit),
    onFilterClick: (() -> Unit)? = null,
    showTaskDialog: (() -> Unit),
    showProjectDialog: (() -> Unit),
    content: @Composable () -> Unit
) {

    var showFabOptions by remember { mutableStateOf(false) }
    Scaffold(
        topBar = {
            Row(modifier = Modifier.height(50.dp).background(MaterialTheme.colors.primary))
            {
                Text(
                    text = "Project application",
                    modifier = Modifier
                        .weight(10F)
                        .padding(12.dp)
                )
                if (onFilterClick != null) {
                    Image(
                        painterResource(R.drawable.ic_baseline_filter_alt_24),
                        "",
                        modifier = Modifier
                            .clickable { onFilterClick()}
                            .height(50.dp)
                            .padding(start = 12.dp, end = 12.dp)
                            .weight(2F)
                    )
                }
                Image(
                    painterResource(R.drawable.ic_baseline_search_24), "",
                    modifier = Modifier
                        .clickable { onSearchClick() }
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
                        initialOffsetY = { fullHeight -> fullHeight*2 },
                        animationSpec = tween(durationMillis = 250)
                    ),
                    exit = slideOutVertically(
                        // Exits by sliding up from offset 0 to -fullHeight.
                        targetOffsetY = { fullHeight -> fullHeight*2 },
                        animationSpec = tween(durationMillis = 250)
                    )
                ) {
                    FloatingActionButton(
                        modifier = Modifier.padding(bottom = 12.dp),
                        onClick = { showProjectDialog() }
                    ) {
                        Icon(Icons.Filled.AccountBox, "")
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
                        onClick = { showTaskDialog()}
                    ) {
                        Icon(Icons.Filled.Build, "")
                    }
                }

                FloatingActionButton(
                    onClick = { showFabOptions = !showFabOptions }
                ) {
                    Icon(Icons.Filled.Add, "")
                }
            }
        }
    ){
        content()
    }
}