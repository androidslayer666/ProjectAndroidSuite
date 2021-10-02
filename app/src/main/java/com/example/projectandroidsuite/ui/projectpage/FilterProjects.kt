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
        shape = RoundedCornerShape(
            topStart = 0.dp,
            topEnd = 0.dp,
            bottomEnd = 16.dp,
            bottomStart = 16.dp
        ),
        modifier = Modifier
            .fillMaxWidth()
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(MaterialTheme.colors.primary)
        ) {


            Row {
                Row(Modifier.weight(2F)) {
                    Text(text = "Stage", color = MaterialTheme.colors.onPrimary)
                }
                Row(
                    Modifier
                        .selectableGroup()
                        .weight(4F)
                ) {
                    RadioButton(
                        (stage == ProjectStatus.ACTIVE),
                        { viewModel.setStageForFiltering(ProjectStatus.ACTIVE) }
                    )
                    Text(text = "Active", color = MaterialTheme.colors.onPrimary)


                    RadioButton(
                        (stage == ProjectStatus.PAUSED),
                        { viewModel.setStageForFiltering(ProjectStatus.PAUSED) }
                    )
                    Text(text = "Paused", color = MaterialTheme.colors.onPrimary)

                    RadioButton(
                        (stage == ProjectStatus.STOPPED),
                        { viewModel.setStageForFiltering(ProjectStatus.STOPPED) }
                    )
                    Text(text = "Stopped", color = MaterialTheme.colors.onPrimary)

                }
            }
            listUsersFlow?.let {
                Row {
                    Row(
                        Modifier
                            .clickable { showUserPicker = true }
                            .weight(2F)) {
                        Text(text = "Choose team", color = MaterialTheme.colors.onPrimary)
                        user?.let { it1 -> TeamMemberCard(user = it1) }
                    }
                    if (showUserPicker) {
                        Row(Modifier.weight(4F)) {
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
            }
            Text(text = "ClearFilters", color = MaterialTheme.colors.onPrimary, modifier = Modifier
                .clickable { viewModel.clearFiltersProject() }
                .padding(vertical = 12.dp))

            Text(
                text = "Sorting",
                color = MaterialTheme.colors.onPrimary,
                modifier = Modifier.padding(vertical = 12.dp)
            )
            Row {

                Row(Modifier.weight(2F)) {
                    Text(text = "Created ", color = MaterialTheme.colors.onPrimary)

                }
                Row(Modifier.weight(4F)) {

                    RadioButton(
                        (sorting == ProjectSorting.CREATED_ASC),
                        { viewModel.setProjectSorting(ProjectSorting.CREATED_ASC) }
                    )
                    Image(
                        painterResource(R.drawable.sort_variant),
                        contentDescription = "Status"
                    )

                    Spacer(modifier = Modifier.width(6.dp))
                    RadioButton(
                        (sorting == ProjectSorting.CREATED_DESC),
                        { viewModel.setProjectSorting(ProjectSorting.CREATED_DESC) }
                    )
                    Image(
                        painterResource(R.drawable.sort_reverse_variant),
                        contentDescription = "Status"
                    )
                }
            }

            Row {

                Row(
                    Modifier
                        .padding(bottom = 20.dp)
                        .weight(2F)
                ) {
                    Text(text = "Stage ", color = MaterialTheme.colors.onPrimary)
                }

                Row(Modifier.weight(4F)) {
                    RadioButton(
                        (sorting == ProjectSorting.STAGE_ASC),
                        { viewModel.setProjectSorting(ProjectSorting.STAGE_ASC) }
                    )
                    Image(
                        painterResource(R.drawable.sort_variant),
                        contentDescription = "Status"
                    )

                    Spacer(modifier = Modifier.width(6.dp))
                    RadioButton(
                        (sorting == ProjectSorting.STAGE_DESC),
                        { viewModel.setProjectSorting(ProjectSorting.STAGE_DESC) }
                    )
                    Image(
                        painterResource(R.drawable.sort_reverse_variant),
                        contentDescription = "Status"
                    )
                }
            }
        }
    }


}

