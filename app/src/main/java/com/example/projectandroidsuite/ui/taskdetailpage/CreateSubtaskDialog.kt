package com.example.projectandroidsuite.ui.taskdetailpage

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.database.entities.SubtaskEntity
import com.example.domain.repository.Success
import com.example.projectandroidsuite.logic.PickerType
import com.example.projectandroidsuite.ui.parts.TeamMemberCard
import com.example.projectandroidsuite.ui.parts.TeamPickerDialog
import com.example.projectandroidsuite.ui.parts.customitems.ButtonUsers
import com.example.projectandroidsuite.ui.parts.customitems.CustomDialog
import com.example.projectandroidsuite.ui.parts.customitems.CustomTextField
import com.example.projectandroidsuite.ui.parts.customitems.DialogButtonRow

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

    CustomDialog(
        show = true,
        hide = { closeDialog() },
        text = "Create task",
        onSubmit = {
            if (subtask == null) {
                viewModel.createSubtask()
            } else {
                viewModel.updateSubtask()
            }
        },
        onDeleteClick = onDeleteClick

    ) {
        CreateSubtaskDialogInput(viewModel)
    }
}

@Composable
fun CreateSubtaskDialogInput(viewModel: SubtaskCreateEditViewModel) {
    var showTeamPicker by remember { mutableStateOf(false) }

    val title by viewModel.title.observeAsState("")
    val listUsersFlow by viewModel.userListFlow.observeAsState()
    val userSearch by viewModel.userSearchQuery.observeAsState("")
    val responsible by viewModel.responsible.observeAsState()

    Column {
        Row(Modifier.padding(vertical = 12.dp)) {
            CustomTextField(
                label = "Title",
                value = title,
                onValueChange = { text -> viewModel.setTitle(text) })
        }

        listUsersFlow?.let {
            Row(Modifier.padding(vertical = 12.dp)) {
                ButtonUsers(
                    singleUser = true,
                    onClicked = { showTeamPicker = true }
                )

                responsible?.let { user ->
                    Row(
                        Modifier
                            .weight(4F)
                    ) { TeamMemberCard(user = user, showFullName = true) }
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
