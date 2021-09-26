package com.example.projectandroidsuite.ui.parts

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.AlertDialog
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.database.entities.TaskEntity
import com.example.database.entities.UserEntity
import com.example.domain.repository.Success
import com.example.projectandroidsuite.logic.PickerType
import com.example.projectandroidsuite.ui.parts.customitems.CustomTextField
import com.example.projectandroidsuite.ui.taskdetailpage.TaskCreateEditViewModel
import java.text.SimpleDateFormat
import java.util.*

@Composable
fun CreateTaskDialog(
    viewModel: TaskCreateEditViewModel,
    closeDialog: () -> Unit,
    task: TaskEntity? = null,
    onTaskDeletedOrEdited: (String) -> Unit = { }
) {
    task?.let { viewModel.setTask(it) }

    val taskUpdatingStatus by viewModel.taskUpdatingStatus.observeAsState()
    val taskCreationStatus by viewModel.taskCreationStatus.observeAsState()

    if (taskCreationStatus is Success<String>) {
        onTaskDeletedOrEdited((taskCreationStatus as Success<String>).value)
        viewModel.clearInput()
        closeDialog()
    }

    if (taskUpdatingStatus is Success<String>) {
        onTaskDeletedOrEdited((taskUpdatingStatus as Success<String>).value)
        viewModel.clearInput()
        closeDialog()
    }

    AlertDialog(
        onDismissRequest = {
            closeDialog()
            //viewModel.clearInput()
        },
        title = {
            if (task == null) Text(text = "Create Task") else Text(text = "Update Task")
        },
        text = {
            CreateTaskDialogInput(viewModel)
        },
        confirmButton = {
            Button(
                onClick = {
                    closeDialog()
                    //viewModel.clearInput()
                    if (task != null) viewModel.updateTask() else viewModel.createTask()
                }, modifier = Modifier.width(100.dp)
            ) {
                Text("Confirm")
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
fun CreateTaskDialogInput(viewModel: TaskCreateEditViewModel) {
    var showTeamPicker by remember { mutableStateOf(false) }
    var showProjectPicker by remember { mutableStateOf(false) }
    var showDatePicker by remember { mutableStateOf(false) }

    val title by viewModel.title.observeAsState("")
    val description by viewModel.description.observeAsState("")
    val listProjects by viewModel.projectList.observeAsState()
    val project by viewModel.project.observeAsState()
    val listUsersFlow by viewModel.userListFlow.observeAsState()
    val listChosenUsers by viewModel.chosenUserList.observeAsState(mutableListOf<UserEntity>())
    val endDate by viewModel.endDate.observeAsState(Date())
    val projectSearch by viewModel.projectSearchQuery.observeAsState("")
    val userSearch by viewModel.userSearchQuery.observeAsState("")

    Column(Modifier.defaultMinSize(minHeight = 250.dp)) {
        Row(Modifier.padding(vertical = 12.dp)) {
            Text(text = "Title", modifier = Modifier.weight(2F))
            CustomTextField(modifier = Modifier
                .fillMaxWidth()
                .weight(4F),
                value = title,
                onValueChange = { text -> viewModel.setTitle(text) })
        }
        Row(Modifier.padding(vertical = 12.dp)) {
            Text(text = "Description", modifier = Modifier.weight(2F))
            CustomTextField(modifier = Modifier
                .fillMaxWidth()
                .weight(4F),
                value = description, onValueChange = { text -> viewModel.setDescription(text) })
        }

        listUsersFlow?.let {
            Row(Modifier.padding(vertical = 12.dp)) {
                Text(text = "Team",
                    Modifier
                        .clickable { showTeamPicker = true }
                        .align(Alignment.CenterVertically)
                        .weight(2F))

                TeamMemberRow(
                    list = listChosenUsers!!,
                    Modifier
                        .weight(4F)
                )
            }
            if (showTeamPicker) {
                TeamPickerDialog(
                    list = it,
                    onSubmitList = { },
                    onClick = { user ->
                        run {
                            //listChosenUsers.addOrRemoveIfExisted(user)
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
        listProjects?.let {
            Row(Modifier.padding(vertical = 12.dp)) {
                Text(
                    text = "Project",
                    Modifier
                        .clickable { showProjectPicker = true }
                        .padding(end = 12.dp)
                        .weight(2F)
                )
                project?.title?.let { it1 -> Text(text = it1, Modifier
                    .weight(4F)) }
                if (showProjectPicker) {
                    ProjectPickerDialog(
                        list = it,
                        onSubmitList = { },
                        onClick = { project ->
                            run {
                                viewModel.setProject(project = project)
                                showProjectPicker = false
                            }
                        },
                        closeDialog = { showProjectPicker = false },
                        ifChooseResponsibleOrTeam = PickerType.SINGLE,
                        projectSearch,
                        { query -> viewModel.setProjectSearch(query) }
                    )
                }
            }
        }
        Row(Modifier.padding(vertical = 12.dp)) {
            Text(
                text = "End date",
                Modifier
                    .clickable { showDatePicker = true }
                    .padding(vertical = 12.dp)
                    .weight(2F)
            )
            Text(
                text = SimpleDateFormat("dd/MM/yyyy").format(endDate),
                Modifier
                    .clickable { showDatePicker = true }
                    .padding(12.dp, 12.dp)
                    .weight(4F)
            )
            if (showDatePicker) {
                DatePickerDialog(
                    date = viewModel.endDate.value ?: Date(),
                    true,
                    { showDatePicker = false },
                    { date -> viewModel.setDate(date) })
            }
        }

    }
}
