package com.example.projectandroidsuite.ui.taskdetailpage

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import com.example.domain.model.Subtask
import com.example.domain.utils.Failure
import com.example.domain.utils.Success
import com.example.projectandroidsuite.ui.parts.CardTeamMember
import com.example.projectandroidsuite.ui.parts.TeamPickerDialog
import com.example.projectandroidsuite.ui.parts.customitems.CustomDialog
import com.example.projectandroidsuite.ui.parts.customitems.TitleInput
import com.example.projectandroidsuite.ui.utils.PickerType
import com.example.projectandroidsuite.ui.utils.makeToast

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

    val context = LocalContext.current

    val userSearch by viewModel.userSearchQuery.collectAsState("")
    var showTeamPicker by remember { mutableStateOf(false) }
    val listUsersFlow by viewModel.users.collectAsState()
    val subtaskInputState by viewModel.subtaskInputState.collectAsState()

    when {
        subtaskInputState.isTitleEmpty == true -> LaunchedEffect(key1 = subtaskInputState) {
            makeToast("Please enter title", context )
        }
        subtaskInputState.isResponsibleEmpty == true -> LaunchedEffect(key1 = subtaskInputState) {
            makeToast("Please choose responsible", context )
        }
        subtaskInputState.serverResponse is Success -> {
            onSubtaskDeletedOrEdited((subtaskInputState.serverResponse as Success<String>).value)
            closeDialog()
            viewModel.clearInput()
        }
        subtaskInputState.serverResponse is Failure -> {
            LaunchedEffect(key1 = subtaskInputState) {
                makeToast( "Something went wrong with the server request", context )
            }
        }
    }

    CustomDialog(
        show = true,
        hide = { closeDialog() },
        text = "Create Subtask",
        onSubmit = { viewModel.createSubtask() },
        onDeleteClick = onDeleteClick

    ) {
        CreateSubtaskDialogInput(viewModel, { showTeamPicker = true })
    }

    if (showTeamPicker) {
        TeamPickerDialog(
            list = listUsersFlow,
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

@Composable
fun CreateSubtaskDialogInput(
    viewModel: SubtaskCreateEditViewModel,
    showTeamPicker: () -> Unit
) {


    val title by viewModel.title.collectAsState()
    val responsible by viewModel.responsible.collectAsState()

    Column {
        TitleInput(
            text = title,
            onInputChange = { text -> viewModel.setTitle(text) })

            responsible?.let { user ->
                Row(
                    Modifier
                        .weight(4F)
                ) { CardTeamMember(user = user, showFullName = true) }
            }
        }

}
