package com.example.projectandroidsuite.ui.parts

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.example.database.entities.SubtaskEntity
import com.example.domain.repository.Success
import com.example.projectandroidsuite.R
import com.example.projectandroidsuite.logic.PickerType
import com.example.projectandroidsuite.ui.parts.customitems.CustomTextField
import com.example.projectandroidsuite.ui.taskdetailpage.SubtaskCreateEditViewModel

@Composable
fun CreateSubtaskDialog(
    taskId: Int,
    viewModel: SubtaskCreateEditViewModel,
    closeDialog: () -> Unit,
    subtask: SubtaskEntity? = null,
    onSubtaskDeletedOrEdited: (String) -> Unit = { },
    onDeleteClick: (() -> Unit)? = null
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
            Row {
                if (onDeleteClick != null) {
                    Image(
                        painterResource(
                            R.drawable.ic_baseline_delete_36_red
                        ),
                        contentDescription = "",
                        modifier = Modifier
                            .weight(0.7F)
                            .clickable { onDeleteClick() }
                    )
                }
                Spacer(Modifier.size(12.dp))
                Surface(
                    color = MaterialTheme.colors.primary,
                    modifier = Modifier
                        .weight(1F)
                        .clickable { closeDialog() },
                    shape = RoundedCornerShape(5.dp)
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.defaultMinSize(minHeight = 30.dp)
                    ) {
                        Spacer(Modifier.size(12.dp))
                        Text("Dismiss", style = MaterialTheme.typography.caption)
                        Spacer(Modifier.size(12.dp))
                        Image(painterResource(R.drawable.window_close), "")
                    }
                }
                Spacer(Modifier.size(12.dp))

                Surface(
                    color = MaterialTheme.colors.primary,
                    modifier = Modifier
                        .weight(1F)
                        .clickable {
                            if (subtask == null) {
                                viewModel.createSubtask()
                            } else {
                                viewModel.updateSubtask()
                            }
                        },
                    shape = RoundedCornerShape(5.dp)
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.defaultMinSize(minHeight = 30.dp)
                    ) {
                        Spacer(Modifier.size(12.dp))
                        Text("Confirm", style = MaterialTheme.typography.caption)
                        Spacer(Modifier.size(12.dp))
                        Image(painterResource(R.drawable.ic_project_status_done), "")
                    }
                }
            }
        },
        dismissButton = {}
    )
}

@Composable
fun CreateSubtaskDialogInput(viewModel: SubtaskCreateEditViewModel) {
    var showTeamPicker by remember { mutableStateOf(false) }

    val title by viewModel.title.observeAsState("")
    val listUsersFlow by viewModel.userListFlow.observeAsState()
    val userSearch by viewModel.userSearchQuery.observeAsState("")
    val responsible by viewModel.responsible.observeAsState()

    Column() {
        Row(Modifier.padding(vertical = 12.dp)) {
            Text(text = "Title", modifier = Modifier.weight(2F))
            CustomTextField(
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

                responsible?.let { user ->
                    Row(
                        Modifier
                            .weight(4F)
                    ) { TeamMemberCard(user = user) }
                }
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
