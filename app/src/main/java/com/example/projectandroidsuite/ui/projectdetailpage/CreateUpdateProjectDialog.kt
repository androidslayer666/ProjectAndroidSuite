package com.example.projectandroidsuite.ui.parts

import android.util.Log
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.database.entities.ProjectEntity
import com.example.database.entities.UserEntity
import com.example.domain.repository.Success
import com.example.projectandroidsuite.logic.PickerType
import com.example.projectandroidsuite.logic.ProjectStatus
import com.example.projectandroidsuite.ui.parts.customitems.*
import com.example.projectandroidsuite.ui.projectdetailpage.ProjectCreateEditViewModel

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
    val userSearch by viewModel.userSearchQuery.observeAsState("")
    val listUsersFlow by viewModel.userListFlow.observeAsState()

    var showTeamPicker by remember { mutableStateOf(false) }
    var showResponsiblePicker by remember { mutableStateOf(false) }


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

    Box {
        CustomDialog(
            show = true,
            hide = { closeDialog() },
            text = if (project == null) "Create project" else "Update project",
            onSubmit = {
                if (project == null) {
                    viewModel.createProject()
                } else {
                    viewModel.updateProject()
                }
            },
            onDeleteClick = onDeleteClick
        ) {
            CreateProjectDialogInput(viewModel, project != null,
                {showTeamPicker = true},
                {showResponsiblePicker = true})
        }

        if (showResponsiblePicker) {
            TeamPickerDialog(
                list = listUsersFlow!!,
                onSubmit = { },
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
        if (showTeamPicker) {
            TeamPickerDialog(
                list = listUsersFlow!!,
                onSubmit = { showTeamPicker = false },
                onClick = { user ->
                    viewModel.addOrRemoveUser(user)
                },
                closeDialog = { showTeamPicker = false },
                pickerType = PickerType.MULTIPLE,
                userSearch,
                { query -> viewModel.setUserSearch(query)
                    showTeamPicker = false
                }
            )
        }
    }
}

@Composable
fun CreateProjectDialogInput(
    viewModel: ProjectCreateEditViewModel,
    modeCreate: Boolean,
    showTeamPicker: () -> Unit,
    showResponsiblePicker: () -> Unit,
) {


    val title by viewModel.title.observeAsState("")
    val description by viewModel.description.observeAsState("\n")
    val listUsersFlow by viewModel.userListFlow.observeAsState()
    val listChosenUsers by viewModel.chosenUserList.observeAsState(mutableListOf<UserEntity>())
    val responsible by viewModel.responsible.observeAsState()
    val userSearch by viewModel.userSearchQuery.observeAsState("")
    val projectStatus by viewModel.projectStatus.observeAsState()


    Column(Modifier.defaultMinSize(minHeight = 250.dp)) {


        Box {
            Column {
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
                    Row(
                        Modifier.padding(vertical = 12.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        ButtonUsers(
                            singleUser = false,
                            onClicked = { showTeamPicker() }
                        )
                        Row(
                            modifier = Modifier
                                .weight(4F)
                        ) {
                            RowTeamMember(
                                list = listChosenUsers,
                            )
                        }

                    }
                    Row(
                        Modifier.padding(vertical = 12.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        ButtonUsers(
                            singleUser = true,
                            onClicked = { showResponsiblePicker() }
                        )
                        Spacer(Modifier.size(12.dp))
                        responsible?.let { user ->
                            Row(
                                modifier = Modifier
                                    .weight(4F)
                            ) { CardTeamMember(user = user) }
                        }
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

    }
}