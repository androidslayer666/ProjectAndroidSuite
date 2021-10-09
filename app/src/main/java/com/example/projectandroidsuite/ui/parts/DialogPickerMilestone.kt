package com.example.projectandroidsuite.ui.parts

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.AlertDialog
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.database.entities.MilestoneEntity
import com.example.projectandroidsuite.ui.parts.customitems.CustomDialog

@Composable
fun DialogPickerMilestone(
    list: List<MilestoneEntity>,
    onClick: (user: MilestoneEntity) -> Unit,
    closeDialog: () -> Unit
) {
    CustomDialog(
        show = true,
        hide = { closeDialog() },
        text = "Milestones",
        onSubmit = { closeDialog() },
        showButtons = false,
        ) {
        PickerMilestoneBody(
            list = list, { project -> onClick(project) }
        )
    }
}

@Composable
fun PickerMilestoneBody(
    list: List<MilestoneEntity>,
    onClick: (project: MilestoneEntity) -> Unit
) {
if(list.isNotEmpty())
        LazyColumn {
            items(list) { milestone ->
                Row(Modifier.padding(12.dp).fillMaxWidth()) {
                    Text(text = milestone.title?:"", Modifier.clickable {
                        onClick(milestone)
                    }.fillMaxWidth())
                }
            }
        }
}