package com.example.projectandroidsuite.ui.parts

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.selection.selectableGroup
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.example.database.entities.ProjectEntity
import com.example.database.entities.UserEntity
import com.example.domain.repository.Success
import com.example.projectandroidsuite.R
import com.example.projectandroidsuite.logic.PickerType
import com.example.projectandroidsuite.logic.ProjectStatus
import com.example.projectandroidsuite.ui.parts.customitems.*
import com.example.projectandroidsuite.ui.projectdetailpage.ProjectCreateEditViewModel
import com.example.projectandroidsuite.ui.taskdetailpage.CreateTaskDialogInput

@Composable
fun CreateUpdateProjectDialog(
    viewModel: ProjectCreateEditViewModel,
    closeDialog: () -> Unit,
    project: ProjectEntity? = null,
    onSuccessProjectCreation: (String) -> Unit,
    onDeleteClick: (() -> Unit)? = null
) {
    project?.let { viewModel.setProject(it) }

    val projectUpdatingStatus by viewModel.projectUpdatingStatus.observeAsState()
    val projectCreationStatus by viewModel.projectCreationStatus.observeAsState()

    if (projectCreationStatus is Success<String>) {
        Log.d("CreateUpdateectDialog", "Success"+(projectCreationStatus as Success<String>).value)
        onSuccessProjectCreation((projectCreationStatus as Success<String>).value)
        closeDialog()
        viewModel.clearInput()
    }

    if (projectUpdatingStatus is Success<String>) {
        Log.d("CreateUpdateectDialog", "Success"+(projectUpdatingStatus as Success<String>).value)
        onSuccessProjectCreation((projectUpdatingStatus as Success<String>).value)
        closeDialog()
        viewModel.clearInput()
    }

    CustomDialog(
        show = true,
        hide = { closeDialog() },
        text = "Create task",
        onSubmit = {
            if (project == null) {
                viewModel.createProject()
            } else {
                viewModel.updateProject()
            }
        },
        onDeleteClick = onDeleteClick
    ) {
        CreateProjectDialogInput(viewModel, project != null)
    }
}

@Composable
fun CreateProjectDialogInput(
    viewModel: ProjectCreateEditViewModel,
    modeCreate: Boolean,
) {
    var showTeamPicker by remember { mutableStateOf(false) }
    var showResponsiblePicker by remember { mutableStateOf(false) }

    val title by viewModel.title.observeAsState("")
    val description by viewModel.description.observeAsState("\n")
    val listUsersFlow by viewModel.userListFlow.observeAsState()
    val listChosenUsers by viewModel.chosenUserList.observeAsState(mutableListOf<UserEntity>())
    val responsible by viewModel.responsible.observeAsState()
    val userSearch by viewModel.userSearchQuery.observeAsState("")
    val projectStatus by viewModel.projectStatus.observeAsState()


    Column(Modifier.defaultMinSize(minHeight = 250.dp)) {

        CustomTextField(
            label = "Title",
            value = title,
            onValueChange = { text ->
                viewModel.setTitle(text)
            })
        CustomTextField(
            label = "Description",
            numberOfLines = 3,
            height = 100,
            value = description,
            onValueChange = { text ->
                viewModel.setDescription(text)
            })

        if (modeCreate)
            Row(Modifier.padding(vertical = 12.dp)) {
                Row(
                    Modifier
                        .weight(4F)
                ) {
                    CustomButton(
                        text = "Active",
                        clicked = (projectStatus == ProjectStatus.ACTIVE),
                        onClick = { viewModel.setProjectStatus(ProjectStatus.ACTIVE) })
                    Spacer(Modifier.size(12.dp))
                    CustomButton(
                        text = "Paused",
                        clicked = (projectStatus == ProjectStatus.PAUSED),
                        onClick = { viewModel.setProjectStatus(ProjectStatus.PAUSED) })
                    Spacer(Modifier.size(12.dp))
                    CustomButton(
                        text = "Stopped",
                        clicked = (projectStatus == ProjectStatus.STOPPED),
                        onClick = { viewModel.setProjectStatus(ProjectStatus.STOPPED) }
                    )
                }
            }

        if (listUsersFlow != null) {
            Row(Modifier.padding(vertical = 12.dp), verticalAlignment = Alignment.CenterVertically) {
                ButtonUsers(
                    singleUser = false,
                    onClicked = {showTeamPicker = true }
                )
                Row(
                    modifier = Modifier
                        .weight(4F)
                ) {
                    TeamMemberRow(
                        list = listChosenUsers,
                    )
                }
                if (showTeamPicker) {
                    TeamPickerDialog(
                        list = listUsersFlow!!,
                        onSubmitList = { showTeamPicker = false},
                        onClick = { user ->
                            viewModel.addOrRemoveUser(user)
                        },
                        closeDialog = { showTeamPicker = false },
                        pickerType = PickerType.MULTIPLE,
                        userSearch,
                        { query -> viewModel.setUserSearch(query) }
                    )
                }
            }
            Row(Modifier.padding(vertical = 12.dp), verticalAlignment = Alignment.CenterVertically) {
                ButtonUsers(
                    singleUser = true,
                    onClicked = { showResponsiblePicker = true }
                )
                Spacer(Modifier.size(12.dp))
                responsible?.let { user ->
                    Row(
                        modifier = Modifier
                            .weight(4F)
                    ) { TeamMemberCard(user = user) }
                }
            }

            if (showResponsiblePicker) {
                TeamPickerDialog(
                    list = listUsersFlow!!,
                    onSubmitList = { },
                    onClick = { user ->
                        run {
                            viewModel.setResponsible(user)
                            showResponsiblePicker = false
                        }
                    },
                    closeDialog = { showResponsiblePicker = false },
                    pickerType = PickerType.SINGLE,
                    userSearch,
                    { query -> viewModel.setUserSearch(query) }
                )
            }

        } else {
            Row(Modifier.padding(vertical = 12.dp)) {
                Text(
                    text = "",
                )
            }
            Row(Modifier.padding(vertical = 12.dp)) {
                Text(
                    text = "",
                )
            }
        }
    }
}