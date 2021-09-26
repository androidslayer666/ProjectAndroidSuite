package com.example.projectandroidsuite.ui.parts.customitems

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.core.tween
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material.FloatingActionButton
import androidx.compose.material.Icon
import androidx.compose.material.Scaffold
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountBox
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Build
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp


@OptIn(ExperimentalAnimationApi::class)
@Composable
fun ScaffoldFabWrapper(
    showTaskDialog: ()-> Unit,
    showProjectDialog: ()-> Unit,
    content : @Composable () -> Unit
){

    var showFabOptions by remember { mutableStateOf(false) }
    Scaffold(
        topBar = { },
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
                        onClick = { showTaskDialog() }
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