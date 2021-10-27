package com.example.projectandroidsuite.ui.taskdetailpage

import androidx.compose.foundation.layout.*
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.domain.model.Subtask
import com.example.domain.Success
import com.example.projectandroidsuite.logic.PickerType
import com.example.projectandroidsuite.ui.parts.CardTeamMember
import com.example.projectandroidsuite.ui.parts.TeamPickerDialog
import com.example.projectandroidsuite.ui.parts.customitems.ButtonUsers
import com.example.projectandroidsuite.ui.parts.customitems.CustomDialog
import com.example.projectandroidsuite.ui.parts.customitems.CustomTextField

@Composable
fun CreateSubtaskDialog(
    taskId: Int,
    viewModel: SubtaskCreateEditViewModel,
    closeDialog: () -> Unit,
    subtask: Subtask? = null,
    onSubtaskDeletedOrEdited: (String) -> Unit = { },
    onDeleteClick: (() -> Unit)? = null
) {
    viewModel.setTaskId(taskId)
    subtask?.let { viewModel.setSubtask(it) }

    val taskUpdatingStatus by viewModel.subtaskUpdatingStatus.collectAsState()
    val taskCreationStatus by viewModel.subtaskCreationStatus.collectAsState()
    val userSearch by viewModel.userSearchQuery.observeAsState("")
    var showTeamPicker by remember { mutableStateOf(false) }
    val listUsersFlow by viewModel.users.collectAsState()

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
        text = "Create Subtask",
        onSubmit = {viewModel.createSubtask()},
        onDeleteClick = onDeleteClick

    ) {
        CreateSubtaskDialogInput(viewModel, {showTeamPicker = true})
    }

    if (showTeamPicker) {
        listUsersFlow?.let {
            TeamPickerDialog(
                list = it,
                onSubmit = { },
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

@Composable
fun CreateSubtaskDialogInput(viewModel: SubtaskCreateEditViewModel,
                             showTeamPicker: ()-> Unit
                             ) {


    val title by viewModel.title.observeAsState("")
    val listUsersFlow by viewModel.users.collectAsState()

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
                    onClicked = { showTeamPicker() }
                )

                responsible?.let { user ->
                    Row(
                        Modifier
                            .weight(4F)
                    ) { CardTeamMember(user = user, showFullName = true) }
                }
            }

        }
    }
}
