package com.example.projectandroidsuite.ui.parts

import android.util.Log
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.core.tween
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material.FloatingActionButton
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.example.projectandroidsuite.R

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun ExpandableButtons(
    expandButtons: Boolean,
    expandOrHide: () -> Unit,
    onEditClick: () -> Unit,
    onDeleteClick: () -> Unit,
    canEdit: Boolean? = false,
    canDelete: Boolean? = false
) {
    var showDeleteDialog by remember { mutableStateOf(false) }

    Column() {
        FloatingActionButton(
            modifier = Modifier.padding(top = 12.dp, end = 12.dp),
            onClick = {
                expandOrHide()
            },
            backgroundColor = MaterialTheme.colors.primary
        ) {
            if (expandButtons) Image(painterResource(R.drawable.ic_baseline_expand_less_24), "")
            else Image(painterResource(R.drawable.ic_baseline_expand_more_24), "")
        }
        if (canEdit == true)
            AnimatedVisibility(
                expandButtons,
                enter = slideInVertically(
                    // Enters by sliding down from offset -fullHeight to 0.
                    initialOffsetY = { fullHeight -> -fullHeight },
                    animationSpec = tween(durationMillis = 250)
                ),
                exit = slideOutVertically(
                    // Exits by sliding up from offset 0 to -fullHeight.
                    targetOffsetY = { fullHeight -> -fullHeight },
                    animationSpec = tween(durationMillis = 250)
                )
            ) {
                FloatingActionButton(
                    modifier = Modifier.padding(top = 12.dp, end = 12.dp),
                    onClick = {
                        onEditClick()
                    },
                    backgroundColor = MaterialTheme.colors.primary
                ) {
                    Image(painterResource(R.drawable.ic_edit_button), "")
                }
            }
        if (canDelete == true)
            AnimatedVisibility(
                expandButtons,
                enter = slideInVertically(
                    // Enters by sliding down from offset -fullHeight to 0.
                    initialOffsetY = { fullHeight -> -fullHeight * 2 },
                    animationSpec = tween(durationMillis = 250)
                ),
                exit = slideOutVertically(
                    // Exits by sliding up from offset 0 to -fullHeight.
                    targetOffsetY = { fullHeight -> -fullHeight * 2 },
                    animationSpec = tween(durationMillis = 250)
                )
            ) {

                FloatingActionButton(
                    modifier = Modifier.padding(top = 12.dp, end = 12.dp),
                    onClick = {
                        onDeleteClick()
                    },
                    backgroundColor = MaterialTheme.colors.primary
                ) {
                    Image(painterResource(R.drawable.ic_cancel), "")
                }
            }

    }
}