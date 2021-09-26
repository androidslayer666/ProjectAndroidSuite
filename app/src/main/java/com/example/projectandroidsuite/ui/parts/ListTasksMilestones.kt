package com.example.projectandroidsuite.ui.parts

import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import com.example.database.entities.MilestoneEntity
import com.example.database.entities.TaskEntity
import com.example.projectandroidsuite.ui.projectpage.TaskItem


@Composable
fun ListTasksMilestones(
    listTasksAndMilestones: Map<MilestoneEntity?, List<TaskEntity>>?,
    navController: NavHostController
) {
    listTasksAndMilestones?.let {
        LazyColumn() {
            items(listTasksAndMilestones.keys.toList()) { entity ->
                Text(entity?.title ?: "", style = MaterialTheme.typography.h5)
                for (task in listTasksAndMilestones[entity]!!) {
                    TaskItem(
                        task = task,
                        onClick = { navController.navigate("tasks/${task.id}") })
                }
            }
        }
    }
}