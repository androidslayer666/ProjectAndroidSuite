package com.example.projectandroidsuite.ui.projectpage


import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.example.domain.filters.task.TaskStatus
import com.example.domain.sorting.TaskSorting
import com.example.projectandroidsuite.R
import com.example.projectandroidsuite.ui.parts.CardTeamMember
import com.example.projectandroidsuite.ui.parts.TeamPickerDialog
import com.example.projectandroidsuite.ui.parts.customitems.ButtonUsers
import com.example.projectandroidsuite.ui.parts.customitems.CustomButton
import com.example.projectandroidsuite.ui.parts.customitems.CustomSortButton
import com.example.projectandroidsuite.ui.utils.PickerType

@Composable
fun FilterTasks(
    viewModel: TasksViewModel,
) {
    var showUserPicker by remember { mutableStateOf(false) }

    val listUsersFlow by viewModel.users.collectAsState()
    val userSearch by viewModel.userSearchProject.collectAsState()
    val stage by viewModel.stageForFilteringTask.collectAsState()
    val user by viewModel.userForFilteringTask.collectAsState()
    val sorting by viewModel.taskSorting.collectAsState()
    Box {
        Row {

            Row(Modifier.weight(3F)) {}

            Row(Modifier.weight(2F)) {
                Surface(
                    elevation = 10.dp,
                    modifier = Modifier
                        .fillMaxSize()
                ) {

                    Column(
                        modifier = Modifier
                            .background(MaterialTheme.colors.primary)
                            .padding(12.dp)
                    ) {
                        CustomButton(
                            text = "Active",
                            clicked = (stage == TaskStatus.ACTIVE),
                            onClick = { viewModel.setStatusForFilteringTask(TaskStatus.ACTIVE) })
                        Spacer(Modifier.size(12.dp))
                        CustomButton(
                            text = "Complete",
                            clicked = (stage == TaskStatus.COMPLETE),
                            onClick = { viewModel.setStatusForFilteringTask(TaskStatus.COMPLETE) })
                        Spacer(Modifier.size(24.dp))
                        Column(modifier = Modifier.padding(vertical = 12.dp)) {
                            ButtonUsers(
                                singleUser = true,
                                onClicked = { showUserPicker = true }
                            )
                            Spacer(Modifier.size(12.dp))
                            user?.let { it1 -> CardTeamMember(user = it1) }
                        }

                        Spacer(Modifier.size(12.dp))

                        Surface(
                            elevation = 10.dp,
                            color = MaterialTheme.colors.primaryVariant
                        ) {
                            Row {
                                Image(
                                    painterResource(id = R.drawable.ic_cancel), ""
                                )
                                Spacer(Modifier.size(12.dp))
                                Text(
                                    text = "Clear filters",
                                    modifier = Modifier.clickable { viewModel.clearFiltersTask() })
                            }
                        }

                        Spacer(Modifier.size(24.dp))

                        Text(
                            text = "Sorting",
                            color = MaterialTheme.colors.onPrimary,
                            modifier = Modifier.padding(vertical = 12.dp)
                        )
                        Column() {
                            Row() {
                                Text(text = "Deadline ", color = MaterialTheme.colors.onPrimary)
                            }
                            Row() {
                                CustomSortButton(
                                    ascending = true,
                                    clicked = (sorting == TaskSorting.DEADLINE_ASC)
                                ) { viewModel.setTaskSorting(TaskSorting.DEADLINE_ASC) }
                                Spacer(modifier = Modifier.width(12.dp))
                                CustomSortButton(
                                    ascending = false,
                                    clicked = (sorting == TaskSorting.DEADLINE_DESC)
                                ) { viewModel.setTaskSorting(TaskSorting.DEADLINE_DESC) }
                            }
                        }

                        Spacer(Modifier.size(24.dp))

                        Column() {
                            Row {
                                Text(text = "Stage ", color = MaterialTheme.colors.onPrimary)
                            }
                            Row() {
                                CustomSortButton(
                                    ascending = true, clicked = (sorting == TaskSorting.STAGE_ASC)
                                ) { viewModel.setTaskSorting(TaskSorting.STAGE_ASC) }
                                Spacer(modifier = Modifier.width(12.dp))
                                CustomSortButton(
                                    ascending = false, clicked = (sorting == TaskSorting.STAGE_DESC)
                                ) { viewModel.setTaskSorting(TaskSorting.STAGE_DESC) }
                            }
                        }

                        Spacer(Modifier.size(24.dp))

                        Column() {
                            Row {
                                Text(text = "Importance", color = MaterialTheme.colors.onPrimary)
                            }
                            Row() {
                                CustomSortButton(
                                    ascending = true,
                                    clicked = (sorting == TaskSorting.IMPORTANT_ASC)
                                ) { viewModel.setTaskSorting(TaskSorting.IMPORTANT_ASC) }
                                Spacer(modifier = Modifier.width(12.dp))
                                CustomSortButton(
                                    ascending = false,
                                    clicked = (sorting == TaskSorting.IMPORTANT_DESC)
                                ) { viewModel.setTaskSorting(TaskSorting.IMPORTANT_DESC) }
                            }
                        }
                    }
                }
            }

        }
        if (showUserPicker) {
            TeamPickerDialog(
                list = listUsersFlow,
                onSubmit = { },
                onClick = { user -> viewModel.setUserForFilteringTask(user) },
                closeDialog = { showUserPicker = false },
                pickerType = PickerType.SINGLE,
                userSearch,
                { query -> viewModel.setUserSearch(query) }
            )
        }
    }
}