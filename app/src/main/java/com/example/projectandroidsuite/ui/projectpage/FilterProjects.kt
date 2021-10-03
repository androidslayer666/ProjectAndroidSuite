package com.example.projectandroidsuite.ui.projectpage

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.selection.selectableGroup
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.MaterialTheme
import androidx.compose.material.RadioButton
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.example.projectandroidsuite.R
import com.example.projectandroidsuite.logic.PickerType
import com.example.projectandroidsuite.logic.ProjectSorting
import com.example.projectandroidsuite.logic.ProjectStatus
import com.example.projectandroidsuite.ui.parts.TeamMemberCard
import com.example.projectandroidsuite.ui.parts.TeamPickerDialog
import com.example.projectandroidsuite.ui.parts.customitems.CustomButton
import com.example.projectandroidsuite.ui.parts.customitems.CustomSortButton

@Composable
fun FilterProjects(
    viewModel: ProjectViewModel,
) {
    var showUserPicker by remember { mutableStateOf(false) }

    val listUsersFlow by viewModel.userListFlow.observeAsState()
    val userSearch by viewModel.userSearchProject.observeAsState("")
    val stage by viewModel.stageForFilteringProject.observeAsState()
    val user by viewModel.userForFilteringProject.observeAsState()
    val sorting by viewModel.projectSorting.observeAsState()

    Surface(
        elevation = 10.dp,
        modifier = Modifier
            .fillMaxSize()
    ) {
        Column(
            modifier = Modifier
                .background(MaterialTheme.colors.primary)
        ) {
            Text(text = "Stage", color = MaterialTheme.colors.onPrimary)
            CustomButton(
                text = "Active",
                clicked = (stage == ProjectStatus.ACTIVE),
                onClick = { viewModel.setStageForFiltering(ProjectStatus.ACTIVE) })
            Spacer(Modifier.size(12.dp))
            CustomButton(
                text = "Paused",
                clicked = (stage == ProjectStatus.PAUSED),
                onClick = { viewModel.setStageForFiltering(ProjectStatus.PAUSED) })
            Spacer(Modifier.size(12.dp))
            CustomButton(
                text = "Stopped",
                clicked = (stage == ProjectStatus.STOPPED),
                onClick = { viewModel.setStageForFiltering(ProjectStatus.STOPPED) }
            )




            listUsersFlow?.let {
                Column() {
                    Text(
                        text = "Responsible",
                        color = MaterialTheme.colors.onPrimary,
                        modifier = Modifier.clickable { showUserPicker = true })
                    Spacer(Modifier.size(12.dp))
                    user?.let { it1 -> TeamMemberCard(user = it1) }
                    if (showUserPicker) {
                        TeamPickerDialog(
                            list = it,
                            onSubmitList = { },
                            onClick = { user -> viewModel.setUserForFilteringProject(user) },
                            closeDialog = { showUserPicker = false },
                            pickerType = PickerType.SINGLE,
                            userSearch,
                            { query -> viewModel.setUserSearch(query) }
                        )
                    }
                }
            }

            Spacer(Modifier.size(12.dp))
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

            Spacer(Modifier.size(12.dp))
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
                        ascending = true, clicked = (sorting == ProjectSorting.CREATED_ASC)
                    ) { viewModel.setProjectSorting(ProjectSorting.CREATED_ASC) }

                    Spacer(modifier = Modifier.width(12.dp))
                    CustomSortButton(
                        ascending = false, clicked = (sorting == ProjectSorting.CREATED_DESC)
                    ) { viewModel.setProjectSorting(ProjectSorting.CREATED_DESC) }

                }
            }

            Column() {

                Row(
                    Modifier
                        .padding(bottom = 20.dp)
                ) {
                    Text(text = "Stage ", color = MaterialTheme.colors.onPrimary)
                }

                Row() {
                    CustomSortButton(
                        ascending = true, clicked = (sorting == ProjectSorting.STAGE_ASC)
                    ) { viewModel.setProjectSorting(ProjectSorting.STAGE_ASC) }

                    Spacer(modifier = Modifier.width(12.dp))
                    CustomSortButton(
                        ascending = false, clicked = (sorting == ProjectSorting.STAGE_DESC)
                    ) { viewModel.setProjectSorting(ProjectSorting.STAGE_DESC) }
                }
            }
        }
    }
}



