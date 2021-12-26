package com.example.projectandroidsuite.ui.parts.lists

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
import com.example.domain.model.Milestone
import com.example.domain.model.Task
import com.example.projectandroidsuite.R
import com.example.projectandroidsuite.ui.mainscreen.TaskItem
import com.example.projectandroidsuite.ui.parts.customitems.DrawSideLine


@Composable
fun ListTasksMilestones(
    listTasksAndMilestones: Map<Milestone?, List<Task>>?,
    navigateToTaskId: (Int) -> Unit,
    onEditMilestone: (milestone: Milestone?) -> Unit = {},
) {
    listTasksAndMilestones?.let {
        LazyColumn {
            items(it.keys.toList()) { milestone ->
                var showButtons by remember { mutableStateOf(false) }
//                var showDeleteDialog by remember { mutableStateOf(false) }
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
                            onClick = { navigateToTaskId(task.id) })
                    }

                }
            }
        }
    }
}