package com.example.projectandroidsuite.ui.parts

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.AlertDialog
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.database.entities.MilestoneEntity

@Composable
fun PickerMilestoneDialog(
    list: List<MilestoneEntity>,
    onClick: (user: MilestoneEntity) -> Unit,
    closeDialog: () -> Unit
) {

    AlertDialog(
        onDismissRequest = {
            closeDialog()
        },
        title = {
                Text(text = "Choose milestone")
        },
        text = {
            PickerMilestoneBody(
                list = list, { project -> onClick(project) }
            )
        },
        confirmButton = {},
        dismissButton = {}
    )
}

@Composable
fun PickerMilestoneBody(
    list: List<MilestoneEntity>,
    onClick: (project: MilestoneEntity) -> Unit
) {
    Column {
        LazyColumn {
            items(list) { milestone ->
                Row(Modifier.padding(12.dp)) {
                    Text(text = milestone.title?:"", Modifier.clickable {
                        onClick(milestone)
                    })
                }
            }
        }
    }
}