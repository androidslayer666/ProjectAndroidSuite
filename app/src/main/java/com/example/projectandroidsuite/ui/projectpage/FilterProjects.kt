package com.example.projectandroidsuite.ui.projectpage

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.domain.utils.ProjectSorting
import com.example.domain.utils.ProjectStatus
import com.example.projectandroidsuite.ui.utils.PickerType
import com.example.projectandroidsuite.ui.parts.CardTeamMember
import com.example.projectandroidsuite.ui.parts.TeamPickerDialog
import com.example.projectandroidsuite.ui.parts.customitems.ButtonUsers
import com.example.projectandroidsuite.ui.parts.customitems.CustomButton
import com.example.projectandroidsuite.ui.parts.customitems.CustomSortButton

@Composable
fun FilterProjects(
    viewModel: ProjectsViewModel,
) {
    var showUserPicker by remember { mutableStateOf(false) }

    val listUsersFlow by viewModel.users.collectAsState()
    val userSearch by viewModel.userSearchProject.collectAsState()
    val stage by viewModel.stageForFilteringProject.collectAsState()
    val user by viewModel.userForFilteringProject.collectAsState()
    val sorting by viewModel.projectSorting.collectAsState()

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
                            clicked = (stage == ProjectStatus.ACTIVE),
                            onClick = { viewModel.setStatusForFilteringProjects(ProjectStatus.ACTIVE) })
                        Spacer(Modifier.size(12.dp))
                        CustomButton(
                            text = "Paused",
                            clicked = (stage == ProjectStatus.PAUSED),
                            onClick = { viewModel.setStatusForFilteringProjects(ProjectStatus.PAUSED) })
                        Spacer(Modifier.size(12.dp))
                        CustomButton(
                            text = "Stopped",
                            clicked = (stage == ProjectStatus.STOPPED),
                            onClick = { viewModel.setStatusForFilteringProjects(ProjectStatus.STOPPED) }
                        )
                        Spacer(Modifier.size(24.dp))


                        Column {
                            ButtonUsers(
                                singleUser = true,
                                onClicked = { showUserPicker = true }
                            )
                            Spacer(Modifier.size(12.dp))
                            user?.let { user -> CardTeamMember(user = user) }
                        }


                        Spacer(Modifier.size(24.dp))
                        Surface(
                            elevation = 10.dp,
                            color = MaterialTheme.colors.primaryVariant
                        ) {
                            Text(text = "ClearFilters",
                                color = MaterialTheme.colors.onPrimary,
                                modifier = Modifier
                                    .clickable { viewModel.clearFiltersProject() }
                                    .padding(vertical = 12.dp))
                        }

                        Spacer(Modifier.size(24.dp))
                        Text(
                            text = "Sorting",
                            color = MaterialTheme.colors.onPrimary,
                            modifier = Modifier.padding(vertical = 12.dp)
                        )
                        Column() {
                            Row() {
                                Text(text = "Created ", color = MaterialTheme.colors.onPrimary)
                            }
                            Row() {
                                CustomSortButton(
                                    ascending = true,
                                    clicked = (sorting == ProjectSorting.CREATED_ASC)
                                ) { viewModel.setProjectSorting(ProjectSorting.CREATED_ASC) }

                                Spacer(modifier = Modifier.width(12.dp))
                                CustomSortButton(
                                    ascending = false,
                                    clicked = (sorting == ProjectSorting.CREATED_DESC)
                                ) { viewModel.setProjectSorting(ProjectSorting.CREATED_DESC) }
                            }
                        }

                        Spacer(Modifier.size(24.dp))

                        Column() {
                            Row {
                                Text(text = "Stage ", color = MaterialTheme.colors.onPrimary)
                            }

                            Row() {
                                CustomSortButton(
                                    ascending = true,
                                    clicked = (sorting == ProjectSorting.STAGE_ASC)
                                ) { viewModel.setProjectSorting(ProjectSorting.STAGE_ASC) }

                                Spacer(modifier = Modifier.width(12.dp))
                                CustomSortButton(
                                    ascending = false,
                                    clicked = (sorting == ProjectSorting.STAGE_DESC)
                                ) { viewModel.setProjectSorting(ProjectSorting.STAGE_DESC) }
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
                onClick = { user -> viewModel.setUserForFilteringProjects(user = user) },
                closeDialog = { showUserPicker = false },
                pickerType = PickerType.SINGLE,
                userSearch,
                { query -> viewModel.setUserSearch(query) }
            )
        }
    }

}



