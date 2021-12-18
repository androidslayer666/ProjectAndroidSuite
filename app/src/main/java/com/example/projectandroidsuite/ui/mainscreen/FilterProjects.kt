package com.example.projectandroidsuite.ui.mainscreen

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.unit.dp
import com.example.domain.filters.project.ProjectStatus
import com.example.domain.sorting.ProjectSorting
import com.example.projectandroidsuite.ui.parts.CardTeamMember
import com.example.projectandroidsuite.ui.parts.TeamPickerDialog
import com.example.projectandroidsuite.ui.parts.customitems.ButtonUsers
import com.example.projectandroidsuite.ui.parts.customitems.CustomButton
import com.example.projectandroidsuite.ui.parts.customitems.CustomSortButton
import com.example.projectandroidsuite.ui.utils.PickerType

@Composable
fun FilterProjects(
    viewModel: ProjectsViewModel,
) {
    var showUserPicker by remember { mutableStateOf(false) }

    val uiState by viewModel.uiState.collectAsState()

    Box(
        Modifier.semantics { contentDescription = "FilterProjects" }
    ) {
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
                            clicked = (uiState.stageForFilteringProject == ProjectStatus.ACTIVE),
                            onClick = { viewModel.setStatusForFilteringProjects(ProjectStatus.ACTIVE) })
                        Spacer(Modifier.size(12.dp))
                        CustomButton(
                            text = "Paused",
                            clicked = (uiState.stageForFilteringProject == ProjectStatus.PAUSED),
                            onClick = { viewModel.setStatusForFilteringProjects(ProjectStatus.PAUSED) })
                        Spacer(Modifier.size(12.dp))
                        CustomButton(
                            text = "Stopped",
                            clicked = (uiState.stageForFilteringProject == ProjectStatus.STOPPED),
                            onClick = { viewModel.setStatusForFilteringProjects(ProjectStatus.STOPPED) }
                        )
                        Spacer(Modifier.size(24.dp))

                        Column {
                            ButtonUsers(
                                singleUser = true,
                                onClicked = { showUserPicker = true }
                            )
                            Spacer(Modifier.size(12.dp))
                            uiState.userForFilteringProject?.let { user -> CardTeamMember(user = user) }
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
                                    clicked = (uiState.projectSorting == ProjectSorting.CREATED_ASC)
                                ) { viewModel.setProjectSorting(ProjectSorting.CREATED_ASC) }

                                Spacer(modifier = Modifier.width(12.dp))
                                CustomSortButton(
                                    ascending = false,
                                    clicked = (uiState.projectSorting == ProjectSorting.CREATED_DESC)
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
                                    clicked = (uiState.projectSorting == ProjectSorting.STAGE_ASC)
                                ) { viewModel.setProjectSorting(ProjectSorting.STAGE_ASC) }

                                Spacer(modifier = Modifier.width(12.dp))
                                CustomSortButton(
                                    ascending = false,
                                    clicked = (uiState.projectSorting == ProjectSorting.STAGE_DESC)
                                ) { viewModel.setProjectSorting(ProjectSorting.STAGE_DESC) }
                            }
                        }
                    }
                }
            }
        }
        if (showUserPicker) {
            TeamPickerDialog(
                list = uiState.users,
                onSubmit = { },
                onClick = { user -> viewModel.setUserForFilteringProjects(user = user) },
                closeDialog = { showUserPicker = false },
                pickerType = PickerType.SINGLE,
                searchString = uiState.userSearchProject,
                onSearchChanged = { query -> viewModel.setUserSearch(query) }
            )
        }
    }

}



