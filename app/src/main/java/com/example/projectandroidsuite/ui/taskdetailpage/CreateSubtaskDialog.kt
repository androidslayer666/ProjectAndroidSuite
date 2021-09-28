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
import com.example.database.entities.SubtaskEntity
import com.example.domain.repository.Success
import com.example.projectandroidsuite.logic.PickerType
import com.example.projectandroidsuite.ui.parts.customitems.CustomTextField
import com.example.projectandroidsuite.ui.taskdetailpage.SubtaskCreateEditViewModel

@Composable
fun CreateSubtaskDialog(
    taskId : Int,
    viewModel: SubtaskCreateEditViewModel,
    closeDialog: () -> Unit,
    subtask: SubtaskEntity? = null,
    onSubtaskDeletedOrEdited: (String) -> Unit = { }
) {
    viewModel.setTaskId(taskId)
    subtask?.let { viewModel.setSubtask(it) }

    val taskUpdatingStatus by viewModel.subtaskUpdatingStatus.observeAsState()
    val taskCreationStatus by viewModel.subtaskCreationStatus.observeAsState()

    if (taskCreationStatus is Success<String>) {
        onSubtaskDeletedOrEdited((taskCreationStatus as Success<String>).value)
        viewModel.clearInput()
        closeDialog()
    }

    if (taskUpdatingStatus is Success<String>) {
        onSubtaskDeletedOrEdited((taskUpdatingStatus as Success<String>).value)
        viewModel.clearInput()
        closeDialog()
    }

    AlertDialog(
        onDismissRequest = {
            closeDialog()
            //viewModel.clearInput()
        },
        title = {
            if (subtask == null) Text(text = "Create subtask") else Text(text = "Update subtask")
        },
        text = {
            CreateSubtaskDialogInput(viewModel)
        },
        confirmButton = {
            Button(
                onClick = {
                    closeDialog()
                    //viewModel.clearInput()
                    if (subtask != null) viewModel.updateSubtask() else viewModel.createSubtask()
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
fun CreateSubtaskDialogInput(viewModel: SubtaskCreateEditViewModel) {
    var showTeamPicker by remember { mutableStateOf(false) }

    val title by viewModel.title.observeAsState("")
    val listUsersFlow by viewModel.userListFlow.observeAsState()
    val userSearch by viewModel.userSearchQuery.observeAsState("")
    val responsible by viewModel.responsible.observeAsState()

    Column(Modifier.defaultMinSize(minHeight = 250.dp)) {
        Row(Modifier.padding(vertical = 12.dp)) {
            Text(text = "Title", modifier = Modifier.weight(2F))
            CustomTextField(modifier = Modifier
                .fillMaxWidth()
                .weight(4F),
                value = title,
                onValueChange = { text -> viewModel.setTitle(text) })
        }

        listUsersFlow?.let {
            Row(Modifier.padding(vertical = 12.dp)) {
                Text(text = "Team",
                    Modifier
                        .clickable { showTeamPicker = true }
                        .align(Alignment.CenterVertically)
                        .weight(2F))

                responsible?.let { user -> Row( Modifier
                    .weight(4F) ){TeamMemberCard(user = user) }}
            }
            if (showTeamPicker) {
                TeamPickerDialog(
                    list = it,
                    onSubmitList = { },
                    onClick = { user ->
                        run {
                            //listChosenUsers.addOrRemoveIfExisted(user)
                            viewModel.setResponsible(user)
                        }
                    },
                    closeDialog = { showTeamPicker = false },
                    pickerType = PickerType.SINGLE,
                    userSearch,
                    { query -> viewModel.setUserSearch(query) }
                )
            }
        }
    }
}
