package com.example.projectandroidsuite.ui.parts

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import com.example.database.entities.MilestoneEntity
import com.example.database.entities.TaskEntity
import com.example.projectandroidsuite.ui.projectpage.TaskItem


@Composable
fun ListTasksMilestones(
    listTasksAndMilestones: Map<MilestoneEntity?, List<TaskEntity>>?,
    navController: NavHostController,
    onDeleteMilestone: (milestone: MilestoneEntity?) -> Unit = {}
) {
    listTasksAndMilestones?.let {
        LazyColumn() {
            items(listTasksAndMilestones.keys.toList()) { entity ->
                var showDeleteButton by remember { mutableStateOf(false) }
                var showDeleteDialog by remember { mutableStateOf(false) }

                Row (verticalAlignment = Alignment.CenterVertically){
                    Text(
                        text = entity?.title ?: "",
                        style = MaterialTheme.typography.h5,
                        modifier = Modifier.clickable { showDeleteButton = !showDeleteButton }
                            .weight(5F)
                        )
                    if (showDeleteButton) {
                        Image(
                            Icons.Default.Delete,
                            "",
                            modifier = Modifier
                                .clickable {
                                    showDeleteDialog = true
                                    showDeleteButton = false
                                }.weight(1F))
                    }
                    if (showDeleteDialog) {
                        ConfirmationDialog(
                            text = "Do you want to delete the message?",
                            onSubmit = { onDeleteMilestone(entity) },
                            { showDeleteDialog = false })
                    }
                }
                for (task in listTasksAndMilestones[entity]!!) {
                    TaskItem(
                        task = task,
                        onClick = { navController.navigate("tasks/${task.id}") })
                }
            }
        }
    }
}