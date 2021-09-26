package com.example.projectandroidsuite.ui.projectpage

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.selection.selectableGroup
import androidx.compose.material.MaterialTheme
import androidx.compose.material.RadioButton
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import com.example.projectandroidsuite.R
import com.example.projectandroidsuite.logic.PickerType
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
    val stage by  viewModel.stageForFilteringProject.observeAsState()
    val user by viewModel.userForFilteringProject.observeAsState()

    Column(modifier = Modifier
        .clickable { }
        .fillMaxWidth()
        .background(MaterialTheme.colors.primaryVariant)) {
        Row {
            Text(text = "stage")
            Column(Modifier.selectableGroup()) {
                Row() {
                    RadioButton(
                        (stage == ProjectStatus.ACTIVE),
                        { viewModel.setStageForFiltering(ProjectStatus.ACTIVE)}
                    )
                    Text(text = "Active")
                }
                Row() {
                    RadioButton(
                        (stage == ProjectStatus.PAUSED),
                        { viewModel.setStageForFiltering(ProjectStatus.PAUSED)}
                    )
                    Text(text = "Paused")
                }
                Row() {
                    RadioButton(
                        (stage == ProjectStatus.STOPPED),
                        { viewModel.setStageForFiltering(ProjectStatus.STOPPED)}
                    )
                    Text(text = "Stopped")
                }

            }
        }
        listUsersFlow?.let {
            Row() {
                Text(text = "Choose team", Modifier.clickable { showUserPicker = true })
                user?.let { it1 -> TeamMemberCard(user = it1) }
            }
            if (showUserPicker) {
                TeamPickerDialog(
                    list = it,
                    onSubmitList = { },
                    onClick = { user -> viewModel.setUserForFilteringProject(user) },
                    closeDialog = { showUserPicker = false },
                    ifChooseResponsibleOrTeam = PickerType.SINGLE,
                    userSearch,
                    { query -> viewModel.setUserSearch(query) }
                )
            }
        }
        Text(text = "ClearFilters", modifier = Modifier.clickable { viewModel.clearFiltersProject() })
    }

}

