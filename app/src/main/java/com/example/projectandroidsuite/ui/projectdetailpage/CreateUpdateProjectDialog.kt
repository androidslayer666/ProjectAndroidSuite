package com.example.projectandroidsuite.ui.projectdetailpage

import android.util.Log
import androidx.compose.foundation.layout.*
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.example.domain.filters.project.ProjectStatus
import com.example.domain.model.Project
import com.example.domain.utils.Failure
import com.example.domain.utils.Success
import com.example.projectandroidsuite.ui.parts.CardTeamMember
import com.example.projectandroidsuite.ui.parts.RowTeamMember
import com.example.projectandroidsuite.ui.parts.TeamPickerDialog
import com.example.projectandroidsuite.ui.parts.customitems.*
import com.example.projectandroidsuite.ui.utils.PickerType
import com.example.projectandroidsuite.ui.utils.makeToast

@Composable
fun CreateUpdateProjectDialog(
    viewModel: ProjectCreateEditViewModel,
    closeDialog: () -> Unit,
    project: Project? = null,
    onSuccessProjectCreation: (String) -> Unit,
    onDeleteClick: (() -> Unit)? = null
) {
    LaunchedEffect(key1 = project) {
        project?.let { viewModel.setProject(it) }
    }

    var showTeamPicker by remember { mutableStateOf(false) }
    var showResponsiblePicker by remember { mutableStateOf(false) }

    val userSearch by viewModel.userSearchQuery.collectAsState()
    val listUsersFlow by viewModel.users.collectAsState()
    val projectInputState by viewModel.projectInputState.collectAsState()

    Log.d("CreateUpdateProject", projectInputState.toString())

    when {
        projectInputState.isTitleEmpty == true -> makeToast("Please enter project title", LocalContext.current)
        projectInputState.isTeamEmpty == true -> makeToast("Please choose project team", LocalContext.current)
        projectInputState.isResponsibleEmpty == true -> makeToast("Please enter project responsible", LocalContext.current)
        projectInputState.serverResponse is Success -> {
            onSuccessProjectCreation((projectInputState.serverResponse as Success<String>).value)
            closeDialog()
            viewModel.clearInput()
        }
        projectInputState.serverResponse is Failure -> {
            onSuccessProjectCreation((projectInputState.serverResponse as Failure<String>).reason)
            closeDialog()
            viewModel.clearInput()
        }
    }

    Box {
        CustomDialog(
            show = true,
            hide = {
                closeDialog()
                viewModel.clearInput()
            },
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
            CreateProjectDialogInput(
                viewModel = viewModel,
                modeCreate = project != null,
                showTeamPicker = { showTeamPicker = true },
                showResponsiblePicker = { showResponsiblePicker = true })
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
                onSubmit = {
                    showTeamPicker = false
                    viewModel.updateChosenUsers()
                },
                onClick = { user ->
                    viewModel.addOrRemoveUser(user)
                },
                closeDialog = { showTeamPicker = false },
                pickerType = PickerType.MULTIPLE,
                userSearch,
                { query ->
                    viewModel.setUserSearch(query)
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


    val title by viewModel.title.collectAsState()
    val description by viewModel.description.collectAsState()
    val listUsersFlow by viewModel.users.collectAsState()
    val listChosenUsers by viewModel.chosenUserList.collectAsState()
    val responsible by viewModel.responsible.collectAsState()
    val projectStatus by viewModel.projectStatus.collectAsState()

    Column(Modifier.defaultMinSize(minHeight = 250.dp)) {


        Box {
            Column {
                TitleInput(
                    text = title,
                    onInputChange = { text -> viewModel.setTitle(text) })

                DescriptionInput(
                    text = description,
                    onInputChange = { text -> viewModel.setDescription(text) })


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