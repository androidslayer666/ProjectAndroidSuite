package com.example.projectandroidsuite.ui.parts

import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.AlertDialog
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.DialogProperties
import androidx.navigation.NavHostController
import com.example.database.entities.ProjectEntity
import com.example.database.entities.UserEntity
import com.example.domain.repository.Failure
import com.example.domain.repository.Success
import com.example.projectandroidsuite.logic.PickerType
import com.example.projectandroidsuite.logic.addOrRemoveIfExisted
import com.example.projectandroidsuite.ui.parts.customitems.CustomTextField
import com.example.projectandroidsuite.ui.projectdetailpage.ProjectCreateEditViewModel

@Composable
fun CreateProjectDialog(
    viewModel: ProjectCreateEditViewModel,
    closeDialog: () -> Unit,
    project: ProjectEntity? = null,
    onSuccessProjectCreation: (String) -> Unit
) {
    project?.let { viewModel.setProject(it) }

    val responsible by viewModel.responsible.observeAsState()
    var responsibleIsNotChosen by remember { mutableStateOf(false) }
    val projectUpdatingStatus by viewModel.projectUpdatingStatus.observeAsState()
    val projectCreationStatus by viewModel.projectCreationStatus.observeAsState()

    if (projectCreationStatus is Success<String>) {
        onSuccessProjectCreation((projectCreationStatus as Success<String>).value)
        closeDialog()
        viewModel.clearInput()
    }

    if (projectUpdatingStatus is Success<String>) {
        onSuccessProjectCreation((projectUpdatingStatus as Success<String>).value)
        closeDialog()
        viewModel.clearInput()
    }

    AlertDialog(
        onDismissRequest = {
            viewModel.clearChosenUsers()
            closeDialog()
        },
        title = {
            if (project == null) Text(text = "Create Project") else Text(text = "Update Project")
        },
        text = {
            CreateProjectDialogInput(viewModel)
            if (responsibleIsNotChosen && responsible == null) Text(
                text = "Please choose responsible",
                color = Color.Red
            )
        },
        confirmButton = {
            Button(
                onClick = {
                    Log.d("Responsible", responsible.toString())
                    if (responsible != null) {
                        if (project != null) viewModel.updateProject() else viewModel.createProject()
                    } else {
                        responsibleIsNotChosen = true
                    }

                }, modifier = Modifier.width(100.dp)
            ) {
                if (project == null) Text(text = "Create") else Text(text = "Update")
            }
        },
        dismissButton = {
            Button(
                onClick = {
                    closeDialog()
                    viewModel.clearInput()
                }, modifier = Modifier.width(100.dp)
            ) {
                Text("Dismiss")
            }
        }
    )
}

@Composable
fun CreateProjectDialogInput(
    viewModel: ProjectCreateEditViewModel
) {
    var showTeamPicker by remember { mutableStateOf(false) }
    var showResponsiblePicker by remember { mutableStateOf(false) }

    val title by viewModel.title.observeAsState("")
    val description by viewModel.description.observeAsState("")
    val listUsersFlow by viewModel.userListFlow.observeAsState()
    val listChosenUsers by viewModel.chosenUserList.observeAsState(mutableListOf<UserEntity>())
    val responsible by viewModel.responsible.observeAsState()
    val userSearch by viewModel.userSearchQuery.observeAsState("")



    Column(Modifier.defaultMinSize(minHeight = 200.dp)) {
        Row(Modifier.padding(vertical = 12.dp)) {
            Text(text = "Title", modifier = Modifier.weight(2F))
            CustomTextField(modifier = Modifier
                .fillMaxWidth()
                .weight(4F),
                value = title, onValueChange = { text ->
                    viewModel.setTitle(text)
                })
        }
        Row(Modifier.padding(vertical = 12.dp)) {
            Text(text = "Description", modifier = Modifier.weight(2F))
            CustomTextField(modifier = Modifier
                .fillMaxWidth()
                .weight(4F),
                value = description, onValueChange = { text ->
                run {
                    viewModel.setDescription(text)
                }
            })
        }
        Row (Modifier.padding(vertical = 12.dp)){


            Text(
                text = "Team",
                Modifier
                    .clickable { showTeamPicker = true }
                    .weight(2F)
            )
            listUsersFlow?.let {
                Row(Modifier
                    .weight(4F)) {
                    TeamMemberRow(
                        list = listChosenUsers,
                    )
                }
                if (showTeamPicker) {
                    TeamPickerDialog(
                        list = it,
                        onSubmitList = { },
                        onClick = { user ->
                            run {
                                listChosenUsers.addOrRemoveIfExisted(user)
                                viewModel.addOrRemoveUser(user)
                            }
                        },
                        closeDialog = { showTeamPicker = false },
                        ifChooseResponsibleOrTeam = PickerType.MULTIPLE,
                        userSearch,
                        { query -> viewModel.setUserSearch(query) }
                    )
                }
            }
        }
        Row(Modifier.padding(vertical = 12.dp)) {
            Text(
                text = "Choose responsible",
                Modifier
                    .clickable { showResponsiblePicker = true }
                    .weight(2F)
            )
            responsible?.let { user -> Row( Modifier
                .weight(4F) ){TeamMemberCard(user = user) }}
        }
        listUsersFlow?.let {

            if (showResponsiblePicker) {
                TeamPickerDialog(
                    list = it,
                    onSubmitList = { },
                    onClick = { user ->
                        run {
                            viewModel.setResponsible(user)
                            showResponsiblePicker = false
                        }
                    },
                    closeDialog = { showResponsiblePicker = false },
                    ifChooseResponsibleOrTeam = PickerType.SINGLE,
                    userSearch,
                    { query -> viewModel.setUserSearch(query) }
                )
            }
        }
    }
}