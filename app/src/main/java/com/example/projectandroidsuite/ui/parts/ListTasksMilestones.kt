package com.example.projectandroidsuite.ui.parts

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.example.database.entities.MilestoneEntity
import com.example.database.entities.TaskEntity
import com.example.projectandroidsuite.R
import com.example.projectandroidsuite.ui.parts.customitems.DrawSideLine
import com.example.projectandroidsuite.ui.projectdetailpage.CreateMilestoneDialog
import com.example.projectandroidsuite.ui.projectpage.TaskItem


@Composable
fun ListTasksMilestones(
    listTasksAndMilestones: Map<MilestoneEntity?, List<TaskEntity>>?,
    navController: NavHostController,
    onEditMilestone: (milestone: MilestoneEntity?) -> Unit = {},
) {
    listTasksAndMilestones?.let {
        LazyColumn() {
            items(listTasksAndMilestones.keys.toList()) { milestone ->
                var showButtons by remember { mutableStateOf(false) }
                var showDeleteDialog by remember { mutableStateOf(false) }
                var showEditDialog by remember { mutableStateOf(false) }

                DrawSideLine(
                    enable = (milestone != null),
                    color = MaterialTheme.colors.primary,
                    width = 20F
                ) {
                    Box {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            if (milestone != null) {
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    modifier = Modifier.weight(5F)
                                ) {
                                    if (milestone.isKey == true) {
                                        Image(
                                            painterResource(
                                                R.drawable.ic_baseline_key_24
                                            ),
                                            "",
                                            modifier = Modifier
                                                .padding(horizontal = 4.dp)
                                        )
                                    }
                                    Text(
                                        text = milestone.title ?: "",
                                        style = MaterialTheme.typography.h6,
                                        modifier = Modifier
                                            .clickable { showButtons = !showButtons }
                                    )
                                }
                            }
                            if (showButtons && milestone?.canEdit == true) {
                                Image(
                                    painterResource(id = R.drawable.ic_edit_button),
                                    "",
                                    modifier = Modifier
                                        .clickable {
                                            onEditMilestone(milestone)
                                        }
                                        .weight(1F))
                            }
                        }
                    }
                    for (task in listTasksAndMilestones[milestone]!!) {
                        TaskItem(
                            task = task,
                            onClick = { navController.navigate("task/${task.id}") })
                    }

                    if (showEditDialog) {
                        CreateMilestoneDialog(
                            milestone = milestone,
                            projectId = milestone?.projectId ?: 0,
                            viewModel = hiltViewModel(),
                            closeDialog = { showEditDialog = false },
                            onDeleteClick = {
                                if (milestone?.canDelete == true) {
                                    showDeleteDialog = true
                                }
                            }
                        )
                    }
                }
            }
        }
    }
}